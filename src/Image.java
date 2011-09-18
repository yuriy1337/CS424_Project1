import processing.core.PApplet;
import processing.core.PImage;


public class Image {
	
	private int x;
	private int y;
	private int width;
	private int height;
	private String location;
	
	private PApplet parent;
	private PImage img;
	
	Image(PApplet p, String l, int x, int y){
		parent = p;
		location = l;
		this.x = x;
		this.y = y;
		img = parent.loadImage(l);
	}
	
	
	public void resize(int w, int h, boolean keepRatio){
		
		if(keepRatio){
			if(w>h){
				h = img.height / (img.width/w);
			}
			else{
				w = img.width / (img.height/w);
			}
		}
		System.out.println("resing to w="+ w + " h="+h);
		
		img.resize(w, h);
		
	}
	
	public void centerVertically(){
		y = parent.height/2 - img.height/2;
		System.out.println("setting y to "+ y);
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public int getWidth(){
		return img.width;
	}
	
	public int getHeight(){
		return img.height;
	}
	
	public void setX(int x){
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	public void setXY(int x, int y){
		setX(x);
		setY(y);
	}
	
	void draw() {
	  if (img.width == 0) {
	    // Image is not yet loaded
	  } else if (img.width == -1) {
	    // This means an error occurred during image loading
	  } else {
	    // Image is ready to go, draw it
	    parent.image(img, x, y);
	  }
	}
}
