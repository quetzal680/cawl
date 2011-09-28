package org.uwdl.parser.ast;

import org.sspl.parser.xml.XMLAttribute;
import org.sspl.sax.SaxVisitor;
import org.uwdl.parser.uWDLVisitor;

public class UTransition extends UElement {
	private String linkId;
	
	public UTransition(String linkId) {
		super(Tag.Transition);
		this.linkId = linkId;
	}
	
	public String getLinkId() {
		return linkId;
	}

	public XMLAttribute[] buildAttributes() {
		return new XMLAttribute[] { new XMLAttribute("linkId", linkId) };
	}
	
	@Override
	public void accept(uWDLVisitor v) {
		v.visit(this);
	}

	@Override
	public void accept(SaxVisitor v) {
		v.visit(this);
	}

}
