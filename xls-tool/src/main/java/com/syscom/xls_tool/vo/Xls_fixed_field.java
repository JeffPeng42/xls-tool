package com.syscom.xls_tool.vo;

/**
 * Vo 中值固定不變的欄位
 * @author User
 *
 */
public class Xls_fixed_field {
	
	/** 該欄位名稱 */
	private String fieldName;
	
	/** 該欄位的位置資訊*/
	private Xls_field_loc xls_field_loc;
	
	/** 該欄位固定的值 */
	private String value;
	
	public Xls_fixed_field(String fieldName, Xls_field_loc xls_field_loc) {
		this.fieldName = fieldName;
		this.xls_field_loc = xls_field_loc;
	}
	
	public String getFieldName() {
		return fieldName;
	}
	public Xls_field_loc getXls_field_loc() {
		return xls_field_loc;
	}

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
