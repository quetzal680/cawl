package org.uwdl.parser.ast;

import java.util.ArrayList;

import org.sspl.sax.SaxVisitor;
import org.uwdl.parser.uWDLVisitor;

public class UParallel extends UElement {
	private final ArrayList<UTransition> transitions = new ArrayList<UTransition>();
	
	public UParallel() {
		super(Tag.Parallel);
	}
	
	public ArrayList<UTransition> getTransitions() {
		return transitions;
	}
	
	public void addTransition(UTransition transition) {
		this.transitions.add(transition);
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
