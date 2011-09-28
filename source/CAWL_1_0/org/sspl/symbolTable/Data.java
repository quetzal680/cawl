package org.sspl.symbolTable;

public class Data implements IData {
	private String type;
	private String name;
	private String value;
	//private int level;  level값이 필요하다면 추가 현재는 필요여부를 잘 모르겠음..
	
	public Data(String type, String name) {
		this(type, name, null);
	}

	public Data(String type, String name, String value) {
		this.type = type;
		this.name = name;
		this.value = value;
	}
	
	public String getType() {
		return type;
	}
	
	public String getName() {
		return name;
	}
	
	public String getValue() {
		return value;
	}
	
	// 반드시 초기화 하라는 법이 없기에...setter 만들었음
	public void setValue(String value) {
		this.value = value;
	}
}
