package org.uwdl.entities;

import org.uwdl.parser.ast.USubject;

public interface ISubjectable extends IData {
	public boolean equals(USubject subject);
}
