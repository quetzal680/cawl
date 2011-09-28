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

import org.uwdl.parser.ast.UFrom;
import org.uwdl.parser.ast.UInitialize;
import org.uwdl.parser.ast.UInvoke;
import org.uwdl.parser.ast.UNode;
import org.uwdl.parser.ast.UVariable;

public class Creater {
	private static EndpointReference targetEPR;
	private String namespace = "http://Services";
	private String funcName;
	private String serviceProvider;
	private Vector variable = new Vector();

	public Creater(UNode node, UInvoke descendant) {
		System.out.println("\n[CommandCreate]");
		convert(node, descendant);
	}

	public void invoke() {
		System.out.println("\nCommandCreate.invoke");
		OMElement omElement = null;
		
		switch( getFuncType() ) {
			case robotFunction :
				if( variable.elementAt(0).equals("R-307") ) {
					targetEPR = new EndpointReference("http://203.253.23.84:8080/Axis2/services/LegoControl");	
				} else if( variable.elementAt(0).equals("R-313") ) {
					targetEPR = new EndpointReference("http://203.253.23.56:8080/Axis2/services/LegoControl");
				}
				
				System.out.println("targetEPR " + targetEPR.toString());
				System.out.println("namespace : " + namespace);
				System.out.println("funcName : " + funcName);
				System.out.println("arguments");
				if( variable.size()!=0 ) {
					for( int j=0; j < variable.size(); j++) {
						System.out.print(variable.elementAt(j) + ", ");
					}
				}
				System.out.println();
				
				omElement = robotCommand();
				break;
				
			case pcFunction :
				if( funcName.equals("DownloadFile") ) {
					targetEPR = new EndpointReference("http://203.253.23.56:8080/Axis2/services/PCControl");	
				} else if( funcName.endsWith("musicService") ) {
					targetEPR = new EndpointReference("http://203.253.23.84:8080/Axis2/services/PCControl");
				}
				
				
				System.out.println("targetEPR " + targetEPR.toString());
				System.out.println("namespace : " + namespace);
				System.out.println("funcName : " + funcName);
				System.out.println("arguments");
				if( variable.size()!=0 ) {
					for( int j=0; j < variable.size(); j++) {
						System.out.print(variable.elementAt(j) + ", ");
					}
				}
				System.out.println();
				
				omElement = pcCommand();
				break;
				
			case sensorFunction :
				targetEPR = new EndpointReference("http://203.253.23.84:8080/Axis2/services/SensorControl");	

				System.out.println("targetEPR " + targetEPR.toString());
				System.out.println("namespace : " + namespace);
				System.out.println("funcName : " + funcName);
				System.out.println("arguments");
				if( variable.size()!=0 ) {
					for( int j=0; j < variable.size(); j++) {
						System.out.print(variable.elementAt(j) + ", ");
					}
				}
				System.out.println();
				
				omElement = sensorCommand();
				break;
			case notFunction :
				System.out.println("\t[not function]");
				return ;
		}

		
		try {
			Options options = new Options();
			options.setTo(targetEPR);
			        
			options.setTransportInProtocol(Constants.TRANSPORT_HTTP);
			
			ServiceClient sender = new ServiceClient();
			sender.setOptions(options);
			OMElement result = sender.sendReceive(omElement);
			
			// 원래 리턴 값이 있을경우 처리해 주어야 하지만 현재 그럴 정신이 없다.
			if( getFuncType().name().equals("sensorFunction") ) {
				Vector data = new Vector();
				
		        Iterator child = result.getFirstElement().getChildElements();
		        while( child.hasNext() ) {
		        	OMElement sensorData = (OMElement)child.next();
		            data.addElement(sensorData.getFirstElement().getText());
		        }
				
		        InfoTable infoTable = InfoTable.getInstance();
//		        infoTable.updateData("OutputInfo", "light " + "is " + "30");
		        infoTable.updateData("OutputInfo", "light " + "is " + data.elementAt(0).toString());
			}
			
			System.out.println(result.getFirstElement().getText());
		} catch (AxisFault e) {
			e.printStackTrace();
		}
	}

	public FuncType getFuncType() {
		FuncType funcType;
		InfoTable infoTable = InfoTable.getInstance();
		
		if( infoTable.getData(funcName).equals("pcFunction") ) {
			funcType = FuncType.pcFunction;
		} else if( infoTable.getData(funcName).equals("robotFunction") ) {
			funcType = FuncType.robotFunction;
		} else if( infoTable.getData(funcName).equals("sensorFunction") ) {
			funcType = FuncType.sensorFunction;
		} else {
			funcType = FuncType.notFunction; 
		}
		
		return funcType;
	}
	
	
	private void convert(UNode node, UInvoke descendant) {
		int i, j;
		
		// funcName
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

	
	private OMElement robotCommand() {
		InfoTable infoTable = InfoTable.getInstance();
		OMFactory fac = OMAbstractFactory.getOMFactory();
		
		// create name space
		OMNamespace OMNS = fac.createOMNamespace(namespace, serviceProvider);
		
		// create function element
		OMElement functionElement = fac.createOMElement(funcName, OMNS);

		// create child element
		OMElement robotType = fac.createOMElement("robotType", OMNS);
		OMElement robotMode = fac.createOMElement("robotMode", OMNS);
		OMElement robotCommand = fac.createOMElement("robotCommand", OMNS);
		OMElement robotArgument = fac.createOMElement("robotArgument", OMNS);

		if( variable.size() != 0 ) {
			// set text
			robotType.addChild(fac.createOMText(robotType, infoTable.getData((String)variable.elementAt(0))));
			robotMode.addChild(fac.createOMText(robotMode, infoTable.getData((String)variable.elementAt(1))));
			robotCommand.addChild(fac.createOMText(robotCommand, (String)variable.elementAt(2)));
			robotArgument.addChild(fac.createOMText(robotArgument, infoTable.getData((String)variable.elementAt(3))));
		
			// add child
			functionElement.addChild(robotType);
			functionElement.addChild(robotMode);
			functionElement.addChild(robotCommand);
			functionElement.addChild(robotArgument);
		}
		
		System.out.println(functionElement.toString());
		
		return functionElement;
	}
	
	private OMElement pcCommand() {
		InfoTable infoTable = InfoTable.getInstance();
		OMFactory fac = OMAbstractFactory.getOMFactory();
		
		// create name space
		OMNamespace OMNS = fac.createOMNamespace(namespace, serviceProvider);
		
		OMElement functionElement = null;
		// create function element
		if( funcName.equals("DownloadFile") ) {
			functionElement = fac.createOMElement("executeFile", OMNS);	
		} else if( funcName.endsWith("musicService") ) {
			functionElement = fac.createOMElement(funcName, OMNS);
		}
		
		
		// create child element
		OMElement pcCommand = fac.createOMElement("pcCommand", OMNS);
		OMElement filename = fac.createOMElement("filename", OMNS);
		OMElement targetAddress = fac.createOMElement("targetAddress", OMNS);

		if( variable.size() != 0 ) {
			// set text
			pcCommand.addChild(fac.createOMText(pcCommand, (String)variable.elementAt(1)));
			filename.addChild(fac.createOMText(filename, infoTable.getData((String)variable.elementAt(2))));
			targetAddress.addChild(fac.createOMText(targetAddress, infoTable.getData((String)variable.elementAt(0))));
		
			// add child
			functionElement.addChild(pcCommand);
			functionElement.addChild(filename);
			functionElement.addChild(targetAddress);
		}
		
		System.out.println(functionElement.toString());
		
		return functionElement;
	}
	
	private OMElement sensorCommand() {
		InfoTable infoTable = InfoTable.getInstance();
		OMFactory fac = OMAbstractFactory.getOMFactory();
		
		// create name space
		OMNamespace OMNS = fac.createOMNamespace(namespace, serviceProvider);
		
		// create function element
		OMElement functionElement = fac.createOMElement(funcName, OMNS);
		
		// create child element
		OMElement robotType = fac.createOMElement("robotType", OMNS);
		
		if( variable.size() != 0 ) {
			// set text
			robotType.addChild(fac.createOMText(robotType, infoTable.getData((String)variable.elementAt(0))));

			// add child
			functionElement.addChild(robotType);
		}
		
		System.out.println(functionElement.toString());
		
		return functionElement;
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