package org.uwdl.parser.ast;

import org.sspl.parser.xml.XMLAttribute;
import org.sspl.parser.xml.XMLAttributeMap;
import org.sspl.sax.SaxVisitor;
import org.uwdl.parser.uWDLMissingRequiredAttributeException;
import org.uwdl.parser.uWDLVisitor;

public class UOutput extends UElement {
	//attributes
	private final String name;
	private final String type;
	
	public UOutput(String name, String type) {
		super(Tag.Output);
		this.name = name;
		this.type = type;
	}
	
	public UOutput(XMLAttributeMap map) throws uWDLMissingRequiredAttributeException {
		super(Tag.Output);
		this.name = map.require("name");
		this.type = map.require("type");
	}
	
	public String getName() {
		return name;
	}
	
	public String getType() {
		return type;
	}
	
	public XMLAttribute[] buildAttributes() {
		return new XMLAttribute[] { new XMLAttribute("name", name), new XMLAttribute("type", type) };
	}
	
	public void accept(uWDLVisitor v) {
		// TODO Auto-generated method stub
		v.visit(this);
	}

	public void accept(SaxVisitor v) {
		v.visit(this);
	}
}
