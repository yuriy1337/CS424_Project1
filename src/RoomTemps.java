
import java.util.ArrayList;

import processing.core.*;
import org.gicentre.*;
import org.gicentre.utils.stat.*;


public class RoomTemps extends PApplet {
    
    DBConnector db = new DBConnector();
    XYChart lineChart;
    Image map;
    LineGraph l;
    
    public void setup()
    {
        size(1024, 768);
        ArrayList<ArrayList<Float>> arr = db.getAvg(DBConnector.AvgType.year);
        ArrayList<ArrayList<Float>> arr1 = db.getAvgBySensor(DBConnector.AvgType.year, 1);
        ArrayList<ArrayList<Float>> arr2 = db.getAvgBySensor(DBConnector.AvgType.year, 2);
        l = new LineGraph(this);
        map = new Image(this, "../data/evl_2nd_floor.png", 0, 0);
        map.resize(320, 200, true);
        map.centerVertically();
        l.addLine(arr1);
        l.addLine(arr2);
        
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
        map.draw();
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
        PApplet.main(new String[] { "--present", "RoomTemps" });
      }

}
