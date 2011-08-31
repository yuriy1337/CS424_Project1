import processing.core.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
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
            stat.executeUpdate("drop table if exists daily_temperatures;");
            stat.executeUpdate("create table daily_temperatures (id, date, time, sensor_id, temp);");
            stat.executeUpdate("drop table if exists monthly_temperatures;");
            stat.executeUpdate("create table monthly_temperatures (id, date, time, sensor_id, temp);");
            stat.executeUpdate("drop table if exists yearly_temperatures;");
            stat.executeUpdate("create table yearly_temperatures (id, date, time, sensor_id, temp);");
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
        try{
            File file = new File(filename);
            BufferedReader bufRdr  = new BufferedReader(new FileReader(file));
            String line = null;
            int row = 0;
            int col = 0;
            String[][] numbers = new String[100][100];
             
            //read each line of text file
            while((line = bufRdr.readLine()) != null)
            {
                StringTokenizer st = new StringTokenizer(line,",");
                while (st.hasMoreTokens())
                {
                    //get next token and store it in the array
                    numbers[row][col] = st.nextToken();
                    col++;
                }
                row++;
            }
             
            //close the file
            bufRdr.close();
        }
        catch (Exception e) {
            if(DEBUG)
                System.out.println(e);
        }
    }
}
