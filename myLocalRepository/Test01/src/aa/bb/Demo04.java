package aa.bb;

import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
/**
 * GUI编程中Swing的绘图功能，绘图一般是在JPanel上进行
 * @author admin
 *
 */
public class Demo04 extends JFrame{
	MyPanel myPanel;
	public static void main(String[] args) {
		//创建对象的时候调用无参构造进行初始化
		Demo04 demo04=new Demo04();
	}
	public Demo04() {
		//创建自定义类MyPanel的对象，并将该对象添加到自定义的窗口中
		myPanel = new MyPanel();
		this.add(myPanel);
		this.setTitle("记事本");
		this.setResizable(false);
		this.setLocation(500,250);
		this.setSize(350, 230);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);;
		this.setVisible(true);
	}
}

class MyPanel extends JPanel{
	//重写JPanel中的paint方法，该方法的参数是Graphics,该类相当于一个画笔。
	public void paint(Graphics g) {
		//调用父类完成初始化
		super.paint(g);
		//绘制一个圆
		g.drawOval(10, 10, 30, 30);
		//绘制一个矩形
		g.draw3DRect(50, 50, 50, 50, true);
	}
}
