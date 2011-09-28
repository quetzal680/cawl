package org.uwdl.parser.ast;

import org.sspl.sax.SaxVisitor;
import org.uwdl.parser.uWDLMissingRequiredAttributeException;
import org.uwdl.parser.uWDLVisitor;

public class UVerb extends UElement {
	
	public UVerb() {
		super(Tag.Verb);
	}
	
	public UVerb(String pcdata) throws uWDLMissingRequiredAttributeException {
		super(Tag.Verb, pcdata);
	}
	
	public void setPcdata( String pcdata ) {
		super.setPcdata(pcdata);
	}
	
	public void accept(uWDLVisitor v) {
		v.visit(this);
	}
	
	public void accept(SaxVisitor v) {
		v.visit(this);
	}
}
