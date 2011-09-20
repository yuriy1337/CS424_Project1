import processing.core.PApplet;


public class Selector {

	private PApplet parent;
	private float plotX1;
	private float plotX2;
	private float plotY1;
	private float plotY2;
	private float xRange;
	private float sectionWidth;
	private float sectionStart; 
	private float sectionEnd;
	private boolean hasSelection = false;
	
	private int numOfSections;
	
	public Selector(PApplet p, float plotX1, float plotX2, float plotY1, float plotY2){
		this.plotX1 = plotX1;
		this.plotX2 = plotX2;
		this.plotY1 = plotY1;
		this.plotY2 = plotY2;
		parent = p;
		xRange = plotX2 - plotX1;
		
	}
	
	public void draw(){
		
		int x = parent.mouseX;
		int y = parent.mouseY;
		
		hasSelection = false;
		
		if(x < plotX1 || x > plotX2 || y < plotY1 || y > plotY2)
			return; //we are outside the plot
		
		hasSelection = true;
		
		sectionWidth = xRange / numOfSections;
		
		sectionStart = (float) Math.floor((x - plotX1)/sectionWidth);
		sectionEnd = (float) Math.ceil((x - plotX1)/sectionWidth);
		
		parent.fill(0xAABBBBBB); //i love transparencies 
		parent.rect(plotX1 + sectionStart * sectionWidth, plotY1, plotX1 + sectionEnd * sectionWidth, plotY2);
	}
	
	public float getStart(){
		return sectionStart;
	}
	
	public boolean hasSelection(){
		return hasSelection;
	}
	
	public void setNumOfSelections(int num){
		numOfSections = num;
	}
	
}
