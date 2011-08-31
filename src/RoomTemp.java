import processing.core.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class RoomTemp extends PApplet {
    
    private boolean DEBUG = true; 
    Connection conn = null;
    
    
    public void setup() {
        size(200, 200);
        background(0);
    }

    public void draw() {
        stroke(255);
        if (mousePressed) {
            line(mouseX, mouseY, pmouseX, pmouseY);
        }
    }

    /*
     * Service methods
     */
    public boolean connectToDB() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:test.db");
            return true;
        } catch (Exception e) {
            if(DEBUG)
                System.out.println(e);
            return false;
        }
    }
    
    public void createTables(){
        try{
            Statement stat = conn.createStatement();
            stat.executeUpdate("drop table if exists sensors;");
            stat.executeUpdate("create table sensors (id, sensor);");
            stat.executeUpdate("drop table if exists hourly_temperatures;");
            stat.executeUpdate("create table hourly_temperatures (id, date_time, sensor_id, temp);");
            stat.executeUpdate("drop table if exists daily_temperatures;");
            stat.executeUpdate("create table daily_temperatures (id, date, sensor_id, temp);");
            stat.executeUpdate("drop table if exists monthly_temperatures;");
            stat.executeUpdate("create table monthly_temperatures (id, month, sensor_id, temp);");
            stat.executeUpdate("drop table if exists yearly_temperatures;");
            stat.executeUpdate("create table yearly_temperatures (id, year, sensor_id, temp);");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void closeConnToDB(){
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void parseData(String filename){
        connectToDB();
        
        //variables
        String date = "", sensor = "", token = "";
        int temp = 0, row = 0, col = 0;
        ArrayList<Integer> sensors = new ArrayList<Integer>();
        ArrayList<Integer> daily_avg = new ArrayList<Integer>();
        ArrayList<Integer> monthly_avg = new ArrayList<Integer>();
        ArrayList<Integer> yearly_avg = new ArrayList<Integer>();
        
        
        try{
            File file = new File(filename);
            BufferedReader bufRdr  = new BufferedReader(new FileReader(file));
            String line = null;
            
            DateFormat df = DateFormat.getDateInstance();
             
            //read each line of text file
            while((line = bufRdr.readLine()) != null)
            {
                StringTokenizer st = new StringTokenizer(line,",");
                while (st.hasMoreTokens())
                {
                    token = st.nextToken();
                    if(col < 3){
                        date += " " + token;
                    }
                    else
                    if(col == 3){
                        sensor = token;
                    }
                    else
                    if(col == 4){
                        temp = Integer.parseInt(token);
                    }
                    col++;
                }
                Date d = df.parse(date);
                System.out.println(date + " " + sensor + " " + temp);
                row++;
                date = "";
                col = 0;
            }
             
            //close the file
            bufRdr.close();
        }
        catch (Exception e) {
            if(DEBUG)
                System.out.println(e);
        }
        closeConnToDB();
    }
    
    public static void main(String args[]) {
        RoomTemp rm = new RoomTemp();
        
        rm.parseData("C:\\Users\\Yuriy\\Downloads\\history.txt\\history_small.txt");
        //PApplet.main(new String[] { "--present", "RoomTemp" });
      }
}
