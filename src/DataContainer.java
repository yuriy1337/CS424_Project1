import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    	SimpleDateFormat ts = new SimpleDateFormat();
		if(type == 'Y'){
			ts.applyPattern("yyyy");
			String s = ts.format(d);
			Float f = Float.valueOf(s);
			return(f - startingYear);
			}
		else
		if(type == 'M'){
			ts.applyPattern("yyyy");
			String s1 = ts.format(d);
			Float f1 = Float.valueOf(s1);
			Float yearMultiplier = f1 - startingYear;
			ts.applyPattern("MM");
			String s2 = ts.format(d);
			Float f2 = Float.valueOf(s2);
			return(f2+12*yearMultiplier);

		}
		return null;
    	
    }
    
    public String getStringDateFromFloat(Float f){
    	String s = "";
    	if(type == 'Y'){
    		int year = f.intValue();
    		s = ""+ (year+startingYear) ;
    	}
    	else if(type == 'M'){
    		
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
}
