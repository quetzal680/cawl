package org.uwdl.parser.ast;

import org.sspl.parser.xml.XMLAttribute;
import org.sspl.sax.SaxVisitor;
import org.uwdl.parser.uWDLVisitor;

public class UNSync extends UElement {
	private final String name;

	public UNSync(String name) {
		super(Tag.NSync);
		this.name = name;
	}
	
	public XMLAttribute[] buildAttributes() {
		return new XMLAttribute[] { new XMLAttribute("name", name) }; 
	}

	public String getName() {
		return name;
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
