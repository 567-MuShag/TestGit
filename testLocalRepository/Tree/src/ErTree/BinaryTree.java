package ErTree;

public class BinaryTree {
	/**
	 * ǰ��������ݹ鷽ʽʵ��
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
