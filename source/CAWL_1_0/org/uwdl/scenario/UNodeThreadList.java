package org.uwdl.scenario;

import java.util.ArrayList;

import org.uwdl.parser.ast.UNSync;
import org.uwdl.parser.ast.USynchronize;

public class UNodeThreadList {
	private String from;
	private ArrayList<UNodeThread> nodeThreads;
	
	UNodeThreadList() {
		this.nodeThreads = new ArrayList<UNodeThread>();
	}
	
	UNodeThreadList(String from) {
		this.from = from;
		this.nodeThreads = new ArrayList<UNodeThread>();
	}

	public String getFrom() {
		return from;
	}

	public void add(UNodeThread nodeThread) {
		nodeThreads.add(nodeThread);
	}
	
	public UNodeThread get(int index) {
		return nodeThreads.get(index);
	}
	
	public int size() {
		return nodeThreads.size();
	}
}
