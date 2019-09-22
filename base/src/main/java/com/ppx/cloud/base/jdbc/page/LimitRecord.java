package com.ppx.cloud.base.jdbc.page;

public class LimitRecord {

	private String colName;

	private Object colValue;
	
	public static LimitRecord newInstance(String colName, Object colValue) {
		LimitRecord limitRecord = new LimitRecord();
		limitRecord.setColName(colName);
		limitRecord.setColValue(colValue);
		return limitRecord;
	}

	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public Object getColValue() {
		return colValue;
	}

	public void setColValue(Object colValue) {
		this.colValue = colValue;
	}

}
