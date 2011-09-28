package org.uwdl.parser.ast;

import org.uwdl.parser.uWDLMissingRequiredAttributeException;
import org.uwdl.parser.uWDLUnknownVersionException;
import org.uwdl.parser.uWDLVisitor;

import org.sspl.parser.xml.XMLAttribute;
import org.sspl.parser.xml.XMLAttributeMap;
import org.sspl.sax.SaxVisitor;

import java.util.ArrayList;

public class UUWDL extends UElement {
	//attributes
	private final String name;
	private final String targetNamespace;
	private final String version = "1.0.1";
	
	//elements
	//UDescription�� UDocumention ���� ����
	private UDocumentation documentation;
	private UBaseOntologies baseOntologies;
	private final ArrayList<UServiceProviderType> serviceProviderTypes = new ArrayList<UServiceProviderType>();
	private final ArrayList<UServiceProvider> serviceProviders = new ArrayList<UServiceProvider>();
	private final ArrayList<UMessage> messages = new ArrayList<UMessage>();
	private final ArrayList<UVariable> variables = new ArrayList<UVariable>();
	private final ArrayList<UActivator> activators = new ArrayList<UActivator>();
	private final ArrayList<UFlow> flows = new ArrayList<UFlow>();
	
	public UUWDL(String name, String version, String targetNamespace) 
		throws uWDLUnknownVersionException, uWDLMissingRequiredAttributeException {
		super(Tag.uWDL);
		this.name = name;
		this.targetNamespace = targetNamespace;
		
		if ( !this.version.equals(version) ) throw new uWDLUnknownVersionException(version);
	}
	
	public UUWDL(XMLAttributeMap map, UDocumentation description, UBaseOntologies baseOntologies)
			throws uWDLUnknownVersionException, uWDLMissingRequiredAttributeException {
		super(Tag.uWDL);
		this.name = map.require("name");
		this.targetNamespace = map.require("targetNamespace");
		
		this.documentation = description;
		this.baseOntologies = baseOntologies;
		
		final String version = map.require("version");
		if (!version.equals("1.0.1")) throw new uWDLUnknownVersionException(version);
	}

	
	public UDocumentation getDocumentation() {
		return documentation;
	}

	public void setDocumentation(UDocumentation documentation) {
		this.documentation = documentation;
	}

	public void setBaseOntologies(UBaseOntologies baseOntologies) {
		this.baseOntologies = baseOntologies;
	}

	public UBaseOntologies getBaseOntologies() {
		return baseOntologies;
	}

	public String getName() {
		return name;
	}
	
	public String getTargetNamespace() {
		return targetNamespace;
	}
	
	public UServiceProviderType[] getServiceProviderTypes() {
		return serviceProviderTypes.toArray(new UServiceProviderType[0]);
	}
	
	public void addServiceProviderType(UServiceProviderType serviceProviderType) {
		serviceProviderTypes.add(serviceProviderType);
	}
	
	public UServiceProvider[] getServiceProviders() {
		return serviceProviders.toArray(new UServiceProvider[0]);
	}
	
	public void addServiceProvider(UServiceProvider serviceProvider) {
		serviceProviders.add(serviceProvider);
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
	
	public void addVariable(UVariable variable) {
		variables.add(variable);
	}
	
	public UActivator[] getActivators() {
		return activators.toArray(new UActivator[0]);
	}
	
	public void addActivator(UActivator activator) {
		activators.add(activator);
	}
	
	public UFlow[] getFlows() {
		return flows.toArray(new UFlow[0]);
	}
	
	public void addFlow(UFlow flow) {
		flows.add(flow);
	}
	
	public XMLAttribute[] buildAttributes() {
		return new XMLAttribute[] { new XMLAttribute("version", "2.0.1"), new XMLAttribute("name", name), new XMLAttribute("targetNamespace", targetNamespace) };
	}
	
	public void accept(uWDLVisitor v) {
		v.visit(this);
	}
	
	public void accept(SaxVisitor v) {
		v.visit(this);
	}
}
