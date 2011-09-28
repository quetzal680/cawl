/*
 * Created on 2005. 1. 7
 */
package org.uwdl.parser;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Delios
 */
public interface uWDLTokenTypes {
	public static enum Token {
	    EOF("<eof>"),
	    Close(">"),
	    EmptyClose("/>"),
	    
	    Attribute("<name>"),
	    Eq("="),
	    Value("<value>"),
	    
	    Pcdata("<pcdata>"),
	    
	    OpenXml("<?xml"),
	    CloseXml("?>"),
	    
	    // token table
	    OpenUwdl("<uwdl"),
	    OpenBaseOntologies("<baseontologies"),
	    OpenOntology("<ontology"),
	    OpenServiceProviderType("<serviceprovidertype"),
	    OpenPortType("<porttype"),
	    OpenServiceProvider("<serviceprovider"),
	    OpenLocator("<locator"),
	    OpenMessage("<message"),
	    OpenPart("<part"),
	    OpenVariable("<variable"),
	    OpenInitialize("<initialize"),
	    OpenFrom("<from"),
	    OpenActivator("<activator"),
	    OpenCondition("<condition"),
	    OpenCase("<case"),
	    OpenEvent("<event"),
	    OpenContext("<context"),
	    OpenRule("<rule"),
	    OpenConstraint("<constraint"),
	    OpenSubject("<subject"),
	    OpenVerb("<verb"),
	    OpenObject("<object"),
	    OpenActivate("<activate"),
	    OpenDeactivate("<deactivate"),
	    OpenFlow("<flow"),
	    OpenSource("<source"),
	    OpenInput("<input"),
	    OpenNode("<node"),
	    OpenCopy("<copy"),
	    OpenTo("<to"),
	    OpenInvoke("<invoke"),
	    OpenWait("<wait"),
	    OpenCatch("<catch"),
	    OpenLink("<link"),
	    OpenSink("<sink"),
	    OpenOutput("<output"),
	    OpenDocumentation("<documentation"),
	    OpenSynchronize("<synchronize"),
	    
	    CloseUwdl("</uwdl>"),
	    CloseDocumentation("</documentation>"),
	    CloseBaseOntologies("</baseontologies>"),
	    CloseOntology("</ontology"),
	    CloseServiceProviderType("</serviceprovidertype>"),
	    ClosePortType("</porttype>"),
	    CloseServiceProvider("</serviceprovider>"),
	    CloseLocator("</locator>"),
	    CloseMessage("</message>"),
	    ClosePart("</part>"),
	    CloseVariable("</variable>"),
	    CloseInitialize("</initialize>"),
	    CloseFrom("</from>"),
	    CloseActivator("</activator>"),
	    CloseCondition("</condition>"),
	    CloseCase("</case>"),
	    CloseEvent("</event>"),
	    CloseContext("</context>"),
	    CloseRule("</rule>"),
	    CloseConstraint("</constraint>"),
	    CloseSubject("</subject>"),
	    CloseVerb("</verb>"),
	    CloseObject("</object>"),
	    CloseActivate("</activate>"),
	    CloseDeactivate("</deactivate>"),
	    CloseFlow("</flow>"),
	    CloseSource("</source>"),
	    CloseInput("</input>"),
	    CloseNode("</node>"),
	    CloseCopy("</copy>"),
	    CloseTo("</to>"),
	    CloseInvoke("</invoke>"),
	    CloseWait("</wait>"),
	    CloseCatch("</catch>"),
	    CloseLink("</link>"),
	    CloseSink("</sink>"),
	    CloseSynchronize("</synchronize>"),
	    CloseOutput("</output>");
	
	    
	
	    private final String name;
	    
	    private Token(String name) {
	        this.name = name;
	    }
	    
	    public String getName() {
	        return name;
	    }
	    
	    private static Map<String, Token> tokens = new HashMap<String, Token>();
	    
	    static {
	        for (Token t: Token.values()) {
	            if (t.ordinal() >= OpenUwdl.ordinal() && t.ordinal() <= CloseOutput.ordinal()) {
	                tokens.put(t.getName(), t);
	            }
	        }
	    }
	    
	    public static Token getInstance(String name) {
	        return tokens.get(name);
	    }
	};
}
