/*
 * Created on 2005. 1. 17
 */
package org.uwdl.parser.ast;

import org.sspl.sax.SaxVisitor;
import org.uwdl.parser.uWDLVisitor;

public class UDocumentation extends UElement {
	public UDocumentation() {
		super(Tag.Documentation);
	}
	
	public UDocumentation(String pcdata) {
		super(Tag.Documentation, pcdata);
	}
	
	public void setPcdata( String pcdata ) {
		super.setPcdata(pcdata);
	}
	
	public void accept(uWDLVisitor v) {
		v.visit(this);
	}
	
	public void accept(SaxVisitor v) {
		
	}
}
