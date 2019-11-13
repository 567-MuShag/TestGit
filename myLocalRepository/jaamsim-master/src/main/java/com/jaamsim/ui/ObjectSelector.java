/*
 * JaamSim Discrete Event Simulation
 * Copyright (C) 2011 Ausenco Engineering Canada Inc.
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
package com.jaamsim.ui;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;

import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.jaamsim.Graphics.DisplayEntity;
import com.jaamsim.Graphics.EntityLabel;
import com.jaamsim.basicsim.Entity;
import com.jaamsim.basicsim.ErrorException;
import com.jaamsim.basicsim.JaamSimModel;
import com.jaamsim.basicsim.ObjectType;
import com.jaamsim.basicsim.Simulation;
import com.jaamsim.input.Input;
import com.jaamsim.input.InputAgent;
import com.jaamsim.units.Unit;

public class ObjectSelector extends FrameBox {
	private static ObjectSelector myInstance;

	// Tree view properties
	private final DefaultMutableTreeNode top;
	private final DefaultTreeModel treeModel;
	private final JTree tree;
	private final JScrollPane treeView;
	static Entity currentEntity;

	private long entSequence;

	private static final int MAX_GENERATED_ENTITIES = 10000;

	public ObjectSelector() {
		super( "Object Selector" );
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(FrameBox.getCloseListener("ShowObjectSelector"));
		addWindowFocusListener(new MyFocusListener());
		//DefaultMutableTreeNode树数据结构中的通用节点
		top = new DefaultMutableTreeNode();
		//DefaultTreeModel使用TreeNodes的简单树数据结构模型
		treeModel = new DefaultTreeModel(top);
		tree = new JTree();
		//设置提供数据的模型
		tree.setModel(treeModel);
		//选择返回的模型
		tree.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
		//确定treemodel的根节点是否可见
		tree.setRootVisible(false);
		//设置showsrothandles的属性值，它指定是否显示节点句柄
		tree.setShowsRootHandles(true);
		//通过选择树种的另一个节点、树的数据中的更改或通过其他方法来确定编辑被中断时发生了什么情况，若为true，可导致在编辑被中断时自动保存
		tree.setInvokesStopCellEditing(true);
		//创建树滚动窗格并将树添加到其中
		treeView = new JScrollPane(tree);
		getContentPane().add(treeView);

		entSequence = 0;
		//获取模型
		Simulation simulation = GUIFrame.getJaamSimModel().getSimulation();
		setLocation(simulation.getObjectSelectorPos().get(0), simulation.getObjectSelectorPos().get(1));
		setSize(simulation.getObjectSelectorSize().get(0), simulation.getObjectSelectorSize().get(1));

		addComponentListener(new ComponentAdapter() {

			@Override
			public void componentMoved(ComponentEvent e) {
				simulation.setObjectSelectorPos(getLocation().x, getLocation().y);
			}

			@Override
			public void componentResized(ComponentEvent e) {
				simulation.setObjectSelectorSize(getSize().width, getSize().height);
			}
		});

		tree.addTreeSelectionListener( new MyTreeSelectionListener() );
		treeModel.addTreeModelListener( new MyTreeModelListener(tree) );

		tree.addMouseListener(new MyMouseListener());
		tree.addKeyListener(new MyKeyListener());
	}

	@Override
	public void setEntity(Entity ent) {

		if (ent == currentEntity)
			return;
		currentEntity = ent;

		if (tree == null)
			return;

		JaamSimModel simModel = GUIFrame.getJaamSimModel();
		if (simModel == null || simModel.getSimulation() == null)
			return;

		long curSequence = simModel.getEntitySequence();
		if (entSequence != curSequence) {
			entSequence = curSequence;
			updateTree(simModel);
		}

		if (currentEntity == null) {
			tree.setSelectionPath(null);
			tree.setEditable(false);
			return;
		}
		//设置树可以编辑
		tree.setEditable(true);

		DefaultMutableTreeNode root = (DefaultMutableTreeNode)tree.getModel().getRoot();
		Enumeration<?> e = root.depthFirstEnumeration();
		while (e.hasMoreElements()) {
			DefaultMutableTreeNode aNode = (DefaultMutableTreeNode)e.nextElement();
			if (aNode.getUserObject() == currentEntity) {
				TreePath path = new TreePath(aNode.getPath());
				tree.scrollPathToVisible(path);
				tree.setSelectionPath(path);
				return;
			}
		}

		// Entity not found in the tree  树中没有找到实体
		tree.setSelectionPath(null);
		//设置为不可编辑
		tree.setEditable(false);
	}
	//更新操作
	@Override
	public void updateValues(double simTime) {
		GUIFrame gui = GUIFrame.getInstance();
		if (!this.isVisible() || gui == null || gui.getSimState() == GUIFrame.SIM_STATE_RUNNING)
			return;

		JaamSimModel simModel = GUIFrame.getJaamSimModel();
		if (simModel == null || simModel.getSimulation() == null)
			return;

		long curSequence = simModel.getEntitySequence();
		if (entSequence != curSequence) {
			entSequence = curSequence;
			updateTree(simModel);
		}
	}

	public static void allowUpdate() {
		myInstance.entSequence = 0;
	}

	/**
	 * Returns the only instance of the Object Selector   返回对象选择器的唯一实例
	 */
	public static synchronized ObjectSelector getInstance() {
		if (myInstance == null)
			myInstance = new ObjectSelector();

		myInstance.treeView.getHorizontalScrollBar().getModel().setValue(0);

		return myInstance;
	}

	private synchronized static void killInstance() {
		myInstance = null;
	}

	@Override
	public void dispose() {
		killInstance();
		currentEntity = null;
		super.dispose();
	}

	private void updateTree(JaamSimModel simModel) {

		if (tree == null || top == null)
			return;

		// Store all the expanded paths   存储所有展开的路径
		Enumeration<TreePath> expandedPaths = tree.getExpandedDescendants(new TreePath(top));

		// Identify the selected entity (cannot use currentEntity -- would race with setEntity)  识别选定的实体
		Entity selectedEnt = null;
		TreePath selectedPath = tree.getSelectionPath();
		if (selectedPath != null) {
			Object selectedObj = ((DefaultMutableTreeNode)selectedPath.getLastPathComponent()).getUserObject();
			if (selectedObj instanceof Entity)
				selectedEnt = (Entity)selectedObj;
		}

		// Clear the present tree  清除当前的树
		top.removeAllChildren();

		// Add the instance for Simulation to the top of the tree as a single leaf node  将模拟实例作为单个叶子节点添加到树的顶部
		Simulation simulation = simModel.getSimulation();
		top.add(new DefaultMutableTreeNode(simulation, false));

		// Add the instance for TLS if present    如果存在，为TLS添加实例
		Entity tls = GUIFrame.getJaamSimModel().getNamedEntity("TLS");
		if (tls != null)
			top.add(new DefaultMutableTreeNode(tls, false));

		// Create the tree structure for palettes and object types in the correct order   按照正确的顺序为调色板和对象类型创建树结构
		for (int i = 0; i < simModel.getObjectTypes().size(); i++) {
			try {
				final ObjectType type = simModel.getObjectTypes().get(i);
				if (type == null)
					continue;
				String paletteName = type.getPaletteName();
				String typeName = type.getName();

				// Find or create the node for the palette   查找或创建调色板的节点
				DefaultMutableTreeNode paletteNode = getNodeFor_In(paletteName, top);
				if (paletteNode == null) {
					paletteNode = new DefaultMutableTreeNode(paletteName);
					top.add(paletteNode);
				}

				// Add the node for the Object Type to the palette    将对象类型的节点添加到调色板
				if (typeName == null || typeName.equals(paletteName))
					continue;
				DefaultMutableTreeNode typeNode = new DefaultMutableTreeNode(typeName, true);
				paletteNode.add(typeNode);
			}
			catch (IndexOutOfBoundsException e) {}
		}

		// Prepare a sorted list of entities   准备一个已排序的实体列表
		int numGenerated = 0;
		final ArrayList<? extends Entity> allEnts = GUIFrame.getJaamSimModel().getEntities();
		ArrayList<Entity> entityList = new ArrayList<>();
		for (int i = 0; i < allEnts.size(); i++) {
			try {
				final Entity ent = allEnts.get(i);

				// The instance for Simulation has already been added
				if (ent == simulation)
					continue;

				// The instance for TLS has already been added
				if (ent == tls)
					continue;

				// Do not include the units
				if (ent instanceof Unit)
					continue;

				// Apply an upper bound on the number of generated entities to display
				if (ent.testFlag(Entity.FLAG_GENERATED)) {
					if (numGenerated > MAX_GENERATED_ENTITIES)
						continue;
					numGenerated++;
				}

				entityList.add(ent);
			}
			catch (IndexOutOfBoundsException e) {}
		}
		try {
			Collections.sort(entityList, selectorSortOrder);
		}
		catch (Throwable t) {}

		// Loop through the entities in the model   循环遍历模型中的实体
		for (int i=0; i<entityList.size(); i++) {
			try {
				final Entity ent = entityList.get(i);

				// Determine the object type for this entity   确定此实体的对象类型
				final ObjectType type = ent.getObjectType();
				if (type == null)
					continue;
				String paletteName = type.getPaletteName();
				String typeName = type.getName();

				// Find the palette node for this entity  找到此实体的调色板节点
				DefaultMutableTreeNode paletteNode = getNodeFor_In(paletteName, top);
				if (paletteNode == null)
					continue;

				// Find the object type node for this entity   查找此实体的对象类型节点
				DefaultMutableTreeNode typeNode = getNodeFor_In(typeName, paletteNode);
				if (typeName != null && typeName.equals(paletteName)) {
					typeNode = paletteNode;
				}
				if (typeNode == null)
					continue;

				// Add the entity to the object type node  将实体添加到对象类型节点
				DefaultMutableTreeNode entityNode = new DefaultMutableTreeNode(ent, false);
				typeNode.add(entityNode);
			}
			catch (IndexOutOfBoundsException e) {}
		}

		// Remove any object type tree nodes that have no entities  删除任何没有实体的对象类型树节点
		ArrayList<DefaultMutableTreeNode> nodesToRemove = new ArrayList<>();
		Enumeration<?> paletteEnum = top.children();
		while (paletteEnum.hasMoreElements()) {
			DefaultMutableTreeNode paletteNode = (DefaultMutableTreeNode)paletteEnum.nextElement();
			Enumeration<?> typeEnum = paletteNode.children();
			while (typeEnum.hasMoreElements()) {
				DefaultMutableTreeNode typeNode = (DefaultMutableTreeNode)typeEnum.nextElement();
				if (!typeNode.getAllowsChildren())
					continue;
				if (typeNode.isLeaf())
					nodesToRemove.add(typeNode);
			}
			for (DefaultMutableTreeNode typeNode : nodesToRemove) {
				paletteNode.remove(typeNode);
			}
			nodesToRemove.clear();
		}

		// Remove any palettes that have no object types left  删除任何没有对象类型的调色板
		paletteEnum = top.children();
		while (paletteEnum.hasMoreElements()) {
			DefaultMutableTreeNode paletteNode = (DefaultMutableTreeNode)paletteEnum.nextElement();

			// Do not remove any of the special nodes such as the instance for Simulation  不删除任何特殊节点，例如用于模拟的实例
			if (!paletteNode.getAllowsChildren())
				continue;

			if (paletteNode.isLeaf())
				nodesToRemove.add(paletteNode);
		}
		for (DefaultMutableTreeNode paletteNode : nodesToRemove) {
			top.remove(paletteNode);
		}

		// Refresh the tree
		treeModel.reload(top);

		// Restore the path to the selected entity  还原到所选实体的路径
		if (selectedEnt != null) {
			TreePath path = ObjectSelector.getPathToEntity(selectedEnt, top);
			if (path != null)
				tree.setSelectionPath(path);
		}

		// Restore all the expanded paths  还原所有展开的路径
		while (expandedPaths != null && expandedPaths.hasMoreElements()) {
			TreePath oldPath = expandedPaths.nextElement();
			if (oldPath.getPathCount() < 2)
				continue;

			// Path to a palette
			DefaultMutableTreeNode oldPaletteNode = (DefaultMutableTreeNode) (oldPath.getPath())[1];
			String paletteName = (String) (oldPaletteNode.getUserObject());
			DefaultMutableTreeNode paletteNode = getNodeFor_In(paletteName, top);
			if (paletteNode == null)
				continue;
			if (oldPath.getPathCount() == 2) {
				Object[] nodeList = { top, paletteNode };
				tree.expandPath(new TreePath(nodeList));
				continue;
			}

			// Path to an object type
			DefaultMutableTreeNode oldTypeNode = (DefaultMutableTreeNode) (oldPath.getPath())[2];
			String typeName = (String) (oldTypeNode.getUserObject());
			DefaultMutableTreeNode typeNode = getNodeFor_In(typeName, paletteNode);
			if (typeNode == null)
				continue;
			Object[] nodeList = { top, paletteNode, typeNode };
			tree.expandPath(new TreePath(nodeList));
		}
	}

	private static class EntityComparator implements Comparator<Entity> {
		@Override
		public int compare(Entity ent0, Entity ent1) {

			// Put any null entities at the end of the list  将任何空实体放在列表的末尾
			if (ent0 == null && ent1 == null)
				return 0;
			if (ent0 != null && ent1 == null)
				return -1;
			if (ent0 == null && ent1 != null)
				return 1;

			// Otherwise, sort in natural order  否则，按自然顺序排序
			return Input.uiSortOrder.compare(ent0, ent1);
		}
	}
	private static final Comparator<Entity> selectorSortOrder = new EntityComparator();

	/**
	 * Returns a tree node for the specified userObject in the specified parent.  返回指定父对象中指定userObject的树节点
	 * If a node, already exists for this parent, it is returned. If it does   如果该父节点已经存在，则返回该节点。如果没有存在就返回空
	 * not exist, then null is returned.
	 * @param userObject - object for the tree node.
	 * @param parent - object's parent
	 * @return tree node for the object.
	 */
	private static DefaultMutableTreeNode getNodeFor_In(Object userObject, DefaultMutableTreeNode parent) {

		// Loop through the parent's children循环比例父节点的子节点
		Enumeration<?> enumeration = parent.children();
		while (enumeration.hasMoreElements()) {
			DefaultMutableTreeNode eachNode = (DefaultMutableTreeNode)enumeration.nextElement();
			if (eachNode.getUserObject() == userObject ||
					userObject instanceof String && ((String) userObject).equals(eachNode.getUserObject()) )
				return eachNode;
		}

		return null;
	}

	private static TreePath getPathToEntity(Entity ent, DefaultMutableTreeNode root) {
		final ObjectType type = ent.getObjectType();
		if (type == null)
			return null;
		DefaultMutableTreeNode paletteNode = getNodeFor_In(type.getPaletteName(), root);
		if (paletteNode == null)
			return null;
		DefaultMutableTreeNode typeNode = getNodeFor_In(type.getName(), paletteNode);
		if (typeNode == null)
			return null;
		DefaultMutableTreeNode entityNode = getNodeFor_In(ent, typeNode);
		if (entityNode == null)
			return null;
		Object[] nodeList = { root, paletteNode, typeNode, entityNode };
		return new TreePath(nodeList);
	}

	static class MyTreeSelectionListener implements TreeSelectionListener {
		@Override
		public void valueChanged( TreeSelectionEvent e ) {
			JTree tree = (JTree) e.getSource();
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
			if(node == null) {
				// This occurs when we set no selected entity (null) and then   当我们没有设置选定的实体(null)，然后强制树有一个空选定的节点时，就会发生这种情况
				// force the tree to have a null selected node
				return;
			}

			Object userObj = node.getUserObject();
			if (userObj instanceof Entity) {
				FrameBox.setSelectedEntity((Entity)userObj, false);
			}
			else {
				FrameBox.setSelectedEntity(null, false);
			}
		}
	}

	static class MyTreeModelListener implements TreeModelListener {
		private final JTree tree;

		public MyTreeModelListener(JTree tree) {
			this.tree = tree;
		}

		@Override
		public void treeNodesChanged( TreeModelEvent e ) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
			String newName = ((String)node.getUserObject()).trim();
			try {
				InputAgent.renameEntity(currentEntity, newName);
				if (currentEntity instanceof DisplayEntity) {
					DisplayEntity dEnt = (DisplayEntity) currentEntity;
					EntityLabel label = EntityLabel.getLabel(dEnt);
					if (label != null)
						label.updateForTargetNameChange();
				}
			}
			catch (ErrorException err) {
				GUIFrame.showErrorDialog("Input Error", err.getMessage());
			}
			finally {
				node.setUserObject(currentEntity);
				GUIFrame.updateUI();
			}
		}

		@Override
		public void treeNodesInserted(TreeModelEvent e) {}
		@Override
		public void treeNodesRemoved(TreeModelEvent e) {}
		@Override
		public void treeStructureChanged(TreeModelEvent e) {}
	}

	static class MyMouseListener implements MouseListener {
		private final JPopupMenu menu= new JPopupMenu();

		@Override
		public void mouseClicked(MouseEvent e) {

			if(e.getButton() != MouseEvent.BUTTON3)
				return;

			if(currentEntity == null)
				return;

			// Right mouse click on a movable DisplayEntity
			menu.removeAll();
			ContextMenu.populateMenu(menu, currentEntity, -1, e.getComponent(), e.getX(), e.getY());
			menu.show(e.getComponent(), e.getX(), e.getX());
		}
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
		@Override
		public void mousePressed(MouseEvent e) {}
		@Override
		public void mouseReleased(MouseEvent e) {}
	}

	static class MyKeyListener implements KeyListener {
		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() != KeyEvent.VK_DELETE)
				return;

			try {
				currentEntity.delete();
				FrameBox.setSelectedEntity(null, false);
			}
			catch (ErrorException err) {
				GUIFrame.invokeErrorDialog("User Error", err.getMessage());
			}
		}
		@Override
		public void keyPressed(KeyEvent e) {}
		@Override
		public void keyTyped(KeyEvent e) {}
	}

	static class MyFocusListener implements WindowFocusListener {
		@Override
		public void windowGainedFocus(WindowEvent arg0) {}

		@Override
		public void windowLostFocus(WindowEvent e) {
			// Complete any editing that has started
			ObjectSelector.myInstance.tree.stopEditing();
		}
	}

}
