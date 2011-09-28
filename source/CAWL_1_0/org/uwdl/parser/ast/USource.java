package org.uwdl.parser.ast;

import org.sspl.sax.SaxVisitor;
import org.uwdl.parser.uWDLVisitor;

public class USource extends UElement {
	//elemants
	private UInput input;
	
	public USource() {
		super(Tag.Source);
	}
	
	public USource(UInput input) {
		super(Tag.Source);
		this.input = input;
	}
	
	public void setInput(UInput input) {
		this.input = input;
	}
	
	public UInput getInput() {
		return this.input;
	}
	
	public void accept(uWDLVisitor v) {
		v.visit(this);
	}
	
	public void accept(SaxVisitor v) {
		v.visit(this);
	}
}
