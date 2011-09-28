package org.sspl.symbolTable;

import java.util.Hashtable;

public class Message extends Hashtable<String, Data> implements IData{

	private static final long serialVersionUID = 1L;

	public Message(String[] names) {
		for(String name : names) {
			setMessageVar(name);
		}
	}
	
	private void setMessageVar(String name) {
		// name ==> type:variable_name
		String[] word = name.split("/");
		this.put(word[1], new Data(word[0], word[1]));
	}
	
	public Data getData(String name) {
		Data data = this.get(name);
		
		if(data != null)
			return data;
		
		return null;
	}
	
	//필요할까??? 지금은 그다지 필요없는듯한데... getData().setValue() 사용하면 되는데..
	public String getValue(String name) {
		Data found = (Data) this.getData(name);
		
		if(found != null)
			return found.getValue();
		
		return null;
	}
	
	public void setValue(String name, String value) {
		Data found = (Data) this.getData(name);
		
		if(found != null)
			found.setValue(value);
	}
	
/*	
	public Data find(String name) {
		String[] key = name.split("/");
		Data found = (Data) this.getMessage(key[0]).get(key[1]);
		if(found != null)
			return found;
		return null;
	}
*/
}