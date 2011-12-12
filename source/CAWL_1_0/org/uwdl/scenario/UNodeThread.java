package org.uwdl.scenario;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;

import org.uwdl.entities.ScenarioEntitySet;
import org.uwdl.mapper.ContextComparator;
import org.uwdl.mapper.ContextOntology;
import org.uwdl.mapper.uWDLMapper;
import org.uwdl.parser.ast.INodeDescendant;
import org.uwdl.parser.ast.UActivate;
import org.uwdl.parser.ast.UFlow;
import org.uwdl.parser.ast.UInvoke;
import org.uwdl.parser.ast.UNSync;
import org.uwdl.parser.ast.UNode;
import org.uwdl.parser.ast.USink;
import org.uwdl.parser.ast.USynchronize;
import org.uwdl.scenario.exception.LinkAttributeException;
import org.ws.command.Creater;

public class UNodeThread extends Thread{
	private final UNode node;
	private Hashtable<String, UNodeThread> threads = null;
	private Hashtable<String, Vector<String>> links = null;
	private ContextOntology contextOntology = null;
	
	public UNodeThread(UNode node, ContextOntology contextOntology) {
		this.node = node;
		this.contextOntology = contextOntology;
	}
	
	public void execute(Hashtable<String, UNodeThread> threads, Hashtable<String, Vector<String>> links) {
		this.threads = threads;
		this.links = links;
		this.start();
	}
	
	public void run() {
		System.out.println("\n[NODE] : <" + node.getName() + "> State start");
		
		// sync로 노드를 수행 시킬지 결정
		while( isSynchronize() ) {}
		System.out.println("\n[NODE] : <" + node.getName() + "> State execute ..ok");
		
		// constraint 조건 판단
    	
//    	if(sensorData.getEntities(node.getName()) == null) {
//    		System.out.println("\n\tERROR>Not sensorData.getEntities : " + node.getName());
//    	}
    	
//  		System.out.println(node.getName() + " constraint condition check : wait");
    	System.out.println(node.getName() + " constraint condition check : OK");
    	
		while ( !contextOntology.compare(node.getCondition()) ) {
//			System.out.println(node.getName() + " constraint condition check");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("--------------------- invoke ------------------------");
		
		// invoke 수행
   		for(INodeDescendant descendant : node.getINodeDescendant()) {
			if(descendant instanceof UInvoke) {
				UInvoke invoke = (UInvoke) descendant;
				if(invoke.getServiceProvider().equals("uwdl")) {
					System.out.println("cawl subflow condition OK");
					UFlow flow = ScenarioTest.flowSet.get(invoke.getOperation());
					ScenarioFlow sub = new ScenarioFlow(flow, contextOntology);
					sub.start();
				}
				else {
					System.out.println(((UInvoke)descendant));
					Creater creater = new Creater(node, (UInvoke)descendant, contextOntology);
					creater.invoke();
				}
			}
   		}
   		
		System.out.println("\n[NODE] : <" + node.getName()  + "> State End");
		
		Vector<String> toStrings = links.get(node.getName());
		for( String toString : toStrings ) {
			UNodeThread nodeThread = threads.get(toString);
			if( nodeThread!=null ) {
				synchronized (nodeThread) {
					if( nodeThread.getState()==Thread.State.NEW ) {
						System.out.println("[NODE] : <" + nodeThread.getNode().getName() + "> <" +nodeThread.getName()+">");
						nodeThread.execute(threads, links);
					}
				}
			}
		}
	}

	public UNode getNode() {
		return node;
	}
	
	public boolean isSynchronize() {
		if( node.getSynchronize()==null ) {
			return false;
		} else {
			if( !isNSync() )
				return true;
			else
				return false;
		}
	}
	
	public boolean isNSync() {
		USynchronize uSynchronize = node.getSynchronize();

		// 모든 nSync thread가 종료되었는지 판별
		for (UNSync nSync : uSynchronize.getnSyncs()) {
			UNodeThread nodeThread = threads.get(nSync.getName());
			// 종료되지 않은 노드 존재
			if (nodeThread.getState() != Thread.State.TERMINATED) {
//				System.out.println("[NODE] <" + node.getName() + "> <" +nodeThread.getNode().getName() + "> State not terminated");
				return false;
			}
		}
		
		// 모두 종료
		return true;
	}
}
