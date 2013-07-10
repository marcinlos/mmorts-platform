package com.app.ioapp.modules.example;

public class ExampleData {
	int exampleField;

	public ExampleData(int exampleField) {
		super();
		this.exampleField = exampleField;
	}

	public int getExampleField() {
		return exampleField;
	}
	
	@Override
	public String toString() {
		return String.valueOf(exampleField);
	}
	
}
