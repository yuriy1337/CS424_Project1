
import java.util.ArrayList;

import processing.core.*;
import org.gicentre.*;
import org.gicentre.utils.stat.*;


public class RoomTemps extends PApplet {
    
    DBConnector db = new DBConnector();
    XYChart lineChart;
    LineGraph l;
    
    public void setup()
    {
      /*size(500,200);
      smooth(); 
      noLoop();
      
      PFont font = createFont("Helvetica",11);
      textFont(font,10);

      // Both x and y data set here.  
      lineChart = new XYChart(this);
      
      ArrayList<ArrayList<Float>> arr = db.getAvg(DBConnector.AvgType.month);
      
      ArrayList<Float> floatList1 = arr.get(0);
      ArrayList<Float> floatList2 = arr.get(1);
      float[] floatArray1 = new float[floatList1.size()];
      float[] floatArray2 = new float[floatList2.size()];

      for (int i = 0; i < floatList1.size(); i++) {
          Float f1 = floatList1.get(i);
          Float f2 = floatList2.get(i);
          floatArray1[i] = (f1 != null ? f1 : Float.NaN);
          floatArray2[i] = (f2 != null ? f2 : Float.NaN);
      }
      
      lineChart.setData(floatArray1, floatArray2);
      
      // Axis formatting and labels.
      lineChart.showXAxis(true); 
      lineChart.showYAxis(true); 
      lineChart.setMinX(1);
      lineChart.setMinY(60);
      lineChart.setMaxY(80);
        
      lineChart.setYFormat("##F");  // Monetary value in $US
      lineChart.setXFormat("0");      // Year
      
      // Symbol colours
      lineChart.setPointColour(color(180,50,50,100));
      lineChart.setPointSize(5);
      lineChart.setLineWidth(2);*/
        size(720, 405);
        ArrayList<ArrayList<Float>> arr = db.getAvg(DBConnector.AvgType.year);
        ArrayList<ArrayList<Float>> arr1 = db.getAvgBySensor(DBConnector.AvgType.year, 1);
        ArrayList<ArrayList<Float>> arr2 = db.getAvgBySensor(DBConnector.AvgType.year, 2);
        l = new LineGraph(this);
        //l.addLine(arr1);
        l.addLine(arr);
    }

    public void draw()
    {
      /*background(255);
      textSize(9);
      lineChart.draw(15,15,width-30,height-30);
      
      // Draw a title over the top of the chart.
      fill(120);
      textSize(20);
      text("Income per person, United Kingdom", 70,30);
      textSize(11);
      text("Gross domestic product measured in inflation-corrected $US", 70,45);
      */
        l.drawChart();
        if(!l.isDataLoaded()){
            l.loadLineData();
        }
        
    }
    
    public void mouseClicked() {
        //l.loadData();
    }
    
    public static void main(String args[]) {
        RoomTempParser rm = new RoomTempParser();
        
        //rm.parseData("C:\\Users\\Yuriy\\Downloads\\history.txt\\history.txt");
        //PApplet.main(new String[] { "--present", "RoomTemps" });
      }

}
