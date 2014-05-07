import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;


public class NGCDocxReader {
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	public String serviceId="NGC";
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		NGCDocxReader ngcdr=new NGCDocxReader();
		ngcdr.parseEvents("NGC__.docx");
		System.out.println("done");
	}
	public List parseEvents(String filepath){
		
		File f=new File(filepath);
		List all=new ArrayList();
		try {
			String lastYear=null;
			String lastDate=null;
			String lastTime=null;
			
			FileInputStream fis=new FileInputStream(f);
			String s=extractText(fis);
//			System.out.println(s);
			if(s==null)
				return all;
			String lines[]=readAsLines(s);
			
			for(int i=0;i<lines.length;i++){
//				System.out.println("i: "+i+" "+lines[i]);
				String year=detectYear(lines[i]);
				String date=detectDate(lines[i]);
				String time=detectTime(lines[i]);
//				System.out.println("Y: "+year+" D: "+date +" T: "+time);
				if(lastYear==null)
					lastYear=year!=null?year:lastYear;
				lastDate=date!=null?date:lastDate;
				lastTime=time!=null?time:lastTime;
				if(lastYear!=null && lastDate!=null && lastTime!=null && time!=null && lines[i].length()>6){
					String fullDate=lastYear+"-"+lastDate+" "+lastTime;
//					System.out.println("date: "+fullDate+" "+lines[i].substring(5));
					TXTEvent temp=new TXTEvent();
					temp.setName(lines[i].substring(5));
					temp.setStartDate(temp.sdf.format(sdf.parse(fullDate)));
					temp.setServiceID(serviceId);
					all.add(temp);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return all;
	}
	 public String extractText(InputStream in) throws Exception {
		XWPFDocument doc = new XWPFDocument(in);
		XWPFWordExtractor ex = new XWPFWordExtractor(doc);
		String text = ex.getText();
		return text;
	}
	public String[] readAsLines(String s){
		return s.split("\n");
	}
	private String detectYear(String s){
		String str=s.trim().replaceAll(" ","");
		int index=str.indexOf("¦~");
		if(index>4)
			str=str.substring(index-4,index);
		try{
			Integer.parseInt(str);
		}catch(Exception e){
			return null;
		}
		return str;
	}
	private String detectDate(String s){
		String str=s.trim().replaceAll(" ","");
		int index=str.indexOf("¤ë");
		String month="";
		if(index>0)
			month=str.substring(0,index);
		else
			return null;
		
		int index2=str.indexOf("¤é");
		String day="";
		if(index2>index+1)
			day=str.substring(index+1,index2);
		else
			return null;
//		System.out.println("t: "+day+"/"+month);
		
		try{
			Integer.parseInt(month);
			Integer.parseInt(day);
		}catch(Exception e){
			return null;
		}
		month=month.length()==1?"0"+month:month;
		return month+"-"+day;
	}
	private String detectTime(String str){
		str=str.trim().replaceAll(" ","");
		String s="";
		if(str.length()>6)
			s=str.substring(0,5);
		else
			return null;
		String[] time=s.split(":");
		String hour="";
		String min="";
		if(time.length>1)
		{
			hour=time[0];
			min=time[1];

			try{
				Integer.parseInt(hour);
				Integer.parseInt(min);
			}catch(Exception e){
				return null;
			}
		}else
			return null;
		
		return hour+":"+min;
		
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
}
