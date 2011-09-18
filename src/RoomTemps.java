import java.util.ArrayList;
import java.util.HashMap;

import processing.core.*;
import org.gicentre.utils.stat.*;

import controlP5.ControlEvent;

public class RoomTemps extends PApplet {

	DBConnector db = new DBConnector();
	XYChart lineChart;

	Image room1, room2, room3, room4, room5, room6, room7, map;
	LineGraph lg;
	GUI buttons;
	int zoomLevel = 0;
	
	HashMap<Integer, Line> cachedData;

	public void setup() {
		size(1024, 768);
		smooth();
		frameRate(30);

		cachedData = new HashMap<Integer, Line>();
		
		buttons = new GUI(this);

		//ArrayList<ArrayList<Float>> arr = db.getAvg(DBConnector.AvgType.year);
		ArrayList<ArrayList<Float>> arr1 = db.getAvgBySensor(
				DBConnector.AvgType.year, 1);
		ArrayList<ArrayList<Float>> arr2 = db.getAvgBySensor(
				DBConnector.AvgType.year, 2);
		ArrayList<ArrayList<Float>> arr3 = db.getAvgBySensor(
				DBConnector.AvgType.year, 3);
		ArrayList<ArrayList<Float>> arr4 = db.getAvgBySensor(
				DBConnector.AvgType.year, 4);
		ArrayList<ArrayList<Float>> arr5 = db.getAvgBySensor(
				DBConnector.AvgType.year, 5);
		ArrayList<ArrayList<Float>> arr6 = db.getAvgBySensor(
				DBConnector.AvgType.year, 6);
		ArrayList<ArrayList<Float>> arr7 = db.getAvgBySensor(
				DBConnector.AvgType.year, 7);


		lg = new LineGraph(this);

		// if I make an Image here it work, in GUI Class it doesn't
		map = new Image(this, "../data/evl_2nd_floor_72pxPin.png", 0, 0);
		map.centerVertically();

		// map.resize(320, 200, true);

		lg.addLine(arr1, 1);
		lg.addLine(arr2, 2);
		lg.addLine(arr3, 3);
		lg.addLine(arr4, 4);
		lg.addLine(arr5, 5);
		lg.addLine(arr6, 6);
		lg.addLine(arr7, 7);

	}

	public void draw() {
		buttons.drawGUI();
		lg.drawChart();
		map.draw();

		/*
		 * room2.draw(); room1.setXY(3, 248); room2.setXY(3, 45);
		 */
		if (!lg.isDataLoaded()) {
			lg.loadLineData();
		}
	}

	public void controlEvent(ControlEvent theEvent) {
		System.out.println(theEvent.controller().name());
	}

	
	public void roomClicked(int theValue) {
		buttons.toogleRoomColor(theValue);
		if(lg.containsRoomNumber(theValue)){
			Line l = lg.removeLine(theValue);
			cachedData.put(zoomLevel* 10 + theValue, l);
		}
		else{
			if(cachedData.containsKey(zoomLevel* 10 + theValue)){
				lg.addLine(cachedData.get(zoomLevel* 10 + theValue).getData(), theValue, true);
			}
		}
	}
	
	public void room1(int theValue) {
		roomClicked(theValue);
	}
	
	public void room2(int theValue) {
		roomClicked(theValue);
	}
	
	public void room3(int theValue) {
		roomClicked(theValue);
	}
	
	public void room4(int theValue) {
		roomClicked(theValue);
	}
	
	public void room5(int theValue) {
		roomClicked(theValue);
	}
	
	public void room6(int theValue) {
		roomClicked(theValue);
	}
	
	public void room7(int theValue) {
		roomClicked(theValue);
	}
	
	public void Change_Locale(int theValue){
		lg.toggleLocalization();
	}

	
	public void mouseClicked() {
		// l.loadData();
	}

	public static void main(String args[]) {
		RoomTempParser rm = new RoomTempParser();

		// rm.parseData("C:\\Users\\Yuriy\\Downloads\\history.txt\\history.txt");
		PApplet.main(new String[] { "--present", "RoomTemps" });
	}

}
