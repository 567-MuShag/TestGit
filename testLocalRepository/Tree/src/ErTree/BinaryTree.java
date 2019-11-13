package ErTree;

public class BinaryTree {
	/**
	 * 前序遍历，递归方式实现
	 * @param node
	 */
	public void PreOrder(Node node) {
		if(node != null) {
			System.out.print(node.data);
			if(node.left!=null) {
				PreOrder(node.left);
			}else if(node.right!=null) {
				PreOrder(node.right);
			}
		}
	}
}
