package aa.bb;

import java.io.FileOutputStream;
import java.io.IOException;

public class Demo06 {
	public static void main(String[] args) throws IOException {
		FileOutputStream fos = new FileOutputStream("fos4.txt",true);
		for(int x=0;x<5;x++) {
			fos.write(("hello"+x).getBytes());
			fos.write("\r\n".getBytes());
		}
		fos.close();
	}
}
