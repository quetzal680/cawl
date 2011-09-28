/*
 * Created on 2005. 1. 17
 */
package org.uwdl.parser.ast;

import org.uwdl.parser.uWDLMissingRequiredAttributeException;
import org.uwdl.parser.uWDLVisitor;

import org.sspl.parser.xml.XMLAttribute;
import org.sspl.parser.xml.XMLAttributeMap;
import org.sspl.sax.SaxVisitor;

public class ULink extends UElement {
	//attributes
	private final String from;
	private final String to;
	
	public ULink(String from, String to) {
		super(Tag.Link);
		this.from = from;
		this.to = to;
	}
	
	public ULink(XMLAttributeMap attributes) throws uWDLMissingRequiredAttributeException {
		super(Tag.Link);
		this.from = attributes.require("from");
		this.to = attributes.require("to");
	}
	
	public String getFrom() {
		return from;
	}
	
	public String getTo() {
		return to;
	}
	
	public XMLAttribute[] buildAttributes() {
		return new XMLAttribute[] { new XMLAttribute("from", from), new XMLAttribute("to", to) };
	}
	
	public void accept(uWDLVisitor v) {
		v.visit(this);
	}
	
	public void accept(SaxVisitor v) {
		v.visit(this);
	}
}
