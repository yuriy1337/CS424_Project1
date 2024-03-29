import java.awt.Color;
import java.util.ArrayList;

import javax.swing.colorchooser.ColorSelectionModel;

import controlP5.CColor;
import controlP5.ControlP5;
import processing.core.PApplet;

public class GUI {

	Image map;

	PApplet parent;

	ControlP5 controlP5;
	CColor c1, c2, c3, c4, c5, c6, c7;
	private static ArrayList<CColor> colors;
	ArrayList<controlP5.Button> rooms;
	controlP5.Button next;
	controlP5.Button back;
	private int zoomLevel = 0;
	

	public GUI(PApplet p) {
		parent = p;

		controlP5 = new ControlP5(parent);

		// dont know why but it doesn't work here
		// map = new Image(p, "../data/evl_2nd_floor_72pxPin.png", 0, 0);
		initColors();
		createRoomButtons();
		createTempChngrBttn();
		createNextBackButtons();
		createTableButton();
		createResetButton();
	}

	private void createResetButton() {
		controlP5.Button reset = controlP5.addButton("reset", 1, parent.width - 250, 10, 35,
				12);
	}

	private void createTableButton() {
		  //controlP5.addToggle("viewTable",false,parent.width - 175,10,50,15).setMode(ControlP5.SWITCH);
		  controlP5.Toggle t = controlP5.addToggle("viewTable",false,parent.width - 175,10,50,15);
		  t.setMode(ControlP5.SWITCH);
		  t.setColorCaptionLabel(0xFF000000);
	}

	private void createNextBackButtons() {
		next = controlP5.addButton("next", 1, 420, 40, 36,
				12);
		
		back = controlP5.addButton("back", 1, parent.width - 75, 40, 36,
				12);
		
		next.setVisible(false);
		back.setVisible(false);
		
	}

	private void createTempChngrBttn() {
		controlP5.Button temperatureButton = controlP5.addButton("Change_Locale", 1, parent.width - 100, 10, 72,
				12);
	}

	private void initColors() {
		
		colors = new ArrayList<CColor>();
		c1 = new CColor();
		c1.setForeground(0xAAD73027);
		c1.setBackground(0xFFD73027);
		colors.add(c1);

		c2 = new CColor();
		c2.setForeground(0xAAFC8D59);
		c2.setBackground(0xFFFC8D59);
		colors.add(c2);

		c3 = new CColor();
		c3.setForeground(0xAAFEE090);
		c3.setBackground(0xFFFEE090);
		colors.add(c3);
		
		c4 = new CColor();
		c4.setForeground(0xAAFFFFBF);
		c4.setBackground(0xFFFFFFBF);
		colors.add(c4);
		
		c5 = new CColor();
		c5.setForeground(0xAAE0F3F8);
		c5.setBackground(0xFFE0F3F8);
		colors.add(c5);
		
		c6 = new CColor();
		c6.setForeground(0xAA91BFDB);
		c6.setBackground(0xFF91BFDB);
		colors.add(c6);
		
		c7 = new CColor();
		c7.setForeground(0xAA4575B4);
		c7.setBackground(0xFF4575B4);
		colors.add(c7);
	}

	private void createRoomButtons() {
		rooms = new ArrayList<controlP5.Button>();
		
		controlP5.Button room1 = controlP5.addButton("room1", 1, 3, 248, 123,
				208);
		room1.setColor(c1);
		rooms.add(room1);

		controlP5.Button room2 = controlP5.addButton("room2", 2, 21, 45, 178,
				201);
		room2.setColor(c2);
		rooms.add(room2);
		
		controlP5.Button room3 = controlP5.addButton("room3", 3, 202, 45, 91,
				201);
		room3.setColor(c3);
		rooms.add(room3);
		
		controlP5.Button room4 = controlP5.addButton("room4", 4, 171, 248, 122,
				105);
		room4.setColor(c4);
		rooms.add(room4);
		
		controlP5.Button room5 = controlP5.addButton("room5", 5, 171, 356, 122,
				98);
		room5.setColor(c5);
		rooms.add(room5);
		
		controlP5.Button room6 = controlP5.addButton("room6", 6, 171, 458, 122,
				208);
		room6.setColor(c6);
		rooms.add(room6);
		
		controlP5.Button room7 = controlP5.addButton("room7", 7, 3, 458, 123,
				208);
		room7.setColor(c7);
		rooms.add(room7);
	}
	
	public void toogleRoomColor(int roomNumber){
		controlP5.Button r = rooms.get(roomNumber - 1);
		CColor c = r.getColor();
		int b = c.getBackground();
		int f = c.getForeground();
		c.setBackground(f);
		c.setForeground(b);
		r.setColor(c);
	}

	public void drawGUI() {
		if (map != null)
			map.draw();
	}
	
	public static int getRoomColor(int room){
		int c1 = colors.get(room-1).getBackground();
		int c2 = colors.get(room-1).getForeground();
		return c1 > c2 ? c1 : c2;
	}
	
	public boolean isRoomSelected(int room){
		CColor c = rooms.get(room-1).getColor();
		if(c.getBackground() > 0xFF000000)
			return true;
		else
			return false;
	}
	
	public void setZoomLevel(int zoom){
		zoomLevel = zoom;
		if(zoomLevel > 0){
			next.setVisible(true);
			back.setVisible(true);
		}
	}
}

