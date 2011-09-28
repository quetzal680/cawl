package org.uwdl.parser.ast;

public abstract class URuleDescendant extends UElement implements IRuleDescendant {
	private final LogicTag tag;
	
	public URuleDescendant(Tag tag) {
		this(tag, null, LogicTag.CONSTRAINT);
	}

	public URuleDescendant(Tag tag, String pcdata) {
		this(tag, pcdata, LogicTag.CONSTRAINT);
	}
	
	public URuleDescendant(Tag tag, String pcdata, LogicTag logicTag) {
		super(tag, pcdata);
		this.tag = logicTag;
	}
	
	public LogicTag getLogicTag() {
		return tag;
	}

}
