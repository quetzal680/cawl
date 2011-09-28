package org.uwdl.parser.ast;

import org.sspl.parser.xml.XMLAttribute;
import org.sspl.parser.xml.XMLAttributeMap;
import org.sspl.sax.SaxVisitor;
import org.uwdl.parser.uWDLMissingRequiredAttributeException;
import org.uwdl.parser.uWDLVisitor;

public class UPortType extends UElement {
	//attribute
	private final String name;
	private final String import_;
	//elements
	// ���� ����
	//private final UOperation Operation = new ArrayList<UOperation>();
	
	public UPortType(XMLAttributeMap map) throws uWDLMissingRequiredAttributeException {
		super(Tag.PortType);
		this.name = map.require("name");
		this.import_ = map.optional("import");
	}
	
	public String getName() {
		return name;
	}
	
	public String getImport() {
		return import_;
	}
	
	public XMLAttribute[] buildAttributes() {
		return new XMLAttribute[] { new XMLAttribute("name", name), new XMLAttribute("import", import_) }; 
	}
	
	public void accept(uWDLVisitor v) {
		v.visit(this);
	}
	
	public void accept(SaxVisitor v) {
		v.visit(this);
	}
}
