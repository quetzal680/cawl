package org.uwdl.scenario;

import java.util.ArrayList;

import org.uwdl.parser.ast.UNode;

public class ExecuteUNode {
	private ArrayList<UNode> nodes = new ArrayList<UNode>(); 
	
	ExecuteUNode(ArrayList<UNode> nodes) {
		this.nodes = nodes;
		initThreadList();
	}
	
	// <from> <to> -> thread list 생성
	private void initThreadList() {
		ArrayList<UNodeThread> threadList = new ArrayList<UNodeThread>();
		for( UNode node : nodes ) {
			threadList.add( new UNodeThread(node) );
			
		}
	}
}
