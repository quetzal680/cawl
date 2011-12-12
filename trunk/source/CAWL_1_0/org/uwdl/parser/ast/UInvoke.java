package org.uwdl.parser.ast;

import org.sspl.parser.xml.XMLAttribute;
import org.sspl.parser.xml.XMLAttributeMap;
import org.sspl.sax.SaxVisitor;
import org.uwdl.parser.uWDLMissingRequiredAttributeException;
import org.uwdl.parser.uWDLVisitor;

public class UInvoke extends UElement implements INodeDescendant {
	//attributes
	private final String targetERP;
	private final String namespace;
	private final String serviceProvider;
	private final String portType;
	private final String operation;
	private final String input;
	private final String output;
	private final String subflow;

	
	public UInvoke(String targetERP, String namespace, String serviceProvider, String portType, String operation, String input, String output, String subflow) {
		super(Tag.Invoke);
		this.targetERP = targetERP;
		this.namespace = namespace;
		this.serviceProvider = serviceProvider;
		this.portType = portType;
		this.operation = operation;
		this.input = input;
		this.output = output;
		this.subflow = subflow;
	}
	
	public UInvoke(XMLAttributeMap map) throws uWDLMissingRequiredAttributeException {
		super(Tag.Invoke);
		this.targetERP = map.require("targetERP");
		this.namespace = map.require("namespace");
		this.serviceProvider = map.require("serviceProvider");
		this.portType = map.require("portType");
		this.operation = map.require("operation");
		this.input = map.optional("input");
		this.output = map.optional("output");
		this.subflow = map.optional("subflow");
		
		System.out.println("+++++++++++++++++++++++  " + serviceProvider);
	}

	public String getTargetERP() {
		return targetERP;
	}

	public String getNamespace() {
		return namespace;
	}

	public String getServiceProvider() {
		return serviceProvider;
	}
	
	public String getPortType() {
		return portType;
	}
	
	public String getOperation() {
		return operation;
	}
	
	public String getInput() {
		return input;
	}
	
	public String getOutput() {
		return output;
	}
	
	public String getSubflow() {
		if(subflow == null)
			return "false";
		
		return subflow;
	}
	
	public XMLAttribute[] buildAttributes() {
		return new XMLAttribute[] { new XMLAttribute("serviceProvider", serviceProvider), new XMLAttribute("portType", portType),
				new XMLAttribute("operation", operation), new XMLAttribute("input", input), new XMLAttribute("output", output), new XMLAttribute("subflow", subflow) };
	}
	
	public void accept(uWDLVisitor v) {
		// TODO Auto-generated method stub
		v.visit(this);
	}

	public String toString() {
		return "invoke" + "\n-serviceProvider: " + serviceProvider + "\n-portType: " + portType + "\n-operation: " + operation + "\n-input: " + input + "\n-output: " + output;
	}
	
	public void accept(SaxVisitor v) {
		v.visit(this);
	}
}
