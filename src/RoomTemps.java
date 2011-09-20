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
	int linesAtZ1 = 0;
	String currYear = "";
	
	HashMap<Integer, Line> cachedData;

	public void setup() {
		size(1024, 768);
		smooth();
		frameRate(30);

		cachedData = new HashMap<Integer, Line>(50);
		
		buttons = new GUI(this);
		
		//ArrayList<ArrayList<Float>> arr = db.getAvg(DBConnector.AvgType.year);
		/*DataContainer arr1 = db.getHourlyData(1);
		DataContainer arr2 = db.getHourlyData(2);
		DataContainer arr3 = db.getHourlyData(3);
		DataContainer arr4 = db.getHourlyData(4);
		DataContainer arr5 = db.getHourlyData(5);
		DataContainer arr6 = db.getHourlyData(6);
		DataContainer arr7 = db.getHourlyData(7);*/
		//DataContainer arr1 = db.getAvgBySensor(DBConnector.AvgType.year, 1);
		DataContainer arr1 = db.getAvgBySensor(DBConnector.AvgType.year, 1);
		DataContainer arr2 = db.getAvgBySensor(
				DBConnector.AvgType.year, 2);
		DataContainer arr3 = db.getAvgBySensor(
				DBConnector.AvgType.year, 3);
		DataContainer arr4 = db.getAvgBySensor(
				DBConnector.AvgType.year, 4);
		DataContainer arr5 = db.getAvgBySensor(
				DBConnector.AvgType.year, 5);
		DataContainer arr6 = db.getAvgBySensor(
				DBConnector.AvgType.year, 6);
		DataContainer arr7 = db.getAvgBySensor(
				DBConnector.AvgType.year, 7);
	
		lg = new LineGraph(this);

		// if I make an Image here it work, in GUI Class it doesn't
		map = new Image(this, "../data/evl_2nd_floor_72pxPin.png", 0, 0);
		map.centerVertically();

		// map.resize(320, 200, true);
		
		cachedData.put(zoomLevel* 10 + 1, lg.addLine(arr1, 1));
	
		cachedData.put(zoomLevel* 10 + 2, lg.addLine(arr2, 2));
		cachedData.put(zoomLevel* 10 + 3, lg.addLine(arr3, 3));
		cachedData.put(zoomLevel* 10 + 4, lg.addLine(arr4, 4));
		cachedData.put(zoomLevel* 10 + 5, lg.addLine(arr5, 5));
		cachedData.put(zoomLevel* 10 + 6, lg.addLine(arr6, 6));
		cachedData.put(zoomLevel* 10 + 7, lg.addLine(arr7, 7));
		
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
			else{
				
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
		if(lg.hasSelection()){
			if(zoomLevel == 0){
				zoomLevel++;
				String s = lg.getSelectionStartYear();
				currYear = s;
				lg.removeAllLines();
				ArrayList<DataContainer> arr = new ArrayList<DataContainer>();
				arr.add(db.getAvgBySensorMonthRange(1,s,s));
				arr.add(db.getAvgBySensorMonthRange(2,s,s));
				arr.add(db.getAvgBySensorMonthRange(3,s,s));
				arr.add(db.getAvgBySensorMonthRange(4,s,s));
				arr.add(db.getAvgBySensorMonthRange(5,s,s));
				arr.add(db.getAvgBySensorMonthRange(6,s,s));
				arr.add(db.getAvgBySensorMonthRange(7,s,s));
				
				int i = 1;
				for (DataContainer dataContainer : arr) {
					if(buttons.isRoomSelected(i) == true){
						Line l = lg.addLine(dataContainer, i);
						cachedData.put(zoomLevel* 10 + i, l);
					}
					else{
						lg.addLine(dataContainer, i);
						Line l = lg.removeLine(i);
						cachedData.put(zoomLevel* 10 + i, l);
						
					}
					i++;
					linesAtZ1++;
				}
			}else
			if(zoomLevel == 1){
				zoomLevel++;
				String s = lg.getSelectionStartMonth();
				lg.removeAllLines();
				ArrayList<DataContainer> arr = new ArrayList<DataContainer>();
				arr.add(db.getAvgBySensorDayRange(1,currYear,s,s));
				arr.add(db.getAvgBySensorDayRange(2,currYear,s,s));
				arr.add(db.getAvgBySensorDayRange(3,currYear,s,s));
				arr.add(db.getAvgBySensorDayRange(4,currYear,s,s));
				arr.add(db.getAvgBySensorDayRange(5,currYear,s,s));
				arr.add(db.getAvgBySensorDayRange(6,currYear,s,s));
				arr.add(db.getAvgBySensorDayRange(7,currYear,s,s));
				
				int i = 1;
				for (DataContainer dataContainer : arr) {
					if(buttons.isRoomSelected(i) == true){
						Line l = lg.addLine(dataContainer, i);
						cachedData.put(zoomLevel* 10 + i, l);
					}
					else{
						lg.addLine(dataContainer, i);
						Line l = lg.removeLine(i);
						cachedData.put(zoomLevel* 10 + i, l);
						
					}
					i++;
					linesAtZ1++;
				}
			}
			lg.loadLineData();
			
		}
		lg.setZoomLevel(zoomLevel);
		buttons.setZoomLevel(zoomLevel);
	}
	
	public void next(int theValue) {
		System.out.println("next");
		for(int i=1;i<linesAtZ1+1;i++){
			DataContainer dc = db.getAvgBySensorMonthOne(i, lg.getNextMonth(), lg.getNextMonthYear());
			Line l = cachedData.get(zoomLevel* 10 + i);
			l.loadData(dc);
			break;
		}
			
	}
	
	public void back(int theValue) {
		roomClicked(theValue);
	}
	
	public void viewTable(boolean val){
		if(val)
			lg.createTable();
		else
			lg.deleteTable();
	}
	
	public void reset(int value){
		zoomLevel = 0;
		lg.removeAllLines();
		lg.resetRowCount();
		for(int i=1;i<8;i++){
			if(buttons.isRoomSelected(i) == true){
				Line l = cachedData.get(zoomLevel * 10 + i);
				lg.addLine(l, i, false);
			}
		}
	}
	

	public static void main(String args[]) {
		RoomTempParser rm = new RoomTempParser();

		// rm.parseData("C:\\Users\\Yuriy\\Downloads\\history.txt\\history.txt");
		PApplet.main(new String[] { "--present", "RoomTemps" });
	}

}
