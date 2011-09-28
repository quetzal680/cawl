package org.uwdl.scenario.exception;

public class LinkAttributeException extends Exception {
	private String from;
	
	public LinkAttributeException(String from) {
		this.from = from;
	}
	
	@Override
	public String getMessage() {
		return "[from]:"+from +"[to] null";
	}
}
