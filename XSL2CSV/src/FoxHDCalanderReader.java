import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class FoxHDCalanderReader {
	private String inputFile;
	private int timeCellShift=3;
	private String serviceID="Fox HD";
	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}

	public void read() throws IOException {
		File inputWorkbook = new File(inputFile);
		Workbook w;
		try {
			w = Workbook.getWorkbook(inputWorkbook);
			// Get the first sheet
			Sheet sheet = w.getSheet(0);
			// Loop over first 10 column and lines

			for (int j = 0; j < sheet.getColumns(); j++) {
				for (int i = 0; i < sheet.getRows(); i++) {
					Cell cell = sheet.getCell(j, i);
					CellType type = cell.getType();
					if (cell.getType() == CellType.LABEL) {
						System.out.println("I got a label "
								+ cell.getContents());
					}
					if (cell.getType() == CellType.DATE) {
						System.out
								.println("I got a date " + cell.getContents());
					}

					if (cell.getType() == CellType.NUMBER) {
						System.out.println("I got a number "
								+ cell.getContents());
					}

				}
			}
			String str = sheet.getCell(24, 9).getContents();
			System.out.println("My test: " + str);
		} catch (BiffException e) {
			e.printStackTrace();
		}
	}
	public List reorderEvents(List<List> allEventList){
		List all = new ArrayList();
		for (int i = 0; i < allEventList.size(); i++) {
			List aPageEventList = allEventList.get(i);
			Date newestDate=null;
			try {
				newestDate = (new TXTEvent()).sdf
						.parse("2000-01-01 01:01:01.0");
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			for (int j = 0; j < aPageEventList.size(); j++) {
				List aDayEventList = (List) aPageEventList.get(j);
				for (int k = 0; k < aDayEventList.size(); k++) {
					TXTEvent temp1 = (TXTEvent) aDayEventList.get(k);
					// TXTEvent temp2=(TXTEvent) aDayEventList.get(k+1);

					System.out.println(temp1.getStartDate() + ","
							+ temp1.getName() + "|");
					// System.out.print(temp1.getName()+"|"+temp1.getStartDate()+"|"+temp2.getStartDate()+"|");
					if (i == allEventList.size() - 1
							&& j == aPageEventList.size() - 1) {

					}
					try {
						if (newestDate == null) {
							newestDate=temp1.sdf.parse(temp1.getStartDate());
						} else if (temp1.sdf.parse(temp1.getStartDate())
								.compareTo(newestDate) > 0) {
							newestDate = temp1.sdf.parse(temp1.getStartDate());
						} else {
//							Calendar cal = Calendar.getInstance();
//							cal.add(Calendar.DATE, -30);
							System.out.println("date reversed!!");
							Calendar cal = Calendar.getInstance();
							cal.setTime(temp1.sdf.parse(temp1.getStartDate()));
							cal.add(Calendar.MONTH, +1);
							temp1.setStartDate(temp1.sdf.format(cal.getTime()));
							System.out.println("shifted a month!!");
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					temp1.setServiceID(serviceID);
					all.add(temp1);

				}
			}
		}
		return all;
	}
	public void printAllEvents(List all) {
		

		
		// TXTEvent lastEvent=null;
		for (int i = 0; i < all.size() - 1; i++) {
			// if(lastEvent!=null)
			// {
			// lastEvent.gets
			// }
			TXTEvent temp1 = (TXTEvent) all.get(i);
			TXTEvent temp2 = (TXTEvent) all.get(i + 1);
//			System.out.print(temp1.getName() + "|" + temp1.getStartDate() + "|"
//					+ temp2.getStartDate() + "|");
			// lastEvent=temp1;
		}

	}

	public List parseEvents(String[][][] sheet) {
//		File inputWorkbook = new File(inputFile);
//		Workbook w;
		List allEvents =new ArrayList();
		
//		try {
//			w = Workbook.getWorkbook(inputWorkbook);
			// Get the first sheet
			List tempEvents=null;
//			for(int i=0;i<w.getNumberOfSheets();i++){
//				Sheet sheet = w.getSheet(i);
				// detectMonthOfCanlander(sheet);
			for(int i=0;i<sheet.length;i++)
			if(sheet.length>0){
				tempEvents = parseACanlander(sheet[i],"12/2012");
					//	detectMonthOfCanlander(sheet[i]));
				allEvents.addAll(tempEvents);
			}
//			}

		

			System.out.println("done");
//		} catch (BiffException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		List all = reorderEvents(allEvents);
		return all;
	}

	static String MONTH_PREFIX_STRING = "PROGRAMME SCHEDULE : ";

	int _canlanderWide = 30;

	String months[] = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug",
			"Sep", "Oct", "Nov", "Dec" };

	private static String HEADER_KEY = "DATE";

	private String fixMonthStr(String str) {
		for (int i = 0; i < months.length; i++)
			if (months[i].equalsIgnoreCase(str)) {
				int m = i + 1;
				if (m < 10)
					return "0" + m;
				else
					return "" + m;
			}
		return str;
	}

	private List findHeaderLines(Sheet sheet) {
		List<Integer> startIndexOfPageList = new ArrayList<Integer>();
		for (int i = 0; i < sheet.getRows(); i++) {
			Cell cell1 = sheet.getCell(0, i);

			if (cell1.getContents().endsWith(HEADER_KEY)) {
				startIndexOfPageList.add(new Integer(i));
			}

		}
		return startIndexOfPageList;

	}

	private int findHeaderLinesAfter(String[][] sheet, int y) {
		List<Integer> startIndexOfPageList = new ArrayList<Integer>();
		if(sheet.length>0)
		for (int i = y + 1; i < sheet[0].length; i++) {
//			Cell cell1 = sheet.getCell(0, i);
//System.out.println(sheet[i][0]);
			if (sheet[i][0].toUpperCase().indexOf(HEADER_KEY)>-1) {
				startIndexOfPageList.add(new Integer(i));
				return i;
			}

		}
		return 0;//sheet[0].getRows() - 1;

	}

	private List parseACanlander(String[][] sheet, String date) {
		int currentY = 0;
		List aCanlander = new ArrayList();
		
		if(sheet.length>0)
//		while (true) {

			currentY = findHeaderLinesAfter(sheet, currentY);
		currentY++;
//			System.out.println(			currentY);
			if (currentY > sheet.length - 10)
				return aCanlander;
			List temp = parseAPage(sheet, currentY, date);
			aCanlander.add(temp);

//		}
		return aCanlander;

	}

	public boolean isParsableToInt(String i) {
		try {
			Integer.parseInt(i);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	private List parseAPage(String[][] sheet, int y, String date) {
		List aPage = new ArrayList();
		int lastReadDay=0;
		for (int i = 0; i < sheet[y].length && i < _canlanderWide; i++) {
//			Cell cell1 = sheet.getCell(i, y );
			String str =sheet[y][i];
			System.out.println(str);
			String[] str_weekday_day=Tool.trim2OneSpace(str).trim().split("/");
			
//			if(cell1.getCellFormat()!=null)///////////////////////filter the white bk black font
//			if (cell1.getCellFormat().getBackgroundColour().getDefaultRGB()
//					.getBlue() == 255
//					&& cell1.getCellFormat().getFont().getColour()
//							.getDefaultRGB().getBlue() == 0)///////////////////////////
			if(str_weekday_day.length>2)
				if (str_weekday_day[0] != null && !str_weekday_day[0].equalsIgnoreCase("")
						&& isParsableToInt(str_weekday_day[0])) {
					if (str_weekday_day[1] != null && !str_weekday_day[1].equalsIgnoreCase("")
							&& isParsableToInt(str_weekday_day[1]))
					if (Integer.parseInt(str_weekday_day[1]) > 0 && Integer.parseInt(str_weekday_day[1]) < 13) {
						 int day=lastReadDay;
						if (str_weekday_day[2] != null && !str_weekday_day[2].equalsIgnoreCase("")){
							
						    int startLength=str_weekday_day[2].length();
						   
							do{
								if(isParsableToInt(str_weekday_day[2].substring(0,startLength))){
									day=Integer.parseInt(str_weekday_day[2].substring(0,startLength));
									break;
								}
								startLength--;
							}while(startLength>0);					}
						
						if (day> 0 && day < 32) {
						
						String date2 = String.format("%02d", day)
						+ "/" +String.format("%02d", Integer.parseInt(str_weekday_day[1]))
						+ "/" + str_weekday_day[0];
						System.out.println("date2: "+date2);
						
						
						List temp = parseAday(sheet, i, y + 1, date2);
						
					//	String newDateStr=((TXTEvent)temp.get(0)).getStartDate();
//						try {
//							if(temp.size()>0){
//								Date newDate = ((TXTEvent) temp.get(0)).sdf
//										.parse(newDateStr);
//								Calendar newC = Calendar.getInstance();
//								newC.setTime(newDate);
//								int newDay = newC.get(Calendar.DATE);
//								if (newDay > lastReadDay)
//									aPage.add(temp);
//							}
//						} catch (ParseException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
						aPage.add(temp);
						}
					}
				}
		}
		return aPage;
	}

	private boolean isParsableToDate(String i) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
			sdf.parse(i);
			return true;
		} catch (Exception e) {
			System.out.println(i+" is not a time format.");
			return false;
		}
	}

	private List<TXTEvent> parseAday(String[][] sheet, int x, int y, String date) {
		List aday = new ArrayList();
		for (int i = y; i < sheet.length; i++) {
					TXTEvent temp = parseAEvent(sheet, x, i, date);
					if (temp != null)
						aday.add(temp);
//				}
		}
		return aday;
	}
	private boolean isAEventName(String name){
		if((name.indexOf("<")>0 && name.indexOf(">")>0) || name.startsWith("*"))
			return true;
		return true;
	}
	private TXTEvent parseAEvent(String[][] sheet, int x, int y, String date) {
		TXTEvent temp = new TXTEvent();
//		for (int i = 0; i < 5; i++) {
		if(sheet[y]!=null){
//			System.out.println("x: "+x+" y: "+y);
			if(sheet[y].length<x+1)
				return null;
			else if(sheet[y][x]==null)
				return null;
			String name = sheet[y][x].trim();

//			if(y>0 )
//			if(x<sheet[y-1].length)
//			if(sheet[y-1][x].equalsIgnoreCase("")){
//				return null;
//			}
			if(name.equalsIgnoreCase("")){
				for(int i=0;i<sheet.length-y-1;i++)//
					if (y + i < sheet.length) {
						if (x < sheet[y + i].length) {
							String checkNext = sheet[y + i][x].trim();
							if (!checkNext.equalsIgnoreCase(""))
								if (isAEventName(checkNext)) {
											
								} else if(checkNext.replace(" ", "").length()==0){
									return null;
								}
							
						}
					}
				for(int i=0;i<x-1;i++){
					name = sheet[y][x-i].trim();
					if(name.length()>0){
						System.out.println("find the y "+y+" x "+x+" i "+i+ "name "+name);
						break;
					}
				}
			}
			
			if (!name.equalsIgnoreCase("")) {
				name=name.replaceAll("\r","").replaceAll("\n","");
				//if(!isAEventName(name))
					//return null;
//				if(names.length>0)
//					temp.setName(names[0]);
//				if(names.length>1)
//					temp.setEngName(names[1]);
				name=trim2OneSpace(name);
				if(name.indexOf("~#")>0)
					temp.setName(name.substring(0,name.indexOf("~#")));
				else
					temp.setName(name);
				String time_str=sheet[y][0].trim();
			//	System.out.println("name: "+temp.getName());
				
		
				//time_str=time_str.length()>4?time_str:"0"+time_str;
				String[] timeA=time_str.split(":");
				if(timeA.length<1)
					return null;
				time_str=String.format("%02d", Integer.parseInt(timeA[0]))+":"+String.format("%02d",Integer.parseInt(timeA[1]));
				//System.out.println(time_str);
				temp.setStartDate(time_str+" "+date);
				temp.setServiceID(serviceID);
				
//				System.out.println("date: "+ time_str+" "+date+" name: "+str);
//					str = "24" + str.substring(str.indexOf(":"));
//				String date2 = date + " " + str;
//				break;
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy");
				Date d1;
				try {
					d1 = sdf.parse(time_str+" "+date);
					temp.setStartDate(temp.sdf.format(d1));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					System.out.println("crash: "+x+" "+y);
					e.printStackTrace();
					return null;
				}
			}else{
				return null;
			}
//		}
//		for (int i = 0; y + i < findHeaderLinesAfter(sheet, y); i++) {
//			String str = sheet.getCell(x + 1, y + i).getContents().trim();
//			if (!str.equalsIgnoreCase("") && str.length() >= 3
//					&& (str.startsWith("[") && str.endsWith("]"))) {
//
//				temp.setName(temp.getName() + "[" + str.substring(1, 2) + "]");
//				break;
//			}
//		}
		}else{
			return null;
		}

		return temp;
	}
	private String trim2OneSpace(String str){
		String result="";
		char[] c_array=str.toCharArray();
		char last_c='x';
		for(char c:c_array){
			if(last_c!=' ' || c!=' '){
				result+=c;
				
			}
			last_c=c;
		}
		
		return result;
	}
	private String replaceMonthbyNum(String s){
		String[] mon={"JA","FE","MAR","AP","MA","JUN","JUL","AUG","SEP","OC","NO","DE"};
		if(s!=null)
			for(int i=0;i<mon.length;i++)
			{
				if(s.toUpperCase().indexOf(mon[i])>-1)
						return i+1<10?"0"+String.valueOf(i+1):String.valueOf(i+1);
			}
		return null;
	}
	private String detectMonthOfCanlander(String[][] sheet) {
		if(sheet.length>0)
		if(sheet[0].length>3){
			String str=sheet[1][1];//sheet.getCell(0,2).getContents();
			String[] str_dates=trim2OneSpace(str).split(":");
			if(str_dates.length>1)
			{
//				String num=str_dates[0];
				String DATE_FORMAT = "MM/yyyy";
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
//				System.out.println(sdf.format(new Date()));
				String y=String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
				
				String[] year=str_dates[1].split(" ");
				
				if(year.length>1)
					if(year[1].length()==4)
						y=year[1];
					
				String date=replaceMonthbyNum(str_dates[1])+"/"+y;
				try {
					
					Date d = sdf.parse(date);
					System.out.println(d.toString());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("Can not get correct month and year.");
				}
				return date;
			}
		}
//		for (int j = 0; j < sheet.getColumns(); j++) {  //old from Star Movie
//			for (int i = 0; i < sheet.getRows(); i++) {
//				Cell cell = sheet.getCell(j, i);
//				if (cell.getContents().startsWith(MONTH_PREFIX_STRING)) {
//					String[] temps = cell.getContents().trim().split(" ");
//					if (temps.length == 5) {
//						String month = fixMonthStr(temps[3]) + "-" + temps[4];
//
//						System.out.println("month: " + month);
//						String DATE_FORMAT = "MM-yyyy";
//						SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
//						System.out.println(sdf.format(new Date()));
//						try {
//							Date d = sdf.parse(month);
//							System.out.println(d.toString());
//						} catch (ParseException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//							break;
//						}
//
//						return month;
//					}
//				}
//
//			}
//		}
		return null;

	}

	public static void main(String[] args) throws IOException {
		FoxHDCalanderReader test = new FoxHDCalanderReader();
		test.setInputFile("./SCC.xlsx");
//		 test.read();
		XLSXReader reader=new XLSXReader();
		String[][][] contents=reader.parseXLSX("SCC.xlsx");
		List allEvents=test.parseEvents(contents);
		test.printAllEvents(allEvents);
	}

	public int getTimeCellShift() {
		return timeCellShift;
	}

	public void setTimeCellShift(int timeCellShift) {
		this.timeCellShift = timeCellShift;
	}

	public String getServiceID() {
		return serviceID;
	}

	public void setServiceID(String serviceID) {
		this.serviceID = serviceID;
	}
}
