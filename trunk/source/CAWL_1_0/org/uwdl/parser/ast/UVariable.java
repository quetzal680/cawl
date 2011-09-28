package org.uwdl.parser.ast;

import java.util.ArrayList;

import org.sspl.parser.xml.XMLAttribute;
import org.sspl.parser.xml.XMLAttributeMap;
import org.sspl.sax.SaxVisitor;
import org.uwdl.parser.uWDLMissingRequiredAttributeException;
import org.uwdl.parser.uWDLVisitor;

public class UVariable extends UElement {
	//attribute
	private final String name;
	private final String type;
	//element
	private final ArrayList<UInitialize> initializes = new ArrayList<UInitialize>();
	
	public UVariable(String name, String type) {
		super(Tag.Variable);
		
		this.name = name;
		this.type = type;
	}
	
	public UVariable(XMLAttributeMap map) throws uWDLMissingRequiredAttributeException {
		super(Tag.Variable);
		
		this.name = map.require("name");
		this.type = map.require("type");
	}
	
	public String getName() {
		return name;
	}
	
	public String getType() {
		return type;
	}
	
	public UInitialize[] getInitializes() {
		return initializes.toArray(new UInitialize[0]);
	}
	
	public void addInitialize(UInitialize initialize) {
		initializes.add(initialize);
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
