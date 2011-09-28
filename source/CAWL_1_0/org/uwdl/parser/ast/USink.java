package org.uwdl.parser.ast;

import org.sspl.sax.SaxVisitor;
import org.uwdl.parser.uWDLVisitor;

/**
 * @author Delios
 */
public class USink extends UElement {
	//elements
	private UOutput output;
	private USynchronize synchronize;
	
	public USink() {
		super(Tag.Sink);
	}
	
	public USink(UOutput output) {
		super(Tag.Sink);
		this.output = output;
	}
	
	public void setOutput(UOutput output) {
		this.output = output;
	}

	public UOutput getOutput() {
		return this.output;
	}
	
	public USynchronize getSynchronize() {
		return synchronize;
	}

	public void setSynchronize(USynchronize synchronize) {
		this.synchronize = synchronize;
	}

	public void accept(uWDLVisitor v) {
		v.visit(this);
	}
	
	public void accept(SaxVisitor v) {
		v.visit(this);
	}
}
