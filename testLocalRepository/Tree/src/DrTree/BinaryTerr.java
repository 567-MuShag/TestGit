package DrTree;


public class BinaryTerr {
	/**
	 * ǰ��������ݹ鷽ʽʵ��
	 * @param node
	 */
	public void PreOrder(TreeNode node) {
		if(node != null) {
			System.out.print(node.val);
			if(node.left!=null) {
				PreOrder(node.left);
			}else if(node.right!=null) {
				PreOrder(node.right);
			}
		}
	}
}
