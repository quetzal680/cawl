package org.uwdl.parser.ast;

import org.sspl.parser.xml.XMLAttribute;
import org.sspl.parser.xml.XMLAttributeMap;
import org.sspl.sax.SaxVisitor;
import org.uwdl.parser.uWDLMissingRequiredAttributeException;
import org.uwdl.parser.uWDLVisitor;

public class UActivate extends UElement {
	//attributes
	private final String activator;
	private final String flow;
	private final String input;
	
	public UActivate(String activator, String flow, String input) {
		super(Tag.Activate);
		
		this.activator = activator;
		this.flow = flow;
		this.input = input;
	}
	
	public UActivate(XMLAttributeMap map) throws uWDLMissingRequiredAttributeException {
		super(Tag.Activate);
		// TODO Auto-generated constructor stub
		this.activator = map.optional("activator");
		this.flow = map.optional("flow");
		this.input = map.optional("input");
	}

	public String getActivator() {
		return activator;
	}
	
	public String getFlow() {
		return flow;
	}
	
	public String getInput() {
		return input;
	}
	
	public XMLAttribute[] buildAttributes() {
		return new XMLAttribute[] { new XMLAttribute("activator", activator), new XMLAttribute("flow", flow), new XMLAttribute("input", input) };
	}
	
	public void accept(uWDLVisitor v) {
		// TODO Auto-generated method stub
		v.visit(this);
	}
	
	public void accept(SaxVisitor v) {
		v.visit(this);
	}

}
