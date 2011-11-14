/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.pa2;

import java.util.Vector;
import java.util.TreeMap;
import java.util.Map;
/**
 *
 * @author jmk
 */
public class DataCalculator {
    
    private Vector<Conditions> points;
    
    public static final String MAX_TEMP = " Max Temperature";
    public static final String MIN_TEMP = " Min Temperature";
    
    public static final String TOTAL_RAIN = "Total Rainfall";
    public static final String MAX_RAIN = "Maximum Rainfall";
    public static final String MIN_RAIN = "Minimum Rainfall";
    
    public DataCalculator( Vector<Conditions> p )
    {
        points = p;
    }
    
    public DataCalculator()
    {
        points = null;
    }
    
    public TreeMap<String,Object> computeData()
    {
        TreeMap<String, Object> map = new TreeMap<String, Object>();
        
        // Calculated Values
        double maxTemp = 0.0;
        double minTemp = 1000.0;
        double maxWindSpeed = 0.0;
        double maxPressure = 0.0;
        double minPressure = 100.0;
        double minRain = 1000.0;
        double maxRain = 0.0;
        
        // Values used for Averaging
        double pressureSum = 0;
        int pressureCount = 0;
        double TempSum = 0.0;
        int tempCount = 0;
        double totalRainfall = 0.0;
        int rainCount = 0;
        double WindSpeedSum = 0.0;
        int windCount = 0;
        
        TreeMap<String, Integer> windDirCount = new TreeMap<String, Integer>();
        String prevailingWind = "";
        
        windDirCount.put( "N",new Integer(0));
        windDirCount.put( "NNE",new Integer(0));
        windDirCount.put( "NE",new Integer(0));
        windDirCount.put( "ENE",new Integer(0));
        windDirCount.put( "E",new Integer(0));
        windDirCount.put( "ESE",new Integer(0));
        windDirCount.put( "SE",new Integer(0));
        windDirCount.put( "SSE",new Integer(0));
        windDirCount.put( "S",new Integer(0));
        windDirCount.put( "SSW",new Integer(0));
        windDirCount.put( "SW",new Integer(0));
        windDirCount.put( "WSW",new Integer(0));
        windDirCount.put( "W",new Integer(0));
        windDirCount.put( "WNW",new Integer(0));
        windDirCount.put( "NW",new Integer(0));
        windDirCount.put( "NNW",new Integer(0));
        
        for(Conditions current : points)
        {       
        	if(current.getRain() != Conditions.INVALID_RAIN)
        	{	
        		totalRainfall += current.getRain();
        		rainCount++;
        		if(current.getRain() > maxRain)
        		{
        			maxRain = current.getRain();
        		}
        		if(current.getRain() < minRain)
        		{
        			minRain = current.getRain();
        		}
        	}
        	
            if(current.getTemperature() != Conditions.INVALID_TEMP)
            {
            	TempSum = current.getTemperature();
            	tempCount++;
            	
            	if( current.getTemperature() > maxTemp )
                {
                    maxTemp = current.getTemperature();
                }
                
                if( current.getTemperature() < minTemp )
                {
                    minTemp = current.getTemperature();
                }
            }
            if(current.getWind() != Conditions.INVALID_WIND)
            {
            	WindSpeedSum = current.getWind();
            	windCount++;
	            if( current.getWind() > maxWindSpeed )
	            {
	                maxWindSpeed = current.getWind();
	            }
            }
            if(current.getPressure() != Conditions.INVALID_PRESSURE)
            {
            	pressureSum += current.getPressure();
            	++pressureCount;
            	if( current.getPressure() > maxPressure)
            	{
                	maxPressure = current.getPressure();
                }
                if( current.getPressure() < minPressure && current.getPressure() != Conditions.INVALID_PRESSURE)
                {
                	minPressure = current.getPressure();
                }
            }
                       
            int dir = windDirCount.get( current.getWindAngle().trim() );
            ++dir;
            windDirCount.put(current.getWindAngle().trim(), dir);
        }
        
        int maxCount = 0;
        
        for( Map.Entry<String, Integer> current : windDirCount.entrySet())
        {            
            if( (Integer) current.getValue() > maxCount )
            {
                maxCount = (Integer) current.getValue();
                prevailingWind = (String) current.getKey();
            }
        }

        /*
         * It looks a lot nicer if we organize these strings. This is probably the best time, since doing it later requires
         * string comparisons and gets painful. These strings should be sorted alphabetically, so adding an extra space to
         * the start of the string is a cost-efficient way of forcing the strings into the correct order. We can remove the
         * extra white space by simply calling trim() on the strings before putting them into JLabels.
         */
        map.put(" Rainfall", "-------------");
        map.put(TOTAL_RAIN, new Float(totalRainfall) );
        map.put(MAX_RAIN, new Float(maxRain));
        map.put(MIN_RAIN, new Float(minRain));
        
        map.put("  Temperature", "-------------");
        if(tempCount > 0)
        	map.put( " Average Temperature", new Float( TempSum / tempCount ) );
        else
        	map.put( " Averatge Temperature", "N/A");
        map.put( MAX_TEMP, new Float( maxTemp) );
        map.put( MIN_TEMP, new Float( minTemp) );
        
        map.put("   Wind", "-------------");
        if(windCount != 0)
        	map.put( "  Average Wind Speed", new Float( WindSpeedSum / windCount ) );
        else
        	map.put( "  Average Wind Speed", "N/A" );
        map.put( "  Max Wind Speed", new Float( maxWindSpeed) );
        map.put( "  Prevailing Wind", prevailingWind );
        
        map.put("     Pressure","-------------");
        if(pressureCount != 0)
        	map.put("    Average Barometric Pressure", new Float( pressureSum / pressureCount));
        else
        	map.put("    Average Barometric Pressure", new Float( pressureSum / pressureCount));
        map.put("    Max Barometric Pressure", new Float(maxPressure));
        map.put("    Min Barometric Pressure", new Float(minPressure));
        
        return map;
    }
    
    public Vector<Conditions> getPoints()
    {
        return points;
    }
    
    public void setPoints(Vector<Conditions> p)
    {
        points = p;
    }
    
}
