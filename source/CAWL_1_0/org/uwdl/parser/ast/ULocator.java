package org.uwdl.parser.ast;

import org.sspl.parser.xml.XMLAttribute;
import org.sspl.parser.xml.XMLAttributeMap;
import org.sspl.sax.SaxVisitor;
import org.uwdl.parser.uWDLMissingRequiredAttributeException;
import org.uwdl.parser.uWDLUnknownLocatorTypeException;
import org.uwdl.parser.uWDLVisitor;

public class ULocator extends UElement {
	//attributes
	// ����Ÿ���� �ٽ��ѹ� ��� �غ��߰���-> type (static | UDDI) 
	private final String type;
	
	public ULocator( String type ) throws uWDLMissingRequiredAttributeException, uWDLUnknownLocatorTypeException {
		super( Tag.Locator );
		
		this.type = type;
		if(!type.equals("static") && !type.equals("uddi")) throw new uWDLUnknownLocatorTypeException(type);
	}
	
	public ULocator(XMLAttributeMap map) throws uWDLMissingRequiredAttributeException, uWDLUnknownLocatorTypeException {
		super(Tag.Locator);
		this.type = map.require("type").toLowerCase();
		if(!type.equals("static") && !type.equals("uddi")) throw new uWDLUnknownLocatorTypeException(type); 
	}
	
	public String getType() {
		return type;
	}
	
	public XMLAttribute[] buildAttributes() {
		return new XMLAttribute[] { new XMLAttribute("type", type) };
	}
	
	public void accept(uWDLVisitor v) {
		// TODO Auto-generated method stub
		v.visit(this);
	}
	
	public void accept(SaxVisitor v) {
		v.visit(this);
	}
	
}
