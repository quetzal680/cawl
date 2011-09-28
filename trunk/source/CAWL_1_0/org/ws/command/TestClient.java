package org.ws.command;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.client.ServiceClient;


public class TestClient {
	private static EndpointReference targetEPR = new EndpointReference("http://203.253.23.56:8080/Lego/services/Control");
	private String namespace = "http://Services";
	private String funcName = "displayLegoService";
	private String serviceProvider = "ssCom";
	
	
	public static void main(String args[]) {
		TestClient client = new TestClient();
		client.invoke();
	}
	
	public void invoke() {
		System.out.println("CommandCreate.invoke");

		OMElement omElement = command();

		Options options = new Options();
		options.setTo(targetEPR);
		        
		options.setTransportInProtocol(Constants.TRANSPORT_HTTP);
		
		ServiceClient sender;
		try {
			sender = new ServiceClient();
			sender.setOptions(options);
			OMElement result = sender.sendReceive(omElement);
			System.out.println(result.getFirstElement().getText());
		} catch (AxisFault e) {
			e.printStackTrace();
		}
	}
	
	private OMElement command() {
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

		// move leg
//		robotType.addChild(fac.createOMText(robotType, "BasicLego"));
//		robotMode.addChild(fac.createOMText(robotType, "Leg"));
//		robotCommand.addChild(fac.createOMText(robotType, "Move"));
//		robotArgument.addChild(fac.createOMText(robotType, "50,20"));
	
		// move arm		
//		robotType.addChild(fac.createOMText(robotType, "TeaLego"));
//		robotMode.addChild(fac.createOMText(robotType, "Arm"));
//		robotCommand.addChild(fac.createOMText(robotType, "MoveUp"));
//		robotArgument.addChild(fac.createOMText(robotType, "55"));
		
		
		// speak music	
//		robotType.addChild(fac.createOMText(robotType, "TeaLego"));
//		robotMode.addChild(fac.createOMText(robotType, "Sound"));
//		robotCommand.addChild(fac.createOMText(robotType, "PlayWaveFile"));
//		robotArgument.addChild(fac.createOMText(robotType, "wd.wav"));
		

		// display music	
		robotType.addChild(fac.createOMText(robotType, "TeaLego"));
		robotMode.addChild(fac.createOMText(robotType, "LCD"));
		robotCommand.addChild(fac.createOMText(robotType, "Display"));
		robotArgument.addChild(fac.createOMText(robotType, "307 is bright"));

		
		// add child
		functionElement.addChild(robotType);
		functionElement.addChild(robotMode);
		functionElement.addChild(robotCommand);
		functionElement.addChild(robotArgument);

		System.out.println(functionElement.toString());
		
		return functionElement;
	}
}
