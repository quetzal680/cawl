package org.sspl.symbolTable;

import java.util.Hashtable;
import java.util.Vector;

public class MTable {
	private Hashtable<String, Vector<String>> table;
	private Vector<String> names;
	
	public MTable() {
		table = new Hashtable<String, Vector<String>>();
	}
	
	public void addName(String name) {
		if(names == null)
			names = new Vector<String>();
		
		names.add(name);
	}
	
	public void addMessageSet(String name) {
		table.put(name, names);
		names = null;
	}
	
	public boolean exist(String name) {
		return table.containsKey(name);
	}
	
	public String[] getNames(String name) {
		Vector<String> nameList = table.get(name);
		if(nameList != null)
			return (String[])nameList.toArray(new String[0]);
		
		return null;
	}

}
