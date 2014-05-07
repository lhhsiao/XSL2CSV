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

public class StarWorldCalanderReader2 {
	private String inputFile;
	private int timeCellShift=3;
	private String serviceID="SCM HD";
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

	public List reorderEvents(List<List> allEventList) {
		List all = new ArrayList();
		boolean find1STday=false;
		for (int i = 0; i < allEventList.size(); i++) {
			List aPageEventList = allEventList.get(i);
			Date newestDate = null;
			try {
				newestDate = (new TXTEvent()).sdf
						.parse("2000-01-01 00:00:00.0");
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			for (int j = 0; j < aPageEventList.size(); j++) {
				List aDayEventList = (List) aPageEventList.get(j);
				for (int k = 0; k < aDayEventList.size(); k++) {
					TXTEvent temp1 = (TXTEvent) aDayEventList.get(k);
					// TXTEvent temp2=(TXTEvent) aDayEventList.get(k+1);
					if(!find1STday && temp1.getStartDate().split(" ")[0].endsWith("01")){
						find1STday=true;
					}
					if(!find1STday)
						continue;
//					System.out.println(temp1.getStartDate() + ","
//							+ temp1.getName() + "|");
					// System.out.print(temp1.getName()+"|"+temp1.getStartDate()+"|"+temp2.getStartDate()+"|");
//					if (i == allEventList.size() - 1
//							&& j == aPageEventList.size() - 1) {
//
//					}

					
					try {
						if (newestDate == null) {
							newestDate = temp1.sdf.parse(temp1.getStartDate());
						} else if (temp1.sdf.parse(temp1.getStartDate())
								.compareTo(newestDate) > 0) {
							newestDate = temp1.sdf.parse(temp1.getStartDate());
						} else {
							// Calendar cal = Calendar.getInstance();
							// cal.add(Calendar.DATE, -30);
							System.out.println("date reversed!!");
							
							Calendar cal = Calendar.getInstance();
							cal.setTime(temp1.sdf.parse(temp1.getStartDate()));
							Calendar cal_newest = Calendar.getInstance();
							cal_newest.setTime(newestDate);

							System.out.println(temp1.getName()+" a: "+newestDate.getDay()+" "+cal_newest.get(Calendar.DAY_OF_MONTH));
							cal.add(Calendar.MONTH, 1);
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
			System.out.print(temp1.getName() + "|" + temp1.getStartDate() + "|"
					+ temp2.getStartDate() + "|");
			// lastEvent=temp1;
		}

	}
	public List parseEvents() {//old
		XLS2CSV xls2txt = new XLS2CSV();
		File inputWorkbook = new File(inputFile);
		Workbook w;
		List allEvents =null;
//		String[][][] contents=xls2txt.read(inputWorkbook);
		try {

			String[][][] contents=xls2txt.read(inputWorkbook);
			// detectMonthOfCanlander(sheet);
			 allEvents = parseACanlander(contents,
					detectMonthOfCanlander(contents[0]));
			

			System.out.println("done");
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List all = reorderEvents(allEvents);
		return all;
	}
	public List parseEvents(String[][][] contents) {


		List allEvents = null;
		if(contents.length>0)
		allEvents = parseACanlander(contents, "");

		List all = reorderEvents(allEvents);
		return all;
	}

	static String MONTH_PREFIX_STRING = "As";

	int _canlanderWide = 30;

	String months[] = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug",
			"Sep", "Oct", "Nov", "Dec" };

	private static String HEADER_KEY = "Schedule";

	private String fixMonthStr(String str) {
		for (int i = 0; i < months.length; i++)
			if (str.toLowerCase().startsWith(months[i].toLowerCase())) {
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
		for (int i = y + 1; i < sheet.length; i++) {
			String cell1 = sheet[i][0];
			
			if (cell1.indexOf(HEADER_KEY)>-1) {
				startIndexOfPageList.add(new Integer(i));
				return i;
			}

		}
		return sheet.length - 1;

	}

	private List parseACanlander(String[][][] sheet, String date) {
		
		List aCanlander = new ArrayList();
		for(int i=0;i<sheet.length;i++){
			int currentY = 0;
			date=detectMonthOfCanlander(sheet[i]);
			while (true) {
				currentY = findHeaderLinesAfter(sheet[i], currentY);
				if (currentY > sheet[i].length - 10)
					break;
				List temp = parseAPage(sheet[i], currentY, date);
				aCanlander.add(temp);
	
			}
		}
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
		int lastday=-1;
		for (int i = 0; i < sheet[y+1].length && i < _canlanderWide; i++) {
			// Cell cell1 = sheet.getCell(i, y + 1);
			String str = sheet[y+1][i].replace("\n", " ");
			str=str.split(" ").length>=2?str.split(" ")[1]:null;
			if (str != null && !str.equalsIgnoreCase("")
					&& isParsableToInt(str)) {
				if (Integer.parseInt(str) > 0 && Integer.parseInt(str) < 32) {
					
					String date2 = str + "-" + date;
					List temp = parseAday(sheet, i, y + 1, date2);
					aPage.add(temp);
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
			System.out.println(i + " is not a time format.");
			return false;
		}
	}

	private List<TXTEvent> parseAday(String[][] sheet, int x, int y, String date) {
		List aday = new ArrayList();

		for (int i = y+1; i < sheet.length; i++) {
			// Cell cell = sheet[i][x+3];//(x + 3, i);
		
			String str = sheet[i][x];// cell.getContents();
			if (str != null && !str.trim().equalsIgnoreCase("")) {
				
				TXTEvent temp = parseAEvent(sheet, x, i, date);
				if (temp != null)
					aday.add(temp);
			}
		}
		return aday;
	}

	private TXTEvent parseAEvent(String[][] sheet, int y, int x, String date) {
		TXTEvent temp = new TXTEvent();
		temp.setName(sheet[x][y]);
		temp.setServiceID(serviceID);
		String time=sheet[x][0];
		date=date+" "+time;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		Date d1;
		try {
			d1 = sdf.parse(date);
			temp.setStartDate(temp.sdf.format(d1));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return temp;
	}

	private String detectMonthOfCanlander(String[][] sheet) {
		for (int j = 0; j < sheet.length; j++) {
			for (int i = 0; i < sheet[j].length; i++) {
				
				String cell = sheet[j][i];
			//	System.out.println("j: "+j+" i: "+i +" cell: "+cell);
				if(cell==null)
					System.out.println("cell =null");
				else
				if (cell.startsWith(MONTH_PREFIX_STRING)) {
					System.out.println("found the key,As");
					String[] temps = cell.trim().replace(" ", "/").split("/");
					if (temps.length == 5) {
						String month = fixMonthStr(temps[2]) + "-" + temps[4];

//						System.out.println("month: " + month);
						String DATE_FORMAT = "MM-yyyy";
						SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
//						System.out.println(sdf.format(new Date()));
						try {
							Date d = sdf.parse(month);
							System.out.println(d.toString());
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							break;
						}
						System.out.println("month: "+month);
						return month;
					}
				}

			}
		}
		return null;

	}

	public static void main(String[] args) throws IOException {
		StarWorldCalanderReader2 test = new StarWorldCalanderReader2();
		test.setInputFile("./SCMTWHDOCT0930(drama).xlsx");
		XLSXReader reader=new XLSXReader();
		String[][][] contents=reader.parseXLSX("SCMTWHDOCT0930(drama).xlsx");
		List allEvents = test.parseEvents(contents);
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
