package com.syscom.xls_tool.vo;

public class Xls_field_loc {
	private int row_Index;
	private int col_Index;
	
	public Xls_field_loc(int row_Index, int col_Index) {
		this.row_Index = row_Index;
		this.col_Index = col_Index;
	}
	
	public int getRow_Index() {
		return row_Index;
	}
	public void setRow_Index(int row_Index) {
		this.row_Index = row_Index;
	}
	
	public int getCol_Index() {
		return col_Index;
	}
	public void setCol_Index(int col_Index) {
		this.col_Index = col_Index;
	}

}
