package aa.bb;

import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
/**
 * GUI�����Swing�Ļ�ͼ���ܣ���ͼһ������JPanel�Ͻ���
 * @author admin
 *
 */
public class Demo04 extends JFrame{
	MyPanel myPanel;
	public static void main(String[] args) {
		//���������ʱ������޲ι�����г�ʼ��
		Demo04 demo04=new Demo04();
	}
	public Demo04() {
		//�����Զ�����MyPanel�Ķ��󣬲����ö�����ӵ��Զ���Ĵ�����
		myPanel = new MyPanel();
		this.add(myPanel);
		this.setTitle("���±�");
		this.setResizable(false);
		this.setLocation(500,250);
		this.setSize(350, 230);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);;
		this.setVisible(true);
	}
}

class MyPanel extends JPanel{
	//��дJPanel�е�paint�������÷����Ĳ�����Graphics,�����൱��һ�����ʡ�
	public void paint(Graphics g) {
		//���ø�����ɳ�ʼ��
		super.paint(g);
		//����һ��Բ
		g.drawOval(10, 10, 30, 30);
		//����һ������
		g.draw3DRect(50, 50, 50, 50, true);
	}
}
