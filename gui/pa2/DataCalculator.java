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
        
        double totalRainfall = 0.0;
        double TempSum = 0.0;
        double maxTemp = 0.0;
        double minTemp = 1000.0;
        double WindSpeedSum = 0.0;
        double maxWindSpeed = 0.0;
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
            totalRainfall += current.getRain();
            TempSum = current.getTemperature();
            WindSpeedSum = current.getWind();
            
            if( current.getTemperature() > maxTemp )
            {
                maxTemp = current.getTemperature();
            }
            
            if( current.getTemperature() < minTemp )
            {
                minTemp = current.getTemperature();
            }
            if( current.getWind() > maxWindSpeed )
            {
                maxWindSpeed = current.getWind();
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

        map.put( "  Average Wind Speed", new Float( WindSpeedSum / points.size() ) );
        map.put( "Total Rainfall", new Float(totalRainfall) );
        map.put( " Average Temperature", new Float( TempSum / points.size() ) );
        map.put( " Max Temperature", new Float( maxTemp) );
        map.put( " Min Temperature", new Float( minTemp) );
        map.put( "  Max Wind Speed", new Float( maxWindSpeed) );
        map.put( "  Prevailing Wind", prevailingWind );
        
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
