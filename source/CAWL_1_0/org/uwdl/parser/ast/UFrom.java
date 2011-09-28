package org.uwdl.parser.ast;

import org.sspl.parser.xml.XMLAttribute;
import org.sspl.parser.xml.XMLAttributeMap;
import org.sspl.sax.SaxVisitor;
import org.uwdl.parser.uWDLMissingRequiredAttributeException;
import org.uwdl.parser.uWDLVisitor;

public class UFrom extends UElement {
	//attribute
	private final String variable;
	private final String part;
	private final String expression;
	
	public UFrom( String variable, String part, String expression ) {
		super(Tag.From);
		
		this.variable = variable;
		this.part = part;
		this.expression = expression;
	}
	
	public UFrom(XMLAttributeMap map) throws uWDLMissingRequiredAttributeException {
		super(Tag.From);
		// TODO Auto-generated constructor stub
		this.variable = map.optional("variable");
		this.part = map.optional("part");
		this.expression = map.optional("expression");
	}

	public String getVariable() {
		return variable;
	}
	
	public String getPart() {
		return part;
	}
	
	public String getExpression() {
		return expression;
	}
	
	public XMLAttribute[] buildAttributes() {
		return new XMLAttribute[] { new XMLAttribute("variable", variable), new XMLAttribute("part", part), new XMLAttribute("expression", expression) };
	}
	
	public void accept(uWDLVisitor v) {
		// TODO Auto-generated method stub
		v.visit(this);
	}

	public void accept(SaxVisitor v) {
		v.visit(this);
	}
}
