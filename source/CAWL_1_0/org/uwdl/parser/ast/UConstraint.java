/*
 * Created on 2005. 1. 18
 */
package org.uwdl.parser.ast;

import org.uwdl.parser.uWDLMissingRequiredAttributeException;
import org.uwdl.parser.uWDLVisitor;

import org.sspl.parser.xml.XMLAttribute;
import org.sspl.parser.xml.XMLAttributeMap;
import org.sspl.sax.SaxVisitor;

/**
 * @author Delios
 */
public class UConstraint extends URuleDescendant {
	//attributes
	private final String name;
	private final String expression;
	//elements
	private USubject subject;
	private UVerb verb;
	private UObject object;
	
	public UConstraint( String name, String expression ) {
		super(Tag.Constraint);
		
		this.name = name;
		this.expression = expression;
	}
	
	public UConstraint(XMLAttributeMap map, USubject subject, UVerb verb, UObject object) throws uWDLMissingRequiredAttributeException {
		super(Tag.Constraint);
		this.name = map.optional("name");
		this.expression = map.optional("expression");
		this.subject = subject;
		this.verb = verb;
		this.object = object;
	}
	
	public UConstraint getConstraint() {
		return this;
	}
	
	public String getName() {
		return name;
	}
	
	public String getExpression() {
		return expression;
	}
	
	public USubject getSubject() {
		return subject;
	}
	
	public void setSubject(USubject subject) {
		this.subject = subject;
	}

	public void setVerb(UVerb verb) {
		this.verb = verb;
	}

	public void setObject(UObject object) {
		this.object = object;
	}

	public UVerb getVerb() {
		return verb;
	}
	
	public UObject getObject() {
		return object;
	}
	
	public XMLAttribute[] buildAttributes() {
		return new XMLAttribute[] { new XMLAttribute("name",name), new XMLAttribute("expression",expression) };
	}
	
	public void accept(uWDLVisitor v) {
		v.visit(this);
	}
	
	public String toString() {
		return "[Constraint:" + subject.getPcdata() + "," + verb.getPcdata() + "," + object.getPcdata() + "]";
	}
	
	public void accept(SaxVisitor v) {
		v.visit(this);
	}
}
