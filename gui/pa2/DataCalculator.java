/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.pa2;

import java.util.Vector;
import java.util.Iterator;
import java.util.HashMap;
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
        
    }
    
    public HashMap<String,Object> computeData()
    {
        HashMap<String, Object> map = new HashMap();
        
        double totalRainfall = 0.0;
        double TempSum = 0.0;
        double maxTemp = 0.0;
        double minTemp = 1000.0;
        double WindSpeedSum = 0.0;
        double maxWindSpeed = 0.0;
        HashMap<String, Integer> windDirCount = new HashMap();
        
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
        
        Iterator i = points.iterator();
        
        while( i.hasNext() )
        {
            Conditions current = (Conditions) i.next();
            
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
            
            Integer dir = windDirCount.get( current.getWindAngle() );
            dir++;
        }
        
        map.put( "totalRainfall", new Float(totalRainfall) );
        map.put( "averageTemp", new Float( TempSum / points.size() ) );
        map.put( "averageWindSpeed", new Float( WindSpeedSum / points.size() ) );
        map.put( "maxTemperature", new Float( maxTemp) );
        map.put( "minTemperature", new Float( minTemp) );
        map.put( "maxWindSpeed", new Float( maxWindSpeed) );
        
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
