package com.syscom.xls_tool.sample;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;

import com.syscom.xls_tool.sample.vo.SampleVo;
import com.syscom.xls_tool.util.XlsParser;
import com.syscom.xls_tool.util.XlsTool;

public class SampleMain {
	
	public static void main(String[] args) {
		XlsParser xlsParser = new XlsParser.Builder()
				// ========== 設定 excel 中需要處理的資料範圍 ==========
				.buildDataRange(5, 0)
				
				// ========== 設定vo欄位對應的excel 中欄位位置 ==========
				// 設定每筆資料  皆固定抓取excel中同一欄位的資料
				.buildFixedField("table_name", 0, 2)
				.buildFixedField("cache_name", 0, 4)
				
				// 設定每筆資料  隨row index 值會不一樣的欄位
				.buildDataField("pkeyIndex", 3)
				.buildDataField("db_column_name", 2)
				.buildDataField("db_column_type", 1)
				.buildDataField("safecache_column_name", 4)
				.buildDataField("safecache_column_type", 5)
				
				// ========== 設定要記錄xls中 rowIndex資訊的欄位 ==========
				.buildRowIdxField("rowIndex")
				
				// ========== 設定要得到的vo ==========
				.buildVo(SampleVo.class)
				
				// ========== 設定那些欄位若為null 則不處理該筆資料 ==========
				.buildCheckNullField("db_column_name")
				.build();

				File file = new File("./sample-schema/mgx/TestSample.xls");
		
				try {
					Sheet sheet = XlsTool.getSheet(file, "Layout");
					List<SampleVo> vos = xlsParser.getVos(sheet);
					
					for (SampleVo sampleVo : vos) {
						System.out.println(sampleVo);
					}
					
				}
				catch (InvalidFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
	
	
	
}
