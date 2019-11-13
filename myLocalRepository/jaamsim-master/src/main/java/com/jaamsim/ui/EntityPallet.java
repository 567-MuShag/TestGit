/*
 * JaamSim Discrete Event Simulation
 * Copyright (C) 2011 Ausenco Engineering Canada Inc.
 * Copyright (C) 2019 JaamSim Software Inc.
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

import java.awt.Component;
import java.awt.Cursor;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.jaamsim.basicsim.JaamSimModel;
import com.jaamsim.basicsim.ObjectType;
import com.jaamsim.basicsim.Simulation;
import com.jaamsim.controllers.RenderManager;
//继承自JFrame
public class EntityPallet extends OSFixJFrame implements DragGestureListener {

	private static EntityPallet myInstance;  // only one instance allowed to be open只允许打开一个实例

	private final JScrollPane treeView;
	private final JTree tree;
	//DefaultMutableTreeNode树数据结构中的通用节点
	private final DefaultMutableTreeNode top;
	//DefaultTreeModel使用TreeNodes的简单树数据结构模型
	private final DefaultTreeModel treeModel;

	private EntityPallet() {

		super( "Model Builder" );
		//设置窗口类型
		setType(Type.UTILITY);
		//自动获取焦点
		setAutoRequestFocus(false);
		// Make the x button do the same as the close button让x按钮做与关闭按钮相同的操作，该字段表示无操作默认窗口关闭操作
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(FrameBox.getCloseListener("ShowModelBuilder"));

		tree = new MyTree();
		//设置根是否可见
		tree.setRootVisible(false);
		//设置是否显示节点句柄
		tree.setShowsRootHandles(true);
		//拖拉操作
		DragSource dragSource = new DragSource();
		dragSource.createDefaultDragGestureRecognizer(tree, DnDConstants.ACTION_COPY, this);
		
		//DefaultMutableTreeNode树数据结构中的通用节点
		top = new DefaultMutableTreeNode();
		//DefaultTreeModel使用TreeNodes的简单树数据结构模型
		treeModel = new DefaultTreeModel(top);
		//设置提供数据的模型
		tree.setModel(treeModel);
		//设置树的选择模型，然后返回树的选择模型
		tree.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );

		// Create the tree scroll pane and add the tree to it创建树滚动窗格并将树添加到其中
		treeView = new JScrollPane( tree );
		getContentPane().add( treeView );
		//设置每个单元格的高度
		tree.setRowHeight(25);
		//设置用户绘制每个单元格的TreeCellRenderer，TreeCellRenderer定义了显示树节点的对象的要求
		tree.setCellRenderer(new TreeCellRenderer());
		//先创建一个共享的ToolTipManager实例，然后注册一个工具提示管理组件
		ToolTipManager.sharedInstance().registerComponent(tree);
		//指定初始延迟值
		ToolTipManager.sharedInstance().setDismissDelay(600000);
		//获取仿真模型
		Simulation simulation = GUIFrame.getJaamSimModel().getSimulation();
		setLocation(simulation.getModelBuilderPos().get(0), simulation.getModelBuilderPos().get(1));
		setSize(simulation.getModelBuilderSize().get(0), simulation.getModelBuilderSize().get(1));
		//??????
		addComponentListener(new ComponentAdapter() {

			@Override
			public void componentMoved(ComponentEvent e) {
				simulation.setModelBuilderPos(getLocation().x, getLocation().y);
			}

			@Override
			public void componentResized(ComponentEvent e) {
				simulation.setModelBuilderSize(getSize().width, getSize().height);
			}
		});
	}
	//当特定的DragGesturerecognizer检测到它正在跟踪的Component上已发生与平台相关的拖动启动动作时，将DragGestureEvent传递给DragGestureListener的dragGestureRecognized()方法
	//DragGestureRecognizer已经检测到与平台有关的拖动启动动作，并且正通知此监听器，以便启动用户操作
	@Override
	public void dragGestureRecognized(DragGestureEvent event) {
		//返回首选节点的路径
		TreePath path = tree.getSelectionPath();
		if (path != null) {

			// Dragged node is a DefaultMutableTreeNode  拖动节点是一个DefaultMutableTreeNode
			//getLastPathComponent返回此路径的最后一个组件,返回的是一个对象，DefaultMutableTreeNode是树数据结构中的通用节点，是一个类
			//instanceof用来判断getLastPathComponent返回的对象是否是DefaultMutableTreeNode类的实例
			if(path.getLastPathComponent() instanceof DefaultMutableTreeNode) {
				DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) path.getLastPathComponent();

				// This is an ObjectType node  这是一个ObjectType节点
				//getUserObject方法用于返回用户存储在此节点的Object，如果用户存储在此几点的Object是一个ObjectType类的实例，则继续进行判断。
				if(treeNode.getUserObject() instanceof ObjectType) {
					ObjectType type = (ObjectType) treeNode.getUserObject();
					//Cursor是用于封装鼠标光标的位图表示形式的类
					Cursor cursor = null;
					//返回用户所选操作的int表示形式，并和DnDconstants类指定的字段进行比较，ACTION_COPY表示复制操作的int值
					if (event.getDragAction() == DnDConstants.ACTION_COPY) {
						//用于复制操作的默认Cursor，表示当前允许放置
						cursor = DragSource.DefaultCopyDrop;
					}
					//渲染？？
					if (RenderManager.isGood()) {
						// The new renderer is initialized  初始化新的呈现程序
						RenderManager.inst().startDragAndDrop(type);
						event.startDrag(cursor,new TransferableObjectType(type), RenderManager.inst());

					} else {
						event.startDrag(cursor,new TransferableObjectType(type));
					}
				}
			}
		}
	}

	private void updateTree() {

		// Create a tree that allows one selection at a time 创建一个树，一次只允许一个选择
		//通过将其父节点设置为null，移除此节点的所有子节点
		top.removeAllChildren();
		HashMap<String, DefaultMutableTreeNode> paletteNodes = new HashMap<>();
		//获取仿真模型
		JaamSimModel simModel = GUIFrame.getJaamSimModel();
		for (ObjectType type : simModel.getObjectTypes()) {
			if (!type.isDragAndDrop())
				continue;
			
			String pName = type.getPaletteName();
			DefaultMutableTreeNode palNode = paletteNodes.get(pName);//根据键从HashMap集合中获取值
			if (palNode == null) {
				//如果集合中不存在相应的键值对，就创建一个没有父节点和子节点的树节点，使用指定的用户对象对它进行初始化，仅在指定时才允许有子节点
				palNode = new DefaultMutableTreeNode(pName, true);
				//将对应的键值对添加到集合中
				paletteNodes.put(pName, palNode);
				//从其父节点移除palNode节点，并通过将其添加到此节点的子数组的末尾，使其成为此节点的子节点。
				top.add(palNode);
			}

			DefaultMutableTreeNode classNode = new DefaultMutableTreeNode(type, true);
			//将classNode节点添加到palNode节点后，使其成为其子节点
			palNode.add(classNode);
		}
		//如果已修改此模型依赖的TreeNode,调用此方法，该模型将通知其所有监听器给定节点下面的模型已经更改。
		treeModel.reload(top);
	}

	public synchronized static EntityPallet getInstance() {

		if (myInstance == null) {
			myInstance = new EntityPallet();
			myInstance.updateTree();
		}

		return myInstance;
	}

	/**
	 * Disposes the only instance of the entity pallet  处理实体托盘的唯一实例
	 */
	public synchronized static void clear() {
		if (myInstance != null) {
			//释放由此Window、其子组件及其拥有的所有子组件所使用的所有本机屏幕资源
			myInstance.dispose();
			myInstance = null;
		}
	}
	//DefaultTreeCellRenderer显示树中的条目，是透明的
	private static class TreeCellRenderer extends DefaultTreeCellRenderer {
		//创建一个未初始化的图像图标
		private final ImageIcon icon = new ImageIcon();
		//配置基于传入组件的渲染器   ？？？？
		@Override
		public Component getTreeCellRendererComponent(JTree tree,
				Object value, boolean selected, boolean expanded,
				boolean leaf, int row, boolean hasFocus) {

			super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

			// If not a leaf, just return
			if (!leaf)
				return this;

			// If we don't find an ObjectType (likely we will) just return如果没有找到ObjectType(很可能会找到)，就返回
			//getUserObject返回用户存储在此节点的Object
			Object userObj = ((DefaultMutableTreeNode)value).getUserObject();
			if (!(userObj instanceof ObjectType))
				return this;
			//如果userObj对象是ObjectType类的实例就将userObject进行强转
			ObjectType type = (ObjectType)userObj;
			this.setText(type.getName());

			if (!RenderManager.isGood())
				return this;

			if (type.getIconImage() == null)
				return this;
			//设置由此图标显示的图像
			icon.setImage(type.getIconImage());
			//定义此组件将要显示的图标
			this.setIcon(icon);
			return this;
		}
	}

	static class MyTree extends JTree {

		public MyTree() {
		}

		/*
		 * override getToolTipText to control what to display  覆盖getToolTipText来控制要显示什么
		 */
		@Override
		public String getToolTipText(MouseEvent e) {

			if(this.getPathForLocation(e.getX(), e.getY()) == null) {
				return null;
			}
 
			// Obtain the node under the mouse 获取鼠标下的节点
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)this.getPathForLocation(e.getX(), e.getY()).getLastPathComponent();
			if(node == null) {
				return null;
			}

			Object object = node.getUserObject();

			// It is a leaf node
			if (!(object instanceof ObjectType)) {
				return null;
			}
			ObjectType ot = (ObjectType)object;
			return GUIFrame.formatToolTip(ot.getName(), ot.getDescription());
		}
	}

	private final static DataFlavor OBJECT_TYPE_FLAVOR;
	static {
		try {
			// Create OBJECT_TYPE_FLAVOR
			String objectTypeFlavor = DataFlavor.javaJVMLocalObjectMimeType +
			";class=" + TransferableObjectType.class.getName();
			OBJECT_TYPE_FLAVOR = new DataFlavor(objectTypeFlavor);
		} catch (ClassNotFoundException ex) {
			throw new RuntimeException(ex);
		}
	}

	private static class TransferableObjectType implements Transferable {
		private final ObjectType type;

		TransferableObjectType(ObjectType type) {
			this.type = type;
		}

		@Override
		public DataFlavor [] getTransferDataFlavors() {
			return new DataFlavor [] {OBJECT_TYPE_FLAVOR};
		}

		@Override
		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return OBJECT_TYPE_FLAVOR.equals(flavor);
		}

		@Override
		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
			if (flavor.equals(OBJECT_TYPE_FLAVOR)) {
				return type;
			} else {
				throw new UnsupportedFlavorException(flavor);
			}
		}
	}

}
