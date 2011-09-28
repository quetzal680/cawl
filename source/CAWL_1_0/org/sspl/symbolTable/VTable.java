package org.sspl.symbolTable;

import java.util.Hashtable;

public class VTable {
	private Hashtable<String, IData> table = null;
	protected VTable prev = null;
	
	public VTable(VTable t) {
		table = new Hashtable<String, IData>();
		prev = t;
	}
	
	//word -> type_name:variable_name
	public void put(String word, IData data) {
		table.put(word, data);
		System.out.println("word = " + word + "  Data = " + data.toString());
	}
	
	public Data getData(String word) {
		Data found = null;
		for(VTable t = this; t != null; t = t.prev) {
			if(t.table.get(word) instanceof Data) {
				found = (Data) t.table.get(word);
			}
			else {
				System.out.println("getData word = " + word);
				String[] key = word.split("/");
				System.out.println("getData key[0] = " + key[0]);
				Message message = (Message) t.table.get(key[0]);
				if(message != null) {
					showTable();
					found = (Data) message.getData(key[1]);
				}
			}
			
			if(found != null)
				return found;
		}
		return null;
	}
	
	public void showTable() {
		System.out.println("--------------------------------------------");
		for(VTable t = this; t != null; t = t.prev) {
			for(int i=0; i<t.table.size(); i++) {
				System.out.print(t.table.keySet().toArray()[i] + " - ");
				System.out.println(t.table.values().toArray()[i]);
			}
		}
	}
	
	/*	public Data getData(String word) {
	for(VTable t = this; t != null; t = t.prev) {
		Data found = t.table.get(word);
		if(found != null)
			return found;
	}
	return null;
}
*/

}
