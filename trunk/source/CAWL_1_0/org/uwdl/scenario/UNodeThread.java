package org.uwdl.scenario;

import java.util.ArrayList;
import java.util.Random;

import org.uwdl.entities.ScenarioEntitySet;
import org.uwdl.mapper.uWDLMapper;
import org.uwdl.parser.ast.INodeDescendant;
import org.uwdl.parser.ast.UFlow;
import org.uwdl.parser.ast.UInvoke;
import org.uwdl.parser.ast.UNSync;
import org.uwdl.parser.ast.UNode;
import org.uwdl.scenario.exception.LinkAttributeException;

public class UNodeThread extends Thread{
	private final UNode node;
	private ScenarioEntitySet sensorData = null;
	private uWDLMapper mapper = null;
	
	public UNodeThread(UNode node) {
		this.node = node;
		this.sensorData = ScenarioEntitySet.getInstance();
		this.mapper = new uWDLMapper();
	}
	
	public void run() {
//		try {
//			Thread.sleep(new Random().nextInt(10000));
//		} catch (InterruptedException e1) {
//			e1.printStackTrace();
//		}
		System.out.println("\n[NODE] : <" + node.getName() + "> State execute ..ok");
		
    	//constraint �˻��� ���� 
    	
    	if(sensorData.getEntities(node.getName()) == null) {
    		System.out.println("\n\tERROR>Not sensorData.getEntities : " + node.getName());
    	}
    	
  		System.out.println(node.getName() + " constraint condition check : wait");
  		
    	while( !mapper.find(sensorData.getEntities(node.getName()), node.getCondition()) )
    	{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    	
    	System.out.println(node.getName() + " constraint condition check : OK");
    	
   		for(INodeDescendant descendant : node.getINodeDescendant()) {
			if(descendant instanceof UInvoke) {
				UInvoke invoke = (UInvoke) descendant;
				if(invoke.getServiceProvider().equalsIgnoreCase("uwdl")) {
					System.out.println("uwdl subflow condition OK");
					UFlow flow = ScenarioTest.flowSet.get(invoke.getOperation());
					ScenarioFlow sub = new ScenarioFlow(flow, mapper);
					try {
						sub.execute();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (LinkAttributeException e) {
						e.printStackTrace();
					}
				}
				else {
					System.out.println(((UInvoke)descendant));
//    					Creater creater = new Creater(node, (UInvoke)descendant);
//    					creater.invoke();
				}
				descendant.getAttributes();
			}
   		}
		System.out.println("\n[NODE] : <" + node.getName()  + "> End");

	}

	public UNode getNode() {
		return node;
	}
	
	public boolean isSynchronize() {
		if(node.getSynchronize()!=null)
			return true;
		else
			return false;
	}
	
	public ArrayList<UNSync> getNSync() {
		return node.getSynchronize().getnSyncs();
	}
}
