/*
 * Created on 2005. 1. 18
 */
package org.uwdl.parser.ast;

import org.uwdl.parser.uWDLMissingRequiredAttributeException;
import org.uwdl.parser.uWDLVisitor;

import org.sspl.parser.xml.XMLAttribute;
import org.sspl.parser.xml.XMLAttributeMap;
import org.sspl.sax.SaxVisitor;

public class UCase extends UElement implements IConditionDescendant {
	//attributes
	private final String name;
	private final String expression;
	//elements
	private final UEvent event;
	
	
	public UCase(XMLAttributeMap map, UEvent event) throws uWDLMissingRequiredAttributeException {
		super(Tag.Case);
		this.name = map.optional("name");
		this.expression = map.optional("expression");
		this.event = event;
	}
	
	public String getName() {
		return name;
	}
	
	public String getExpression() {
		return expression;
	}
	
	public UEvent getEvent() {
		return this.event;
	}
	
	public XMLAttribute[] buildAttributes() {
		return new XMLAttribute[] { new XMLAttribute("name", name), new XMLAttribute("expression", expression) };
	}
	
	public void accept(uWDLVisitor v) {
		v.visit(this);
	}
	public void accept(SaxVisitor v) {
		v.visit(this);
	}
}
