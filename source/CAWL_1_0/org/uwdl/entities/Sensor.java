package org.uwdl.entities;

import java.util.Iterator;
import java.util.Vector;

import org.uwdl.entities.IEntities.EntitiesTag;
import org.ws.command.InfoTable;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.Constants;
import org.apache.axis2.client.ServiceClient;


public class Sensor {
	private static EndpointReference targetEPR = new EndpointReference("http://203.253.23.84:8080/Axis2/services/LegoControl");

    public static OMElement getSensorDataOMElement(String legoName) {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://Services", "lego");
        OMElement method = fac.createOMElement("getSensorData", omNs);
        OMElement value = fac.createOMElement("legoName", omNs);
        value.addChild(fac.createOMText(value, legoName));
        method.addChild(value);
        
        System.out.println("clinet method>> : " + method);
        return method;
    }

	public Entities getSensorEntities(EntitiesTag entitiesTag, String legoName) {
		System.out.println("getSensorData start...");

		String brightness;
        String distance;
		Vector data = new Vector();

		// entities
        Entities sensorInfo = new Entities(entitiesTag);

		
		try {
            OMElement getSensorDataElement = getSensorDataOMElement(legoName);
            Options options = new Options();
            options.setTo(targetEPR);
            
            options.setTransportInProtocol(Constants.TRANSPORT_HTTP);

            ServiceClient sender = new ServiceClient();
            sender.setOptions(options);
            OMElement result = sender.sendReceive(getSensorDataElement);
            
//            System.out.println("result : " + result.toString());

            Iterator child = result.getFirstElement().getChildElements();
            while( child.hasNext() ) {
            	OMElement sensorData = (OMElement)child.next();
                data.addElement(sensorData.getFirstElement().getText());
            }
            
//          for( int i=0; i<data.size(); i++ ) {
//        		System.out.println("sensorData : " + data.elementAt(i).toString() );
//        	}

	        // brightness
//	        if( Integer.parseInt(data.elementAt(0).toString())<30 ) {
//	        	brightness = "dark";	
//	        }
//	        else {
//	        	brightness = "bright";	
//	        }
            brightness = data.elementAt(0).toString();
            System.out.println("light sensor : " + brightness + ", " + data.elementAt(0).toString());
	        
	        // distance
	        distance = data.elementAt(3).toString();
	        System.out.println("distance sensor : " + distance);
	        
	        sensorInfo.addEntity(new Entity(new ENoun("meetingRoomNum", "Room", "307"), 
											new EVerb("verb", "is"),
											new ENoun("brightness", "Brightness", brightness) ) );
	        sensorInfo.addEntity(new Entity(new ENoun("effective-Distance", "Distance", "effective-Distance"), 
	        								new EVerb("verb", "is"),
	        								new ENoun("R2U-Distance", "Distance", distance) ) );
	        
        } catch (Exception e) {
//            System.out.println(e.toString());
        	System.out.println("sensor server is turned off");
        }

        return sensorInfo;
	}
	
	public static void main(String args[]) {
		Sensor sensorData = new Sensor();
		//while(true) {
			sensorData.getSensorEntities(EntitiesTag.STOPLightInfo,"BasicLego");
//		}
	}
}














// XML -> OMElement 로 변환 
//ByteArrayInputStream bais = new ByteArrayInputStream(result.toString().getBytes());
//XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(bais);
//StAXOMBuilder builder = new StAXOMBuilder(reader);
//OMElement retValue = builder.getDocumentElement();
//
//System.out.println("retValue : " + retValue);