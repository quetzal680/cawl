package org.uwdl.parser.ast;

import org.sspl.parser.xml.XMLAttribute;
import org.sspl.parser.xml.XMLAttributeMap;
import org.sspl.sax.SaxVisitor;
import org.uwdl.parser.uWDLMissingRequiredAttributeException;
import org.uwdl.parser.uWDLVisitor;

public class UOntology extends UElement {
	//attributes
	// URI ǥ����� ã�Ƽ� ����
	private final String location;
	private final String namespace;
 
	public UOntology(String location, String namespace) {
		super(Tag.Ontology);
		this.location = location;
		this.namespace = namespace; 
	}
	
	public UOntology(XMLAttributeMap map) throws uWDLMissingRequiredAttributeException {
		super(Tag.Ontology);
		
		this.location = map.require("location");
		this.namespace = map.require("namespace"); 
	}
	
	public String getLocation() {
		return location;
	}

	public String getNamespace() {
		return namespace;
	}
	
	public XMLAttribute[] buildAttributes() {
		return new XMLAttribute[] { new XMLAttribute("location", location), new XMLAttribute("namespace", namespace) };
	}
	
	public void accept(uWDLVisitor v) {
		// TODO Auto-generated method stub
		v.visit(this);
	}
	
	public void accept(SaxVisitor v) {
		v.visit(this);
	}
}
