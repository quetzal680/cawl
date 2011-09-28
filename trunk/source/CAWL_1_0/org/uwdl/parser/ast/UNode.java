package org.uwdl.parser.ast;

import java.util.ArrayList;

import org.uwdl.parser.uWDLMissingRequiredAttributeException;
import org.uwdl.parser.uWDLVisitor;

import org.sspl.parser.xml.XMLAttribute;
import org.sspl.parser.xml.XMLAttributeMap;
import org.sspl.sax.SaxVisitor;

public class UNode extends UElement {
	//attributes
	private final String name;
	//elements
	private /*final*/ UCondition condition;
	private /*final*/ USynchronize synchronize;
	private UParallel parallel;
	private final ArrayList<UMessage> messages = new ArrayList<UMessage>();
	private final ArrayList<UVariable> variables = new ArrayList<UVariable>();
	//choice (copy | invoke | wait)*
	private final ArrayList<INodeDescendant> descendants = new ArrayList<INodeDescendant>();
	
	public UNode(String name) {
		super(Tag.Node);
		
		this.name = name;
	}
	
	public UNode(XMLAttributeMap map) throws uWDLMissingRequiredAttributeException {
		super(Tag.Node);
		this.name = map.require("name");
	}
	
	public String getName() {
		return name;
	}
	
	public UCondition getCondition() {
		return this.condition;
	}
	
	public void setCondition(UCondition condition) {
		this.condition = condition;
	}
	
	public USynchronize getSynchronize() {
		return this.synchronize;
	}
	
	public void setSynchronize(USynchronize synchronize) {
		this.synchronize = synchronize;
	}
	
	public UMessage[] getMessages() {
		return messages.toArray(new UMessage[0]);
	}
	
	public void addMessage(UMessage message) {
		messages.add(message);
	}
	
	public UVariable[] getVariable() {
		return variables.toArray(new UVariable[0]);
	}
	
	public void addVariable(UVariable variable) {
		variables.add(variable);
	}
	
	public INodeDescendant[] getINodeDescendant() {
		return descendants.toArray(new INodeDescendant[0]);
	}
	
	public void addDescendant(INodeDescendant descendant) {
		descendants.add(descendant);
	}
	
	public UParallel getParallel() {
		return parallel;
	}

	public void setParallel(UParallel parallel) {
		this.parallel = parallel;
	}

	public XMLAttribute[] buildAttributes() {
		return new XMLAttribute[] { new XMLAttribute("name", name) };
	}
	
	public void accept(uWDLVisitor v) {
		v.visit(this);
	}
	
	public void accept(SaxVisitor v) {
		v.visit(this);
	}
}
