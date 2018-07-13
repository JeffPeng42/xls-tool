package com.syscom.xls_tool.util;

import java.io.File;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class XlsTool {

	public static Sheet getSheet(File tableSpec, String sheetName) throws InvalidFormatException, IOException{
		Workbook workbook = getWorkbook(tableSpec);
		Sheet sheet = getSheet(workbook, sheetName);
		return sheet;
	}
	
	
	public static Workbook getWorkbook(File tableSpec) throws InvalidFormatException, IOException {
		Workbook wb = null;
		wb = WorkbookFactory.create(tableSpec);
		return wb;
	}
	
	public static Sheet getSheet(Workbook wb, String sheetName){
		Sheet sheet = wb.getSheet(sheetName);
		return sheet;
	}
	

}
