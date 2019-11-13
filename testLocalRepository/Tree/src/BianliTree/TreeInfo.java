package BianliTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class TreeInfo {
	List<Integer> qianxuNumList;
	List<Integer> zhongxuNumList;
	List<Integer> houxuNumList;
	public TreeInfo() {
		qianxuNumList = new ArrayList<Integer>();
		zhongxuNumList = new ArrayList<Integer>();
		houxuNumList = new ArrayList<Integer>();
	}
	
	/**
	 * �ݹ鷽ʽ�������
	 * @param treeNode
	 */
	public void qianxuDg(TreeNode treeNode) {
		qianxuNumList.add(treeNode.val);
		if(treeNode.left!=null) {
			qianxuDg(treeNode.left);
		}
		if(treeNode.right!=null) {
			qianxuDg(treeNode.right);
		}
	}
	/**
	 * �ݹ鷽ʽ�������
	 * @param treeNode
	 */
	public void zhongxuDg(TreeNode treeNode) {
		if(treeNode.left!=null) {
			zhongxuDg(treeNode.left);
		}
		zhongxuNumList.add(treeNode.val);
		if(treeNode.right!=null) {
			zhongxuDg(treeNode.right);
		}
	}
	/**
	 * �ݹ鷽ʽ�������
	 * @param treeNode
	 */
	public void houxuDg(TreeNode treeNode) {
		if(treeNode.left!=null) {
			houxuDg(treeNode.left);
		}
		if(treeNode.right!=null) {
			houxuDg(treeNode.right);
		}
		houxuNumList.add(treeNode.val);
	}
	/**
	 * �ǵݹ鷽ʽ�������
	 * @param treeNode
	 */
	public void qianxuFdg(TreeNode treeNode) {
		Stack<TreeNode> stack = new Stack<TreeNode>();
		while(treeNode!=null||!stack.isEmpty()) {
			while(treeNode != null) {
				qianxuNumList.add(treeNode.val);
				stack.push(treeNode);
				treeNode = treeNode.left;
			}
			if(!stack.isEmpty()) {
				treeNode = stack.pop();
				treeNode = treeNode.right;
			}
		}
	}
	/**
	 * �ǵݹ鷽ʽ�������
	 * @param treeNode
	 */
	public void zhongxuFdg(TreeNode treeNode) {
		Stack<TreeNode> stack = new Stack<TreeNode>();
		while(treeNode != null || !stack.isEmpty()) {
			while(treeNode != null) {
				stack.push(treeNode);
				treeNode = treeNode.left;
			}
			if(!stack.isEmpty()) {
				treeNode = stack.pop();
				zhongxuNumList.add(treeNode.val);
				treeNode = treeNode.right;
			}
		}
	}
	/**
	 * �ǵݹ鷽ʽ�������
	 * @param treeNode
	 */
	public void houxuFdg(TreeNode treeNode) {
		Stack<TreeNode> stack = new Stack<TreeNode>();
		while(treeNode != null || !stack.isEmpty()) {
			while(treeNode !=null) {
				stack.push(treeNode);
				treeNode = treeNode.left;
			}
			boolean tag = true;
			//ǰ���ڵ�
			TreeNode preNode = null;
			while(!stack.isEmpty()&&tag==true) {
				treeNode = stack.peek();
				//֮ǰ���ʵ�Ϊ�սڵ����ջ���ڵ�����ӽڵ�
				if(treeNode.right==preNode) {
					treeNode = stack.pop();
					houxuNumList.add(treeNode.val);
					if(stack.isEmpty()) {
						return;
					}else {
						preNode = treeNode;
					}
				}else {
					treeNode = treeNode.right;
					tag=false;
				}
			}
		}
	}
	/**
	 * ��������������ظ����
	 * @return
	 */
	public TreeNode treeSet() {
		TreeNode root = new TreeNode(1);
		TreeNode a = new TreeNode(2);
		TreeNode b = new TreeNode(3);
		TreeNode c = new TreeNode(4);
		TreeNode d = new TreeNode(5);
		TreeNode e = new TreeNode(6);
		TreeNode f = new TreeNode(7);
		TreeNode g = new TreeNode(8);
		
		root.left=a;
		root.right=b;
		a.left=c;
		c.right=f;
		b.left=d;
		b.right=e;
		e.left=g;
		
		return root;
	}
	/**
	 * ��ӡ����
	 * @param type
	 */
	public void print(int type) {
		if(type==1) {
			System.out.print("ǰ�������");
			for(Integer integer : this.qianxuNumList) {
				System.out.print(integer+" ");
			}
			System.out.println();
		}else if(type==2) {
			System.out.print("���������");
			for(Integer integer : this.zhongxuNumList) {
				System.out.print(integer+" ");
			}
			System.out.println();
		}else if(type==3) {
			System.out.print("���������");
			for(Integer integer : this.houxuNumList) {
				System.out.print(integer+" ");
			}
			System.out.println();
		}else {
			return ;
		}
	}
	
	public static void main(String[] args) {
		TreeInfo treeInfo = new TreeInfo();
		TreeNode root = treeInfo.treeSet();
		
		System.out.println("-----�ݹ鷽ʽ-----");
		treeInfo.qianxuDg(root);
		treeInfo.print(1);
		treeInfo.zhongxuDg(root);
		treeInfo.print(2);
		treeInfo.houxuDg(root);
		treeInfo.print(3);
		
		System.out.println("-----�ǵݹ鷽ʽ-----");
		treeInfo.qianxuNumList.clear();
		treeInfo.zhongxuNumList.clear();
		treeInfo.houxuNumList.clear();
		
		treeInfo.qianxuFdg(root);
		treeInfo.print(1);
		treeInfo.zhongxuFdg(root);
		treeInfo.print(2);
		treeInfo.houxuFdg(root);
		treeInfo.print(3);
	}
}
