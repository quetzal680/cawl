package org.uwdl.entities;

import org.uwdl.parser.ast.UObject;

public interface IObjectable extends IData {
	public boolean equals(UObject object);
}
