package org.uwdl.entities;

public class Entity {
	private final ISubjectable subject;
	private final IVerbable verb;
	private final IObjectable object;
	
	public Entity(ISubjectable subject, IVerbable verb, IObjectable object) {
		this.subject = subject;
		this.verb = verb;
		this.object = object;
	}
	
	public ISubjectable getSubject() {
		return subject;
	}
	
	public IVerbable getVerb() {
		return verb;
	}
	
	public IObjectable getObject() {
		return object;
	}
	
	public String toString() {
		return "[Context:" + subject.getValue() + "," + verb.getValue() + "," + object.getValue() + "]";
	}
}
