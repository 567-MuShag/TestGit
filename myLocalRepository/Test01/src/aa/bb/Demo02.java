package aa.bb;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Demo02 {
	public static void main(String[] args) {
		Frame f = new Frame("添加按钮");
		f.setSize(400,300);
		f.setLayout(new FlowLayout());
		Button bu = new Button("点我啊");
		f.add(bu);
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		bu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("点一下");
			}
		});
		
		f.setVisible(true);
	}
}
