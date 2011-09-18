import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;


public class LineGraph {
    
    private PApplet parent;
    
    private float plotX1, plotY1;
    private float plotX2, plotY2;
    private float labelX, labelY;
    
    private float dataMin, dataMax;
    
    private int yearInterval = 1;
    private int interval = 1;
    private int intervalMinor = 1;
    
    private int rowCount;
    
    private int[] years;
    private int yearMin, yearMax;
    
    private PFont plotFont;
    
    ArrayList<Line> lines;
    
    private boolean isDataLoaded = false;
    
    public LineGraph(PApplet p){
        parent = p;
        
        lines = new ArrayList<Line>();
        
        plotX1 = 420;
        plotX2 = parent.width - 80;
        labelX = 50;
        plotY1 = 60;
        plotY2 = parent.height - 70;
        labelY = parent.height - 25;

        plotFont = parent.createFont("SansSerif", 20);
        parent.textFont(plotFont);

        parent.smooth();
    }
    
    public void addLine(ArrayList<ArrayList<Float>> a, int roomNum){
    	addLine(a, roomNum, false);
    }
    
    public void addLine(Line l, int roomNum, boolean loadData){
    	lines.add(l);
    	ArrayList<ArrayList<Float>> a = l.getData();
        //FIXME different year ranges
        //this will break if we start adding different year ranges
        //BUT I don't need that right now
        years = Utils.floatArrToIntArr((ArrayList<Float>) a.get(0));
        
        int rows = a.get(0).size();
        int lastIndex = lines.size() - 1;
        int yMN = lines.get(lastIndex).getYearMin();
        int yMX = lines.get(lastIndex).getYearMax();
        float dMin = lines.get(lastIndex).getDataMin();
        float dMax = lines.get(lastIndex).getDataMax();
        
        if(rows > rowCount)
            rowCount = rows;
        
        if(yearMin == 0)
            yearMin = yMN;
        
        if(dataMin == 0)
        	dataMin = dMin;
        	
        if(yMN < yearMin)
            yearMin = yMN;

        if(yMX > yearMax)
            yearMax = yMX;
        
        if(dMin < dataMin)
            dataMin = dMin;

        if(dMax > dataMax)
            dataMax = dMax;
        
        if(loadData)
        	lines.get(lastIndex).loadData();

    }
    public void addLine(ArrayList<ArrayList<Float>> a, int roomNum, boolean loadData){
        addLine(new Line(parent, a, interval, plotX1, plotX2, plotY1, plotY2, roomNum), roomNum, loadData);
    }
    
    public void loadLineData(){
        for (Line l : lines) {
            l.loadData();
        }
        isDataLoaded = true;
    }
    
    public boolean isDataLoaded(){
        return isDataLoaded;
    }

    public void drawChart() {
        parent.background(224);

        // Show the plot area as a white box
        parent.fill(255);
        parent.rectMode(PApplet.CORNERS);
        parent.noStroke();
        parent.rect(plotX1, plotY1, plotX2, plotY2);

        drawAxisLabels();

        updateLines();

        drawYearLabels();
        drawVolumeLabels();

        // parent.noStroke();
        // parent.fill(0xFF5679C1); //with eclipse need to add the 'FF' in the
        // front
        /*parent.stroke(0xFF5679C1);
        parent.strokeWeight(5);
        parent.noFill();*/
        drawLines();
    }

    private void drawAxisLabels() {
        parent.fill(0);
        parent.textSize(13);
        parent.textLeading(15);

        parent.textAlign(PConstants.CENTER, PConstants.CENTER);
        parent.text("Temperature\nin Farenheit", labelX, (plotY1 + plotY2) / 2);
        parent.textAlign(PConstants.CENTER);
        parent.text("Year", (plotX1 + plotX2) / 2, labelY);
    }

    private void drawYearLabels() {
        parent.fill(0);
        parent.textSize(10);
        parent.textAlign(PConstants.CENTER);

        // Use thin, gray lines to draw the grid
        parent.stroke(224);
        parent.strokeWeight(1);

        for (int row = 0; row < rowCount; row++) {
             if (years[row] % yearInterval == 0) {
                float x = PApplet.map(years[row], yearMin, yearMax, plotX1,
                        plotX2);
                parent.text(years[row], x, plotY2 + parent.textAscent() + 10);
                parent.line(x, plotY1, x, plotY2);
            }
        }
    }

    private void drawVolumeLabels() {
        parent.fill(0);
        parent.textSize(10);
        parent.textAlign(PConstants.RIGHT);

        parent.stroke(128);
        parent.strokeWeight(1);

        for (float v = dataMin; v <= dataMax; v += intervalMinor) {
            if (v % intervalMinor == 0) { // If a tick mark
                float y = PApplet.map(v, dataMin, dataMax, plotY2, plotY1);
                if (v % interval == 0) { // If a major tick mark
                    float textOffset = parent.textAscent() / 2; // Center
                                                                // vertically
                    if (v == dataMin) {
                        textOffset = 0; // Align by the bottom
                    } else if (v == dataMax) {
                        textOffset = parent.textAscent(); // Align by the top
                    }
                    parent.text(PApplet.floor(v), plotX1 - 10, y + textOffset);
                    parent.line(plotX1 - 4, y, plotX1, y); // Draw major tick
                } else {
                    // parent.line(plotX1 - 2, y, plotX1, y); // Draw minor tick
                }
            }
        }
    }
    
    private void updateLines(){
        for (Line l : lines) {
            l.updateInterpolators();
        }
    }
    
    private void drawLines(){
        for (Line l : lines) {
            l.drawDataArea(yearMin, yearMax);
        }
    }
    
    public boolean containsRoomNumber(int roomNum){
    	for (Line l : lines) {
			if(l.getRoomNumber() == roomNum)
				return true;
		}
    	return false;
    }
    
    public Line removeLine(int roomNum){
    	for(int i=0; i < lines.size(); ++i){
    		Line l = lines.get(i);
    		if(l.getRoomNumber() == roomNum){
    			lines.remove(i);
    			return l; 
    		}
    	}
		return null;
    }
    
}
