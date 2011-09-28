package org.uwdl.entities;

import org.uwdl.parser.ast.UVerb;

public interface IVerbable extends IData {
	public boolean equals(UVerb verb);
}
