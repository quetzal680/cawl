/*
 * Created on 2005. 1. 17
 */
package org.uwdl.parser.ast;

import org.uwdl.parser.uWDLVisitor;

import org.sspl.parser.xml.XMLAttribute;
import org.sspl.sax.SaxVisitor;

/**
 * @author Delios
 */
public interface IElement {
	public static enum Tag {
		uWDL("uwdl"),
		Documentation("documentation"),
		BaseOntologies("baseontologies"),
		Ontology("ontology"),
		ServiceProviderType("serviceprovidertype"),
		PortType("porttype"),
		ServiceProvider("serviceprovider"),
		Locator("locator"),
		Message("message"),
		Part("part"),
		Variable("variable"),
		Initialize("initialize"),
		From("from"),
		Activator("activator"),
		Condition("condition"),
		Case("case"),
		Event("event"),
		Context("context"),
		Rule("rule"),
		Constraint("constraint"),
		Subject("subject"),
		Verb("verb"),
		Object("object"),
		Activate("activate"),
		Deactivate("deactivate"),
		Flow("flow"),
		Source("source"),
		Input("input"),
		Node("node"),
		Copy("copy"),
		To("to"),
		Invoke("invoke"),
		Wait("wait"),
		Catch("catch"),
		Link("link"),
		Sink("sink"),
		Synchronize("synchronize"),
		Output("output"),
		Parallel("parallel"),
		Transition("transition"),
		NSync("nSync");
		
		
		private final String text;
		
		private Tag(String text) {
			this.text = text;
		}
		
		public String getText() {
			return text;
		}
	};
	
	public Tag getTag();
	public String getPcdata();
	public XMLAttribute[] getAttributes();
	public void accept(uWDLVisitor v);
	public void accept(SaxVisitor v);
}
