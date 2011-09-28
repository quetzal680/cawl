package org.sspl.symbolTable;

public class Data implements IData {
	private String type;
	private String name;
	private String value;
	//private int level;  level���� �ʿ��ϴٸ� �߰� ����� �ʿ俩�θ� �� �𸣰���..
	
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
	
	// �ݵ�� �ʱ�ȭ �϶�� ���� ���⿡...setter �������
	public void setValue(String value) {
		this.value = value;
	}
}
