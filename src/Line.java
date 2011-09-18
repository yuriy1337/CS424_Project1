import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;

public class Line{

    private PApplet parent;

    private DataContainer data;

    private float dataMin, dataMax;

    private float plotX1, plotY1;
    private float plotX2, plotY2;
    
    private int xMin, xMax;

    private int roomNumber;
    
    private int rowCount;
    private int interval;
    
    private char localization;

    private Integrator[] interpolatorsX, interpolatorsY;

    public Line(PApplet p, DataContainer d, int interval, float pX1, float pX2, float pY1, float pY2, int roomNum, char loc) {
        parent = p;
        data = d;
        plotX1 = pX1;
        plotX2 = pX2;
        plotY1 = pY1;
        plotY2 = pY2;

        roomNumber = roomNum;
        
        this.interval = interval;
        
        localization = loc;
        
        dataMin = PApplet.floor(Utils.getArrayListMin(data.getTempsArr()) / interval)* interval;
        dataMax = PApplet.ceil(Utils.getArrayListMax(data.getTempsArr()) / interval)* interval;
        
        xMin = getXMin();
        xMax = getXMax();

        //interpolatorsY = new Integrator[rowCount];
        /*
         * for (int row = 0; row < rowCount; row++) { float initialValue =
         * (Float) ((ArrayList<Float>)(data.get(1))).get(row);
         * interpolatorsY[row] = new Integrator(parent, initialValue);
         * interpolatorsY[row].attraction = (float) 0.1; // Set lower than the
         * default }
         * 
         * interpolatorsX = new Integrator[rowCount]; for (int row = 0; row <
         * rowCount; row++) { float initialValue = (Float)
         * ((ArrayList<Float>)(data.get(0))).get(row); interpolatorsX[row] = new
         * Integrator(parent, initialValue); interpolatorsX[row].attraction =
         * (float) 0.1; // Set lower than the default }
         */

    }

    public void updateInterpolators(){
        for (int row = 0; row < rowCount; row++) {
            if (interpolatorsX != null && interpolatorsY != null) {
                interpolatorsY[row].update();
                interpolatorsX[row].update();
            }
        }
    }

    void drawDataArea(int yearMin, int yearMax, char localization) {
    	if(this.localization != localization){
    		this.localization = localization;
    		toggleLocalization();
    	}
    	parent.stroke(GUI.getRoomColor(roomNumber));
        parent.strokeWeight(5);
        parent.noFill();
        parent.beginShape();
        for (int row = 0; row < rowCount; row++) {
            // TODO FIXME
            if (interpolatorsX != null && interpolatorsY != null) {
                float valueY = interpolatorsY[row].value;
                float valueX = interpolatorsX[row].value;
                float x = PApplet.map(valueX, yearMin, yearMax, plotX1, plotX2);
                float y = PApplet.map(valueY, dataMin, dataMax, plotY2, plotY1);
                parent.vertex(x, y);
            }
        }
        // for fill:
        // parent.vertex(plotX2, plotY2);
        // parent.vertex(plotX1, plotY2);
        // parent.endShape(PConstants.CLOSE);
        parent.endShape();
    }

    void addPoint(Float year, Float temp) {

        if (rowCount == 0) {
            interpolatorsX = new Integrator[rowCount];
            interpolatorsY = new Integrator[rowCount];
            interpolatorsX = Utils.AppendItemToArray(interpolatorsX,
                    new Integrator(parent, year));
            interpolatorsX[rowCount].attraction = (float) 0.1; // Set lower than
                                                               // the default

            interpolatorsY = Utils.AppendItemToArray(interpolatorsY,
                    new Integrator(parent, temp));
            interpolatorsY[rowCount].attraction = (float) 0.1; // Set lower than
                                                               // the default
            interpolatorsX[rowCount].target(year);
            interpolatorsY[rowCount].target(temp);
            rowCount++;
        } else {
            interpolatorsX = Utils.AppendItemToArray(interpolatorsX,
                    new Integrator(parent, interpolatorsX[rowCount-1].value));
            interpolatorsX[rowCount-1].attraction = (float) 0.1; // Set lower than
                                                               // the default

            interpolatorsY = Utils.AppendItemToArray(interpolatorsY,
                    new Integrator(parent, interpolatorsY[rowCount-1].value));
            interpolatorsY[rowCount-1].attraction = (float) 0.1; // Set lower than
                                                               // the default

            

            interpolatorsX[rowCount].target(year);
            interpolatorsY[rowCount].target(temp);
            
            rowCount++;
        }

        /*
         * ArrayList l = ((ArrayList<Float>) data.get(1)).add((float) 72.5);
         * interpolatorsX = Utils.AppendItemToArray(interpolatorsX, new
         * Integrator(parent, (float)2011)); interpolatorsX[7].attraction =
         * (float) 0.1; // Set lower than the default
         * 
         * interpolatorsY = Utils.AppendItemToArray(interpolatorsY, new
         * Integrator(parent, interpolatorsY[6].value));
         * interpolatorsY[7].attraction = (float) 0.1; // Set lower than the
         * default interpolatorsX[7].target(2012);
         * interpolatorsY[7].target((float) 72.5);
         */

    }

    void loadData() {
        int length = data.size();
        ArrayList<Float> xValues = data.getDatesArr();
        ArrayList<Float> temps = data.getTempsArr();
        for (int row = 0; row < length; row++) {
            float date = xValues.get(row);
            float temp = temps.get(row);
            addPoint(date, temp);
        }
    }
    
    public int getXMin(){
        return Math.round(Utils.getArrayListMin(data.getDatesArr()));

    }
    
    public int getXMax(){
        return Math.round(Utils.getArrayListMax(data.getDatesArr()));
    }
    
    public float getDataMin(){
        return Math.round(Utils.getArrayListMin(data.getTempsArr()));

    }
    
    public float getDataMax(){
        return Math.round(Utils.getArrayListMax(data.getTempsArr()));

    }
    
    public int getRoomNumber(){
    	return roomNumber;
    }
    
    public DataContainer getData(){
    	return data;
    }
    
    private void toggleLocalization(){
    	
    	data.toggleLocalization();
    	
    	for (int i = 0; i < data.size(); i++) {
			interpolatorsY[i].target(data.getTempsArr().get(i));
		}
    	
    	
        dataMin = PApplet.floor(Utils.getArrayListMin(data.getTempsArr()) / interval)* interval;
        dataMax = PApplet.ceil(Utils.getArrayListMax(data.getTempsArr()) / interval)* interval;
    }
    
}
