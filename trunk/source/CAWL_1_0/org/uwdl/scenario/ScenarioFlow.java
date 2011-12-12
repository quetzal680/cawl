package org.uwdl.scenario;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Random;
import java.util.Vector;

import org.uwdl.entities.ScenarioEntitySet;
import org.uwdl.mapper.ContextComparator;
import org.uwdl.mapper.ContextOntology;
import org.uwdl.mapper.uWDLMapper;
import org.uwdl.parser.ast.INodeDescendant;
import org.uwdl.parser.ast.UFlow;
import org.uwdl.parser.ast.UInput;
import org.uwdl.parser.ast.UInvoke;
import org.uwdl.parser.ast.ULink;
import org.uwdl.parser.ast.UNSync;
import org.uwdl.parser.ast.UNode;
import org.uwdl.parser.ast.UParallel;
import org.uwdl.parser.ast.USynchronize;
import org.uwdl.parser.ast.UTransition;
import org.uwdl.scenario.exception.LinkAttributeException;
import org.ws.command.Creater;

//������ ���� �ʿ�
public class ScenarioFlow extends Thread {
	private UFlow flow = null;
	private ArrayList<UNode> nodes = null;
	private ScenarioEntitySet sensorData = null;

	private Hashtable<String, UNodeThread> threads = null;
	
	private Hashtable<String, Vector<String>> tmpLinks = null;

	public ScenarioFlow(UFlow flow, ContextOntology contextOntology) {
		System.out.println("\n[FLOW] <" + flow.getName() + "> State create");
		
		this.flow = flow;
		this.sensorData = ScenarioEntitySet.getInstance();
		nodes = flow.getNodes();

		threads = new Hashtable<String, UNodeThread>();
		tmpLinks = new Hashtable<String, Vector<String>>();

		initThread(contextOntology);
		link();
	}
	
	private void initThread(ContextOntology contextOntology) {
		for (UNode uNode : nodes) {
			UNodeThread nodeThread = new UNodeThread(uNode, contextOntology);
			threads.put(uNode.getName(), nodeThread);
		}
	}
	
	private void link() {
		ULink[] alink = flow.getLinks();
		System.out.println("\tlink : ");
		
		for (ULink link : alink) {
			System.out.println("\t\t" + link.getFrom() + "->" + link.getTo());
			String from = link.getFrom();
			String to = link.getTo();
			
			if (tmpLinks.containsKey(from)) {
				tmpLinks.get(from).add(to);
			} else {
				Vector<String> vector = new Vector<String>();
				vector.add(to);
				tmpLinks.put(from, vector);
			}
		}
	}
	
	// flow execute
	public void run() {
		System.out.println("\n[FLOW] <" + flow.getName() + "> State start");
		// input data 출력
		printUinput();
		
		Vector<String> toStrings = tmpLinks.get("source");
		for( String toString : toStrings ) {
			UNodeThread nodeThread = threads.get(toString);
			nodeThread.execute(threads, tmpLinks);
		}
	}
	
	// print [source]->[input]
	private void printUinput() {
		UInput input = flow.getSource().getInput();
		if( input!=null ) {
			System.out.println("input variable :  type(" + input.getType() + "), variable name(" + input.getName() + ")");	
		}
	}
}