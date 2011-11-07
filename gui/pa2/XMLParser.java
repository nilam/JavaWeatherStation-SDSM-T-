package gui.pa2;

import org.jdom.*;
import org.jdom.input.SAXBuilder;
import java.io.IOException;
import java.util.*;

public class XMLParser
{
    private String filename;

    public XMLParser()
    {

    }

    public String getFileName()
    {
        return filename;
    }

    public void setFileName( String name )
    {
        filename = name;
    }
    
    public Vector<Conditions> parse()
    {
    		Vector<Conditions> conditions = new Vector<Conditions>();
    		SAXBuilder builder = new SAXBuilder();
    		try
    		{
    				Document document = builder.build(filename);
    				Element root = document.getRootElement();
    				List children = root.getChildren("weather");
    				Iterator i = children.iterator();
    				
    				while( i.hasNext() )
    				{
    						Conditions temp = new Conditions();
    						Element child = ( Element ) i.next();
    						
    						temp.setTemperature( Float.parseFloat(child.getChild("temperature").getValue()) );
    						temp.setPressure( Float.parseFloat(child.getChild("barometer").getValue()) );
    						temp.setHumidity( Float.parseFloat(child.getChild("humidity").getValue()) );
    						temp.setUv( Float.parseFloat(child.getChild("uvindex").getValue()) );
    						temp.setRain( Float.parseFloat(child.getChild("rainfall").getValue()) );
    						temp.setWind( Float.parseFloat(child.getChild("windspeed").getValue()) );
    						temp.setWindAngle( child.getChild("winddirection").getValue() );
    						
    						conditions.add( temp );
    				}
    		}
    		catch(JDOMException e)
    		{
    				System.out.println(e.getMessage());
    		}
    		catch( IOException e)
    		{
    				System.out.println(e.getMessage());
    		}
    		
    		return conditions;
    }
}
