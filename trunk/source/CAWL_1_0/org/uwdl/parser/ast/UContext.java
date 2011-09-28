/*
 * Created on 2005. 1. 18
 */
package org.uwdl.parser.ast;

import java.util.ArrayList;

import org.sspl.parser.xml.XMLAttribute;
import org.sspl.parser.xml.XMLAttributeMap;
import org.sspl.sax.SaxVisitor;
import org.uwdl.parser.uWDLMissingRequiredAttributeException;
import org.uwdl.parser.uWDLVisitor;

/**
 * @author Delios
 */
public class UContext extends UElement implements IConditionDescendant {
	//attributes
	private final String name;
	private final String priority;
	//elements
	private final ArrayList<URule> rules = new ArrayList<URule>();
	
	public UContext( String name, String priority ) {
		super(Tag.Context);
		
		this.name = name;
		this.priority = priority;
	}
	
	public UContext(XMLAttributeMap map) throws uWDLMissingRequiredAttributeException {
		super(Tag.Context);
		this.name = map.optional("name");
		this.priority = map.optional("priority");
	}
	
	public String getName() {
		return name;
	}
	
	public String getPriority() {
		return priority;
	}
	
	public URule[] getRules() {
		return rules.toArray(new URule[0]);
	}
	
	public void addRule(URule rule) {
		rules.add(rule);
	}
	
	public XMLAttribute[] buildAttributes() {
		return new XMLAttribute[] { new XMLAttribute("name", name), new XMLAttribute("priority", priority) }; 
	}
	
	public void accept(uWDLVisitor v) {
		v.visit(this);
	}
	
	public void accept(SaxVisitor v) {
		v.visit(this);
	}
}
