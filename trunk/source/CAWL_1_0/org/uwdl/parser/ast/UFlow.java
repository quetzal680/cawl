package org.uwdl.parser.ast;

import java.util.ArrayList;

import org.sspl.parser.xml.XMLAttribute;
import org.sspl.parser.xml.XMLAttributeMap;
import org.sspl.sax.SaxVisitor;
import org.uwdl.parser.uWDLMissingRequiredAttributeException;
import org.uwdl.parser.uWDLVisitor;

public class UFlow extends UElement {
	//attributes
	private final String name;
	private final String default_;
	
	//elements
	private /*final*/ USource source;
	private /*final*/ USink sink;
	private final ArrayList<UMessage> messages = new ArrayList<UMessage>();
	private final ArrayList<UVariable> variables = new ArrayList<UVariable>();
	private final ArrayList<UNode> nodes = new ArrayList<UNode>();
	private final ArrayList<ULink> links = new ArrayList<ULink>();
	
	public UFlow( String name ) {
		super(Tag.Flow);
		this.name = name;
		this.default_ = null;
	}
	
	public UFlow(XMLAttributeMap map) throws uWDLMissingRequiredAttributeException {
		super(Tag.Flow);
		this.name = map.require("name");
		String defalutValue = map.optional("default");
		if(defalutValue != null) {
			defalutValue = defalutValue.toLowerCase();
			if(defalutValue.equals("true") || defalutValue.equals("false"))
				default_ = defalutValue;
			else throw new uWDLMissingRequiredAttributeException("flow", "default");
		}
		else default_ = null;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDefault() {
		return default_;
	}
	
	public USource getSource() {
		return this.source;
	}
	
	public void setSource(USource source) {
		this.source = source;
	}
	
	public USink getSink() {
		return this.sink;
	}
	
	public void setSink(USink sink) {
		this.sink = sink;
	}

	public void addMessage(UMessage message) {
		messages.add(message);
	}
	
	public UMessage[] getMessages() {
		return messages.toArray(new UMessage[0]);
	}

	public UVariable[] getVariables() {
		return variables.toArray(new UVariable[0]);
	}
	
	public void addVariable(UVariable variable) {
		variables.add(variable);
	}	
	
	public ArrayList<UNode> getNodes() {
		return nodes;
	}
	
	public void addNode(UNode node) {
		nodes.add(node);
	}
	
	public ULink[] getLinks() {
		return links.toArray(new ULink[0]);
	}
	
	public void addLink(ULink link) {
		links.add(link);
	}
	
	public XMLAttribute[] buildAttributes() {
		return new XMLAttribute[] { new XMLAttribute("name", name), new XMLAttribute("default", default_) };
	}

	public void accept(uWDLVisitor v) {
		// TODO Auto-generated method stub
		v.visit(this);
	}

	public void accept(SaxVisitor v) {
		v.visit(this);
	}
}
