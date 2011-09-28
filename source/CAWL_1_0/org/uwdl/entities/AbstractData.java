package org.uwdl.entities;

public abstract class AbstractData implements IData {
	protected final String name;
	private final String value;
	
	protected AbstractData(String name, String value) {
		this.name = name;
		this.value = value;		
	}

	public String getValue() {
		return value;
	}

	public String getName() {
		return name;
	}
}
