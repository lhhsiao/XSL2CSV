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

public class StartMovieCalanderReader {
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
			System.out.print(temp1.getName() + "|" + temp1.getStartDate() + "|"
					+ temp2.getStartDate() + "|");
			// lastEvent=temp1;
		}

	}

	public List parseEvents() {
		File inputWorkbook = new File(inputFile);
		Workbook w;
		List allEvents =null;
		try {
			w = Workbook.getWorkbook(inputWorkbook);
			// Get the first sheet
			Sheet sheet = w.getSheet(0);
			// detectMonthOfCanlander(sheet);
			 allEvents = parseACanlander(sheet,
					detectMonthOfCanlander(sheet));
			

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

	static String MONTH_PREFIX_STRING = "PROGRAMME SCHEDULE : ";

	int _canlanderWide = 70;

	String months[] = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug",
			"Sep", "Oct", "Nov", "Dec" };

	private static String HEADER_KEY = "Time";

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

	private int findHeaderLinesAfter(Sheet sheet, int y) {
		List<Integer> startIndexOfPageList = new ArrayList<Integer>();
		for (int i = y + 1; i < sheet.getRows(); i++) {
			Cell cell1 = sheet.getCell(0, i);

			if (cell1.getContents().endsWith(HEADER_KEY)) {
				startIndexOfPageList.add(new Integer(i));
				return i;
			}

		}
		return sheet.getRows() - 1;

	}

	private List parseACanlander(Sheet sheet, String date) {
		int currentY = 0;
		List aCanlander = new ArrayList();
		while (true) {

			currentY = findHeaderLinesAfter(sheet, currentY);
			if (currentY > sheet.getRows() - 10)
				break;
			List temp = parseAPage(sheet, currentY, date);
			aCanlander.add(temp);

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

	private List parseAPage(Sheet sheet, int y, String date) {
		List aPage = new ArrayList();
		for (int i = 0; i < sheet.getColumns() && i < _canlanderWide; i++) {
			Cell cell1 = sheet.getCell(i, y + 1);
			String str = cell1.getContents();
			if(cell1.getCellFormat()!=null)
			if (cell1.getCellFormat().getBackgroundColour().getDefaultRGB()
					.getBlue() == 255
					&& cell1.getCellFormat().getFont().getColour()
							.getDefaultRGB().getBlue() == 0)
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
			System.out.println(i+" is not a time format.");
			return false;
		}
	}

	private List<TXTEvent> parseAday(Sheet sheet, int x, int y, String date) {
		List aday = new ArrayList();
		boolean first00=true;
		for (int i = y; i < sheet.getRows()
				&& i < findHeaderLinesAfter(sheet, y); i++) {
			Cell cell = sheet.getCell(x + timeCellShift, i);//+3 or +5
			String str = cell.getContents();
			if(cell.getCellFormat()!=null)
			if (cell.getCellFormat().getBackgroundColour().getDefaultRGB()
					.getBlue() == 255
					&& cell.getCellFormat().getFont().getColour()
							.getDefaultRGB().getBlue() == 0)
				if (str != null && !str.trim().equalsIgnoreCase("")
						&& isParsableToDate(str)) {
					if (str.startsWith("00:") || str.startsWith("0:"))
						if(!first00)
							str = "24" + str.substring(str.indexOf(":"));
						else{}
					else
						first00=false;
					String date2 = date + " " + str;
					TXTEvent temp = parseAEvent(sheet, x, i, date2);
					if (temp != null)
						aday.add(temp);
				}
		}
		return aday;
	}

	private TXTEvent parseAEvent(Sheet sheet, int x, int y, String date) {
		TXTEvent temp = new TXTEvent();
		for (int i = 0; i < 5; i++) {
			String str = sheet.getCell(x, y + i).getContents().trim();
			if (!str.equalsIgnoreCase("")
					&& !(str.startsWith("<") && str.endsWith(">"))) {
				temp.setName(str);
				break;
			}
		}
		for (int i = 0; y + i < findHeaderLinesAfter(sheet, y); i++) {
			String str = sheet.getCell(x + 1, y + i).getContents().trim();
			if (!str.equalsIgnoreCase("") && str.length() >= 3
					&& (str.startsWith("[") && str.endsWith("]"))) {

				temp.setName(temp.getName() + "[" + str.substring(1, 2) + "]");
				break;
			}
		}
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

	private String detectMonthOfCanlander(Sheet sheet) {
		for (int j = 0; j < sheet.getColumns(); j++) {
			for (int i = 0; i < sheet.getRows(); i++) {
				Cell cell = sheet.getCell(j, i);
				if (cell.getContents().startsWith(MONTH_PREFIX_STRING)) {
					String[] temps = cell.getContents().trim().split(" ");
					if (temps.length == 5) {
						String month = fixMonthStr(temps[3]) + "-" + temps[4];

						System.out.println("month: " + month);
						String DATE_FORMAT = "MM-yyyy";
						SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
						System.out.println(sdf.format(new Date()));
						try {
							Date d = sdf.parse(month);
							System.out.println(d.toString());
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							break;
						}

						return month;
					}
				}

			}
		}
		return null;

	}

	public static void main(String[] args) throws IOException {
		StartMovieCalanderReader test = new StartMovieCalanderReader();
		test.setInputFile("./SCMTWHDSEP00830(drama).xls");
//		 test.read();
		List allEvents=test.parseEvents();
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
