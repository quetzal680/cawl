/*
 * Created on 2005. 1. 18
 */
package org.uwdl.parser.ast;

import org.sspl.parser.xml.XMLAttribute;
import org.sspl.parser.xml.XMLAttributeMap;
import org.sspl.sax.SaxVisitor;
import org.uwdl.parser.uWDLMissingRequiredAttributeException;
import org.uwdl.parser.uWDLVisitor;

public class UCondition extends UElement {
	//attributes
	private final String expression;
	
	//elements choice (case | context)
	private IConditionDescendant descendant;
	
	public UCondition( String expression ) {
		super( Tag.Condition );
		
		this.expression = expression;
	}
	
	public UCondition(XMLAttributeMap map, IConditionDescendant descendant) throws uWDLMissingRequiredAttributeException {
		super(Tag.Condition);
		this.expression = map.optional("expression");
		this.descendant = descendant;
	}
	
	public String getExpression() {
		return expression;
	}
	
	public IConditionDescendant getDescendant() {
		return descendant;
	}
	
	public void setDescendant(IConditionDescendant descendant) {
		this.descendant = descendant;
	}

	public XMLAttribute[] buildAttributes() {
		return new XMLAttribute[] { new XMLAttribute("expression", expression) };
	}
	
	public void accept(uWDLVisitor v) {
		v.visit(this);
	}
	
	public void accept(SaxVisitor v) {
		v.visit(this);
	}
}
