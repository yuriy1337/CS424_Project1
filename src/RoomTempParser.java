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
    
    DBConnector db = new DBConnector();
    
    public void parseData(String filename){
        if(DBConnector.localDB)
            db.connectToDB();
        else
            db.connectToDB("a1124959_rt");
        db.createTables();
        
        //variables
        String date = "", sensor = "", token = "";
        int temp = 0, row = 0, col = 0, id = 0;
        ArrayList<String> sensors = new ArrayList<String>();
        //ArrayList<Integer> daily_avg = new ArrayList<Integer>(Arrays.asList(0,0,0,0,0,0));
        //ArrayList<Integer> monthly_avg = new ArrayList<Integer>(Arrays.asList(0,0,0,0,0,0));
        //ArrayList<Integer> yearly_avg = new ArrayList<Integer>(Arrays.asList(0,0,0,0,0,0));
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
                    PreparedStatement prep = db.getConnection().prepareStatement("insert into sensors(sensor) values (?);");
                    prep.setString(1, sensor);
                    prep.executeUpdate();
                    prep.close();
                }
                date = date.trim();
                Date parsed_date = ts.parse(date);
                String sql_date_string = sql_date.format(parsed_date);

                PreparedStatement prep = db.getConnection().prepareStatement("SELECT id FROM sensors WHERE sensor = (?);");
                prep.setString(1, sensor);
                ResultSet r = prep.executeQuery();
                while ( r.next() ) { 
                    // Read the next item
                     id = r.getInt("id");
                 }
                prep.close();
                r.close();
                
                prep = db.getConnection().prepareStatement("insert into hourly_temperatures(date_time, sensor_id, temp) values (?, ?, ?);");
                prep.setString(1, sql_date_string);
                prep.setInt(2, id);
                prep.setInt(3, temp);
                prep.executeUpdate();
                prep.close();
                if(row % 1000 ==0)
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
        db.closeConnToDB();
    }
}
