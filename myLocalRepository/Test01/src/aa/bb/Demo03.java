package aa.bb;

import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JLabel;
/**
 * GUI编程，使用图形
 * @author admin
 *
 */
public class Demo03 {
	public static void main(String[] args) {
		Lab gui = new Lab();
		gui.go();
	}
}
class myG extends JLabel{
	public void paint(Graphics g) {
//		g.drawOval(35, 30, 100, 35);
//		g.drawString("hello world", 50, 50);
		g.drawLine(0, 100, 100, 0);
	}
}

class Lab{
	public void go() {
		JFrame frame = new JFrame("Hello");
		myG ll = new myG();
		frame.add(ll);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);;
		frame.setSize(200, 200);
		frame.setVisible(true);
	}
}
