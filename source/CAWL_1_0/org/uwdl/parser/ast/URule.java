package org.uwdl.parser.ast;

import java.util.ArrayList;

import org.sspl.parser.xml.XMLAttribute;
import org.sspl.parser.xml.XMLAttributeMap;
import org.sspl.sax.SaxVisitor;
import org.uwdl.parser.uWDLMissingRequiredAttributeException;
import org.uwdl.parser.uWDLVisitor;

public class URule extends UElement {
	//attributes
	private final String name;
	private final String expression;
	private final String contribute;
	//eleemnts
	private final ArrayList<IRuleDescendant> constraints = new ArrayList<IRuleDescendant>();
	
	public URule( String name, String expression, String contribute ) {
		super( Tag.Rule );
		
		this.name = name;
		this.expression = expression;
		this.contribute = contribute;
	}
	
	public URule(XMLAttributeMap map) throws uWDLMissingRequiredAttributeException {
		super(Tag.Rule);
		this.name = map.optional("name");
		this.expression = map.optional("expression");
		String contribute_value = map.require("contribute");
		if(contribute_value.equals("positive") || contribute_value.equals("negative"))
			this.contribute = contribute_value;
		else throw new uWDLMissingRequiredAttributeException("contribute", "contribute");
	}
	
	public String getName() {
		return name;
	}
	
	public String getExpression() {
		return expression;
	}
	
	public String getContribute() {
		return contribute;
	}
	
	public IRuleDescendant[] getConstraints() {
		return constraints.toArray(new IRuleDescendant[0]);
	}
	
	public void addConstraint(IRuleDescendant constraint) {
		constraints.add(constraint);
	}
	
	public XMLAttribute[] buildAttributes() {
		return new XMLAttribute[] { new XMLAttribute("name", name), new XMLAttribute("expression", expression), new XMLAttribute("contribute", contribute) };
	}
	
	public void accept(uWDLVisitor v) {
		v.visit(this);
	}
	
	public void accept(SaxVisitor v) {
		v.visit(this);
	}
}
