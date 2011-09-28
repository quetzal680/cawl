package org.uwdl.parser.ast;

import java.util.ArrayList;

import org.sspl.sax.SaxVisitor;
import org.uwdl.parser.uWDLVisitor;

public class USynchronize extends UElement {
	private final ArrayList<UNSync> nSyncs = new ArrayList<UNSync>();
	
	public USynchronize() {
		super(Tag.Synchronize);
	}

	
	public ArrayList<UNSync> getnSyncs() {
		return nSyncs;
	}

	public void addUNSync(UNSync nSync) {
		this.nSyncs.add(nSync);
	}

	public void accept(uWDLVisitor v) {
		v.visit(this);
	}

	public void accept(SaxVisitor v) {
		v.visit(this);
	}
}
