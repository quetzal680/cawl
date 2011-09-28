package org.uwdl.entities;

import org.uwdl.parser.ast.UObject;
import org.uwdl.parser.ast.USubject;

public class ENoun extends AbstractData implements ISubjectable,IObjectable {
	private final String type;
	
	public ENoun(String name, String type, String value) {
		super(name, value);
		this.type = type;
	}

	public boolean equals(UObject object) {
		return object.getType().equals(type) && object.getPcdata().equals(getValue());
	}

	public boolean equals(USubject subject) {
		return subject.getType().equals(type) && subject.getPcdata().equals(getValue());
	}

	public String getType() {
		return type;
	}
}