import java.sql.Array;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DBConnector {
    
    private Connection conn = null;
    private String dbName = "roomtemps";
    
    public static enum AvgType {
        year, month, day
    }
    
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
            System.out.println(e);
            e.printStackTrace();
            return false;
        }
    }
    
    public void createTables(){
        try{
            Statement stat = conn.createStatement();
            stat.executeUpdate("create database if not exists roomtemps;");
            connectToDB(dbName);
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
    
    public Connection getConnection(){
        return conn;
    }
    
    public ArrayList<ArrayList<Float>> getAvg(AvgType t) {
        connectToDB(dbName);
        ArrayList<ArrayList<Float>> arr = new ArrayList<ArrayList<Float>>();
        arr.add(new ArrayList<Float>());
        arr.add(new ArrayList<Float>());
        SimpleDateFormat ts= new SimpleDateFormat();
        switch(t){
        case year:
            ts.applyPattern("yyyy");
            break;
        case month:
            ts.applyPattern("MM");
            break;
        }
        try {
            PreparedStatement prep;
            prep = conn.prepareStatement("SELECT date_time, AVG(temp) as avg FROM hourly_temperatures GROUP BY " + t + "(date_time);");
            ResultSet r = prep.executeQuery();
            while (r.next()) {
                ((ArrayList<Float>) arr.get(0)).add(Float.valueOf(ts.format(r.getDate(1))));
                ((ArrayList<Float>) arr.get(1)).add(r.getFloat(2));
            }
            prep.close();
            r.close();
            closeConnToDB();
            return arr;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeConnToDB();
        return null;

    }
    
    public DataContainer getAvgBySensor(AvgType t, int sensor) {
        connectToDB(dbName);
        ArrayList<ArrayList<Float>> arr = new ArrayList<ArrayList<Float>>();
        DataContainer dc = null;
        arr.add(new ArrayList<Float>());
        arr.add(new ArrayList<Float>());
        SimpleDateFormat ts= new SimpleDateFormat();
        switch(t){
        case year:
            ts.applyPattern("yyyy");
            dc = new DataContainer('Y');
            break;
        case month:
            ts.applyPattern("MM");
            dc = new DataContainer('M');
            break;
        }
        try {
            PreparedStatement prep;
            //SELECT date_time, AVG(temp) as avg FROM roomtemps.hourly_temperatures WHERE sensor_id = 1 GROUP BY sensor_id, year(date_time);
            //prep = conn.prepareStatement("SELECT date_time, AVG(temp) as avg FROM hourly_temperatures WHERE sensor_id =(?) GROUP BY " + t + "(date_time);");
        	prep = conn.prepareStatement("SELECT date_time, AVG(temp) as avg FROM hourly_temperatures WHERE sensor_id = (SELECT id FROM sensors WHERE number = (?)) GROUP BY " + t +"(date_time);");
            prep.setInt(1, sensor);
            ResultSet r = prep.executeQuery();
            while (r.next()) {

                Date date = r.getDate(1);
				String d = ts.format(date);
                Float f = Float.valueOf(d);
                
                
                Float f2 = r.getFloat(2);
                
                dc.add(f2, date);
                
				((ArrayList<Float>) arr.get(0)).add(f);
                ((ArrayList<Float>) arr.get(1)).add(f2);
            }
            prep.close();
            r.close();
            closeConnToDB();
            return dc;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeConnToDB();
        return null;

    }

}
