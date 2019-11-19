package com.tfzq.util.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class WriteExcelUtil {

	private static String EXCEL_XLS = "xls";
	private static String EXCEL_XLSX = "xlsx";

	/**
	 * 判断Excel的版本,获取Workbook
	 * 
	 * @param in
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static Workbook getWorkbok(String filename) throws IOException {
		Workbook wb = null;
		FileInputStream in = new FileInputStream(new File(filename));
		if (filename.endsWith(EXCEL_XLS)) { // Excel 2003
			wb = new HSSFWorkbook(in);
		} else if (filename.endsWith(EXCEL_XLSX)) { // Excel 2007/2010
			wb = new XSSFWorkbook(in);
		}
		return wb;
	}

	/**
	 * 找到需要插入的行数，并新建一个POI的row对象
	 * 
	 * @param sheet
	 * @param rowIndex
	 * @return
	 */
	private static Row createRow(Sheet sheet, Integer rowIndex) {
		Row row = null;
		if (sheet.getRow(rowIndex) != null) {
			int lastRowNo = sheet.getLastRowNum();
			sheet.shiftRows(rowIndex, lastRowNo, 1);
		}
		row = sheet.createRow(rowIndex);
		return row;
	}

	/**
	 * 将数据写入到文件中
	 * 
	 * @param wb
	 * @param sheetName
	 * @param fileName
	 * @param sheetResult
	 * @throws IOException
	 */
	public static void writeDataToExcel(Workbook wb, String outFileName, SheetResult sheetResult) throws IOException {

		FileOutputStream out = null;
		try {
			// excel模板路径
			Sheet sheet = wb.getSheetAt(0);
			// 在相应的单元格进行赋值
			for (CellData cd : sheetResult.getDataList()) {
				Row row = sheet.getRow(cd.getRow());
				if (row == null) {
					row = sheet.createRow(cd.getRow());
				}
				Cell cell = row.getCell(cd.getColumn());
				if (cell == null) {
					cell = row.createCell(cd.getColumn());
				}
				cell.setCellValue(cd.getText());
			}
			// 修改模板内容导出新模板
			out = new FileOutputStream(outFileName);
			wb.write(out);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public static void copyRow(Workbook workbook, Sheet worksheet, int sourceRowNum, int destinationRowNum) {
		// Get the source / new row
		Row newRow = worksheet.getRow(destinationRowNum);
		Row sourceRow = worksheet.getRow(sourceRowNum);

		// If the row exist in destination, push down all rows by 1 else create
		// a new row
		if (newRow != null) {
			worksheet.shiftRows(destinationRowNum, worksheet.getLastRowNum(), 1);
		} else {
			newRow = worksheet.createRow(destinationRowNum);
		}

		// Loop through source columns to add to new row
		for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
			// Grab a copy of the old/new cell
			Cell oldCell = sourceRow.getCell(i);
			Cell newCell = newRow.createCell(i);

			// If the old cell is null jump to next cell
			if (oldCell == null) {
				newCell = null;
				continue;
			}

			// Copy style from old cell and apply to new cell
			CellStyle newCellStyle = workbook.createCellStyle();
			newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
			;
			newCell.setCellStyle(newCellStyle);

			// If there is a cell comment, copy
			if (oldCell.getCellComment() != null) {
				newCell.setCellComment(oldCell.getCellComment());
			}

			// If there is a cell hyperlink, copy
			if (oldCell.getHyperlink() != null) {
				newCell.setHyperlink(oldCell.getHyperlink());
			}

			// Set the cell data type
			newCell.setCellType(oldCell.getCellTypeEnum());

			// Set the cell data value
			switch (oldCell.getCellTypeEnum()) {
			case BLANK:
				newCell.setCellValue(oldCell.getStringCellValue());
				break;
			case BOOLEAN:
				newCell.setCellValue(oldCell.getBooleanCellValue());
				break;
			case ERROR:
				newCell.setCellValue("");
				break;
			case FORMULA:
				newCell.setCellFormula(oldCell.getCellFormula());
				break;
			case NUMERIC:
				newCell.setCellValue(oldCell.getNumericCellValue());
				break;
			case STRING:
				newCell.setCellValue(oldCell.getRichStringCellValue());
				break;
			}
		}

		// If there are are any merged regions in the source row, copy to new
		// row
		for (int i = 0; i < worksheet.getNumMergedRegions(); i++) {
			CellRangeAddress cellRangeAddress = worksheet.getMergedRegion(i);
			if (cellRangeAddress.getFirstRow() == sourceRow.getRowNum()) {
				CellRangeAddress newCellRangeAddress = new CellRangeAddress(newRow.getRowNum(),
						(newRow.getRowNum() + (cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow())),
						cellRangeAddress.getFirstColumn(), cellRangeAddress.getLastColumn());
				worksheet.addMergedRegion(newCellRangeAddress);
			}
		}
	}

	/**
	 * 以字符串形式读取单元格中的内容，如果该单元格内容为空或者空格，则返回null
	 * @param hssfRow
	 * @param columnNum
	 * @return
	 */
	public static String trimCellStringToNull(Row hssfRow, int columnNum) {
		if (null != hssfRow.getCell(columnNum)) {
			hssfRow.getCell(columnNum).setCellType(CellType.STRING);
			String val = hssfRow.getCell(columnNum).getStringCellValue();
			if (StringUtils.isNotBlank(val)) {
				//替换中文空格、tab符号
				val=val.replace("　","").replace("\t","");
				return val;
			}
		}
		return null;
	}
	/**
	 * 以字符串形式读取单元格中的内容，如果该单元格内容为空或者空格，则返回null
	 * @param hssfRow
	 * @param columnNum
	 * @return
	 */
	public static String trimAllBlankCellStringToNull(Row hssfRow, int columnNum) {
		if (null != hssfRow.getCell(columnNum)) {
			hssfRow.getCell(columnNum).setCellType(CellType.STRING);
			String val = hssfRow.getCell(columnNum).getStringCellValue();
			if (StringUtils.isNotBlank(val)) {
				//替换中文空格、英文空格、tab符号
				val=val.replace("　","").replace(" ","").replace("\t","");
				return val;
			}
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		// Workbook wb = createWorkBook(XLSX);
		// Workbook readWb =
		// ReadExcelUtil.getWorkBook("J:\\MyEclipse2014\\studyworkspace\\MicroftOffice\\temp\\test.xlsx");
		//
		// Set<String> includeColNameSet = new HashSet<String>();
		// includeColNameSet.add("START");
		// includeColNameSet.add("VOL");
		// includeColNameSet.add("VOH");
		// includeColNameSet.add("DFS");
		// includeColNameSet.add("FG");
		// writeDataToExcel(wb, "Cell",
		// "J:\\MyEclipse2014\\studyworkspace\\MicroftOffice\\temp\\writetest.xlsx",
		// ReadExcelUtil.readFromSheet(readWb, "type", includeColNameSet, 1));

		String fileName = "C:\\Users\\Administrator\\Desktop\\investor.xlsx";
		String outFile = "D:\\export.xlsx";
		// Cell cell2 = sheet.getRow(3).getCell(0);
		// cell2.setCellValue(" 经基金托管人核准，截至2017-09-15,以下基金资产净值如下：");
		SheetResult sheetD = new SheetResult();
		sheetD.getDataList().add(new CellData(1, 0, "自然人(非员工跟投)"));
		sheetD.getDataList().add(new CellData(2, 0, "自然人(员工跟投)"));
		sheetD.getDataList().add(new CellData(3, 0, "境内法人机构(公司等)"));
		sheetD.getDataList().add(new CellData(4, 0, "境内非法人机构(一般合伙企业等)"));
		sheetD.getDataList().add(new CellData(1, 1, "张三"));
		sheetD.getDataList().add(new CellData(2, 1, "李四"));
		sheetD.getDataList().add(new CellData(3, 1, "王五"));
		sheetD.getDataList().add(new CellData(4, 1, "赵六"));

		try {
			Workbook wb = WriteExcelUtil.getWorkbok(fileName);
			// WriteExcelUtil.copyRow(wb, wb.getSheetAt(0), 6, 7);
			WriteExcelUtil.writeDataToExcel(wb, outFile, sheetD);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
