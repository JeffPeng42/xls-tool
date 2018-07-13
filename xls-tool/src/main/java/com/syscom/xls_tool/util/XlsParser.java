package com.syscom.xls_tool.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.google.common.base.Preconditions;
import com.syscom.xls_tool.vo.Xls_data_field;
import com.syscom.xls_tool.vo.Xls_field_loc;
import com.syscom.xls_tool.vo.Xls_fixed_field;


public class XlsParser {
	private String voName;
	private List<Xls_fixed_field> lstFixedField = new ArrayList<Xls_fixed_field>();
	private List<Xls_data_field> lstDataField = new ArrayList<Xls_data_field>();
	private List<String> lstCheckNullField = new ArrayList<String>();
	private String rowIdxFieldName;
	private Class clazz;
	private int data_strt_idx = -99;
	private int data_end_idx = -99;
	
	public XlsParser(Builder builder) {
		this.voName = builder.VoName;
		this.lstFixedField = builder.lstFixedField;
		this.lstDataField = builder.lstDataField;
		this.rowIdxFieldName = builder.rowIdxFieldName;
		this.clazz = builder.clazz;
		this.data_strt_idx = builder.data_strt_idx;
		this.data_end_idx = builder.data_end_idx;
		this.lstCheckNullField = builder.lstCheckNullField;
	}
	
	public <T> List<T> getVos(Sheet sheet) throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException{
		
		List<Object> lstVos = new ArrayList<Object>();
		// 從xlsx中取出固定欄位的值，準備設定到vo中
		extractFixedFieldVal(sheet);
		
		int row_strt_idx = data_strt_idx;
		int row_end_idx = (data_end_idx == -99 || data_end_idx == 0) ? sheet.getLastRowNum() : data_end_idx;
		for ( int rowIndex = row_strt_idx ; rowIndex <= row_end_idx; rowIndex++) {
			// 建出vo
			Object newVo =  Class.forName(clazz.getName()).newInstance();
			
			// ========== 設定RowIndex欄位 ==========
			setRowIndexField(rowIndex, newVo);
			
			// ========== 設定固定不變的值 ==========
			setFixedFieldVal2Vo(newVo);
			
			// ========== 設定各個欄位的值 ==========
			setDataField(sheet, rowIndex, newVo);
			lstVos.add(newVo);
		}
		
		List<Object> lstFilterVos = filterNullField(lstVos);
		
		List<T> lst = castList(clazz, lstFilterVos);
		
		return lst;
	}

	private void setRowIndexField(int rowIndex, Object newVo) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (rowIdxFieldName != null && rowIdxFieldName.trim().length() > 0) {
			setFieldVal(newVo, rowIdxFieldName, String.valueOf(rowIndex));
		}
	}
	
	public static <T> List<T> castList(Class<? extends T> clazz, Collection<?> collection) {
		List<T> lst = new ArrayList<T>(collection.size());
		for(Object o: collection) {
			lst.add(clazz.cast(o));
		}
		return lst;
	}

	/**
	 * 過濾掉 欄位不能為空值的vo
	 * @param lstVos
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws NoSuchMethodException 
	 */
	private List<Object> filterNullField(List<?> lstVos) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		List<Object> lstFilter = new ArrayList<Object>();
		
		for (Object object : lstVos) {
			for (String checkNullField : lstCheckNullField) {
				Object fieldVal = getFieldVal(object, checkNullField);
				// 濾掉null or 空字串
				if (fieldVal == null) continue;
				if (fieldVal instanceof String) {
					String sVal = (String)fieldVal;
					if (sVal.trim().length() == 0) {
						continue;
					}
				}
				lstFilter.add(object);
			}
		}
		return lstFilter;
	}

	private void setDataField(Sheet sheet, int rowIndex, Object newVo) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Row row = sheet.getRow(rowIndex); 
		for (Xls_data_field Xls_data_field : lstDataField) {
			
			// 取出xls中的值
			int col_Index = Xls_data_field.getXls_field_loc().getCol_Index();
			String xlsValue = getXlsValue(sheet, rowIndex, col_Index);
			
			// 將值設定到Vo中
			String fieldName = Xls_data_field.getFieldName();
			setFieldVal(newVo, fieldName, xlsValue);
		}
	}
	
	private Object getFieldVal(Object newVo, String fieldName) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method getMethod = getGetMethod(fieldName);
		Object value = getMethod.invoke(newVo);
		return value;
	}
	
	
	private void setFieldVal(Object newVo, String fieldName, String val) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method setMethod = getSetMethod(fieldName);
		
		Type[] genericParameterTypes = setMethod.getGenericParameterTypes();
		Type type = genericParameterTypes[0];
		String typeName = type.getTypeName();
		if (typeName.equals("int")) {
			Integer iVal = Integer.valueOf(val);
			setMethod.invoke(newVo, iVal);
		}
		else{
			setMethod.invoke(newVo, val);
		}
	}
	
	
	
	private void setFixedFieldVal2Vo(Object newVo) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		for (Xls_fixed_field xls_fixed_field : lstFixedField) {
			String fieldName = xls_fixed_field.getFieldName();
			String value = xls_fixed_field.getValue();
			setFieldVal(newVo, fieldName, value);
//			Method setMethod = getSetMethod(fieldName);
//			setMethod.invoke(newVo, value);
		}
	}
	
	private Method getSetMethod(String fieldName) throws NoSuchMethodException{
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			String methodName = method.getName();
			if (methodName.startsWith("set")) {
				methodName = methodName.replace("set", "");
				if (methodName.equalsIgnoreCase(fieldName)) {
					return method;
				}
			}
		}
		throw new NoSuchMethodException("fieldName:<"+fieldName+"> can't find mapping method");
	}
	
	private Method getGetMethod(String fieldName) throws NoSuchMethodException{
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			String methodName = method.getName();
			if (methodName.startsWith("get")) {
				methodName = methodName.replace("get", "");
				if (methodName.equalsIgnoreCase(fieldName)) {
					return method;
				}
			}
		}
		throw new NoSuchMethodException("fieldName:<"+fieldName+"> can't find mapping method");
	}
	
	
	/**
	 * 從xlsx 中找出固定欄位的值
	 * @param sheet
	 */
	private void extractFixedFieldVal(Sheet sheet) {
		for (Xls_fixed_field xls_fixed_field : lstFixedField) {
			Xls_field_loc xls_field_loc = xls_fixed_field.getXls_field_loc();
			int row_idx = xls_field_loc.getRow_Index();
			int col_idx = xls_field_loc.getCol_Index();
			String value = getXlsValue(sheet, row_idx, col_idx);
			xls_fixed_field.setValue(value);
		}
	}
	
	private String getXlsValue(Sheet sheet, int row_idx, int col_idx){
		Row row = sheet.getRow(row_idx);
		if (row == null ) return null;
		
		Cell cell = row.getCell(col_idx);
		if (cell == null ) return null;
		
		String value = null;
		try{
			value = cell.getStringCellValue();
			value = value.trim();
		}
		catch (IllegalStateException ex) {
			value = String.valueOf(cell.getNumericCellValue());
		}
		return value;
	}
	

	public String getVoName() {
		return voName;
	}


	public static class Builder {
		private String VoName;
		private List<Xls_fixed_field> lstFixedField = new ArrayList<Xls_fixed_field>();
		private List<Xls_data_field> lstDataField = new ArrayList<Xls_data_field>();
		private List<String> lstCheckNullField = new ArrayList<String>();
		private String rowIdxFieldName;
		
		private Class clazz;
		private int data_strt_idx = -99;
		private int data_end_idx = -99;

		/**
		 * 設定vo中值不會變動的欄位
		 * @param fieldName
		 * @param col_idx
		 * @param row_idx
		 * @return
		 */
		public Builder buildFixedField(String fieldName, int row_idx, int col_idx) {
			Xls_field_loc xls_field_loc = new Xls_field_loc(row_idx, col_idx);
			Xls_fixed_field xls_fixed_field = new Xls_fixed_field(fieldName, xls_field_loc);
			lstFixedField.add(xls_fixed_field);
			return this;
		}

		/**
		 * 設定Vo中 值會變動的欄位
		 * @param fieldName
		 * @param col_idx
		 * @return
		 */
		public Builder buildDataField(String fieldName, int col_idx) {
			Xls_field_loc xls_field_loc = new Xls_field_loc(-1, col_idx);
			Xls_data_field xls_data_field = new Xls_data_field(fieldName, xls_field_loc);
			lstDataField.add(xls_data_field);
			return this;
		}
		
		public Builder buildRowIdxField(String rowIdxFieldName) {
			this.rowIdxFieldName = rowIdxFieldName;
			return this;
		}
		
		/**
		 * 若該field 在excel中為空值，則最後不會產生該對應vo
		 * @param fieldName
		 * @return
		 */
		public Builder buildCheckNullField(String fieldName) {
			boolean isContainField = false;
			for (Xls_fixed_field xls_fixed_field : lstFixedField) {
				String existFieldName = xls_fixed_field.getFieldName();
				if (existFieldName.equals(fieldName)) {
					isContainField = true;
					break;
				}
			}
			
			for (Xls_data_field xls_data_field : lstDataField) {
				String existFieldName = xls_data_field.getFieldName();
				if (existFieldName.equals(fieldName)) {
					isContainField = true;
					break;
				}
			}
			
			Preconditions.checkArgument((isContainField), "buildCheckNullField failed, field:<"+fieldName+"> 需先定義");
			lstCheckNullField.add(fieldName);
			return this;
		}
		
		public Builder buildVo(Class clazz) {
			Preconditions.checkArgument((clazz != null), "Vo 不可為null");
			
			this.clazz = clazz;
			String simpleName = clazz.getSimpleName();
			this.VoName = simpleName;
			return this;
		}
		
		/**
		 * @param data_strt_idx : 資料起始位置，需 > 0
		 * @param data_end_idx : 資料結束位置，可不設定
		 * @return
		 */
		public Builder buildDataRange(int data_strt_idx, int data_end_idx) {
			Preconditions.checkArgument((data_strt_idx >= 0), "data_strt_idx 不可為負數");
			this.data_strt_idx = data_strt_idx;
			this.data_end_idx = data_end_idx;
			return this;
			
		}
		
		public XlsParser build() {
			Preconditions.checkArgument((clazz != null), "Vo 不可為null, 請呼叫buildVo");
			Preconditions.checkArgument((data_strt_idx != -99 && data_strt_idx >=0 ), "請呼叫buildDataRange, 設定資料範圍");
			return new XlsParser(this);
		}


	}

}
