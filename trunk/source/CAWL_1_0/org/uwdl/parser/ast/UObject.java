package org.uwdl.parser.ast;

import org.uwdl.parser.uWDLMissingRequiredAttributeException;
import org.uwdl.parser.uWDLVisitor;

import org.sspl.parser.xml.XMLAttribute;
import org.sspl.parser.xml.XMLAttributeMap;
import org.sspl.sax.SaxVisitor;

public class UObject extends UElement {
	private final String type;
	
	public UObject( String type ) {
		super(Tag.Object);
		this.type = type;
	}
	
	public UObject(XMLAttributeMap map, String pcdata) throws uWDLMissingRequiredAttributeException {
		super(Tag.Object, pcdata);
		this.type = map.require("type");
	}
	
	public String getType() {
		return type;
	}
	
	public void setPcdata( String pcdata ) {
		super.setPcdata(pcdata);
	}
	
	@Override 
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
