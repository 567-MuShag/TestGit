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
//�̳���JFrame
public class EntityPallet extends OSFixJFrame implements DragGestureListener {

	private static EntityPallet myInstance;  // only one instance allowed to be openֻ�����һ��ʵ��

	private final JScrollPane treeView;
	private final JTree tree;
	//DefaultMutableTreeNode�����ݽṹ�е�ͨ�ýڵ�
	private final DefaultMutableTreeNode top;
	//DefaultTreeModelʹ��TreeNodes�ļ������ݽṹģ��
	private final DefaultTreeModel treeModel;

	private EntityPallet() {

		super( "Model Builder" );
		//���ô�������
		setType(Type.UTILITY);
		//�Զ���ȡ����
		setAutoRequestFocus(false);
		// Make the x button do the same as the close button��x��ť����رհ�ť��ͬ�Ĳ��������ֶα�ʾ�޲���Ĭ�ϴ��ڹرղ���
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(FrameBox.getCloseListener("ShowModelBuilder"));

		tree = new MyTree();
		//���ø��Ƿ�ɼ�
		tree.setRootVisible(false);
		//�����Ƿ���ʾ�ڵ���
		tree.setShowsRootHandles(true);
		//��������
		DragSource dragSource = new DragSource();
		dragSource.createDefaultDragGestureRecognizer(tree, DnDConstants.ACTION_COPY, this);
		
		//DefaultMutableTreeNode�����ݽṹ�е�ͨ�ýڵ�
		top = new DefaultMutableTreeNode();
		//DefaultTreeModelʹ��TreeNodes�ļ������ݽṹģ��
		treeModel = new DefaultTreeModel(top);
		//�����ṩ���ݵ�ģ��
		tree.setModel(treeModel);
		//��������ѡ��ģ�ͣ�Ȼ�󷵻�����ѡ��ģ��
		tree.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );

		// Create the tree scroll pane and add the tree to it�������������񲢽�����ӵ�����
		treeView = new JScrollPane( tree );
		getContentPane().add( treeView );
		//����ÿ����Ԫ��ĸ߶�
		tree.setRowHeight(25);
		//�����û�����ÿ����Ԫ���TreeCellRenderer��TreeCellRenderer��������ʾ���ڵ�Ķ����Ҫ��
		tree.setCellRenderer(new TreeCellRenderer());
		//�ȴ���һ�������ToolTipManagerʵ����Ȼ��ע��һ��������ʾ�������
		ToolTipManager.sharedInstance().registerComponent(tree);
		//ָ����ʼ�ӳ�ֵ
		ToolTipManager.sharedInstance().setDismissDelay(600000);
		//��ȡ����ģ��
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
	//���ض���DragGesturerecognizer��⵽�����ڸ��ٵ�Component���ѷ�����ƽ̨��ص��϶���������ʱ����DragGestureEvent���ݸ�DragGestureListener��dragGestureRecognized()����
	//DragGestureRecognizer�Ѿ���⵽��ƽ̨�йص��϶�����������������֪ͨ�˼��������Ա������û�����
	@Override
	public void dragGestureRecognized(DragGestureEvent event) {
		//������ѡ�ڵ��·��
		TreePath path = tree.getSelectionPath();
		if (path != null) {

			// Dragged node is a DefaultMutableTreeNode  �϶��ڵ���һ��DefaultMutableTreeNode
			//getLastPathComponent���ش�·�������һ�����,���ص���һ������DefaultMutableTreeNode�������ݽṹ�е�ͨ�ýڵ㣬��һ����
			//instanceof�����ж�getLastPathComponent���صĶ����Ƿ���DefaultMutableTreeNode���ʵ��
			if(path.getLastPathComponent() instanceof DefaultMutableTreeNode) {
				DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) path.getLastPathComponent();

				// This is an ObjectType node  ����һ��ObjectType�ڵ�
				//getUserObject�������ڷ����û��洢�ڴ˽ڵ��Object������û��洢�ڴ˼����Object��һ��ObjectType���ʵ��������������жϡ�
				if(treeNode.getUserObject() instanceof ObjectType) {
					ObjectType type = (ObjectType) treeNode.getUserObject();
					//Cursor�����ڷ�װ������λͼ��ʾ��ʽ����
					Cursor cursor = null;
					//�����û���ѡ������int��ʾ��ʽ������DnDconstants��ָ�����ֶν��бȽϣ�ACTION_COPY��ʾ���Ʋ�����intֵ
					if (event.getDragAction() == DnDConstants.ACTION_COPY) {
						//���ڸ��Ʋ�����Ĭ��Cursor����ʾ��ǰ�������
						cursor = DragSource.DefaultCopyDrop;
					}
					//��Ⱦ����
					if (RenderManager.isGood()) {
						// The new renderer is initialized  ��ʼ���µĳ��ֳ���
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

		// Create a tree that allows one selection at a time ����һ������һ��ֻ����һ��ѡ��
		//ͨ�����丸�ڵ�����Ϊnull���Ƴ��˽ڵ�������ӽڵ�
		top.removeAllChildren();
		HashMap<String, DefaultMutableTreeNode> paletteNodes = new HashMap<>();
		//��ȡ����ģ��
		JaamSimModel simModel = GUIFrame.getJaamSimModel();
		for (ObjectType type : simModel.getObjectTypes()) {
			if (!type.isDragAndDrop())
				continue;
			
			String pName = type.getPaletteName();
			DefaultMutableTreeNode palNode = paletteNodes.get(pName);//���ݼ���HashMap�����л�ȡֵ
			if (palNode == null) {
				//��������в�������Ӧ�ļ�ֵ�ԣ��ʹ���һ��û�и��ڵ���ӽڵ�����ڵ㣬ʹ��ָ�����û�����������г�ʼ��������ָ��ʱ���������ӽڵ�
				palNode = new DefaultMutableTreeNode(pName, true);
				//����Ӧ�ļ�ֵ����ӵ�������
				paletteNodes.put(pName, palNode);
				//���丸�ڵ��Ƴ�palNode�ڵ㣬��ͨ��������ӵ��˽ڵ���������ĩβ��ʹ���Ϊ�˽ڵ���ӽڵ㡣
				top.add(palNode);
			}

			DefaultMutableTreeNode classNode = new DefaultMutableTreeNode(type, true);
			//��classNode�ڵ���ӵ�palNode�ڵ��ʹ���Ϊ���ӽڵ�
			palNode.add(classNode);
		}
		//������޸Ĵ�ģ��������TreeNode,���ô˷�������ģ�ͽ�֪ͨ�����м����������ڵ������ģ���Ѿ����ġ�
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
	 * Disposes the only instance of the entity pallet  ����ʵ�����̵�Ψһʵ��
	 */
	public synchronized static void clear() {
		if (myInstance != null) {
			//�ͷ��ɴ�Window�������������ӵ�е������������ʹ�õ����б�����Ļ��Դ
			myInstance.dispose();
			myInstance = null;
		}
	}
	//DefaultTreeCellRenderer��ʾ���е���Ŀ����͸����
	private static class TreeCellRenderer extends DefaultTreeCellRenderer {
		//����һ��δ��ʼ����ͼ��ͼ��
		private final ImageIcon icon = new ImageIcon();
		//���û��ڴ����������Ⱦ��   ��������
		@Override
		public Component getTreeCellRendererComponent(JTree tree,
				Object value, boolean selected, boolean expanded,
				boolean leaf, int row, boolean hasFocus) {

			super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

			// If not a leaf, just return
			if (!leaf)
				return this;

			// If we don't find an ObjectType (likely we will) just return���û���ҵ�ObjectType(�ܿ��ܻ��ҵ�)���ͷ���
			//getUserObject�����û��洢�ڴ˽ڵ��Object
			Object userObj = ((DefaultMutableTreeNode)value).getUserObject();
			if (!(userObj instanceof ObjectType))
				return this;
			//���userObj������ObjectType���ʵ���ͽ�userObject����ǿת
			ObjectType type = (ObjectType)userObj;
			this.setText(type.getName());

			if (!RenderManager.isGood())
				return this;

			if (type.getIconImage() == null)
				return this;
			//�����ɴ�ͼ����ʾ��ͼ��
			icon.setImage(type.getIconImage());
			//����������Ҫ��ʾ��ͼ��
			this.setIcon(icon);
			return this;
		}
	}

	static class MyTree extends JTree {

		public MyTree() {
		}

		/*
		 * override getToolTipText to control what to display  ����getToolTipText������Ҫ��ʾʲô
		 */
		@Override
		public String getToolTipText(MouseEvent e) {

			if(this.getPathForLocation(e.getX(), e.getY()) == null) {
				return null;
			}
 
			// Obtain the node under the mouse ��ȡ����µĽڵ�
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
