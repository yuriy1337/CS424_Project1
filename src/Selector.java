import processing.core.PApplet;


public class Selector {

	PApplet parent;
	float plotX1;
	float plotX2;
	float plotY1;
	float plotY2;
	float xRange;
	float sectionWidth;
	
	int numOfSections = 6;
	
	public Selector(PApplet p, float plotX1, float plotX2, float plotY1, float plotY2){
		this.plotX1 = plotX1;
		this.plotX2 = plotX2;
		this.plotY1 = plotY1;
		this.plotY2 = plotY2;
		parent = p;
		xRange = plotX2 - plotX1;
		sectionWidth = xRange / numOfSections;
	}
	
	public void draw(){
		
		int x = parent.mouseX;
		int y = parent.mouseY;
		
		if(x < plotX1 || x > plotX2 || y < plotY1 || y > plotY2)
			return; //we are outside the plot
		
		float sectionEnd = (float) Math.ceil((x - plotX1)/sectionWidth);
		float sectionStart= (float) Math.floor((x - plotX1)/sectionWidth);
		
		parent.fill(0xAABBBBBB);
		parent.rect(plotX1 + sectionStart * sectionWidth, plotY1, plotX1 + sectionEnd * sectionWidth, plotY2);
	}
	
	
}
