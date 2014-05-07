import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class testPOI {
	public static void main(String[] args) {
		InputStream inp;
		try {
			inp = new FileInputStream("SCMTWHDSEP00830(drama).xlsx");
			Workbook wb = WorkbookFactory.create(inp);
			Sheet sheet = wb.getSheetAt(0);
			Row row = sheet.getRow(2);

			Cell cell = row.getCell(3);

			if (cell == null)
				cell = row.createCell(3);
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue("a test");
			HSSFDataFormatter dfter=new HSSFDataFormatter();
//			HSSFWorkbook hssfworkbook = new HSSFWorkbook();
//			HSSFDataFormat df = hssfworkbook.createDataFormat();
			for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
				for (int j = 0; j < sheet.getRow(0).getPhysicalNumberOfCells(); j++) {
					Cell c = sheet.getRow(i).getCell(j);
					// c.getRichStringCellValue().getString()
					if (c != null) {
						// c.setCellType(Cell.CELL_TYPE_STRING);

						if (c.getCellType() == Cell.CELL_TYPE_NUMERIC) {
							if (HSSFDateUtil.isCellDateFormatted(c)) {
								
								System.out.print("d "+ c.getCellStyle().getDataFormatString()+" "+dfter.formatCellValue(c)
										+ ",");

								//c.getCellStyle().getDataFormatString()
								
								
							}else
								System.out.print(dfter.formatCellValue(c)
									+ ",");
						} else if (c.getCellType() == Cell.CELL_TYPE_STRING)
							System.out.print(c.getRichStringCellValue()
									.getString()
									+ ",");
						else if (c.getCellType() == Cell.CELL_TYPE_FORMULA){
//							System.out.print(HSSFDateUtil.getJavaDate(c
//									.getNumericCellValue())
//									+ ",");
							c.setCellType(Cell.CELL_TYPE_NUMERIC);
							System.out.print("d "+ c.getCellStyle().getDataFormatString()+" "+dfter.formatCellValue(c)+ ",");
						}
						// c.getCellStyle().getDataFormatString();
					}
					;
				}
				System.out.println();
			}
			System.out.println(cell.getStringCellValue());
			// Write the output to a file
			FileOutputStream fileOut = new FileOutputStream("workbook.xlsx");
			wb.write(fileOut);

			fileOut.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// InputStream inp = new FileInputStream("workbook.xlsx");

	}
}
