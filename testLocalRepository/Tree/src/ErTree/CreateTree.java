package ErTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/**
 * �ǵݹ�ķ�ʽ����һ����
 * @author admin
 *
 */
public class CreateTree {
	//��һ�����������һ��Node
	public static List<Node> list = new ArrayList<Node>();
	public void createTree(int[] arr) {
		for(int i=0;i<arr.length;i++) {
			//������㣬ÿһ���������Ӻ��Һ���Ϊnull
			Node node = new Node(arr[i],null,null);
			//list�д����ÿһ�����
			list.add(node);
		}
		//����������
		if(list.size()>0) {
			//i��ʾ���Ǹ�������������0��ʼ
			for(int i=0;i<arr.length/2-1;i++) {
				if(list.get(2*i+1)!=null) {
					//����
					list.get(i).left=list.get(2*i+1);
				}else if(list.get(2*i+2)!=null) {
					//�ҽ��
					list.get(i).right=list.get(2*i+2);
				}
			}
			//�ж����һ������㣺��Ϊ���һ����������û���ҽ�㣬���Ե����ó�������
			int lastIndex = arr.length/2-1;
			//����
			list.get(lastIndex).left = list.get(lastIndex*2+1);
			//�ҽ�㣬�������ĳ���Ϊ���������ҽ��
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
