package DrTree;


public class BinaryTerr {
	/**
	 * 前序遍历，递归方式实现
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
