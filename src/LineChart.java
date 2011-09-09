import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;

public class LineChart {

    private PApplet parent;

    private ArrayList data;

    private float dataMin, dataMax;

    private float plotX1, plotY1;
    private float plotX2, plotY2;
    private float labelX, labelY;

    private int rowCount;
    private int columnCount;
    private int currentColumn = 0;

    private int yearMin, yearMax;
    private int[] years;

    // these have to be renamed
    private int yearInterval = 1;
    private int volumeInterval = 1;
    private int volumeIntervalMinor = 1;

    private float[] tabLeft, tabRight;
    private float tabTop, tabBottom;
    private float tabPad = 10;

    private Integrator[] interpolatorsX, interpolatorsY;

    private PFont plotFont;
    
    public boolean isDataLoaded = false;

    public LineChart(PApplet p, ArrayList d) {
        parent = p;
        data = d; // shallow copy, it is okay here?

        dataMin = PApplet.floor(Utils.getArrayListMin((ArrayList<Float>) data
                .get(1)) / volumeInterval)
                * volumeInterval;
        dataMax = PApplet.ceil(Utils.getArrayListMax((ArrayList<Float>) data
                .get(1)) / volumeInterval)
                * volumeInterval;

        //rowCount = ((ArrayList<Float>) data.get(0)).size();
        columnCount = data.size();

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

        years = Utils.floatArrToIntArr((ArrayList<Float>) data.get(0));
        yearMin = Math.round(Utils.getArrayListMin((ArrayList<Float>) data
                .get(0)));
        yearMax = Math.round(Utils.getArrayListMax((ArrayList<Float>) data
                .get(0)));

        plotX1 = 120;
        plotX2 = parent.width - 80;
        labelX = 50;
        plotY1 = 60;
        plotY2 = parent.height - 70;
        labelY = parent.height - 25;

        plotFont = parent.createFont("SansSerif", 20);
        parent.textFont(plotFont);

        parent.smooth();

    }

    void drawChart() {
        parent.background(224);

        // Show the plot area as a white box
        parent.fill(255);
        parent.rectMode(PApplet.CORNERS);
        parent.noStroke();
        parent.rect(plotX1, plotY1, plotX2, plotY2);

        drawAxisLabels();

        for (int row = 0; row < rowCount; row++) {
            if (interpolatorsX != null && interpolatorsY != null) {
                interpolatorsY[row].update();
                interpolatorsX[row].update();
            }
        }

        drawYearLabels();
        drawVolumeLabels();

        // parent.noStroke();
        // parent.fill(0xFF5679C1); //with eclipse need to add the 'FF' in the
        // front
        parent.stroke(0xFF5679C1);
        parent.strokeWeight(5);
        parent.noFill();
        drawDataArea(currentColumn);
    }

    void drawAxisLabels() {
        parent.fill(0);
        parent.textSize(13);
        parent.textLeading(15);

        parent.textAlign(PConstants.CENTER, PConstants.CENTER);
        parent.text("Temperature\nin Farenheit", labelX, (plotY1 + plotY2) / 2);
        parent.textAlign(PConstants.CENTER);
        parent.text("Year", (plotX1 + plotX2) / 2, labelY);
    }

    void drawYearLabels() {
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

    void drawVolumeLabels() {
        parent.fill(0);
        parent.textSize(10);
        parent.textAlign(PConstants.RIGHT);

        parent.stroke(128);
        parent.strokeWeight(1);

        for (float v = dataMin; v <= dataMax; v += volumeIntervalMinor) {
            if (v % volumeIntervalMinor == 0) { // If a tick mark
                float y = PApplet.map(v, dataMin, dataMax, plotY2, plotY1);
                if (v % volumeInterval == 0) { // If a major tick mark
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

    void drawDataArea(int col) {
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
    
    void onMouseOver(){
    }
}
