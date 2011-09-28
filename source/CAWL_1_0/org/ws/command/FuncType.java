package org.ws.command;

public enum FuncType {
	robotFunction("robotFunction"),
	pcFunction("pcFunction"),
	sensorFunction("sensorFunction"),
	notFunction("notFunction");
	
	private final String text;
	
	private FuncType(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
}
