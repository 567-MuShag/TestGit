/*	JaamSim��ɢ�¼�����
 * JaamSim Discrete Event Simulation
 * Copyright (C) 2016-2019 JaamSim Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jaamsim.basicsim;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

import com.jaamsim.Samples.SampleExpression;
import com.jaamsim.StringProviders.StringProvExpression;
import com.jaamsim.datatypes.IntegerVector;
import com.jaamsim.events.Conditional;
import com.jaamsim.events.EventManager;
import com.jaamsim.events.EventTimeListener;
import com.jaamsim.input.ExpError;
import com.jaamsim.input.Input;
import com.jaamsim.input.InputAgent;
import com.jaamsim.input.InputErrorException;
import com.jaamsim.input.KeywordIndex;
import com.jaamsim.states.StateEntity;
import com.jaamsim.ui.EventViewer;
import com.jaamsim.ui.GUIFrame;
import com.jaamsim.ui.LogBox;
import com.jaamsim.ui.View;
import com.jaamsim.units.DimensionlessUnit;
import com.jaamsim.units.Unit;

public class JaamSimModel {
	private static final Object createLock = new Object();
	private static JaamSimModel createModel = null;

	private final EventManager eventManager;
	private Simulation simulation;
	private String name;
	private int runNumber;    // labels each run when multiple runs are being made
	private IntegerVector runIndexList;
	private InputErrorListener inputErrorListener;
	private final AtomicLong entityCount = new AtomicLong(0);
	private final ArrayList<Entity> allInstances = new ArrayList<>(100);
	private final HashMap<String, Entity> namedEntities = new HashMap<>(100);

	private File configFile;           // present configuration file
	private File reportDir;         // directory for the output reports
	private FileEntity reportFile;  // file to which the output report will be written
	private PrintStream outStream;  // location where the custom outputs will be written

	private boolean batchRun;       // true if the run is to be terminated automatically
	private boolean scriptMode;     // TRUE if script mode (command line) is specified
	private boolean sessionEdited;  // TRUE if any inputs have been changed after loading a configuration file
	private boolean recordEditsFound;  // TRUE if the "RecordEdits" marker is found in the configuration file
	private boolean recordEdits;       // TRUE if input changes are to be marked as edited

	private FileEntity logFile;
	private int numErrors = 0;
	private int numWarnings = 0;

	private long lastTickForTrace = -1L;
	private long preDefinedEntityCount = 0L;  // Number of entities after loading autoload.cfg

	private final ArrayList<ObjectType> objectTypes = new ArrayList<>();
	private final HashMap<Class<? extends Entity>, ObjectType> objectTypeMap = new HashMap<>();

	private final ArrayList<View> views = new ArrayList<>();
	private int nextViewID = 1;

	public JaamSimModel() {
		this("");
	}

	public JaamSimModel(String name) {
		//Ĭ���¼�����
		eventManager = new EventManager("DefaultEventManager");
		simulation = null;
		this.name = name;
		runNumber = 1;
		runIndexList = new IntegerVector();
		runIndexList.add(1);
	}

	public final void setTimeListener(EventTimeListener l) {
		eventManager.setTimeListener(l);
	}

	public void setInputErrorListener(InputErrorListener l) {
		inputErrorListener = l;
	}

	public void clear() {
		eventManager.clear();
		eventManager.setTraceListener(null);
		simulation = null;

		// Kill all entities
		while (allInstances.size() > 0) {
			Entity ent = allInstances.get(allInstances.size() - 1);
			ent.kill();
		}

		// close warning/error trace file
		closeLogFile();

		// Reset the run number and run indices
		runNumber = 1;

		configFile = null;
		reportDir = null;
		if (reportFile != null) {
			reportFile.close();
			reportFile = null;
		}
		if (outStream != null) {
			outStream.close();
			outStream = null;
		}
		setSessionEdited(false);
		recordEditsFound = false;
		numErrors = 0;
		numWarnings = 0;
		lastTickForTrace = -1L;
	}

	/**
	 * Pre-loads the simulation model with basic objects such as DisplayModels and Units.
	 * �û�������Ԥ���ط���ģ��
	 */
	public void autoLoad() {
		setRecordEdits(false);
		InputAgent.readResource(this, "<res>/inputs/autoload.cfg");
		preDefinedEntityCount = allInstances.get( allInstances.size() - 1 ).getEntityNumber();
	}

	/**
	 * Loads the specified configuration file to create the objects in the model.
	 * @param file - configuration file
	 * @throws URISyntaxException
	 */
	public void configure(File file) throws URISyntaxException {
		configFile = file;
		openLogFile();
		InputAgent.loadConfigurationFile(this, file);

		// The session is not considered to be edited after loading a configuration file
		setSessionEdited(false);

		// Save and close the input trace file
		if (numWarnings == 0 && numErrors == 0) {
			closeLogFile();

			// Open a fresh log file for the simulation run
			openLogFile();
		}
	}

	/**
	 * Performs consistency checks on the model inputs.
	 */
	public void validate() {
		for (Entity each : getClonesOfIterator(Entity.class)) {
			try {
				each.validate();
			}
			catch (Throwable t) {
				if (inputErrorListener != null) {
					inputErrorListener.handleInputError(t, each);
				}
				else {
					System.out.format("Validation Error - %s: %s%n", each.getName(), t.getMessage());
				}
				return;
			}
		}
	}

	/**
	 * Starts the simulation model on a new thread.
	 */
	public void start() {
		//System.out.format("%s.start%n", this);
		validate();
		prepareReportDirectory();
		eventManager.clear();

		// Set up any tracing to be performed
		eventManager.setTraceListener(null);
		if (getSimulation().traceEvents()) {
			String evtName = configFile.getParentFile() + File.separator + getRunName() + ".evt";
			EventRecorder rec = new EventRecorder(evtName);
			eventManager.setTraceListener(rec);
		}
		else if (getSimulation().verifyEvents()) {
			String evtName = configFile.getParentFile() + File.separator + getRunName() + ".evt";
			EventTracer trc = new EventTracer(evtName);
			eventManager.setTraceListener(trc);
		}
		else if (getSimulation().showEventViewer()) {
			eventManager.setTraceListener(EventViewer.getInstance());
		}

		eventManager.setTickLength(getSimulation().getTickLength());

		runNumber = getSimulation().getStartingRunNumber();
		setRunIndexList();
		startRun();
	}

	void initRun() {
		eventManager.scheduleProcessExternal(0, 0, false, new InitModelTarget(this), null);
	}

	/**
	 * Starts a single simulation run.
	 */
	public void startRun() {
		//System.out.format("%s.startRun%n", this);
		initRun();
		double pauseTime = getSimulation().getPauseTime();
		eventManager.resume(eventManager.secondsToNearestTick(pauseTime));
	}

	/**
	 * Performs the first stage of initialization for each entity.
	 */
	public void earlyInit() {
		for (Entity each : getClonesOfIterator(Entity.class)) {
			each.earlyInit();
		}
	}

	/**
	 * Performs the second stage of initialization for each entity.
	 */
	public void lateInit() {
		for (Entity each : getClonesOfIterator(Entity.class)) {
			each.lateInit();
		}
	}

	/**
	 * Performs the start-up procedure for each entity.
	 */
	public void startUp() {
		double startTime = getSimulation().getStartTime();
		long startTicks = eventManager.secondsToNearestTick(startTime);
		for (Entity each : getClonesOfIterator(Entity.class)) {
			if (!each.isActive())
				continue;
			EventManager.scheduleTicks(startTicks, 0, true, new StartUpTarget(each), null);
		}
	}

	public void doPauseCondition() {
		if (getSimulation().isPauseConditionSet())
			EventManager.scheduleUntil(pauseModelTarget, pauseCondition, null);
	}

	private final PauseModelTarget pauseModelTarget = new PauseModelTarget(this);

	private final Conditional pauseCondition = new Conditional() {
		@Override
		public boolean evaluate() {
			double simTime = EventManager.simSeconds();
			return getSimulation().isPauseConditionSatisfied(simTime);
		}
	};

	/**
	 * Reset the statistics for each entity.
	 */
	public void clearStatistics() {
		for (Entity ent : getClonesOfIterator(Entity.class)) {
			if (!ent.isActive())
				continue;
			ent.clearStatistics();
		}

		// Reset state statistics
		for (StateEntity each : getClonesOfIterator(StateEntity.class)) {
			if (!each.isActive())
				continue;
			each.collectInitializationStats();
		}
	}

	/**
	 * Temporarily stops the simulation model at the present simulation time.
	 */
	public void pause() {
		//System.out.format("%s.pause%n", this);
		eventManager.pause();
	}

	/**
	 * Re-starts the simulation model at the present simulation and allows it to proceed to the
	 * specified pause time.
	 * @param simTime - next pause time
	 */
	public void resume(double simTime) {
		eventManager.resume(eventManager.secondsToNearestTick(simTime));
	}

	/**
	 * Sets the simulation time to zero and re-initializes the model.
	 */
	public void reset() {
		eventManager.pause();
		eventManager.clear();
		killGeneratedEntities();

		// Perform earlyInit
		for (Entity each : getClonesOfIterator(Entity.class)) {
			// Try/catch is required because some earlyInit methods use simTime which is only
			// available from a process thread, which is not the case when called from endRun
			try {
				each.earlyInit();
			} catch (Exception e) {}
		}

		// Perform lateInit
		for (Entity each : getClonesOfIterator(Entity.class)) {
			// Try/catch is required because some lateInit methods use simTime which is only
			// available from a process thread, which is not the case when called from endRun
			try {
				each.lateInit();
			} catch (Exception e) {}
		}

		// Reset the run number and run indices
		runNumber = getSimulation().getStartingRunNumber();
		setRunIndexList();

		// Close the output reports
		if (reportFile != null) {
			reportFile.close();
			reportFile = null;
		}
		if (outStream != null) {
			outStream.close();
			outStream = null;
		}
	}

	/**
	 * Prepares the model for the next simulation run number.
	 */
	public void endRun() {

		// Execute the end of run method for each entity
		for (Entity each : getClonesOfIterator(Entity.class)) {
			if (!each.isActive())
				continue;
			each.doEnd();
		}

		// Print the output report
		if (getSimulation().getPrintReport())
			InputAgent.printReport(this, EventManager.simSeconds());

		// Print the selected outputs
		if (getSimulation().getRunOutputList().getValue() != null) {
			if (outStream == null) {
				outStream = getOutStream();
				InputAgent.printRunOutputHeaders(this, outStream);
			}
			InputAgent.printRunOutputs(this, outStream, EventManager.simSeconds());
		}

		// Increment the run number and check for last run
		if (isLastRun()) {
			end();
			return;
		}

		// Start the next run
		runNumber++;
		setRunIndexList();

		eventManager.pause();
		eventManager.clear();
		killGeneratedEntities();

		new Thread(new Runnable() {
			@Override
			public void run() {
				startRun();
			}
		}).start();
	}

	/**
	 * Destroys the entities that were generated during the present simulation run.
	 */
	public void killGeneratedEntities() {
		for (int i = 0; i < allInstances.size();) {
			Entity ent = allInstances.get(i);
			if (ent.testFlag(Entity.FLAG_GENERATED))
				ent.kill();
			else
				i++;
		}
	}

	/**
	 * Ends a set of simulation runs.
	 */
	public void end() {

		// Close warning/error trace file
		LogBox.logLine("Made it to do end at");
		closeLogFile();

		// Always terminate the run when in batch mode
		if (isBatchRun() || getSimulation().getExitAtStop())
			GUIFrame.shutdown(0);

		pause();
	}

	/**
	 * Returns whether events are being executed.
	 * @return true if the events are being executed
	 */
	public boolean isRunning() {
		return eventManager.isRunning();
	}

	/**
	 * Returns the present simulation time in seconds.
	 * @return simulation time
	 */
	public double getSimTime() {
		return eventManager.ticksToSeconds(eventManager.getTicks());
	}

	/**
	 * Evaluates the specified expression and returns its value as a string.
	 * Any type of result can be returned by the expression, including an entity or an array.
	 * If it returns a number, it must be dimensionless.
	 * @param expString - expression to be evaluated
	 * @return expression value as a string
	 */
	public String getStringValue(String expString) {
		double simTime = getSimTime();
		try {
			Class<? extends Unit> unitType = DimensionlessUnit.class;
			Entity thisEnt = getSimulation();
			StringProvExpression strProv = new StringProvExpression(expString, thisEnt, unitType);
			return strProv.getNextString(simTime);
		}
		catch (ExpError e) {
			return "Cannot evaluate";
		}
	}

	/**
	 * Evaluates the specified expression and returns its value.
	 * The expression must return a dimensionless number.
	 * All other types of expressions return NaN.
	 * @param expString - expression to be evaluated
	 * @return expression value
	 */
	public double getDoubleValue(String expString) {
		double simTime = getSimTime();
		try {
			Class<? extends Unit> unitType = DimensionlessUnit.class;
			Entity thisEnt = getSimulation();
			SampleExpression sampleExp = new SampleExpression(expString, thisEnt, unitType);
			return sampleExp.getNextSample(simTime);
		}
		catch (ExpError e) {
			return Double.NaN;
		}
	}

	/**
	 * Creates a new entity for the specified class with the specified name.
	 * If the name already used, "_1", "_2", etc. will be appended to the name until an unused
	 * name is found.
	 * @param type - type of entity to be created
	 * @param name - name for the created entity
	 */
	public void defineEntity(String type, String name) {
		try {
			Class<? extends Entity> klass = Input.parseEntityType(this, type);
			InputAgent.defineEntityWithUniqueName(this, klass, name, "_", true);
		}
		catch (InputErrorException e) {
			return;
		}
	}

	/**
	 * Sets the input for the specified entity and keyword to the specified string.
	 * @param entName - name of the entity whose input is to be set
	 * @param keyword - input keyword whose value is to be set
	 * @param arg - input string as it would appear in the Input Editor
	 */
	public void setInput(String entName, String keyword, String arg) {
		setRecordEdits(true);
		Entity ent = getNamedEntity(entName);
		KeywordIndex kw = InputAgent.formatInput(keyword, arg);
		InputAgent.apply(ent, kw);
	}

	/**
	 * Writes the inputs for the simulation model to the specified file.
	 * @param file - file to which the model inputs are to be saved
	 */
	public void save(File file) {
		InputAgent.printNewConfigurationFileWithName(this, file.getName());
		configFile = file;
	}

	public EventManager getEventManager() {
		return eventManager;
	}

	public Simulation getSimulation() {
		if (simulation == null) {
			for (Simulation ent : getInstanceIterator(Simulation.class)) {
				simulation = ent;
				break;
			}
		}
		return simulation;
	}

	public boolean isMultipleRuns() {
		return getSimulation().getEndingRunNumber() > getSimulation().getStartingRunNumber();
	}

	public boolean isFirstRun() {
		return runNumber == getSimulation().getStartingRunNumber();
	}

	public boolean isLastRun() {
		return runNumber >= getSimulation().getEndingRunNumber();
	}

	/**
	 * Returns the run indices that correspond to a given run number.
	 * @param n - run number.
	 * @param rangeList - maximum value for each index.
	 * @return run indices.
	 */
	public static IntegerVector getRunIndexList(int n, IntegerVector rangeList) {
		IntegerVector indexList = new IntegerVector(rangeList.size());
		indexList.fillWithEntriesOf(rangeList.size(), 0);
		int denom = 1;
		for (int i=rangeList.size()-1; i>=0; i--) {
			indexList.set(i, (n-1)/denom % rangeList.get(i) + 1);
			denom *= rangeList.get(i);
		}
		return indexList;
	}

	/**
	 * Returns the run number that corresponds to a given set of run indices.
	 * @param indexList - run indices.
	 * @param rangeList - maximum value for each index.
	 * @return run number.
	 */
	public static int getRunNumber(IntegerVector indexList, IntegerVector rangeList) {
		int n = 1;
		int factor = 1;
		for (int i=indexList.size()-1; i>=0; i--) {
			n += (indexList.get(i)-1)*factor;
			factor *= rangeList.get(i);
		}
		return n;
	}

	/**
	 * Returns the input format used to specify a set of run indices.
	 * @param indexList - run indices.
	 * @return run code.
	 */
	public static String getRunCode(IntegerVector indexList) {
		StringBuilder sb = new StringBuilder();
		sb.append(indexList.get(0));
		for (int i=1; i<indexList.size(); i++) {
			sb.append("-").append(indexList.get(i));
		}
		return sb.toString();
	}

	public void setRunNumber(int n) {
		runNumber = n;
		setRunIndexList();
	}

	public void setRunIndexList() {
		runIndexList = getRunIndexList(runNumber, getSimulation().getRunIndexDefinitionList());
	}

	public int getRunNumber() {
		return runNumber;
	}

	public IntegerVector getRunIndexList() {
		return runIndexList;
	}

	public String getRunCode() {
		return getRunCode(runIndexList);
	}

	public String getRunHeader() {
		return String.format("##### RUN %s #####", getRunCode());
	}

	final long getNextEntityID() {
		return entityCount.incrementAndGet();
	}

	public final Entity getNamedEntity(String name) {
		synchronized (allInstances) {
			//namedEntities��һ��HashMap���ϣ����ݴ���ļ���ȡ��Ӧ��ֵ
			return namedEntities.get(name);
		}
	}

	public final long getEntitySequence() {
		long seq = (long)allInstances.size() << 32;
		seq += entityCount.get();
		return seq;
	}

	private final int idToIndex(long id) {
		int lowIdx = 0;
		int highIdx = allInstances.size() - 1;

		while (lowIdx <= highIdx) {
			int testIdx = (lowIdx + highIdx) >>> 1; // Avoid sign extension
			long testNum = allInstances.get(testIdx).getEntityNumber();

			if (testNum < id) {
				lowIdx = testIdx + 1;
				continue;
			}

			if (testNum > id) {
				highIdx = testIdx - 1;
				continue;
			}

			return testIdx;
		}

		// Entity number not found
		return -(lowIdx + 1);
	}

	public final Entity idToEntity(long id) {
		synchronized (allInstances) {
			int idx = this.idToIndex(id);
			if (idx < 0)
				return null;

			return allInstances.get(idx);
		}
	}

	public final ArrayList<? extends Entity> getEntities() {
		synchronized(allInstances) {
			return allInstances;
		}
	}

	static JaamSimModel getCreateModel() {
		synchronized (createLock) {
			JaamSimModel mod = createModel;
			createModel = null;
			return mod;
		}
	}

	public final <T extends Entity> T createInstance(Class<T> proto) {
		T ent = null;
		try {
			synchronized (createLock) {
				createModel = this;
				ent = proto.newInstance();
			}
			addInstance(ent);
		}
		catch (Throwable e) {}

		return ent;
	}

	final void renameEntity(Entity e, String newName) {
		synchronized (allInstances) {
			// Unregistered entities do not appear in the named entity hashmap, no consistency checks needed
			if (!e.testFlag(Entity.FLAG_REGISTERED)) {
				e.entityName = newName;
				return;
			}

			if (namedEntities.get(newName) != null)
				throw new ErrorException("Entity name: %s is already in use.", newName);

			String oldName = e.entityName;
			if (oldName != null && namedEntities.remove(oldName) != e)
				throw new ErrorException("Named Entities Internal Consistency error");

			e.entityName = newName;
			namedEntities.put(newName, e);
		}
	}

	final void addInstance(Entity e) {
		synchronized(allInstances) {
			allInstances.add(e);
		}
	}

	final void restoreInstance(Entity e) {
		synchronized(allInstances) {
			int index = idToIndex(e.getEntityNumber());
			if (index >= 0) {
				throw new ErrorException("Entity already included in allInstances: %s", e);
			}
			allInstances.add(-index - 1, e);
		}
	}

	final void removeInstance(Entity e) {
		synchronized (allInstances) {
			int index = idToIndex(e.getEntityNumber());
			if (index < 0)
				return;

			if (e != allInstances.remove(index))
				throw new ErrorException("Internal Consistency Error - Entity List");

			if (e.testFlag(Entity.FLAG_REGISTERED)) {
				if (e != namedEntities.remove(e.entityName))
					throw new ErrorException("Named Entities Internal Consistency error: %s", e);
			}

			e.entityName = null;
			e.setFlag(Entity.FLAG_DEAD);
		}
	}

	/**
	 * Returns an Iterator that loops over the instances of the specified class. It does not
	 * include instances of any sub-classes of the class.
	 * The specified class must be a sub-class of Entity.
	 * @param proto - specified class
	 * @return Iterator for instances of the class
	 */
	public <T extends Entity> InstanceIterable<T> getInstanceIterator(Class<T> proto){
		return new InstanceIterable<>(this, proto);
	}

	/**
	 * Returns an Iterator that loops over the instances of the specified class and its
	 * sub-classes.
	 * The specified class must be a sub-class of Entity.
	 * @param proto - specified class
	 * @return Iterator for instances of the class and its sub-classes
	 */
	public <T extends Entity> ClonesOfIterable<T> getClonesOfIterator(Class<T> proto){
		return new ClonesOfIterable<>(this, proto);
	}

	/**
	 * Returns an iterator that loops over the instances of the specified class and its
	 * sub-classes, but of only those classes that implement the specified interface.
	 * The specified class must be a sub-class of Entity.
	 * @param proto - specified class
	 * @param iface - specified interface
	 * @return Iterator for instances of the class and its sub-classes that implement the specified interface
	 */
	public <T extends Entity> ClonesOfIterableInterface<T> getClonesOfIterator(Class<T> proto, Class<?> iface){
		return new ClonesOfIterableInterface<>(this, proto, iface);
	}

	public void addObjectType(ObjectType ot) {
		synchronized (objectTypes) {
			objectTypes.add(ot);
			objectTypeMap.put(ot.getJavaClass(), ot);
		}
	}

	public void removeObjectType(ObjectType ot) {
		synchronized (objectTypes) {
			objectTypes.remove(ot);
			objectTypeMap.remove(ot.getJavaClass());
		}
	}

	public ArrayList<ObjectType> getObjectTypes() {
		synchronized (objectTypes) {
			return objectTypes;
		}
	}

	public ObjectType getObjectTypeForClass(Class<? extends Entity> klass) {
		synchronized (objectTypes) {
			return objectTypeMap.get(klass);
		}
	}

	public void addView(View v) {
		synchronized (views) {
			views.add(v);
		}
	}

	public void removeView(View v) {
		synchronized (views) {
			views.remove(v);
		}
	}

	public ArrayList<View> getViews() {
		synchronized (views) {
			return views;
		}
	}

	public int getNextViewID() {
		nextViewID++;
		return nextViewID;
	}

	/**
	 * Sets the present configuration file.
	 * @param file - the present configuration file.
	 */
	public void setConfigFile(File file) {
		configFile = file;
	}

	/**
	 * Returns the present configuration file.
	 * Null is returned if no configuration file has been loaded or saved yet.
	 * @return present configuration file
	 */
	public File getConfigFile() {
		return configFile;
	}

	/**
	 * Returns the name of the simulation run.
	 * For example, if the configuration file name is "case1.cfg", then the run name is "case1".
	 * @return name of the simulation run
	 */
	public String getRunName() {
		if (configFile == null)
			return "";

		String name = configFile.getName();
		int index = name.lastIndexOf('.');
		if (index == -1)
			return name;

		return name.substring(0, index);
	}

	private String getReportDirectory() {
		if (reportDir != null)
			return reportDir.getPath() + File.separator;

		if (configFile != null)
			return configFile.getParentFile().getPath() + File.separator;

		return null;
	}

	public String getReportFileName(String name) {
		return getReportDirectory() + name;
	}

	public void setReportDirectory(File dir) {
		reportDir = dir;
		if (reportDir == null)
			return;
		if (!reportDir.exists() && !reportDir.mkdirs())
			throw new InputErrorException("Was unable to create the Report Directory: %s", reportDir.toString());
	}

	public void prepareReportDirectory() {
		if (reportDir != null) reportDir.mkdirs();
	}

	public FileEntity getReportFile() {
		if (reportFile == null) {
			StringBuilder tmp = new StringBuilder("");
			tmp.append(getReportFileName(getRunName()));
			tmp.append(".rep");
			reportFile = new FileEntity(tmp.toString());
		}
		return reportFile;
	}

	public PrintStream getOutStream() {
		if (outStream == null) {

			// Select either standard out or a file for the outputs
			outStream = System.out;
			if (!isScriptMode()) {
				StringBuilder sb = new StringBuilder();
				sb.append(getReportFileName(getRunName()));
				sb.append(".dat");
				try {
					outStream = new PrintStream(sb.toString());
				}
				catch (FileNotFoundException e) {
					throw new InputErrorException(
							"FileNotFoundException thrown trying to open PrintStream: " + e );
				}
				catch (SecurityException e) {
					throw new InputErrorException(
							"SecurityException thrown trying to open PrintStream: " + e );
				}
			}
		}
		return outStream;
	}

	public void setBatchRun(boolean bool) {
		batchRun = bool;
	}

	public boolean isBatchRun() {
		return batchRun;
	}

	public void setScriptMode(boolean bool) {
		scriptMode = bool;
	}

	public boolean isScriptMode() {
		return scriptMode;
	}

	public void setSessionEdited(boolean bool) {
		if (bool == sessionEdited)
			return;
		sessionEdited = bool;
		if (GUIFrame.getInstance() != null)
			GUIFrame.getInstance().updateSaveButton();
	}

	public boolean isSessionEdited() {
		return sessionEdited;
	}

	/**
	 * Specifies whether a RecordEdits marker was found in the present configuration file.
	 * @param bool - TRUE if a RecordEdits marker was found.
	 */
	public void setRecordEditsFound(boolean bool) {
		recordEditsFound = bool;
	}

	/**
	 * Indicates whether a RecordEdits marker was found in the present configuration file.
	 * @return - TRUE if a RecordEdits marker was found.
	 */
	public boolean isRecordEditsFound() {
		return recordEditsFound;
	}

	/**
	 * Sets the "RecordEdits" mode for the InputAgent.
	 * ��InputAgent����ΪrecordEditsģʽ
	 * @param bool - boolean value for the RecordEdits mode
	 */
	public void setRecordEdits(boolean bool) {
		recordEdits = bool;
	}

	/**
	 * Returns the "RecordEdits" mode for the InputAgent.
	 * <p>
	 * When RecordEdits is TRUE, any model inputs that are changed and any objects that
	 * are defined are marked as "edited". When FALSE, model inputs and object
	 * definitions are marked as "unedited".
	 * ��recordeΪ��ʱ���κα��ı��ģ��������κα��ı�Ķ��󣬱�����Ϊ���ѱ༭������Ϊ��ʱ��ģ������Ͷ��󣬶��屻���Ϊ��δ�༭��
	 * <p>
	 * RecordEdits mode is used to determine the way JaamSim saves a configuration file
	 * through the graphical user interface. Object definitions and model inputs
	 * that are marked as unedited will be copied exactly as they appear in the original
	 * configuration file that was first loaded.  Object definitions and model inputs
	 * that are marked as edited will be generated automatically by the program.
	 *recordeģʽ����ȷ��JaamSim���������ļ��ķ�ʽ��ͨ��ͼ���û����档�������ģ�����룬�����Ϊδ�༭�����ݽ�����ȫ���ƣ�����������ԭ���г���һ�����״μ��ص������ļ����������ģ�����룬���Ϊ�ѱ༭�����ݽ��ɳ����Զ����ɡ�
	 * @return the RecordEdits mode for the InputAgent.
	 */
	public boolean isRecordEdits() {
		//�����Ƿ���isRecordEditsģʽ
		return recordEdits;
	}

	public FileEntity getLogFile() {
		return logFile;
	}

	public void openLogFile() {
		String logFileName = getRunName() + ".log";
		URI logURI = null;
		try {
			URI confURI = configFile.toURI();
			logURI = confURI.resolve(new URI(null, logFileName, null)); // The new URI here effectively escapes the file name
			logFile = new FileEntity(logURI.getPath());
		}
		catch( Exception e ) {
			InputAgent.logWarning(this, "Could not create log file.%n%s", e.getMessage());
		}
	}

	public void closeLogFile() {
		if (logFile == null)
			return;

		logFile.close();

		// Delete the log file if no errors or warnings were recorded
		if (numErrors == 0 && numWarnings == 0) {
			logFile.delete();
		}

		logFile = null;
	}

	public void logMessage(String msg) {
		if (logFile == null)
			return;

		logFile.write(msg);
		logFile.newLine();
		logFile.flush();
	}

	public void recordError() {
		numErrors++;
	}

	public int getNumErrors() {
		return numErrors;
	}

	public void recordWarning() {
		numWarnings++;
	}

	public int getNumWarnings() {
		return numWarnings;
	}

	public void setLastTickForTrace(long tick) {
		lastTickForTrace = tick;
	}

	public long getLastTickForTrace() {
		return lastTickForTrace;
	}

	public boolean isPreDefinedEntity(Entity ent) {
		return ent.getEntityNumber() <= preDefinedEntityCount;
	}

	@Override
	public String toString() {
		return name;
	}

}
