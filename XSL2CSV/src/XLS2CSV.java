import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class XLS2CSV {
	public static int newbTimeshift=8;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("+---------EPG XLS2CSV Converter----------+ \n"
				+ "|(轉換Excel格式的EPG為NGC官網所需csv格式)|\n"
				+ "|         本程式為NGC ASIA所有           |\n"
				+ "|                 委外開發               |\n"
				+ "|    開發者：蕭立賢(lhhsiao@gmail.com)   |\n"
				+ "+----------------------------------------+");
		Properties p=new Properties();
		
		try {
			p.load(new FileInputStream(new File("setting.txt")));
			newbTimeshift=Integer.valueOf(p.getProperty("NEWSB_TIMESHIFT").trim());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		XLS2CSV xls2txt = new XLS2CSV();
		File wd = new File("./");
		File[] xlsFiles = new File[0];

		if (wd.isDirectory()) {
			xlsFiles = xls2txt.getXLSFromDirectory(wd);
		} else {
			try {
				throw new Exception();
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
		System.out.println("Found " + xlsFiles.length + " xls file(s)");
		int counter = 0;
		if (xlsFiles.length > 0)
			for (int i = 0; i < xlsFiles.length; i++) {
				System.out
						.println("=====================================================");
				System.out.println("Found A Excel File: "
						+ xlsFiles[i].getName());
				try {
					XLS xls = new XLS();
					try {
						xls.setFilename(xlsFiles[i].getName());
						xls.setContent(xls2txt.read(xlsFiles[i]));
						xls.setSheetNames(sheetNames);
					} catch (BiffException e) {
						System.out
								.println("This File can not be read by JXL. Trying to read by CSV mode.");
						SimpleTabDelimitedReader csvr = new SimpleTabDelimitedReader(
								xlsFiles[i]);
						xls.setContent(csvr.getContent());
						System.out.println("read!!");
					}
					List txtEvents = xls.generateTXTEvents();

					if (txtEvents != null) {
						xls2txt.write2CSVFile(txtEvents, new File(xlsFiles[i]
								.getName().substring(0,
										xlsFiles[i].getName().lastIndexOf("."))
								+ ".csv"));
						counter++;
					} else
						System.out
								.println("Can not generate it's csv file for some reason. ");

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		else
			System.out.println("Did not find any .xls or .XLS file.");
		System.out
				.println("=====================================================");
		System.out.println("Generated " + counter + " File(s).");
		handleXLSXFiles();
		handleDOCXFiles();

	}
	static void handleDOCXFiles(){
		XLS2CSV xls2txt = new XLS2CSV();
		File wd = new File("./");
		File[] xlsxFiles = new File[0];

		if (wd.isDirectory()) {
			xlsxFiles = xls2txt.getDOCXFromDirectory(wd);
		} else {
			try {
				throw new Exception();
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
		System.out.println("Found " + xlsxFiles.length + " docx file(s)");
		int counter = 0;
		if (xlsxFiles.length > 0)
			for (int i = 0; i < xlsxFiles.length; i++) {
				System.out
						.println("=====================================================");
				System.out.println("Found A Word File: "
						+ xlsxFiles[i].getName());
				try {
					XLS xls = new XLS();

					NGCDocxReader reader = new NGCDocxReader();
					String[][][] contents = new String[1][1][];
					contents[0][0]=reader.readAsLines(reader.extractText(new FileInputStream(xlsxFiles[i])));
					xls.setFilename(xlsxFiles[i].getName());
					xls.setContent(contents);
					xls.setSheetNames(sheetNames);

					List txtEvents = xls.generateTXTEvents();

					if (txtEvents != null) {
						xls2txt.write2CSVFile(txtEvents, new File(
								xlsxFiles[i].getName().substring(0,xlsxFiles[i].getName().lastIndexOf("."))
										+ ".csv"));
						counter++;
					} else
						System.out
								.println("Can not generate it's csv file for some reason. ");

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		else
			System.out.println("Did not find any .docx or .DOCX file.");
		System.out
				.println("=====================================================");
		System.out.println("Generated " + counter + " File(s).");
	}
	static void handleXLSXFiles() {
		XLS2CSV xls2txt = new XLS2CSV();
		File wd = new File("./");
		File[] xlsxFiles = new File[0];

		if (wd.isDirectory()) {
			xlsxFiles = xls2txt.getXLSXFromDirectory(wd);
		} else {
			try {
				throw new Exception();
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
		System.out.println("Found " + xlsxFiles.length + " xlsx file(s)");
		int counter = 0;
		if (xlsxFiles.length > 0)
			for (int i = 0; i < xlsxFiles.length; i++) {
				System.out
						.println("=====================================================");
				System.out.println("Found A Excel File: "
						+ xlsxFiles[i].getName());
				try {
					XLS xls = new XLS();

					XLSXReader reader = new XLSXReader();
					String[][][] contents = reader.parseXLSX(xlsxFiles[i]
							.getName());
					xls.setFilename(xlsxFiles[i].getName());
					xls.setContent(contents);
					xls.setSheetNames(sheetNames);

					List txtEvents = xls.generateTXTEvents();

					if (txtEvents != null) {
						xls2txt.write2CSVFile(txtEvents, new File(
								xlsxFiles[i].getName().substring(0,xlsxFiles[i].getName().lastIndexOf("."))
										+ ".csv"));
						counter++;
					} else
						System.out
								.println("Can not generate it's txt file for some reason. ");

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		else
			System.out.println("Did not find any .xlsx or .XLSX file.");
		System.out
				.println("=====================================================");
		System.out.println("Generated " + counter + " File(s).");
	}

	void write2TxtFile(List content, File target) {
		Writer out;
		try {
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(target), "UTF8"));
			for (int i = 0; i < content.size(); i++) {
				TXTEvent temp = (TXTEvent) content.get(i);
				temp.getName();

				if (temp.getEndDate().equalsIgnoreCase("")
						&& i + 1 < content.size()) {
					TXTEvent temp2 = (TXTEvent) content.get(i + 1);

					if (temp.sdf.parse(temp.getStartDate()).compareTo(
							temp2.sdf.parse(temp2.getStartDate())) > 0) {
						System.out
								.println("[Error]Found a end time is earlier than the start time.");
						out.append("\r\n[date error]");
						out.append(temp.getName() + "|" + temp.getStartDate()
								+ "|");
						out.append(temp2.getStartDate() + "|");
						out.append("[date error]\r\n");
					} else {
						out.append(temp.getName() + "|" + temp.getStartDate()
								+ "|");
						out.append(temp2.getStartDate() + "|");
					}
				} else {
					break;
				}
			}

			out.flush();
			out.close();
			System.out.println("Generate a txt file: " + target.getName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	void write2CSVFile(List content, File target) {
		String[] head={"Channel ID","Year","Date","Day","Start Time","Duration","Programme","Programme(en)","Episode #","Episode Title"};
		 SimpleDateFormat startTime_sdf = new SimpleDateFormat("HH:mm");
		Writer out;
		List csvL=new ArrayList();
		try {
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(target),"MS950"));
			for (int i = 0; i < content.size(); i++) {
				TXTEvent temp = (TXTEvent) content.get(i);
				String eventName=temp.getName();
//				eventName=eventName.replaceAll("[^\\p{ASCII}]", "");
				eventName=eventName.replaceAll("®", "");
				if(eventName.length()<1)
					break;
				if (temp.getEndDate().equalsIgnoreCase("")
						&& i + 1 < content.size()) {
					TXTEvent temp2 = (TXTEvent) content.get(i + 1);

					if (temp.sdf.parse(temp.getStartDate()).compareTo(
							temp2.sdf.parse(temp2.getStartDate())) > 0) {
						System.out
								.println("[Error]Found a end time is earlier than the start time.");
//						out.append("\r\n[date error]");
//						out.append(temp.getName() + "|" + temp.getStartDate()
//								+ "|");
//						out.append(temp2.getStartDate() + "|");
//						out.append("[date error]\r\n");
						Date d=temp.sdf.parse(temp.getStartDate());
						Calendar c=Calendar.getInstance(); 
						c.setTime(d);
						String[] values={"[date error]"+temp.getServiceID(),String.valueOf(c.get(Calendar.YEAR)),String.valueOf(d.getMonth()+1)+"."+String.valueOf(d.getDate()),
								chiDays[d.getDay()],startTime_sdf.format(d),"",eventName,temp.getEngName(),"",""};
						csvL.add(values);
					} else {
//						out.append(temp.getName() + "|" + temp.getStartDate()
//								+ "|");
//						out.append(temp2.getStartDate() + "|");
						Date d=temp.sdf.parse(temp.getStartDate());
						Calendar c=Calendar.getInstance(); 
						c.setTime(d);
						String[] values={temp.getServiceID(),String.valueOf(c.get(Calendar.YEAR)),String.valueOf(d.getMonth()+1)+"."+String.valueOf(d.getDate()),
								chiDays[d.getDay()],startTime_sdf.format(d),"",eventName,temp.getEngName(),"",""};
						csvL.add(values);
					}
				} else if(temp.getEndDate().equalsIgnoreCase("")
						&& i == content.size()-1){
					Date d=temp.sdf.parse(temp.getStartDate());
					Calendar c=Calendar.getInstance(); 
					c.setTime(d);
					String[] values={temp.getServiceID(),String.valueOf(c.get(Calendar.YEAR)),String.valueOf(d.getMonth()+1)+"."+String.valueOf(d.getDate()),
							chiDays[d.getDay()],startTime_sdf.format(d),"",temp.getName(),temp.getEngName(),"",""};
					csvL.add(values);
				
				}else {
					break;
				}
			}
			CSVWriter csvw=new CSVWriter(out,',','\0','\0',"\r\n");
			
			csvw.writeAll(csvL,head);
			csvw.close();
//			out.flush();
//			out.close();
			System.out.println("Generate a txt file: " + target.getName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
    String[] chiDays={"日","一","二","三","四","五","六"};
	File[] getXLSFromDirectory(File wd) {
		return wd.listFiles(new FileFilter() {

			public boolean accept(File f) {
				// TODO Auto-generated method stub

				if (f.isFile())
					if (f.canRead()
							&& (f.getName().endsWith(".xls") || f.getName()
									.endsWith(".XLS")))
						return true;
				return false;
			}
		});
	}

	File[] getXLSXFromDirectory(File wd) {
		return wd.listFiles(new FileFilter() {

			public boolean accept(File f) {
				// TODO Auto-generated method stub

				if (f.isFile())
					if (f.canRead()
							&& (f.getName().endsWith(".xlsx") || f.getName()
									.endsWith(".XLSX")))
						return true;
				return false;
			}
		});
	}
	File[] getDOCXFromDirectory(File wd) {
		return wd.listFiles(new FileFilter() {

			public boolean accept(File f) {
				// TODO Auto-generated method stub

				if (f.isFile())
					if (f.canRead()
							&& (f.getName().endsWith(".docx") || f.getName()
									.endsWith(".DOCX")))
						return true;
				return false;
			}
		});
	}

	public String[][] readByTabDelimitedMode(File inputWorkbook) {
		return null;
	}

	static List sheetNames = new ArrayList();

	public String[][][] read(File inputWorkbook) throws IOException,
			BiffException {
		String[][][] content = new String[0][0][0];
		Workbook w;

		WorkbookSettings setting = new WorkbookSettings();

		w = Workbook.getWorkbook(inputWorkbook);
		// Get the first sheet

		// Loop over first 10 column and lines
		// System.out.println("Sheet Columns:" +sheet.getColumns());
		// System.out.println("Sheet Rows:" +sheet.getRows());
		content = new String[w.getSheets().length][0][0];
		sheetNames = new ArrayList();
		for (int k = 0; k < w.getSheets().length; k++) {
			Sheet sheet = w.getSheet(k);
			sheetNames.add(sheet.getName());
			content[k] = new String[sheet.getRows()][sheet.getColumns()];
			for (int j = 0; j < sheet.getRows(); j++) {

				for (int i = 0; i < sheet.getColumns(); i++) {
					Cell cell = sheet.getCell(i, j);
					CellType type = cell.getType();
					// System.out.print("at "+i+","+j+" ");
					content[k][j][i] = cell.getContents();
					
					if (cell.getType() == CellType.DATE) {// for Music Channel
						// format
						if (cell.getCellFormat().getFormat().getFormatString()
								.equalsIgnoreCase("hh\\.mm")) {
							SimpleDateFormat sdf = new SimpleDateFormat("HH.mm");
							sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
							content[k][j][i] = sdf.format(((DateCell) cell)
									.getDate());
							// System.out.println("I got a date "
							// + ((DateCell) cell).getContents()
							// + " format: "
							// + cell.getCellFormat().getFormat()
							// .getFormatString());
							// System.out.println("I got a date "
							// + sdf.format(((DateCell) cell).getDate()));
						}
						if (cell.getCellFormat().getFormat().getFormatString()
								.equalsIgnoreCase("h:mm;@")) {// for SMIT HD

							SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
							sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
							content[k][j][i] = sdf.format(((DateCell) cell)
									.getDate());
						}
						if (cell.getCellFormat().getFormat().getFormatString()
								.equalsIgnoreCase("d-mmm")) {// SMIT SD

							SimpleDateFormat sdf = new SimpleDateFormat(
									"yyyy-MM-dd");
							sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
							content[k][j][i] = sdf.format(((DateCell) cell)
									.getDate());
						}
						// System.out.println(cell.getCellFormat().getFormat().getFormatString());
					}

					// if (cell.getType() == CellType.LABEL) {
					// System.out.println("I got a label "
					// + cell.getContents());
					// }
					// if (cell.getType() == CellType.DATE){
					// SimpleDateFormat sdf = new SimpleDateFormat(
					// "HH:mm");
					// sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
					// System.out.println("I got a date "
					// + ((DateCell)cell).getContents()+" format:
					// "+cell.getCellFormat().getFormat().getFormatString());
					// System.out.println("I got a date
					// "+sdf.format(((DateCell)cell).getDate()));
					// }
					//
					// if (cell.getType() == CellType.NUMBER) {
					// System.out.println("I got a number "
					// + cell.getContents());
					// }

				}
			}
		}

		return content;
	}
}
