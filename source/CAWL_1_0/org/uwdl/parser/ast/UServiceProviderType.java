package org.uwdl.parser.ast;

import java.util.ArrayList;

import org.sspl.parser.xml.XMLAttribute;
import org.sspl.parser.xml.XMLAttributeMap;
import org.sspl.sax.SaxVisitor;
import org.uwdl.parser.uWDLMissingRequiredAttributeException;
import org.uwdl.parser.uWDLVisitor;

public class UServiceProviderType extends UElement {
	//attributes
	private final String name;
	
	//element
	//portType.doc ���� ��� ????????????????????????
	private final ArrayList<UPortType> portTypes = new ArrayList<UPortType>(); 
	
	public UServiceProviderType(XMLAttributeMap map) throws uWDLMissingRequiredAttributeException {
		super(Tag.ServiceProviderType);
		this.name = map.require("name");
	}
	
	public String getName() {
		return name;
	}
	
	public void addPortType(UPortType portType) {
		portTypes.add(portType);
	}
	
	public UPortType[] getPortTypes() {
		return portTypes.toArray(new UPortType[0]);
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
