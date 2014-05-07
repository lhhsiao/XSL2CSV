import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;


public class testPOIWord {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File f=new File("NGC__.docx");
		try {
			FileInputStream fis=new FileInputStream(f);
			String s=extractText(fis);
			System.out.println(s);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	static public String extractText(InputStream in) throws Exception {
		XWPFDocument doc = new XWPFDocument(in);
		XWPFWordExtractor ex = new XWPFWordExtractor(doc);
		String text = ex.getText();
		return text;
	}
}
