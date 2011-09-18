import java.util.ArrayList;

import processing.core.*;
import org.gicentre.utils.stat.*;

import controlP5.ControlEvent;

public class RoomTemps extends PApplet {

	DBConnector db = new DBConnector();
	XYChart lineChart;

	Image room1, room2, room3, room4, room5, room6, room7, map;
	LineGraph l;
	GUI buttons;

	public void setup() {
		size(1024, 768);
		smooth();
		frameRate(30);

		buttons = new GUI(this);

		ArrayList<ArrayList<Float>> arr = db.getAvg(DBConnector.AvgType.year);
		ArrayList<ArrayList<Float>> arr1 = db.getAvgBySensor(
				DBConnector.AvgType.year, 1);
		ArrayList<ArrayList<Float>> arr2 = db.getAvgBySensor(
				DBConnector.AvgType.year, 2);

		l = new LineGraph(this);

		// if I make an Image here it work, in GUI Class it doesn't
		map = new Image(this, "../data/evl_2nd_floor_72pxPin.png", 0, 0);
		map.centerVertically();

		// map.resize(320, 200, true);

		l.addLine(arr1);
		l.addLine(arr2);

	}

	public void draw() {
		buttons.drawGUI();
		l.drawChart();
		map.draw();

		/*
		 * room2.draw(); room1.setXY(3, 248); room2.setXY(3, 45);
		 */
		if (!l.isDataLoaded()) {
			l.loadLineData();
		}
	}

	public void controlEvent(ControlEvent theEvent) {
		System.out.println(theEvent.controller().name());
	}

	public void room1(int theValue) {
		buttons.toogleRoomColor(theValue);
	}
	
	public void room2(int theValue) {
		buttons.toogleRoomColor(theValue);
	}
	
	public void room3(int theValue) {
		buttons.toogleRoomColor(theValue);
	}
	
	public void room4(int theValue) {
		buttons.toogleRoomColor(theValue);
	}
	
	public void room5(int theValue) {
		buttons.toogleRoomColor(theValue);
	}
	
	public void room6(int theValue) {
		buttons.toogleRoomColor(theValue);
	}
	
	public void room7(int theValue) {
		buttons.toogleRoomColor(theValue);
	}

	public void toogleRoomColor(){
		
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
