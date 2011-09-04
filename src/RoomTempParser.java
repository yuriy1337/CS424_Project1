import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

public class RoomTempParser {
    
    private boolean DEBUG = true; 
    Connection conn = null;
    public boolean connectToDB() {
        return connectToDB("");
    }
    
    public boolean connectToDB(String db) {
        try {
            //Class.forName("org.sqlite.JDBC");
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/"+db, "user", "password");
            return true;
        } catch (Exception e) {
            if(DEBUG){
                System.out.println(e);
                e.printStackTrace();
            }
            return false;
        }
    }
    
    public void createTables(){
        try{
            Statement stat = conn.createStatement();
            stat.executeUpdate("create database if not exists roomtemps;");
            connectToDB("roomtemps");
            stat = conn.createStatement();
            stat.executeUpdate("drop table if exists sensors;");
            stat.executeUpdate("create table sensors (id INT NOT NULL AUTO_INCREMENT, sensor VARCHAR(255), PRIMARY KEY(id));");
            stat.executeUpdate("drop table if exists hourly_temperatures;");
            stat.executeUpdate("create table hourly_temperatures (id INT NOT NULL AUTO_INCREMENT, date_time DATETIME NOT NULL, sensor_id INT NOT NULL, temp VARCHAR(255) NOT NULL, PRIMARY KEY(id));");
            /*stat.executeUpdate("drop table if exists daily_temperatures;");
            stat.executeUpdate("create table daily_temperatures (id, date, sensor_id, temp);");
            stat.executeUpdate("drop table if exists monthly_temperatures;");
            stat.executeUpdate("create table monthly_temperatures (id, month, sensor_id, temp);");
            stat.executeUpdate("drop table if exists yearly_temperatures;");
            stat.executeUpdate("create table yearly_temperatures (id, year, sensor_id, temp);");*/
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
        createTables();
        
        //variables
        String date = "", sensor = "", token = "";
        int temp = 0, row = 0, col = 0, id = 0;
        ArrayList<String> sensors = new ArrayList<String>();
        ArrayList<Integer> daily_avg = new ArrayList<Integer>(Arrays.asList(0,0,0,0,0,0));
        ArrayList<Integer> monthly_avg = new ArrayList<Integer>(Arrays.asList(0,0,0,0,0,0));
        ArrayList<Integer> yearly_avg = new ArrayList<Integer>(Arrays.asList(0,0,0,0,0,0));
        //Date d = new Date();
        //Date pd = new Date(); //previous date
        
        try{
            File file = new File(filename);
            BufferedReader bufRdr  = new BufferedReader(new FileReader(file));
            String line = null;
            
            SimpleDateFormat ts= new SimpleDateFormat("MMM/dd/yyyy hh:mm a"); 
            SimpleDateFormat sql_date= new SimpleDateFormat("yyyy-MM-dd HH:mm");
             
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
                if (!sensors.contains(sensor)) {
                    sensors.add(sensor);
                    PreparedStatement prep = conn.prepareStatement("insert into sensors(sensor) values (?);");
                    prep.setString(1, sensor);
                    prep.executeUpdate();
                }
                int index = sensors.indexOf(sensor);
                //daily_avg.set(index, daily_avg.get(index)+temp);
                date = date.trim();
                Date parsed_date = ts.parse(date);
                String sql_date_string = sql_date.format(parsed_date);

                PreparedStatement prep = conn.prepareStatement("SELECT id FROM sensors WHERE sensor = (?);");
                prep.setString(1, sensor);
                ResultSet r = prep.executeQuery();
                while ( r.next() ) { 
                    // Read the next item
                     id = r.getInt("id");
                 }
                
                prep = conn.prepareStatement("insert into hourly_temperatures(date_time, sensor_id, temp) values (?, ?, ?);");
                prep.setString(1, sql_date_string);
                prep.setInt(2, id);
                prep.setInt(3, temp);
                prep.executeUpdate();
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
}
