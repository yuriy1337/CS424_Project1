import java.util.ArrayList;

import processing.core.PApplet;


public class ImageMap {
	
	private ArrayList<ArrayList<Image>> map;
	
	private PApplet parent;
	
	ImageMap(PApplet p){
		parent = p;
		map = new ArrayList<ArrayList<Image>>();
	}
	
	void addImage(Image i, int posX, int posY){
		ArrayList<Image> xArr = map.get(posX);
		if(xArr == null){
			xArr = new ArrayList<Image>();
			map.add(posX, xArr);
		}
		xArr.add(posY, i);
	}
	
	

}
