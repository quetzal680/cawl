package org.uwdl.parser.ast;

import org.sspl.parser.xml.XMLAttribute;
import org.sspl.parser.xml.XMLAttributeMap;
import org.sspl.sax.SaxVisitor;
import org.uwdl.parser.uWDLMissingRequiredAttributeException;
import org.uwdl.parser.uWDLVisitor;

public class UPart extends UElement {
	//attributes
	private final String name;
	private final String element;
	private final String type;
	
	public UPart(String name, String element, String type) {
		super(Tag.Part);
		
		this.name = name;
		this.element = element;
		this.type = type;
	}
	
	public UPart(XMLAttributeMap map) throws uWDLMissingRequiredAttributeException {
		super(Tag.Part);
		this.name = map.require("name");
		this.element = map.optional("element");
		this.type = map.optional("type");
	}
	
	public String getName() {
		return name;
	}
	
	public String getElement() {
		return element;
	}
	
	public String getType() {
		return type;
	}
	
	public XMLAttribute[] buildAttributes() {
		return new XMLAttribute[] { new XMLAttribute("name", name), new XMLAttribute("element", element), new XMLAttribute("type", type) };
	}
	
	public void accept(uWDLVisitor v) {
		// TODO Auto-generated method stub
		v.visit(this);
	}

	public void accept(SaxVisitor v) {
		v.visit(this);
	}
}