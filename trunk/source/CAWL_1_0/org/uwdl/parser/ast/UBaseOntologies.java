package org.uwdl.parser.ast;

import java.util.ArrayList;

import org.sspl.sax.SaxVisitor;
import org.uwdl.parser.uWDLVisitor;

public class UBaseOntologies extends UElement {
	//elements
	private final ArrayList<UOntology> ontologies = new ArrayList<UOntology>();
	
	public UBaseOntologies() {
		super(Tag.BaseOntologies);
	}
	
	public UOntology[] getOntologies() {
		return ontologies.toArray(new UOntology[0]);
	}
	
	public void addOntology(UOntology ontology) {
		ontologies.add(ontology);
	}
	
	public void accept(uWDLVisitor v) {
		// TODO Auto-generated method stub
		v.visit(this);
	}
	public void accept(SaxVisitor v) {
		v.visit(this);
	}
}
