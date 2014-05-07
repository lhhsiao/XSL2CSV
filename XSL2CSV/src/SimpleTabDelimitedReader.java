import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SimpleTabDelimitedReader {
	private BufferedReader br;
	
	public SimpleTabDelimitedReader(File inFile) {
		try {
			this.br = new BufferedReader(new FileReader(inFile));
			parse();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private String[][][] content=new String[0][0][0];
 private void parse(){
	 
	 try {
		String lineTemp="";
		List lines=new ArrayList();
		while((lineTemp=br.readLine())!=null){
			lines.add(lineTemp);
		};
		content=new String[1][lines.size()][0];
		for(int i=0;i<lines.size();i++){
			String[] temp=((String)lines.get(i)).split("\t");
			content[0][i]=temp;
		}
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
 }
 public String[][][] getContent(){
	 return content;
 }
}
