package org.ws.command;

import java.util.Calendar;
import java.util.Iterator;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.addressing.EndpointReference;

public class Test {
	private static EndpointReference targetEPR = new EndpointReference("http://localhost:8080/0_Lego/services/Control");
	private static String namespace = "http://Services";
	
	public static void main(String args[]) {
		OMFactory fac = OMAbstractFactory.getOMFactory();
		
		// OMElement value = fac.createOMElement("sensorNumber", omNs);
		// value.addChild(fac.createOMText(value, Integer.toString(sensorNumber)));
		
		OMElement robotType = fac.createOMElement(new QName("robotType"));
		OMElement robotMode = fac.createOMElement(new QName("robotMode"));
		
		OMElement pcType = fac.createOMElement(new QName("pcType"));
		
		OMElement X2 = fac.createOMElement(new QName("x"));
		OMElement Y2 = fac.createOMElement(new QName("y"));
		X2.setText("");
		Y2.setText("");
		
		OMElement X3 = fac.createOMElement(new QName("x"));
		OMElement Y3 = fac.createOMElement(new QName("y"));
		OMElement Z3 = fac.createOMElement(new QName("z"));
		X3.setText("");
		Y3.setText("");
		Z3.setText("");
		
		OMElement target2D = fac.createOMElement(new QName("target2D"));
		target2D.addChild(X2);
		target2D.addChild(Y2);
		
		OMElement target3D = fac.createOMElement(new QName("target3D"));
		target3D.addChild(X3);
		target3D.addChild(Y3);
		target3D.addChild(Z3);
		
		OMElement filename = fac.createOMElement(new QName("filename"));
		
		
				
		
		
		robotType.setText("TeaLego");
		System.out.println("\n"+robotType.toString());
		
		robotType.setText("BasicLego");
		System.out.println("\n"+robotType.toString());
		
		pcType.setText("HostPC");
		System.out.println("\n"+pcType.toString());
		
		pcType.setText("ClientPC");
		System.out.println("\n"+pcType.toString());
		
		robotMode.setText("Arm");
		System.out.println("\n"+robotMode.toString());

		
		target2D.getFirstChildWithName(new QName("x")).setText("10");
		target2D.getFirstChildWithName(new QName("y")).setText("20");
		System.out.println("\n"+target2D.toString());
		
		target3D.getFirstChildWithName(new QName("x")).setText("30");
		target3D.getFirstChildWithName(new QName("y")).setText("40");
		target3D.getFirstChildWithName(new QName("z")).setText("50");
		System.out.println("\n"+target3D.toString());
		
		filename.setText("back.wma");
		System.out.println("\n"+filename.toString());
		
		
		
		
		OMNamespace OMNS = fac.createOMNamespace(namespace, "lego");
		OMElement functionElement = fac.createOMElement("MoveLegoService", OMNS);
		
		robotType.setNamespace(OMNS);
		functionElement.addChild(robotType);
		System.out.println("\n"+functionElement.toString());
		
		
		System.out.println("\n" + OMAbstractFactory.getOMFactory().createOMElement(new QName("asdf")) );
		
		
		Calendar right = Calendar.getInstance();
		
		int h = right.get(Calendar.HOUR_OF_DAY);
		int m = right.get(Calendar.MINUTE);
		
		String time = Integer.toString(h) + Integer.toString(m);
		
		System.out.println("time int : " + time);
		System.out.println("h : " + right.get(Calendar.HOUR_OF_DAY));
		System.out.println("m : " + right.get(Calendar.MINUTE));

//		
//		setData("None", "null");
//		
//		setData("OutputInfo", "");
		
	}
}
