package org.ws.command;

import java.util.Hashtable;


public class InfoTable extends Hashtable<String, String> {
	private static InfoTable instance =  new InfoTable();
		
	private InfoTable() {
		super();
		initData();
	}
	
	public static InfoTable getInstance() {
		return instance;
	}
	
	//@param name: uWDL node, activator name
	public String getData(String name) {
		if(this.containsKey(name))
			return this.get(name);
		return null; 
	}
	
	public void setData(String name, String infoData) {
		this.put(name, infoData);
	}
	
	public void updateData(String name, String infoData) {
		if(this.containsKey(name)) {
			// remove
			this.remove(name);
			this.setData(name, infoData);
		}
	}
	
	private void initData() {
		setData("R-307", "TeaLego");
		setData("R-313", "BasicLego");
		
		setData("PC-307", "203.253.23.84:5555");
		setData("PC-313", "203.253.23.56:5555");
		
		setData("Arm", "Arm");
		setData("Leg", "Leg");
		setData("Sound", "Sound");
		setData("LCD", "LCD");
		setData("2D-TargetLoc", "500,0");
		setData("3D-TargetLoc", "55");
		
		setData("MusicFilename", "back.mp3");
		setData("PPTFilename", "test.pptx");
		
		setData("None", "null");
		setData("OutputInfo", "");
		
		setData("DownloadFile", "pcFunction");
		setData("musicService", "pcFunction");
		
		setData("displayLegoService", "robotFunction");
		setData("moveLegoService", "robotFunction");
		
		setData("getSensorData", "sensorFunction");
	}
}
