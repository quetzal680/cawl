package org.uwdl.parser.ast;

import org.sspl.parser.xml.XMLAttribute;

public abstract class UElement implements IElement {
	protected static final XMLAttribute[] EMPTY_ATTRIBUTES = new XMLAttribute[0];
	private transient XMLAttribute[] attributes = null;

	private final Tag tag;
	private String pcdata;
	
	protected UElement(Tag tag) {
		this(tag, null);
	}
	
	protected UElement(Tag tag, String pcdata) {
		this.tag = tag;
		this.pcdata = pcdata;
	}
	
	protected void setPcdata(String pcdata) {
		this.pcdata = pcdata;
	}
	
	public final Tag getTag() {
		return tag;
	}
	
	public final String getPcdata() {
		return pcdata;
	}
	
	public XMLAttribute[] buildAttributes() {
		return EMPTY_ATTRIBUTES;
	}
	
	public final XMLAttribute[] getAttributes() {
		return (attributes != null) ? attributes : (attributes = buildAttributes());
	}
}
