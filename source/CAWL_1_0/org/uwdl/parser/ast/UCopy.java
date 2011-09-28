package org.uwdl.parser.ast;

import org.sspl.sax.SaxVisitor;
import org.uwdl.parser.uWDLVisitor;

public class UCopy extends UElement implements INodeDescendant{
	//elements
	private final UFrom from;
	private final UTo to;
	
	public UCopy(UFrom from, UTo to) {
		super(Tag.Copy);
		// TODO Auto-generated constructor stub
		this.from = from;
		this.to = to;
	}
	
	public UFrom getFrom() {
		return this.from;
	}
	
	public UTo getTo() {
		return this.to;
	}
	
	public void accept(uWDLVisitor v) {
		// TODO Auto-generated method stub
		v.visit(this);
	}

	public void accept(SaxVisitor v) {
		v.visit(this);
	}
}
