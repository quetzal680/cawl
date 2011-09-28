package org.uwdl.entities;

public interface IEntities {
	public static enum EntitiesTag {
		activatorEntities("activatorEntities"),
		TeaPreparation("TeaPreparation"),
		FlowDisplayLightInfo("FlowDisplayLightInfo"),
		DisplayLightInfo("DisplayLightInfo"),
		MoveDown("MoveDown"),
		Move("Move"),
		STOPLightInfo("STOPLightInfo"),
		MusicTeaPreparation("MusicTeaPreparation"),
		CheckSystem("CheckSystem"),
		MoveUpMusic("MoveUpMusic"),
		DownloadFile("DownloadFile"),
		
		
		N1("N1"),
		N2("N2"),
		N3("N3"),
		N4("N4"),
		N5("N5"),
		N6("N6"),
		N7("N7"),
		N8("N8"),
		N9("N9")
		;
		
		private final String text;
		
		private EntitiesTag(String text) {
			this.text = text;
		}
		
		public String getText() {
			return text;
		}
	};
	
	public EntitiesTag getEntitiesTag();
}
