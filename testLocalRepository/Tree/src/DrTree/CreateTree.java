package DrTree;
/**
 * 递归方式创建二叉树
 * @author admin
 *
 */
public class CreateTree {
	public TreeNode createTree(Integer[] arr,int index) {
		TreeNode tn = null;
		if(index<arr.length) {
			Integer value = arr[index];
			if(value == null) {
				return null;
			}
			tn = new TreeNode(value);
			tn.left = createTree(arr,2*index+1);
			tn.right = createTree(arr,2*index+2);
			return tn;
		}
		return tn;
	}
	public static void main(String[] args) {
		Integer[] arr = new Integer[] {3,9,20,null,null,15,7};
		CreateTree ct = new CreateTree();
		TreeNode root = ct.createTree(arr,0);
	}
}
