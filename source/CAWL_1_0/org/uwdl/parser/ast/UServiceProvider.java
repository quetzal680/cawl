package org.uwdl.parser.ast;

import org.sspl.parser.xml.XMLAttribute;
import org.sspl.parser.xml.XMLAttributeMap;
import org.sspl.sax.SaxVisitor;
import org.uwdl.parser.uWDLMissingRequiredAttributeException;
import org.uwdl.parser.uWDLVisitor;

public class UServiceProvider extends UElement {
	//attribues
	private final String name;
	private final String type;
	
	//elements
	private ULocator locator;
	
	public UServiceProvider( String name, String type ) {
		super( Tag.ServiceProvider );
		
		this.name = name;
		this.type = type;
	}
	
	public UServiceProvider(XMLAttributeMap map, ULocator locator) throws uWDLMissingRequiredAttributeException {
		super(Tag.ServiceProvider);
		
		this.name = map.require("name");
		this.type = map.require("type");
		this.locator = locator;
	}
	
	public String getName() {
		return name;
	}
	
	public String getType() {
		return type;
	}
	
	public ULocator getLocator() {
		return this.locator;
	}
	
	public void setLocator(ULocator locator) {
		this.locator = locator;
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
