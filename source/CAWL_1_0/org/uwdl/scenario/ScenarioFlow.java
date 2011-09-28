package org.uwdl.scenario;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Random;

import org.uwdl.entities.ScenarioEntitySet;
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
public class ScenarioFlow extends Thread{
	private UFlow flow = null;
//	private Hashtable<String, UNode> nodes = null;
	private ArrayList<UNode> nodes = null;
	private Hashtable<String, ThreadList> links = null;
	private ScenarioEntitySet sensorData = null;
	private uWDLMapper mapper = null;
	
	private ExecuteUNode executeUNode;
	private Hashtable<String, UNodeThread> threads = null;
	
	public ScenarioFlow(UFlow flow, uWDLMapper mapper) {
		System.out.println("\n[FLOW] : <" + flow.getName() + "> start");
		
		this.flow = flow;
		this.mapper = mapper;
		this.sensorData = ScenarioEntitySet.getInstance();
//		nodes = new Hashtable<String, UNode>();
//		links = new Hashtable<String, LinkedList<UNodeThread>>();
//		for( UNode node : flow.getNodes() ) {
//			nodes.put(node.getName(), node);
//		}
		nodes = flow.getNodes();
//		links = new ArrayList<UNodeList>();
		links = new Hashtable<String, ThreadList>();

//		executeUNode = new ExecuteUNode(flow.getNodes());
		threads = new Hashtable<String, UNodeThread>();
		
		initThread();
		link();
	}
	
//	private UNode getNode(String node) {
//		if(nodes.containsKey(node))
//			return nodes.get(node);
//		
//		return null;
//	}
	
	private UNode getNode(String node) {
		for(UNode uNode : nodes) { 
			if( uNode.getName().equals(node) ) {
				return uNode;
			}
		}
		return null;
	}
	
	private void initThread() {
		for(UNode uNode : nodes) {
			threads.put( uNode.getName(), new UNodeThread(uNode) );
		}
	}
	
	private UNodeThread getThread(String nodeName) {
		if(threads.containsKey(nodeName)) {
			return threads.get(nodeName);
		}
		return null;
	}
	
	private void link() {
		ULink[] alink = flow.getLinks();
		System.out.print("\nlink-> ");
		for(ULink link : alink) {
			String from = link.getFrom();
			String to = link.getTo();
			if( links.containsKey(from) ) {
				ThreadList threadList = links.get(from);
				threadList.add(new UNodeThread(getNode(to)));
			}
			else {
				ThreadList threadList = new ThreadList();
				threadList.add(new UNodeThread(getNode(to)));
				links.put(from, threadList);
			}
			
//			if(links.containsKey(link.getFrom())) {
//				UNode node = getNode(link.getTo());
//				if( node!=null ) {
//					UNodeThread nodeThread = new UNodeThread( node );
//					links.get(link.getFrom()).add( nodeThread );
//					System.out.print(link.getTo() + "@ ");	
//				}
//			}
//			else {
//				LinkedList<UNodeThread> list = new LinkedList<UNodeThread>();
//				UNode node = getNode(link.getTo());
//				if( node!=null ) {
//					UNodeThread nodeThread = new UNodeThread( node );
//					list.add( nodeThread );
//					links.put(link.getFrom(), list);
//					System.out.print(link.getTo() + "# ");
//				}
//			}
		}
		
		System.out.print("\n");
	}
	
	private void printNode(UNode node) throws InterruptedException, LinkAttributeException {
		Thread.sleep(new Random().nextInt(10000));
//		Thread.sleep(2000);
		System.out.print("\n[NODE] : <" + node.getName() + "> State execute ");
		System.out.print(".");
//    	Thread.sleep(1000);
    	System.out.print(".");
//    	Thread.sleep(1000);
    	System.out.println("OK");

    	//constraint �˻��� ���� 
    	
    	if(sensorData.getEntities(node.getName()) == null) {
    		System.out.println("\n\tERROR>Not sensorData.getEntities : " + node.getName());
    	}
    	
  		System.out.println(node.getName() + " constraint condition check : wait");
  		
    	while( !mapper.find(sensorData.getEntities(node.getName()), node.getCondition()) )
    	{
    		sleep(1000);
    	}
    	
    	System.out.println(node.getName() + " constraint condition check : OK");
    	
//    	if(mapper.find(sensorData.getEntities(node.getName()), node.getCondition())) {
//    		System.out.print(node.getName() + " constraint condition check :");
//    		System.out.print(".");
//        	Thread.sleep(1000);
//        	System.out.print(".");
//        	Thread.sleep(1000);
		
       		for(INodeDescendant descendant : node.getINodeDescendant()) {
    			if(descendant instanceof UInvoke) {
    				UInvoke invoke = (UInvoke) descendant;
    				if(invoke.getServiceProvider().equalsIgnoreCase("uwdl")) {
    					System.out.println("uwdl subflow condition OK");
    					UFlow flow = ScenarioTest.flowSet.get(invoke.getOperation());
    					ScenarioFlow sub = new ScenarioFlow(flow, mapper);
    					sub.execute();
    				}
    				else {
    					System.out.println(((UInvoke)descendant));
//    					Creater creater = new Creater(node, (UInvoke)descendant);
//    					creater.invoke();
    				}
    				descendant.getAttributes();
    			}
       		}
       		
//    	}
	}

	
	public ArrayList<String> getFromList(String to) {
		ULink[] alink = flow.getLinks();
		ArrayList<String> fromList = new ArrayList<String>();
		for(ULink link : alink) {
			if( link.getTo().equals(to) ) {
				fromList.add(link.getFrom());
			}
		}
		return fromList;
	}
	
	public ArrayList<String> getToList(String from) {
		ULink[] alink = flow.getLinks();
		ArrayList<String> toList = new ArrayList<String>();
		for(ULink link : alink) {
			if( link.getFrom().equals(from) ) {
				toList.add(link.getTo());
			}
		}
		return toList;
	}
	
	private void execute(ArrayList<String> toList) throws InterruptedException {
		for( String to : toList) {
			if( to.equals("sink") ) {
				return;
			}
		
			UNodeThread thread = getThread(to);
			UNodeThread fromThread = null;
			UNodeThread nSyncThread = null;
			
			for( String from : getFromList(to) ) {
				if( !from.equals("source") ) {
					System.out.println("from> join[" + from + "], state[" + getThread(from).getState().toString() +"], "+" waitThread : " + thread.getNode().getName());
					fromThread = getThread( from );
					if( fromThread.getState()==Thread.State.NEW ) {
						System.out.println("start thread[" + fromThread.getNode().getName() + "], state[" + fromThread.getState().toString() +"] from");
						
						ArrayList<String> fromList = new ArrayList<String>(); 
						fromList.add(from);	
						execute(fromList);	
					}
					fromThread.join();	
				}
			}

			if( thread.isSynchronize() ) {
				for( UNSync nSync : thread.getNSync() ) {
					System.out.println("sync> join[" + nSync.getName() + "], state[" + getThread(nSync.getName()).getState().toString() +"], "+" waitThread : " + thread.getNode().getName());
					String nSyncName = nSync.getName();
					nSyncThread = getThread( nSyncName );
					if( nSyncThread.getState()==Thread.State.NEW ) {
						System.out.println("start thread[" + nSync.getName() + "], state[" + getThread(nSync.getName()).getState().toString() +"] sync");

						ArrayList<String> nSyncList = new ArrayList<String>(); 
						nSyncList.add(nSyncName);	
						execute(nSyncList);	
					}
					nSyncThread.join();	
				}
			}
		
//			System.out.println("\n[NODE] : <" + thread.getNode().getName() + ">");
			if( thread.getState()==Thread.State.NEW) {
				thread.start();
			}
		}
		
		ArrayList<String> fromList = new ArrayList<String>(); 
		for( String to : toList) {
			if( to.equals("sink") ) {
				return;
			}
		
			for( String from : getToList(to) ) {
				fromList.add(from);	
			}
		}
		execute(fromList);	
	}
	
	//node
	public void execute() throws InterruptedException, LinkAttributeException {
		System.out.print("\n[SOURCE] state execute ");
		System.out.println("OK");
    	
//		String linkName = "source";
		
		UInput input = flow.getSource().getInput();
		System.out.println("input variable :  type(" + input.getType() + "), variable name(" + input.getName()+")");
//		Thread.sleep(1000);
		
		String from = "source";
		
		execute(getToList(from));
		
		
//		while(!linkName.equals("sink") && links.containsKey(linkName)) {
//			node = getNode(linkName);
//			printNode(node);
//			linkName = links.get(linkName).getFirst();
//		}
		
//		linkName = links.get(linkName).getFirst();
//		while(!linkName.equals("sink") && links.containsKey(linkName)) {
//			node = getNode(linkName);
//			printNode(node);
//			linkName = links.get(linkName).getFirst();
//		}

	}
}