package com.syscom.xls_tool.sample.vo;

public class SampleVo {
	private String table_name;         // Table Name
	private String cache_name;         // Table Name
	
	private int rowIndex = 0;
	private String pkeyIndex;           
	
	private String db_column_name; 
	private String db_column_type;           
	private String safecache_column_name;
	private String safecache_column_type;
	
	public String getDb_column_name() {
		return db_column_name;
	}
	public void setDb_column_name(String db_column_name) {
		this.db_column_name = db_column_name;
	}
	public String getDb_column_type() {
		return db_column_type;
	}
	public void setDb_column_type(String db_column_type) {
		this.db_column_type = db_column_type;
	}
	public String getSafecache_column_name() {
		return safecache_column_name;
	}
	public void setSafecache_column_name(String safecache_column_name) {
		this.safecache_column_name = safecache_column_name;
	}
	public String getSafecache_column_type() {
		return safecache_column_type;
	}
	public void setSafecache_column_type(String safecache_column_type) {
		this.safecache_column_type = safecache_column_type;
	}
	public int getRowIndex() {
		return rowIndex;
	}
	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}
	public String getPkeyIndex() {
		return pkeyIndex;
	}
	public void setPkeyIndex(String pkeyIndex) {
		this.pkeyIndex = pkeyIndex;
	}
	public String getTable_name() {
		return table_name;
	}
	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}
	public String getCache_name() {
		return cache_name;
	}
	public void setCache_name(String cache_name) {
		this.cache_name = cache_name;
	}
	
	
	@Override
	public String toString() {
		return "SafecacheSpecVo [table_name=" + table_name + ", cache_name=" + cache_name + ", rowIndex=" + rowIndex + ", pkeyIndex=" + pkeyIndex + ", db_column_name=" + db_column_name + ", db_column_type=" + db_column_type + ", safecache_column_name=" + safecache_column_name
				+ ", safecache_column_type=" + safecache_column_type + "]";
	}
	
	
	
}
	
