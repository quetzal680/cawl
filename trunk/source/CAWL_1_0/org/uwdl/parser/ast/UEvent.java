/*
 * Created on 2005. 1. 17
 */
package org.uwdl.parser.ast;

import org.uwdl.parser.uWDLMissingRequiredAttributeException;
import org.uwdl.parser.uWDLVisitor;

import org.sspl.parser.xml.XMLAttribute;
import org.sspl.parser.xml.XMLAttributeMap;
import org.sspl.sax.SaxVisitor;

public class UEvent extends UElement implements IConditionDescendant {
	//attributes
	private final String serviceProvider;
	private final String portType;
	private final String operation;
	private final String output;
	
	public UEvent(XMLAttributeMap map) throws uWDLMissingRequiredAttributeException {
		super(Tag.Event);
		this.serviceProvider = map.require("serviceProvider");
		this.portType = map.require("portType");
		this.operation = map.require("operation");
		this.output = map.require("output");
	}
	
	public String getServiceProvider() {
		return serviceProvider;
	}
	
	public String getPortType() {
		return portType;
	}
	
	public String getOperation() {
		return operation;
	}
	
	public String getOutput() {
		return output;
	}
	
	public XMLAttribute[] buildAttributes() {
		return new XMLAttribute[] { new XMLAttribute("serviceProvider", serviceProvider),
									new XMLAttribute("portType", portType),
									new XMLAttribute("operation", operation),
									new XMLAttribute("output", output),
		};
	}
	
	public void accept(uWDLVisitor v) {
		v.visit(this);
	}
	
	public void accept(SaxVisitor v) {
		v.visit(this);
	}
}
