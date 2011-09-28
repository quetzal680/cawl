package org.uwdl.parser.ast;

import org.sspl.parser.xml.XMLAttribute;
import org.sspl.parser.xml.XMLAttributeMap;
import org.sspl.sax.SaxVisitor;
import org.uwdl.parser.uWDLMissingRequiredAttributeException;
import org.uwdl.parser.uWDLVisitor;

public class UDeactivate extends UElement {
	//attributes
	private final String activator;
	private final String flow;
	
	public UDeactivate(XMLAttributeMap map) throws uWDLMissingRequiredAttributeException {
		super(Tag.Deactivate);
		// TODO Auto-generated constructor stub
		this.activator = map.optional("activator");
		this.flow = map.optional("flow");
	}
	
	public String getActivator() {
		return activator;
	}
	
	public String getFlow() {
		return flow;
	}
	
	public XMLAttribute[] buildAttributes() {
		return new XMLAttribute[] { new XMLAttribute("activator", activator), new XMLAttribute("flow", flow) };
	}
	
	public void accept(uWDLVisitor v) {
		// TODO Auto-generated method stub
		v.visit(this);
	}

	public void accept(SaxVisitor v) {
		v.visit(this);
	}
}
