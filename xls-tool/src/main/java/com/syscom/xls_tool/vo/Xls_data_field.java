package com.syscom.xls_tool.vo;

/**
 * Vo 中值會變動的欄位
 * @author User
 *
 */
public class Xls_data_field {
	private String fieldName;
	private Xls_field_loc xls_field_loc;
	
	public Xls_data_field(String fieldName, Xls_field_loc xls_field_loc) {
		this.fieldName = fieldName;
		this.xls_field_loc = xls_field_loc;
	}
	
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public Xls_field_loc getXls_field_loc() {
		return xls_field_loc;
	}
	public void setXls_field_loc(Xls_field_loc xls_field_loc) {
		this.xls_field_loc = xls_field_loc;
	}
	
}
