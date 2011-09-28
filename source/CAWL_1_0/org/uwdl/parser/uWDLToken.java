/*
 * Created on 2005. 1. 3
 */
package org.uwdl.parser;

/**
 * @author Delios
 */

/* Token + lexem */
public class uWDLToken implements uWDLTokenTypes {
	private Token type;			// ex : <uwdl
	private String text;		// ex : OpenUwdl
	
	public uWDLToken(Token type) {
		this(type, "");
	}
	
	public uWDLToken(Token type, String text) {
		this.type = type;
		this.text = text;
	}
	
	public Token getType() {
		return type;
	}
	
	public String getText() {
		return text;
	}
	
	public String toString() {
		return "Token[type=" + type + ", \"" + text + "\"]";		
	}
	
	public boolean equals(Object o) {
		if (o == this) return true;
		if (o != null && o instanceof uWDLToken) {
			uWDLToken t = (uWDLToken)o;
			return (type.equals(t.type) && text.equals(t.text));
		}			
		return false;
	}
}
