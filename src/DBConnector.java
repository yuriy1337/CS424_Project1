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

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;

public class DBConnector extends Thread{
    
    private Connection conn = null;
    private String dbName = "roomtemps";
    private boolean connected = false;
    private PoolingDataSource dataSource = null;
    
    public DBConnector() {
    	 try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	dataSource = setupDataSource("jdbc:mysql://localhost/"+dbName, "user", "password");
    	try {
			conn = dataSource.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    public static enum AvgType {
        year, month, day
    }
    
    public boolean connectToDB() {
        return connectToDB("");
    }
    
    public boolean connectToDB(String db) {
        try {
            //Class.forName("org.sqlite.JDBC");
           
            conn = DriverManager.getConnection("jdbc:mysql://localhost/"+db, "user", "password");
        	//conn = dataSource.getConnection();
            conn.setReadOnly(true);
            connected = true;
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
        DataContainer dc = null;
        switch(t){
        case year:
            dc = new DataContainer('Y');
            break;
        case month:
            dc = new DataContainer('M');
            break;
        }
        try {
            PreparedStatement prep;
        	prep = conn.prepareStatement("SELECT date_time, AVG(temp) as avg FROM hourly_temperatures WHERE sensor_id = (SELECT id FROM sensors WHERE number = (?)) GROUP BY " + t +"(date_time);");
            prep.setInt(1, sensor);
            ResultSet r = prep.executeQuery();
            while (r.next()) {
                Date date = r.getDate(1);
                Float f2 = r.getFloat(2); 
                dc.add(f2, date);
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
    
    public DataContainer getAvgBySensorMonthRange(int sensor, String start, String end) {
        connectToDB(dbName);
        DataContainer dc = new DataContainer('M');
        try {
            PreparedStatement prep;
        	prep = conn.prepareStatement("SELECT date_time, AVG(temp) as avg " +
        								 "FROM hourly_temperatures " +
        								 "WHERE sensor_id = (SELECT id FROM sensors WHERE number = (?)) " +
        								 	"AND date_time BETWEEN '"+start+"-01-01 00:00:00.000' AND '"+end+"-12-31 23:59:59:999' "+
								 		 "GROUP BY year(date_time),month(date_time);");
            prep.setInt(1, sensor);
            ResultSet r = prep.executeQuery();
            while (r.next()) {
                Date date = r.getDate(1);
                Float f2 = r.getFloat(2); 
                dc.add(f2, date);
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
    
    public DataContainer getAvgBySensorMonthOne(int sensor, String month, String year) {
    	//long startTime = System.currentTimeMillis();
    	connectToDB(dbName);
    	//long endTime = System.currentTimeMillis();
    	//System.out.println("Total elapsed time is :"+ (endTime-startTime));
        DataContainer dc = new DataContainer('M');
        try {
            PreparedStatement prep;
        	prep = conn.prepareStatement("SELECT date_time, AVG(temp) as avg " +
        								 "FROM hourly_temperatures " +
        								 "WHERE sensor_id = (SELECT id FROM sensors WHERE number = (?)) " +
        								 	"AND YEAR(date_time) = (?) AND MONTH(date_time) = (?)"+
								 		 "GROUP BY year(date_time),month(date_time);");
            prep.setInt(1, sensor);
            prep.setString(2, year);
            prep.setString(3, month);
            //startTime = System.currentTimeMillis();
            ResultSet r = prep.executeQuery();
            //endTime = System.currentTimeMillis();
            //System.out.println("Total elapsed time is :"+ (endTime-startTime));
            while (r.next()) {
                Date date = r.getDate(1);
                Float f2 = r.getFloat(2); 
                dc.add(f2, date);
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
    
    public DataContainer getAvgBySensorDayRange(int sensor, String year, String start, String end) {
        connectToDB(dbName);
        DataContainer dc = new DataContainer('D');
        try {
            PreparedStatement prep;
        	prep = conn.prepareStatement("SELECT date_time, AVG(temp) as avg " +
        								 "FROM hourly_temperatures " +
        								 "WHERE sensor_id = (SELECT id FROM sensors WHERE number = (?)) " +
        								 	"AND date_time BETWEEN '"+year+"-"+start+"-01 00:00:00.000' AND '"+year+"-"+end+"-31 23:59:59:999' "+
								 		 "GROUP BY year(date_time),month(date_time),day(date_time);");
            prep.setInt(1, sensor);
            ResultSet r = prep.executeQuery();
            while (r.next()) {
                Date date = r.getDate(1);
                Float f2 = r.getFloat(2); 
                dc.add(f2, date);
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
    
    public DataContainer getHourlyData(int sensor) {
    	//long startTime = System.currentTimeMillis();
    	connectToDB(dbName);
    	//long endTime = System.currentTimeMillis();
    	//System.out.println("Total elapsed time is :"+ (endTime-startTime));
        DataContainer dc = new DataContainer('H');
        try {
            PreparedStatement prep;
        	prep = conn.prepareStatement("SELECT date_time, temp FROM hourly_temperatures WHERE sensor_id = (SELECT id FROM sensors WHERE number = (?)) GROUP BY YEAR(date_time), MONTH(date_time), DAY(date_time), HOUR(date_time);");
            prep.setInt(1, sensor);
            //startTime = System.currentTimeMillis();
            ResultSet r = prep.executeQuery();
            //endTime = System.currentTimeMillis();
            //System.out.println("Total elapsed time is :"+ (endTime-startTime));
            while (r.next()) {
            	java.sql.Timestamp dbSqlTimestamp = r.getTimestamp(1);
                Date date = new Date(dbSqlTimestamp.getTime());
                Float f2 = r.getFloat(2); 
                dc.add(f2, date);
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
    
    private PoolingDataSource setupDataSource(String connectURI, String user,
			String password) {
		//
		// First, we'll need a ObjectPool that serves as the
		// actual pool of connections.
		//
		// We'll use a GenericObjectPool instance, although
		// any ObjectPool implementation will suffice.
		//
		GenericObjectPool.Config config = new GenericObjectPool.Config();
		config.maxActive = 150;
		config.maxIdle = 100;
		config.minIdle = 30;
		config.maxWait = 1000;

		ObjectPool connectionPool = new GenericObjectPool(null, config);

		//
		// Next, we'll create a ConnectionFactory that the
		// pool will use to create Connections.
		// We'll use the DriverManagerConnectionFactory,
		// using the connect string passed in the command line
		// arguments.
		//
//		Properties p = new Properties();
//		p.setProperty("user", SQLConstants.USER_NAME);
//		p.setProperty("password", SQLConstants.PASSWORD);
//		p.setProperty("useUnicode", "true");
//		p.setProperty("characterEncoding", "UTF-8");
		
		ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(
				connectURI, user, password);
//		ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(
//				connectURI, p);
		//
		// Now we'll create the PoolableConnectionFactory, which wraps
		// the "real" Connections created by the ConnectionFactory with
		// the classes that implement the pooling functionality.
		//
		PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(
				connectionFactory, connectionPool, null, null, false, true);

		//
		// Finally, we create the PoolingDriver itself,
		// passing in the object pool we created.
		//
		PoolingDataSource poolingDataSource = new PoolingDataSource(connectionPool);

		return poolingDataSource;
	}

}
