import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;

public class Line{

    private PApplet parent;

    private ArrayList data;

    private float dataMin, dataMax;

    private float plotX1, plotY1;
    private float plotX2, plotY2;
    
    private int yearMin, yearMax;


    private int rowCount;

    private Integrator[] interpolatorsX, interpolatorsY;

    public Line(PApplet p, ArrayList d, int interval, float pX1, float pX2, float pY1, float pY2) {
        parent = p;
        data = d;
        plotX1 = pX1;
        plotX2 = pX2;
        plotY1 = pY1;
        plotY2 = pY2;

        dataMin = PApplet.floor(Utils.getArrayListMin((ArrayList<Float>) data
                .get(1)) / interval)
                * interval;
        dataMax = PApplet.ceil(Utils.getArrayListMax((ArrayList<Float>) data
                .get(1)) / interval)
                * interval;
        
        yearMin = getYearMin();
        yearMax = getYearMax();

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

    void drawDataArea(int yearMin, int yearMax) {
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
        int length = ((ArrayList)data.get(0)).size();
        for (int row = 0; row < length; row++) {
            float year = (Float) ((ArrayList<Float>) (data.get(0))).get(row);
            float temp = (Float) ((ArrayList<Float>) (data.get(1))).get(row);
            addPoint(year, temp);
        }
    }
    
    public int getYearMin(){
        return Math.round(Utils.getArrayListMin((ArrayList<Float>) data
                .get(0)));

    }
    
    public int getYearMax(){
        return Math.round(Utils.getArrayListMax((ArrayList<Float>) data
                .get(0)));
    }
    
    public float getDataMin(){
        return Math.round(Utils.getArrayListMin((ArrayList<Float>) data
                .get(1)));

    }
    
    public float getDataMax(){
        return Math.round(Utils.getArrayListMax((ArrayList<Float>) data
                .get(1)));

    }
}
