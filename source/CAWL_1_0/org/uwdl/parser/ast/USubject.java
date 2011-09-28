package org.uwdl.parser.ast;

import org.uwdl.parser.uWDLMissingRequiredAttributeException;
import org.uwdl.parser.uWDLVisitor;

import org.sspl.parser.xml.XMLAttribute;
import org.sspl.parser.xml.XMLAttributeMap;
import org.sspl.sax.SaxVisitor;

public class USubject extends UElement {
	private final String type;
	
	public USubject( String type ) {
		super( Tag.Subject );
		this.type = type;
	}
	
	public USubject(XMLAttributeMap map, String pcdata) throws uWDLMissingRequiredAttributeException {
		super(Tag.Subject, pcdata);
		this.type = map.require("type");
	}
	
	public void setPcdata( String pcdata ) {
		super.setPcdata(pcdata);
	}
	
	public String getType() {
		return type;
	}
	
	public XMLAttribute[] buildAttributes() {
		return new XMLAttribute[] { new XMLAttribute("type", type) };
	}
	
	public void accept(uWDLVisitor v) {
		v.visit(this);
	}
	
	public void accept(SaxVisitor v) {
		v.visit(this);
	}
}
