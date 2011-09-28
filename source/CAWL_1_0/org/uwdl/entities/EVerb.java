package org.uwdl.entities;

import org.uwdl.parser.ast.UVerb;

public class EVerb extends AbstractData implements IVerbable {
	public EVerb(String name, String value) {
		super(name, value);
	}

	public boolean equals(UVerb verb) {
		return verb.getPcdata().equals(getValue());
	}
}
