package org.uwdl.parser.ast;

import java.util.ArrayList;

import org.sspl.parser.xml.XMLAttribute;
import org.sspl.parser.xml.XMLAttributeMap;
import org.sspl.sax.SaxVisitor;
import org.uwdl.parser.uWDLMissingRequiredAttributeException;
import org.uwdl.parser.uWDLVisitor;

public class UMessage extends UElement {
	//attribute
	private final String name;
	
	//element
	private final ArrayList<UPart> parts = new ArrayList<UPart>();
	
	public UMessage( String name ) {
		super( Tag.Message );
		
		this.name = name;
	}
	
	public UMessage(XMLAttributeMap map) throws uWDLMissingRequiredAttributeException {
		super(Tag.Message);
		name = map.require("name");
	}
	
	public String getName() {
		return name;
	}
	
	public UPart[] getParts() {
		return parts.toArray(new UPart[0]);
	}
	
	public void addPart(UPart part) {
		parts.add(part);
	}
	
	public XMLAttribute[] buildAttributes() {
		return new XMLAttribute[] { new XMLAttribute("name", name) };
	}
	
	public void accept(uWDLVisitor v) {
		// TODO Auto-generated method stub
		v.visit(this);
	}

	public void accept(SaxVisitor v) {
		v.visit(this);
	}
}
