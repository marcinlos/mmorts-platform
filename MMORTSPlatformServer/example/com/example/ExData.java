package com.example;

public class ExData {
	private int intField;
	private Boolean boolObject;
	
	public ExData() {
		// to instantiate for gson
	}
	
	public ExData(int intField, Boolean boolObject) {
		this.intField = intField;
		this.boolObject = boolObject;
	}

	public int getIntField() {
		return intField;
	}

	public Boolean getBoolObject() {
		return boolObject;
	}
}
