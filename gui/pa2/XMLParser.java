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
    						Element condition = child.getChild("temperature");
    						
    						if( condition != null )
    						{
    								temp.setTemperature( Float.parseFloat(condition.getValue()) );
    						}
    						
    						condition = child.getChild("barometer");
    						if( condition != null )
    						{
    								temp.setPressure( Float.parseFloat(condition.getValue()) );
    						}
    						
    						condition = child.getChild("humidity");
    						if( condition != null )
    						{
    								temp.setHumidity( Float.parseFloat(condition.getValue()) );
    						}
    						
    						condition = child.getChild("uvindex");
    						if( condition != null )
    						{
    								temp.setUv( Float.parseFloat(condition.getValue()) );
    						}
    						
    						condition = child.getChild("rainfall");
    						if( condition != null )
    						{
    								temp.setRain( Float.parseFloat(condition.getValue()) );
    						}    						
    						condition = child.getChild("windspeed");
    						if( condition != null )
    						{
    								temp.setWind( Float.parseFloat(condition.getValue()) );
    						}    						
    						condition = child.getChild("winddirection");
    						if( condition != null )
    						{
    								temp.setWindAngle( condition.getValue());
    						}
    						
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
