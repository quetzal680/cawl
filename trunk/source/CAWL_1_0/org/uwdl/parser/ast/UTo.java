package org.uwdl.parser.ast;

import org.sspl.parser.xml.XMLAttribute;
import org.sspl.parser.xml.XMLAttributeMap;
import org.sspl.sax.SaxVisitor;
import org.uwdl.parser.uWDLMissingRequiredAttributeException;
import org.uwdl.parser.uWDLVisitor;

public class UTo extends UElement {
		//attributes
	private final String variable;
	private final String part;
	
	public UTo(XMLAttributeMap map) throws uWDLMissingRequiredAttributeException {
		super(Tag.To);
		// TODO Auto-generated constructor stub
		this.variable = map.require("variable");
		this.part = map.optional("part");
	}

	public String getVariable() {
		return variable;
	}
	
	public String getPart() {
		return part;
	}
	
	public XMLAttribute[] buildAttributes() {
		return new XMLAttribute[] { new XMLAttribute("variable", variable), new XMLAttribute("part", part) };
	}
	
	public void accept(uWDLVisitor v) {
		// TODO Auto-generated method stub
		v.visit(this);
	}

	public void accept(SaxVisitor v) {
		v.visit(this);
	}
}
