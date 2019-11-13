package ErTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/**
 * 非递归的方式创建一棵树
 * @author admin
 *
 */
public class CreateTree {
	//用一个集合来存放一个Node
	public static List<Node> list = new ArrayList<Node>();
	public void createTree(int[] arr) {
		for(int i=0;i<arr.length;i++) {
			//创建结点，每一个结点的左孩子和右孩子为null
			Node node = new Node(arr[i],null,null);
			//list中存放着每一个结点
			list.add(node);
		}
		//构建二叉树
		if(list.size()>0) {
			//i表示的是根结点的索引，从0开始
			for(int i=0;i<arr.length/2-1;i++) {
				if(list.get(2*i+1)!=null) {
					//左结点
					list.get(i).left=list.get(2*i+1);
				}else if(list.get(2*i+2)!=null) {
					//右结点
					list.get(i).right=list.get(2*i+2);
				}
			}
			//判断最后一个根结点：因为最后一个根结点可能没有右结点，所以单独拿出来处理
			int lastIndex = arr.length/2-1;
			//左结点
			list.get(lastIndex).left = list.get(lastIndex*2+1);
			//右结点，如果数组的长度为奇数才有右结点
			if(arr.length%2==1) {
				list.get(lastIndex).right=list.get(lastIndex*2+2);
			}
		}
	}

	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String str = sc.nextLine();
		String[] num = str.split(",");
		int[] nums = new int[num.length];
		for(int i=0;i<nums.length;i++) {
			nums[i]=Integer.parseInt(num[i]);
		}
		CreateTree ct = new CreateTree();
		ct.createTree(nums);
		BinaryTree bt = new BinaryTree();
		bt.PreOrder(list.get(0));
	}
}
