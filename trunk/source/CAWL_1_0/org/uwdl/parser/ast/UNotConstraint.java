package org.uwdl.parser.ast;

import org.sspl.parser.xml.XMLAttribute;
import org.sspl.parser.xml.XMLAttributeMap;
import org.sspl.sax.SaxVisitor;
import org.uwdl.parser.uWDLMissingRequiredAttributeException;
import org.uwdl.parser.uWDLVisitor;

public class UNotConstraint extends URuleDescendant {
	final UConstraint constraint;
	//final LogicTag logicTag;
	
	public UNotConstraint(XMLAttributeMap map, USubject subject, UVerb verb, UObject object) throws uWDLMissingRequiredAttributeException {
		super(Tag.Constraint);
		// TODO Auto-generated constructor stub
		//this.logicTag = LogicTag.NOT;
		constraint = new UConstraint(map, subject, verb, object);
	}
	
	public UConstraint getConstraint() {
		return constraint;
	}

	public String getName() {
		return constraint.getName();
	}
	
	public String getExpression() {
		return constraint.getExpression();
	}
	
	public USubject getSubject() {
		return constraint.getSubject();
	}
	
	public UVerb getVerb() {
		return constraint.getVerb();
	}
	
	public UObject getObject() {
		return constraint.getObject();
	}
	
	public XMLAttribute[] buildAttributes() {
		return constraint.buildAttributes();
	}
	
	public void accept(uWDLVisitor v) {
		// TODO Auto-generated method stub
		v.visit(this);
	}
	
	public void accept(SaxVisitor v) {
		v.visit(this);
	}

}
