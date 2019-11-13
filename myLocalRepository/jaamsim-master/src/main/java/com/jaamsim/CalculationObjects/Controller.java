/*
 * JaamSim Discrete Event Simulation
 * Copyright (C) 2013 Ausenco Engineering Canada Inc.
 * Copyright (C) 2018-2019 JaamSim Software Inc.
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
package com.jaamsim.CalculationObjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.jaamsim.Graphics.DisplayEntity;
import com.jaamsim.basicsim.Entity;
import com.jaamsim.basicsim.EntityTarget;
import com.jaamsim.events.ProcessTarget;
import com.jaamsim.input.Keyword;
import com.jaamsim.input.ValueInput;
import com.jaamsim.units.TimeUnit;

/**
 * Generates update signals that are sent to the objects managed by the Controller.
 * @author Harry King
 *
 */
public class Controller extends DisplayEntity {

	@Keyword(description = "Time interval between update signals.",
	         exampleList = {"100 ms"})
	private final ValueInput samplingTime;

	private final ArrayList<Controllable> entityList;  // Entities controlled by this Controller.
	private int count;  // Number of update cycle completed.

	private final ProcessTarget doUpdate = new DoUpdateTarget(this);

	{
		samplingTime = new ValueInput("SamplingTime", KEY_INPUTS, 1.0d);
		samplingTime.setUnitType(TimeUnit.class);
		samplingTime.setValidRange(0.0, Double.POSITIVE_INFINITY);
		this.addInput(samplingTime);
	}

	public Controller() {
		entityList = new ArrayList<>();
	}

	@Override
	public void earlyInit() {
		super.earlyInit();
		count = 0;

		// Prepare a list of the calculation entities managed by this controller
		entityList.clear();
		for (Entity ent : getJaamSimModel().getClonesOfIterator(Entity.class, Controllable.class)) {
			Controllable con = (Controllable) ent;
			if (con.getController() == this)
				entityList.add(con);
		}

		// Sort the calculation entities into the correct sequence
		Collections.sort(entityList, new SequenceCompare());
	}

	// Sorts by increasing sequence number
	private static class SequenceCompare implements Comparator<Controllable> {
		@Override
		public int compare(Controllable c1, Controllable c2) {
			return Double.compare(c1.getSequenceNumber(), c2.getSequenceNumber());
		}
	}

	@Override
	public void startUp() {
		super.startUp();

		// Schedule the first update
		this.scheduleProcess(samplingTime.getValue(), 5, doUpdate);
	}

	private static class DoUpdateTarget extends EntityTarget<Controller> {
		DoUpdateTarget(Controller ent) {
			super(ent, "doUpdate");
		}

		@Override
		public void process() {
			ent.doUpdate();
		}
	}

	public void doUpdate() {

		// Update the last value for each entity
		double simTime = this.getSimTime();
		for (Controllable ent : entityList) {
			ent.update(simTime);
		}

		// Increment the number of cycles
		count++;

		// Schedule the next update
		this.scheduleProcess(samplingTime.getValue(), 5, doUpdate);
	}

	public int getCount() {
		return count;
	}

}
