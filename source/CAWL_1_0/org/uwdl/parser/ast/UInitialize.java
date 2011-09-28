package org.uwdl.parser.ast;

import org.sspl.parser.xml.XMLAttribute;
import org.sspl.parser.xml.XMLAttributeMap;
import org.sspl.sax.SaxVisitor;
import org.uwdl.parser.uWDLMissingRequiredAttributeException;
import org.uwdl.parser.uWDLVisitor;

public class UInitialize extends UElement {
	//attributes
	private final String part;
	
	//elements
	private UFrom from;
	
	public UInitialize(String part) {
		super( Tag.Initialize );
		
		this.part = part;
	}
	
	public UInitialize(XMLAttributeMap map, UFrom from) throws uWDLMissingRequiredAttributeException {
		super(Tag.Initialize);
		// TODO Auto-generated constructor stub
		this.part = map.optional("part");
		this.from = from;
	}

	public String getPart() {
		return part;
	}
	
	public void setFrom(UFrom from) {
		this.from = from;
	}

	public UFrom getFrom() {
		return this.from;
	}
	
	public XMLAttribute[] buildAttributes() {
		return new XMLAttribute[] { new XMLAttribute("part", part) };
	}
	
	public void accept(uWDLVisitor v) {
		// TODO Auto-generated method stub
		v.visit(this);
	}

	public void accept(SaxVisitor v) {
		v.visit(this);
	}
}
