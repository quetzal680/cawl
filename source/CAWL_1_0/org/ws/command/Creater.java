package org.ws.command;

import java.util.Iterator;
import java.util.Vector;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.client.ServiceClient;

import org.uwdl.mapper.ContextOntology;
import org.uwdl.mapper.ContextUpdate;
import org.uwdl.parser.ast.UFrom;
import org.uwdl.parser.ast.UInitialize;
import org.uwdl.parser.ast.UInvoke;
import org.uwdl.parser.ast.UNode;
import org.uwdl.parser.ast.UVariable;

public class Creater {
	private EndpointReference targetEPR;
	private String namespace;
	private String funcName;
	private String serviceProvider;
	private Vector variable = new Vector();
	private ContextOntology contextOntology = null;
	
	public Creater(UNode node, UInvoke descendant, ContextOntology contextOntology) {
		System.out.println("\n[CommandCreate]");
		this.contextOntology = contextOntology;
		convert(node, descendant);
	}

	public void invoke() {
		System.out.println("\nCommandCreate.invoke");
		
		OMFactory fac = OMAbstractFactory.getOMFactory();
		OMNamespace OMNS = fac.createOMNamespace(namespace, serviceProvider);			// create name space
		OMElement omElement = fac.createOMElement(funcName, OMNS);			// create function element	
		System.out.println(omElement.toString());
		
		try {
			Options options = new Options();
			options.setTo(targetEPR);
			        
			options.setTransportInProtocol(Constants.TRANSPORT_HTTP);
			
			ServiceClient sender = new ServiceClient();
			sender.setOptions(options);
			OMElement result = sender.sendReceive(omElement);
			
//			// 원래 리턴 값이 있을경우 처리해 주어야 하지만 현재 그럴 정신이 없다.
//			if( getFuncType().name().equals("sensorFunction") ) {
//				Vector data = new Vector();
//				
//		        Iterator child = result.getFirstElement().getChildElements();
//		        while( child.hasNext() ) {
//		        	OMElement sensorData = (OMElement)child.next();
//		            data.addElement(sensorData.getFirstElement().getText());
//		        }
//				
//		        InfoTable infoTable = InfoTable.getInstance();
////		        infoTable.updateData("OutputInfo", "light " + "is " + "30");
//		        infoTable.updateData("OutputInfo", "light " + "is " + data.elementAt(0).toString());
//			}
//			System.out.println(result.getFirstElement().getText());
		} catch (AxisFault e) {
//			e.printStackTrace();
		}
		
		if(funcName.equals("go")) {
			contextOntology.updateIndividual("DeliveryLego", "locatedIn", "Duyou");
		} else if( funcName.equals("pickUp") ) {
			contextOntology.updateIndividual("DeliveryLego", "hasCan", "Duyou");
		}
	}

	
	private void convert(UNode node, UInvoke descendant) {
		int i, j;
		
		// funcName
		targetEPR = new EndpointReference(descendant.getTargetERP());
		namespace = descendant.getNamespace();
		funcName = descendant.getOperation();
		serviceProvider = descendant.getServiceProvider();
			
		// UVariable
		UVariable uVariables[] = node.getVariable();
		System.out.println("uVariables");
		for (i = 0; i < uVariables.length; i++) {
			// descendant.input == uVariables
			if ((descendant.getInput() != null) && uVariables[i].getName().equals(descendant.getInput().substring(1))) {
				System.out.println("name : " + uVariables[i].getName());
				UInitialize uInitializes[] = uVariables[i].getInitializes();

				for ( j = 0; j < uInitializes.length; j++) {
					System.out.print("  init part : " + uInitializes[j].getPart() + ", ");

					// UFrom
					UFrom uFrom = uInitializes[j].getFrom();
					System.out.println(uFrom.getExpression());

					// set variables
					variable.add(uFrom.getExpression());
				}
			}
		}
	}
	
	public static void main(String args[]) {
//		EndpointReference targetEPR =  new EndpointReference("http://localhost:8080/Sensor/services/Delivery/");
//		String namespace = "http://Services";
//		String funcName = "go";
//		String serviceProvider = "ssCom";
////		Vector variable = new Vector();
//		
//		OMFactory fac = OMAbstractFactory.getOMFactory();
//		OMNamespace OMNS = fac.createOMNamespace(namespace, serviceProvider);			// create name space
//		OMElement omElement = fac.createOMElement(funcName, OMNS);			// create function element	
//		System.out.println(omElement.toString());
//		
//		try {
//			Options options = new Options();
//			options.setTo(targetEPR);
//			        
//			options.setTransportInProtocol(Constants.TRANSPORT_HTTP);
//			
//			ServiceClient sender = new ServiceClient();
//			sender.setOptions(options);
//			OMElement result = sender.sendReceive(omElement);
//			
////			// 원래 리턴 값이 있을경우 처리해 주어야 하지만 현재 그럴 정신이 없다.
////			if( getFuncType().name().equals("sensorFunction") ) {
////				Vector data = new Vector();
////				
////		        Iterator child = result.getFirstElement().getChildElements();
////		        while( child.hasNext() ) {
////		        	OMElement sensorData = (OMElement)child.next();
////		            data.addElement(sensorData.getFirstElement().getText());
////		        }
////				
////		        InfoTable infoTable = InfoTable.getInstance();
//////		        infoTable.updateData("OutputInfo", "light " + "is " + "30");
////		        infoTable.updateData("OutputInfo", "light " + "is " + data.elementAt(0).toString());
////			}
//			if(funcName.equals("go")) {
//				contextOntology.updateIndividual("DeliveryLego", "locatedIn", "Duyou");
//			} else if( funcName.equals("pickUp") ) {
//				contextOntology.updateIndividual("DeliveryLego", "hasCan", "Duyou");
//			}
////			System.out.println(result.getFirstElement().getText());
//		} catch (AxisFault e) {
////			e.printStackTrace();
//		}
	}
	
}











// XML -> OMElement 로 변환
// ByteArrayInputStream bais = new
// ByteArrayInputStream(result.toString().getBytes());
// XMLStreamReader reader =
// XMLInputFactory.newInstance().createXMLStreamReader(bais);
// StAXOMBuilder builder = new StAXOMBuilder(reader);
// OMElement retValue = builder.getDocumentElement();
//
// System.out.println("retValue : " + retValue);