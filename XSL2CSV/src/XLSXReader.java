import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import jxl.WorkbookSettings;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;

public class XLSXReader {
	private Cell lastCellArray[][] = null;

	public String[][][] parseXLSX(String filePath) {
		InputStream inp;
		String[][][] contents = null;
		Workbook wb = null;
		try {
			inp = new FileInputStream(filePath);
			wb = WorkbookFactory.create(inp);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int numberOfSheets = wb.getNumberOfSheets();
		FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
		
		int shownSheetCount=0;
		for (int k = 0; k < numberOfSheets ; k++) {
			if(!(wb.isSheetHidden(k)))
				shownSheetCount++;
		}
		System.out.println("shownSheetCount: "+shownSheetCount);
		contents = new String[shownSheetCount][][];
		int k=-1;
		for (int kk = 0; kk < numberOfSheets ; kk++) {
			if((wb.isSheetHidden(kk)))
				continue;
			k++;
			Sheet sheet = wb.getSheetAt(kk);
			
			HSSFDataFormatter dfter = new HSSFDataFormatter();
			// HSSFWorkbook hssfworkbook = new HSSFWorkbook();
			// HSSFDataFormat df = hssfworkbook.createDataFormat();
			if (sheet.getPhysicalNumberOfRows() < 1)
				continue;
			contents[k] = new String[sheet.getPhysicalNumberOfRows()][];
			lastCellArray = new Cell[sheet.getPhysicalNumberOfRows()][];
			for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
//System.out.println("i:"+i);
				if(sheet.getRow(i)!=null)
					if(i==0 && sheet.getRow(i).getLastCellNum()<5){
						lastCellArray[i] = new Cell[6];//for SMIT HD
						contents[k][i]= new String[6];
					}else{
						if(sheet.getRow(i)==null)
							break;
						if(sheet.getRow(i).getLastCellNum()>0){
							lastCellArray[i] = new Cell[sheet.getRow(i).getLastCellNum()];
							contents[k][i] = new String[sheet.getRow(i).getLastCellNum()];
						}else{
							lastCellArray[i] = new Cell[0];
							contents[k][i] = new String[0];
						}	
					}
				else
					break;
				for (int j = 0; j < sheet.getRow(i).getLastCellNum() || ( i==0 && j<5 && sheet.getRow(i).getPhysicalNumberOfCells()<5); j++) {
					Cell c = sheet.getRow(i).getCell(j);
					if(c==null && sheet.getRow(i).getLastCellNum()>0){
						c=sheet.getRow(i).getCell(sheet.getRow(i).getLastCellNum()-1);
					}
					lastCellArray[i][j] = c;
					contents[k][i][j] = "";
					// c.getRichStringCellValue().getString()
					String content = "";
					try {
						if (c != null) {
							// c.setCellType(Cell.CELL_TYPE_STRING);

							//						
							// if(s==0)
							// continue;
							if (c.getCellType() == Cell.CELL_TYPE_NUMERIC) {
								if (HSSFDateUtil.isCellDateFormatted(c)) {
//									System.out.print("d "
//											+ c.getCellStyle()
//													.getDataFormatString()
//											+ " " + dfter.formatCellValue(c)
//											+ ",");
									content = dfter.formatCellValue(c);
									// c.getCellStyle().getDataFormatString()
								} else {
									System.out.print(dfter.formatCellValue(c)
											+ ",");
									content = dfter.formatCellValue(c);
								}
							} else if (c.getCellType() == Cell.CELL_TYPE_STRING) {
//								System.out.print(c.getRichStringCellValue()
//										.getString()
//										+ ",");
								content = c.getRichStringCellValue()
										.getString();
							} else if (c.getCellType() == Cell.CELL_TYPE_FORMULA) {
								// System.out.print(HSSFDateUtil.getJavaDate(c
								// .getNumericCellValue())
								// + ",");
//								CellValue cellValue = evaluator.evaluate(c);
								
								
//								System.out.println("Formula is " + c.getCellFormula());
						        switch(c.getCachedFormulaResultType()) {
						            case Cell.CELL_TYPE_NUMERIC:
						               // System.out.println("Last evaluated as: " + c.getNumericCellValue());
						            	c.setCellType(Cell.CELL_TYPE_NUMERIC);
//						            	System.out.println("d "
//												+ c.getCellStyle()
//														.getDataFormatString() + " "
//												+ dfter.formatCellValue(c) + ",");
						                content = dfter.formatCellValue(c);//for SCM HD/ SCMTWHD
						            default:
//						            case Cell.CELL_TYPE_String:
//						                System.out.println("Last evaluated as \"" + c.getRichStringCellValue().toString() + "\"");
						            content=c.getRichStringCellValue().toString();
						                break;
						        }
//								c.setCellType(Cell.CELL_TYPE_NUMERIC);
//								System.out.print("d "
//										+ c.getCellStyle()
//												.getDataFormatString() + " "
//										+ dfter.formatCellValue(c) + ",");
//								
//								content = dfter.formatCellValue(c);
						        content=c.getRichStringCellValue().toString();
							}
							else{
								try{
								 content = dfter.formatCellValue(c);
								}catch(Exception e){
									System.out.println("XLSX cell convert error");
								}
							}
							// RichTextString str = c.getRichStringCellValue();
							// for (int g = 0; g < str.numFormattingRuns(); g++)
							// {
							// int fontIndex = str.getIndexOfFormattingRun(g);//
							// .getFontOfFormattingRun(g);
							// XSSFFont font = (XSSFFont) wb
							// .getFontAt((short) fontIndex);
							// int colorIndex = font.getColor();
							// XSSFColor color = font.getXSSFColor();//
							// (XSSFColor)
							// // XSSFColor.getIndexHash().get(new
							// // Integer(colorIndex));//.get(colorIndex);
							// System.out.println("color: "
							// + color.getARGBHex());
							//
							// if (colorIndex != 8) {
							// // /content="";
							// System.out.println("foreground Color: "
							// + color.getARGBHex() + " i: " + i
							// + " j:" + j);
							// }
							// }
							// c.getCellStyle().getDataFormatString();
							CellStyle c_style = c.getCellStyle();
						
							if (i == 98 && j == 10) {
								System.out.println("debug");
							}
							if (c_style != null && k==2) {
								short bcolorshort = c_style
										.getFillBackgroundColor();
								short fcolorshort = c_style.getFillForegroundColor();
								Color bcolorC = c_style
								.getFillBackgroundColorColor();
								if(bcolorC instanceof XSSFColor){
								
								XSSFColor bcolorXSSFC = (XSSFColor)bcolorC;
								XSSFColor fcolorXSSFC = (XSSFColor)c_style.getFillForegroundColorColor();
								// HSSFColor.getIndexHash().get(colorshort);
								
								if (!content.equalsIgnoreCase("") ){//&& !(fcolorshort == 64 ) || bcolorshort != 64) {// (colorshort
																				// !=
																				// 9)
																				// {//
									// 9=white
//									bcolorC.getARGBHex()
//									byte[] bytes = fcolorXSSFC.getRgb();
//									for(int gg=0;gg<bytes.length;gg++)
//										System.out.print(gg+" "+bytes[gg]);
//									System.out.println("k: "+k+ " i: " + i + " j: " + j
//											+ " fcolor: " + fcolorshort
//											+ " bcolor: " + bcolorshort
//											+ " fcolorC: " + fcolorXSSFC
//											+ " bcolorC: " + bcolorXSSFC
//											+" ARGBHex: "+fcolorXSSFC.getRgb()
//											+" "+content);
//									content = "";
								}
								}
							}
						}
					} catch (IllegalStateException e) {

					} catch (Exception e) {
						e.printStackTrace();
					}

					contents[k][i][j] = content;
				}
				// System.out.println();
			}
		}

		// InputStream inp = new FileInputStream("workbook.xlsx");

		return contents;
	}

	public void write2XLSbyJXL() throws IOException, WriteException {
		File file = new File("test.xls");
		WorkbookSettings wbSettings = new WorkbookSettings();

		wbSettings.setLocale(new Locale("en", "EN"));

		WritableWorkbook workbook = jxl.Workbook.createWorkbook(file,
				wbSettings);
		workbook.createSheet("Report", 0);
		WritableSheet excelSheet = workbook.getSheet(0);

		for (int i = 0; i < lastCellArray.length; i++) {
			for (int j = 0; j < lastCellArray[i].length; j++) {
				// Label temp=new Label(i,j,lastCellArray[i][j].);
			}
		}

		workbook.write();
		workbook.close();
	}

	public static void main(String[] args) {
		XLSXReader reader = new XLSXReader();
		String[][][] contents = reader.parseXLSX("SCMTWHDOCT0930(drama).xlsx");
		StartMovieCalanderReader2 smc = new StartMovieCalanderReader2();
		List result = smc.parseEvents(contents[0]);
		smc.printAllEvents(result);
		System.out.println("done");

	}

	public void converXLSX2XLS(File in, File out) {

	}
}
