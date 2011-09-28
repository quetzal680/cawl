package org.uwdl.entities;

import java.util.Hashtable;

import org.uwdl.entities.IEntities.EntitiesTag;


public class ScenarioEntitySet extends Hashtable<String, Entities> {
	private static ScenarioEntitySet instance =  new ScenarioEntitySet();;
	
	private ScenarioEntitySet() {
		super();
		setVitualData();
	}
	
	public static ScenarioEntitySet getInstance() {
		return instance;
	}
	
	//@param name: uWDL node, activator name
	public Entities getEntities(String name) {
		updateEntities(name);
		
		if(this.containsKey(name))
			return this.get(name);
		return null;
	}
	
	public void putEntities(String name, Entities entities) {
		this.put(name, entities);
	}
	
	public void updateEntities(String name) {
		if(this.containsKey(name)) {
			// remove Entities
			Entities tmpEntities = this.get(name);
			this.remove(name);

			// create & put Entities 
			switch( tmpEntities.getEntitiesTag() )
			{
				case activatorEntities :
					Entities activatorEntities = new Entities(EntitiesTag.activatorEntities);
			        activatorEntities.addEntity(new Entity(	new ENoun("militaryTime", "militaryTime", "0900"), 
															new EVerb("verb", "is"), 
															new ENoun("militaryTime","militaryTime", "0900")));
			        activatorEntities.addEntity(new Entity(	new ENoun("person", "Person", "John"), 
															new EVerb("verb", "isLocated"), 
															new ENoun("room", "Room", "313")));
			        activatorEntities.addEntity(new Entity(	new ENoun("militaryTime", "militaryTime", "120500"), 
															new EVerb("verb", "is"), 
															new ENoun("militaryTime","militaryTime", "crtTime")));
			        putEntities("MTRoomServiceActivator", activatorEntities);
					break;
					
				case TeaPreparation :
					Entities TeaPreparation = new Entities(EntitiesTag.TeaPreparation);
			        TeaPreparation.addEntity(new Entity(new ENoun("m", "militaryTime", "1340"), 
														new EVerb("verb", "is"), 
														new ENoun("m", "militaryTime", "1340")));
			        TeaPreparation.addEntity(new Entity(new ENoun("robot1", "Robot", "R-307"), 
			        									new EVerb("verb", "isLocated"), 
			        									new ENoun("room", "Room", "307")));
			        putEntities("TeaPreparation", TeaPreparation);
					break;
					
				case FlowDisplayLightInfo :
					Entities FlowDisplayLightInfo = new Entities(EntitiesTag.FlowDisplayLightInfo);
					FlowDisplayLightInfo.addEntity(	new Entity(	new ENoun("rsvTime", "militaryTime", "1350"), 
			        										new EVerb("verb", "is"), 
			        										new ENoun("crtTime", "militaryTime", "1350")));
					FlowDisplayLightInfo.addEntity(	new Entity(	new ENoun("person", "Person", "John"), 
															new EVerb("verb", "isLocated"), 
															new ENoun("room", "Room", "313")));
			        putEntities("FlowDisplayLightInfo", FlowDisplayLightInfo);
					break;
					
				case DisplayLightInfo :
					Entities DisplayLightInfo = new Entities(EntitiesTag.DisplayLightInfo);
			        DisplayLightInfo.addEntity(	new Entity(	new ENoun("rsvTime", "militaryTime", "120610"), 
			        										new EVerb("verb", "is"), 
			        										new ENoun("crtTime", "militaryTime", "crtTime")));
			        DisplayLightInfo.addEntity(	new Entity(	new ENoun("person", "Person", "John"), 
															new EVerb("verb", "isLocated"), 
															new ENoun("room", "Room", "313")));
			        DisplayLightInfo.addEntity(new Entity(	new ENoun("robot2", "Robot", "R-313"), 
															new EVerb("verb", "isLocated"), 
															new ENoun("room", "Room", "313")));
			        putEntities("DisplayLightInfo", DisplayLightInfo);
					break;
					
				case MoveDown :
					Entities MoveDown = new Entities(EntitiesTag.MoveDown);
			        MoveDown.addEntity(	new Entity(	new ENoun("rsvTime", "militaryTime", "120530"), 
													new EVerb("verb", "is"), 
													new ENoun("crtTime", "militaryTime", "crtTime")));
			        putEntities("MoveDown", MoveDown);
					break;
					
				case MoveUpMusic :
					Entities MoveUpMusic = new Entities(EntitiesTag.MoveUpMusic);
					MoveUpMusic.addEntity(new Entity(new ENoun("rsvTime", "militaryTime", "120600"), 
												new EVerb("verb", "is"), 
												new ENoun("crtTime", "militaryTime", "crtTime")));
			        putEntities("MoveUpMusic", MoveUpMusic);
					break;
					
				case Move :
				    Entities Move = new Entities(EntitiesTag.Move);
			        Move.addEntity(	new Entity(	new ENoun("rsvTime", "militaryTime", "120630"), 
												new EVerb("verb", "is"), 
												new ENoun("crtTime", "militaryTime", "crtTime")));
			        Move.addEntity(	new Entity(	new ENoun("person", "Person", "John"), 
												new EVerb("verb", "isLocated"), 
												new ENoun("room", "Room", "313")));
			        Move.addEntity(	new Entity(	new ENoun("robot1", "Robot", "R-307"), 
												new EVerb("verb", "isLocated"), 
												new ENoun("room", "Room", "307")));
			        putEntities("Move", Move);
					break;
					
				case STOPLightInfo :
					Entities STOPLightInfo = new Entities(EntitiesTag.STOPLightInfo);
					STOPLightInfo.addEntity(new Entity(new ENoun("meetingRoomNum", "Room", "307"), 
													new EVerb("verb", "is"),
													new ENoun("brightness", "Brightness", "dark") ) );
					STOPLightInfo.addEntity(new Entity(new ENoun("effective-Distance", "Distance", "effective-Distance"), 
													new EVerb("verb", "is"),
													new ENoun("R2U-Distance", "Distance", "25") ) );				
					putEntities("STOPLightInfo", STOPLightInfo);
//					putEntities(name, new Sensor().getSensorEntities(EntitiesTag.STOPLightInfo,"BasicLego"));
					break;
					
				case MusicTeaPreparation :
					Entities MusicTeaPreparation = new Entities(EntitiesTag.MusicTeaPreparation);
					MusicTeaPreparation.addEntity(	new Entity(	new ENoun("rsvTime", "militaryTime", "120510"), 
																new EVerb("verb", "is"),
																new ENoun("crtTime", "militaryTime", "crtTime")));
					MusicTeaPreparation.addEntity(	new Entity(	new ENoun("robo1", "Robot", "R-307"), 
																new EVerb("verb", "isLocated"), 
																new ENoun("meetingRoomNum", "Room", "307")));
					putEntities("MusicTeaPreparation", MusicTeaPreparation);
					break;
					
				case CheckSystem :
					Entities CheckSystem = new Entities(EntitiesTag.CheckSystem);
					CheckSystem.addEntity(	new Entity(	new ENoun("rsvTime", "militaryTime", "1351"), 
														new EVerb("verb", "is"),
														new ENoun("crtTime", "militaryTime", "1351")));
					CheckSystem.addEntity(	new Entity(	new ENoun("server1", "PC", "PC-307"), 
														new EVerb("verb", "isLocated"), 
														new ENoun("meetingRoomNum", "Room", "307")));
					putEntities("CheckSystem", CheckSystem);
					break;
					
					
				case DownloadFile :
					Entities DownloadFile = new Entities(EntitiesTag.DownloadFile);
					DownloadFile.addEntity(	new Entity(	new ENoun("rsvTime", "militaryTime", "120730"), 
														new EVerb("verb", "is"),
														new ENoun("crtTime", "militaryTime", "crtTime")));
					DownloadFile.addEntity(	new Entity(	new ENoun("server1", "PC", "PC-307"), 
														new EVerb("verb", "isLocated"), 
														new ENoun("meetingRoomNum", "Room", "307")));
					// 다운로드 PC에 서버 상태 확인해서 값을 넣어 주어야 한다. 
					DownloadFile.addEntity(	new Entity(	new ENoun("downloadServer", "PC", "PC-307"), 
														new EVerb("verb", "isAvailable"), 
														new ENoun("availe", "Check", "TRUE")));
					putEntities("DownloadFile", DownloadFile);
					break;
					
				case N1 :
					Entities N1 = new Entities(EntitiesTag.N1);
					N1.addEntity(	new Entity(	new ENoun("robo1", "Robot", "R-307"), 
																new EVerb("verb", "isLocated"), 
																new ENoun("meetingRoomNum", "Room", "307")));
					putEntities("N1", N1);
					break;
				case N2 :
					Entities N2 = new Entities(EntitiesTag.N2);
					N2.addEntity(	new Entity(	new ENoun("robo1", "Robot", "R-307"), 
																new EVerb("verb", "isLocated"), 
																new ENoun("meetingRoomNum", "Room", "307")));
					putEntities("N2", N2);
					break;
				case N3 :
					Entities N3 = new Entities(EntitiesTag.N3);
					N3.addEntity(	new Entity(	new ENoun("robo1", "Robot", "R-307"), 
																new EVerb("verb", "isLocated"), 
																new ENoun("meetingRoomNum", "Room", "307")));
					putEntities("N3", N3);
					break;
				case N4 :
					Entities N4 = new Entities(EntitiesTag.N4);
					N4.addEntity(	new Entity(	new ENoun("robo1", "Robot", "R-307"), 
																new EVerb("verb", "isLocated"), 
																new ENoun("meetingRoomNum", "Room", "307")));
					putEntities("N4", N4);
					break;
				case N5 :
					Entities N5 = new Entities(EntitiesTag.N5);
					N5.addEntity(	new Entity(	new ENoun("robo1", "Robot", "R-307"), 
																new EVerb("verb", "isLocated"), 
																new ENoun("meetingRoomNum", "Room", "307")));
					putEntities("N5", N5);
					break;
				case N6 :
					Entities N6 = new Entities(EntitiesTag.N6);
					N6.addEntity(	new Entity(	new ENoun("robo1", "Robot", "R-307"), 
																new EVerb("verb", "isLocated"), 
																new ENoun("meetingRoomNum", "Room", "307")));
					putEntities("N6", N6);
					break;
				case N7 :
					Entities N7 = new Entities(EntitiesTag.N7);
					N7.addEntity(	new Entity(	new ENoun("robo1", "Robot", "R-307"), 
																new EVerb("verb", "isLocated"), 
																new ENoun("meetingRoomNum", "Room", "307")));
					putEntities("N7", N7);
					break;
				case N8 :
					Entities N8 = new Entities(EntitiesTag.N8);
					N8.addEntity(	new Entity(	new ENoun("robo1", "Robot", "R-307"), 
																new EVerb("verb", "isLocated"), 
																new ENoun("meetingRoomNum", "Room", "307")));
					putEntities("N8", N8);
					break;
				case N9 :
					Entities N9 = new Entities(EntitiesTag.N9);
					N9.addEntity(	new Entity(	new ENoun("robo1", "Robot", "R-307"), 
																new EVerb("verb", "isLocated"), 
																new ENoun("meetingRoomNum", "Room", "307")));
					putEntities("N9", N9);
					break;
				default :
					System.out.println("ScenarioEntitySet.updateEntities> no match EntitiesTag : " + tmpEntities.getEntitiesTag());
					break;
			}
		}
		else {
			System.out.println("ScenarioEntitySet.updateEntities> no match Entities name : " + name);
		}
	
	}
	
	public void setVitualData() {
		Entities activatorEntities = new Entities(EntitiesTag.activatorEntities);
        Entities TeaPreparation = new Entities(EntitiesTag.TeaPreparation);
        Entities FlowDisplayLightInfo = new Entities(EntitiesTag.FlowDisplayLightInfo);
        Entities DisplayLightInfo = new Entities(EntitiesTag.DisplayLightInfo);
        Entities MoveDown = new Entities(EntitiesTag.MoveDown);
        Entities MoveUpMusic = new Entities(EntitiesTag.MoveUpMusic);
        Entities Move = new Entities(EntitiesTag.Move);
        Entities STOPLightInfo = new Entities(EntitiesTag.STOPLightInfo);
        Entities MusicTeaPreparation = new Entities(EntitiesTag.MusicTeaPreparation);
        Entities CheckSystem = new Entities(EntitiesTag.CheckSystem);
        Entities DownloadFile = new Entities(EntitiesTag.DownloadFile);
        
        
        putEntities("MTRoomServiceActivator", activatorEntities);
        putEntities("TeaPreparation", TeaPreparation);
        putEntities("FlowDisplayLightInfo", FlowDisplayLightInfo);
        putEntities("DisplayLightInfo", DisplayLightInfo);
        putEntities("MoveDown", MoveDown);
        putEntities("MoveUpMusic", MoveUpMusic);
        putEntities("Move", Move);
        putEntities("STOPLightInfo", STOPLightInfo);
        putEntities("MusicTeaPreparation", MusicTeaPreparation);
        putEntities("CheckSystem", CheckSystem);
        putEntities("DownloadFile", DownloadFile);
        
        
        
        
        Entities N1 = new Entities(EntitiesTag.N1);
        Entities N2 = new Entities(EntitiesTag.N2);
        Entities N3 = new Entities(EntitiesTag.N3);
        Entities N4 = new Entities(EntitiesTag.N4);
        Entities N5 = new Entities(EntitiesTag.N5);
        Entities N6 = new Entities(EntitiesTag.N6);
        Entities N7 = new Entities(EntitiesTag.N7);
        Entities N8 = new Entities(EntitiesTag.N8);
        Entities N9 = new Entities(EntitiesTag.N9);
        
        putEntities("N1", N1);
        putEntities("N2", N2);
        putEntities("N3", N3);
        putEntities("N4", N4);
        putEntities("N5", N5);
        putEntities("N6", N6);
        putEntities("N7", N7);
        putEntities("N8", N8);
        putEntities("N9", N9);
	}
}
