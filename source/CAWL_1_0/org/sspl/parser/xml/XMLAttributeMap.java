package org.sspl.parser.xml;

import org.uwdl.parser.uWDLMissingRequiredAttributeException;

import java.util.HashMap;

public class XMLAttributeMap extends HashMap<String, String> {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3904965265430098225L;

	public XMLAttributeMap() {
		super(1);
	}
	
	public String imply(String name, String defaultValue) {
		String found = get(name);
		if (found == null) return defaultValue;
		return found;
	}
	
	// 해당하는 key 값이 꼭 있어야 함, 없는 경우 예외 발생
	public String require(String name) throws uWDLMissingRequiredAttributeException {
		String value = get(name);
		if (value == null) throw new uWDLMissingRequiredAttributeException("", name);
		return value;
	}
	
	// 해당하는 key 값이 꼭 있을 필요가 없음, 없는 경우 null 리턴 
	public String optional(String name) {
		String value = get(name);
		if (value == null) value = null;
		return value;
	}
}
