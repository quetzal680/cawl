package org.uwdl.parser.ast;

import java.util.ArrayList;

import org.sspl.parser.xml.XMLAttribute;
import org.sspl.parser.xml.XMLAttributeMap;
import org.sspl.sax.SaxVisitor;
import org.uwdl.parser.uWDLMissingRequiredAttributeException;
import org.uwdl.parser.uWDLVisitor;

public class UActivator extends UElement {
	//attribures
	private final String name;
	
	//elements
	private /*final*/ UCondition condition;
	private final ArrayList<UMessage> messages = new ArrayList<UMessage>();
	private final ArrayList<UVariable> variables = new ArrayList<UVariable>();
	private final ArrayList<UActivate> activates = new ArrayList<UActivate>();
	private final ArrayList<UDeactivate> deactivates = new ArrayList<UDeactivate>();
	
	public UActivator(String name) {
		super(Tag.Activator);
		this.name = name;
	}
	
	public UActivator(XMLAttributeMap map) throws uWDLMissingRequiredAttributeException {
		super(Tag.Activator);
		this.name = map.require("name");
	}
	
	public String getName() {
		return name;
	}
	
	public UCondition getCondition() {
		return this.condition;
	}
	
	public UMessage[] getMessages() {
		return messages.toArray(new UMessage[0]);
	}
	
	public void addMessage(UMessage message) {
		messages.add(message);
	}
	
	public UVariable[] getVariables() {
		return variables.toArray(new UVariable[0]);
	}
	
	public void addVariables(UVariable variable) {
		variables.add(variable);
	}
	
	public void setCondition(UCondition condition) {
		this.condition = condition;
	}
	
	public UActivate[] getActivates() {
		return activates.toArray(new UActivate[0]);
	}
	
	public void addActivate(UActivate activate) {
		activates.add(activate);
	}
	
	public UDeactivate[] getDeactivates() {
		return deactivates.toArray(new UDeactivate[0]);
	}
	
	public void addDeactivate(UDeactivate deactivate) {
		deactivates.add(deactivate);
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
