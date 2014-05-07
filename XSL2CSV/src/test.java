import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf_src = new SimpleDateFormat(", MM dd yyyy HH:mm");
		try {
			sdf_src.parse(", 05 14 2012 10:00");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			CSVWriter csvw=new CSVWriter(new FileWriter(new File("test.csv")),',','\0','.',"\r\n");
			List l=new ArrayList();
			String[] str_a={"abc","cde"};
			l.add(str_a);
			String[] str_b={"123","456"};
			l.add(str_b);
			String[] head={"first","second"};
			csvw.writeAll(l,head);
			csvw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
