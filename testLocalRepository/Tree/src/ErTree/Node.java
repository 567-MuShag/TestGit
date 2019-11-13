package ErTree;
/*
 * ����ÿһ����㣬ÿ������������ֵ��������ҽ�㣬ʵ��get��set����
 */
public class Node {
	//�Լ�����ֵ
	public int data;
	//����
	public Node left;
	//�ҽ��
	public Node right;
	public Node(int data, Node left, Node right) {
		this.data = data;
		this.left = left;
		this.right = right;
	}
	public int getData() {
		return data;
	}
	public void setData(int data) {
		this.data = data;
	}
	public Node getLeft() {
		return left;
	}
	public void setLeft(Node left) {
		this.left = left;
	}
	public Node getRight() {
		return right;
	}
	public void setRight(Node right) {
		this.right = right;
	}
	public Node() {
	}
	@Override
	public String toString() {
		return "Node [data=" + data + ", left=" + left + ", right=" + right + "]";
	}
	
}
