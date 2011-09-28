package org.uwdl.parser.ast;

public interface IRuleDescendant extends IElement{
	public static enum LogicTag {
		CONSTRAINT("constraint"),
		AND("and"),
		OR("or"),
		NOT("not");
		
		private final String text;
		
		private LogicTag(String text) {
			this.text = text;
		}
		
		public String getLogicText() {
			return text;
		}
	};
	
	public LogicTag getLogicTag();
	public UConstraint getConstraint();
}
