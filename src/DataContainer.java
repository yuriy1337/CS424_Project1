import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;


public class DataContainer {
	
    private ArrayList<Float> temps;
    private ArrayList<Float> tempsAlt;
    private ArrayList<Date> dates;
    private ArrayList<Float> datesFloat;
    
    /*
     * Y = year
     * M = month
     * W = week
     * D = day
     * H = hour
     */
    private char type;
    
    private static final int startingYear = 2005;
    

    public DataContainer(char t) {
    	 temps = new ArrayList<Float>();
    	 tempsAlt = new ArrayList<Float>();
    	 dates = new ArrayList<Date>();
    	 datesFloat = new ArrayList<Float>();
    	 type = t;
	}
    
    public void add(Float t, Date d){
    	if(t == null || d == null){
    		System.out.println("Cannot add null values");
    		return;
    	}
    	temps.add(t);
    	tempsAlt.add((float) ((5.0/9)*(t - 32)));
    	dates.add(d);
    	datesFloat.add(getFloatFromDate(d));
    }
    
    public ArrayList<Float> getTempsArr(){
    	return temps;
    }
    
    
    public Float getFloatFromDate(Date d){
    	Calendar c = Calendar.getInstance();
    	c.setTime(d);
    	String s = "";
    	
    	if(c.get(Calendar.YEAR)%2000 >= 10){
    		s += (c.get(Calendar.YEAR)%2000);
    	}
    	else
    		s += "0"+(c.get(Calendar.YEAR)%2000);
    	if(type == 'M' || type == 'D' || type == 'H'){
	    	if(c.get(Calendar.MONTH) < 10)
	    		s += "0" + (c.get(Calendar.MONTH));
	    	else
	    		s += "" + (c.get(Calendar.MONTH));
	    	if(type == 'D' || type == 'H'){
		    	if(c.get(Calendar.DAY_OF_MONTH) < 10)
		    		s += "0" + c.get(Calendar.DAY_OF_MONTH);
		    	else
		    		s += "" + c.get(Calendar.DAY_OF_MONTH);
		    	if(type == 'H'){
			    	if(c.get(Calendar.HOUR_OF_DAY) < 10)
			    		s +=".0" + c.get(Calendar.HOUR_OF_DAY);
			    	else
			    		s += "." + c.get(Calendar.HOUR_OF_DAY);
		    	}
		    	else{
		    		s+=".0";
		    	}
	    	}
	    	else{
	    		s += "01.0";
	    	}
    	}
    	else
    	{
    		s += "0001.0";
    	}
    	Float f = Float.valueOf(s);
    	return f;
    }
    
    public String getStringDateFromFloat(Float f){
    	Calendar c = Calendar.getInstance();
    	String s = f.toString();
    	
    	/*if(s.length() == 3){
			s = "10000" + s;
		}
    	else
    	if(s.length() == 4){
			s = "1000" + s;
		}
    	else
		if(s.length() == 5){
			s = "100" + s;
		}
    	else
		if(s.length() == 6){
			s = "10" + s;
		}
    	else
    	if(s.charAt(0) == '0' || s.charAt(0) == '1' || s.charAt(0) == '2'){
    		
    		s = "1" + s;
    	}
    	*/
    	int hour = Integer.parseInt(s.substring(s.indexOf('.')+1,s.length()));
    	s=s.substring(0,s.length()-2);
    	if(s.charAt(s.length()-1) == '.')
    		s = s.substring(0,s.length()-1);
    	int day = Integer.parseInt(s.substring(s.length()-2,s.length()));
    	s=s.substring(0,s.length()-2);
    	int month = Integer.parseInt(s.substring(s.length()-2,s.length()));
    	s=s.substring(0,s.length()-2);
    	int year = Integer.parseInt(s);
    	year += 2000;
    	c.set(year, month, day, hour, 00);
    	
//    	String s = "";
    	if(type == 'Y'){
    		return ""+ c.get(Calendar.YEAR);
    	}
    	else if(type == 'M'){
    		return ""+c.get(Calendar.MONTH)+"/"+ c.get(Calendar.YEAR);
    	}
    	else if(type == 'W'){

    	}
    	else if(type == 'D'){
    		return ""+c.get(Calendar.DAY_OF_MONTH);
    	}
    	else if(type == 'H'){
    		return ""+ c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE)+"\n"+ (c.get(Calendar.DAY_OF_MONTH))+"/" + (c.get(Calendar.MONTH)+1) + "/" + c.get(Calendar.YEAR);
    	}
    	return s;
    }
    
    public int size(){
    	return temps.size();
    }
    
    public void toggleLocalization(){
    	ArrayList<Float> t = temps;
    	temps = tempsAlt;
    	tempsAlt = t;
    }

	public ArrayList<Float> getDatesArr() {
		return datesFloat;
	}
	
	public String getYearString(int row){
		SimpleDateFormat sql_date= new SimpleDateFormat("yyyy");
		Date d = dates.get(row);
		
		return sql_date.format(d);
	}
	
	public String getMonthString(int row){
		SimpleDateFormat sql_date= new SimpleDateFormat("MM");
		Date d = dates.get(row);
		
		return sql_date.format(d); 
    }
	
	public Date getDateObject(int row){
		return dates.get(row);
	}
	
	private Float getDateFloat(int row){
		return datesFloat.get(row);
	}
	
	public Float getTempFloat(int row){
		return temps.get(row);
	}
	
	private Float getTempAltFloat(int row){
		return tempsAlt.get(row);
	}
	
	public void removePoint(int p){
		dates.remove(p);
		datesFloat.remove(p);
		temps.remove(p);
		tempsAlt.remove(p);
	}
	
	public void removeFirstPoint(){
		removePoint(0);
	}
	
	public void add(DataContainer d, int position){
		for(int i=0;i<d.size();++i){
			dates.add(position, d.getDateObject(i));
			datesFloat.add(position, d.getDateFloat(i));
			temps.add(position, d.getTempFloat(i));
			tempsAlt.add(position, d.getTempAltFloat(i));
			position++;
		}
	}
	
	public String getType(){
		switch(type){
		case 'Y':
			return "Year";
		case 'M':
			return "Month";
		case 'W':
			return "Week";
		case 'D':
			return "Day";
		case 'H':
			return "Hour";
		default:
			return "Date";
		}
	}
}
