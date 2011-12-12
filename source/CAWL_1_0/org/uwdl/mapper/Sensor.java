package org.uwdl.mapper;

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


public class Sensor extends Thread {
	private static EndpointReference targetEPR = new EndpointReference("http://localhost:8080/Sensor/services/Sensor/");
	private ContextOntology contextOntology = null;
	
	public Sensor( ContextOntology contextOntology ) {
		this.contextOntology = contextOntology;
	}
	
    public static OMElement getSensorDataOMElement() {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://Services", "wsdl");
        OMElement method = fac.createOMElement("getSensorData", omNs);
//        OMElement value = fac.createOMElement("legoName", omNs);
//        value.addChild(fac.createOMText(value, legoName));
//        method.addChild(value);
        
//        System.out.println("client method>> : " + method);
        return method;
    }

    
    @Override
    public void run() {
		while(true) {
			this.getSensorEntities();
//			try {
//				Thread.sleep(500);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		}
    }
//	public Entities getSensorEntities(EntitiesTag entitiesTag, String legoName) {
    public void getSensorEntities() {
//		System.out.println("getSensorData start...");

		Vector data = new Vector();

		try {
            OMElement getSensorDataElement = getSensorDataOMElement();
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
            	if(sensorData.getFirstElement()!=null) {
                    data.addElement(sensorData.getFirstElement().getText());            		
            	}
            }
                        
//for( int i=0; i<data.size(); i++ ) {
//	System.out.println("sensorData ("+i+"): " + data.elementAt(i).toString() );
//}

            if( Integer.parseInt(data.elementAt(0).toString())<45 ) {
            	contextOntology.updateIndividual("Warehouse", "hasBrightness", "Dark");
//                System.out.println("Warehouse hasBrightness Dark");
            } else {
            	contextOntology.updateIndividual("Warehouse", "hasBrightness", "Bright");
//            	System.out.println("Warehouse hasBrightness Bright");
            }

            if( Integer.parseInt(data.elementAt(1).toString())==1 ) {
            	contextOntology.updateIndividual("DeliveryLego", "locatedIn", "Road");
//	        	System.out.println("Delivery locatedIn Road");
	        }

	        if( Integer.parseInt(data.elementAt(2).toString())==1 ) {
	        	contextOntology.updateIndividual("DeliveryLego", "locatedIn", "Warehouse");
//	        	System.out.println("Delivery locatedIn Warehouse");
	        }

	        // distance
	        int distance = Integer.parseInt(data.elementAt(3).toString()); 
	        if( distance<20 || distance>25) {
	        	contextOntology.updateIndividual("DeliveryLego", "locatedIn", "WarehouseMainDoor");
//	        	System.out.println("Delivery locatedIn WarehouseMainDoor <true>");
	        }
//	        else {
//	        	contextOntology.deleteIndividual("DeliveryLego", "locatedIn", "WarehouseMainDoor");
////	        	System.out.println("Delivery locatedIn WarehouseMainDoor <false>");
//	        }
        } catch (Exception e) {
            System.out.println(e.toString());
        	System.out.println("sensor server is turned off");
        }

//        return sensorInfo;
	}
	
//	public static void main(String args[]) {
//		Sensor sensorData = new Sensor();
//		while(true) {
//			sensorData.getSensorEntities();
//		}
//	}
}














// XML -> OMElement 로 변환 
//ByteArrayInputStream bais = new ByteArrayInputStream(result.toString().getBytes());
//XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(bais);
//StAXOMBuilder builder = new StAXOMBuilder(reader);
//OMElement retValue = builder.getDocumentElement();
//
//System.out.println("retValue : " + retValue);