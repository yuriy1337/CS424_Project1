import java.text.DecimalFormat;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PConstants;


public class Table {
	
	PApplet parent;
	ArrayList<Line> lines;
	float plotX1, plotX2, plotY1, plotY2;
	
	public Table(PApplet p, ArrayList<Line> l, float x1, float x2, float y1, float y2){
		parent = p;
		lines = l;
		plotX1 = x1;
		plotX2 = x2;
		plotY1 = y1;
		plotY2 = y2;
		plotY2 += 50;
	}

	public void draw(){
        parent.background(224);

        // Show the plot area as a white box
        parent.fill(255);
        parent.rectMode(PApplet.CORNERS);
        parent.noStroke();
        parent.rect(plotX1, plotY1, plotX2, plotY2);
        
        parent.fill(0);
        parent.textSize(13);
        parent.textLeading(15);

        parent.textAlign(PConstants.LEFT, PConstants.RIGHT);
        parent.text("Temp", (plotY1 + plotY2) / 2, plotX1);
        
        int xOffset = 20;
        int yOffset = 80;
        int yLineOffset = 62;
        int j = 1;
        for (Line l : lines) {        
        	parent.stroke(224);
        	parent.strokeWeight(1);
        	parent.text("Room " +l.getRoomNumber(), plotX1 + xOffset + 30, plotY1 +yOffset - 50 );
        	
         	DataContainer d = l.getData();
         	parent.text(d.getType(), plotX1 + xOffset, plotY1 + yOffset - 20);
         	parent.text("Temp", plotX1 + 80+ xOffset, plotY1 + yOffset - 20);
        	for(int i=0; i<d.size(); i++){
        		float date = d.getDatesArr().get(i);
        		String s = d.getStringDateFromFloat(date);
        		float temp = d.getTempsArr().get(i);
        		DecimalFormat df = new DecimalFormat("##.#");
        		df.setMinimumFractionDigits(1);
        		String roundedTemp = df.format(temp);
        		
        		parent.text(s, plotX1 + xOffset, yOffset + plotY1 + i*20 );
        		parent.line(plotX1, plotY1+yLineOffset,plotX2, plotY1+yLineOffset);
        		parent.text(roundedTemp, plotX1 + xOffset + 80, yOffset + plotY1 + i*20 );
        		parent.line(plotX1, plotY1+yLineOffset + 20 ,plotX2, plotY1+yLineOffset + 20 );
        		yLineOffset+=20;
        	}
        	yLineOffset = 62;
        	xOffset += 200;
        	if(j % 3 == 0){
        		xOffset = 20;
        		yLineOffset+=(j/3) * (d.size() * 27 + 10);
        		yOffset += d.size() * 27 + 10;
        	}
        	j++;
		}
	}
}
