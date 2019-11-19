package com.tfzq.generate;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

/**
 * 
 * @author pengtao
 *
 */
public class ExcelUtils {
	public static String designDir = "template/design/";

	public static TableDesign readXls(String file) {
		InputStream is;
		TableDesign table = new TableDesign();
		// 设置共用属性
		table.setTableName(StringUtils.substringBefore(file, "."));
		table.setBeanName(Utils.calBeanName(table.getTableName()));
		table.setBeanObj(Utils.calBeanObjName(table.getBeanName()));
		table.setCodeDir(TableDesign.rootDir +File.separator+ table.getBeanObj()+File.separator);

		try {
			is = new FileInputStream(designDir + file);
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
			Field field = null;
			// 循环工作表Sheet
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
			table.setTableDesc(StringUtils.remove(hssfSheet.getSheetName(), "表"));
			// 循环行Row
			for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow != null) {
					
					field = new Field(hssfRow.getCell(0).getStringCellValue(), 
							hssfRow.getCell(1).getStringCellValue(),
							hssfRow.getCell(2, Row.CREATE_NULL_AS_BLANK).getStringCellValue(), 
							hssfRow.getCell(3, Row.CREATE_NULL_AS_BLANK).getStringCellValue(),
							hssfRow.getCell(4, Row.CREATE_NULL_AS_BLANK).getStringCellValue());
					table.addField(field);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return table;
	}

	// @SuppressWarnings("static-access")
	// private String getValue(HSSFCell hssfCell) {
	// if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
	// // 返回布尔类型的值
	// return String.valueOf(hssfCell.getBooleanCellValue());
	// } else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
	// // 返回数值类型的值
	// return String.valueOf(hssfCell.getNumericCellValue());
	// } else {
	// // 返回字符串类型的值
	// return String.valueOf(hssfCell.getStringCellValue());
	// }
	// }
}