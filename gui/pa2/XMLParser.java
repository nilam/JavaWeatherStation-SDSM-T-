/*
 * 
 */
package gui.pa2;

import org.jdom.*;
import org.jdom.input.SAXBuilder;
import java.io.IOException;
import java.util.*;
// TODO: Auto-generated Javadoc

/**
 * The Class XMLParser.
 *
 * @author 1828107
 */
public class XMLParser
{
    
    /** The filename. */
    private String filename;
    
    /**
     * Instantiates a new xML parser.
     *
     * @param file the file
     */
    public XMLParser(String file)
    {
    	filename = file;
    }
    
    /**
     * Gets the file name.
     *
     * @return the file name
     */
    public String getFileName()
    {
        return filename;
    }

/**
 * Sets the file name.
 *
 * @param name the new file name
 */
    public void setFileName( String name )
    {
        filename = name;
    }
    
    /**
     * Parses the.
     *
     * @return the vector
     */
    @SuppressWarnings("unchecked")
    
    /**
     * 
     */
	public Vector<Conditions> parse()
    {
    		Vector<Conditions> conditions = new Vector<Conditions>();
    		SAXBuilder builder = new SAXBuilder();
    		try
    		{
    				Document document = builder.build(filename);
    				Element root = document.getRootElement();
    				List<org.jdom.Element> children = root.getChildren("weather");
    				Iterator<org.jdom.Element> i = children.iterator();
    				Calendar c = Calendar.getInstance();
    				
    				while( i.hasNext() )
    				{
    						Conditions temp = new Conditions();
    						Element child = i.next();
    						Element condition = child.getChild("temperature");
    						String datestring = "";
    						
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
    						condition = child.getChild("date");
    						if(condition != null)
    						{
    						    datestring = condition.getValue();
    						    datestring = datestring.trim();
    						    String[] toks = datestring.split("/");
    						    c.set(Calendar.MONTH, Integer.parseInt(toks[0]));
    						    c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(toks[1]));
    						    c.set(Calendar.YEAR, Integer.parseInt(toks[2]) + 2000);
    						}
    						condition = child.getChild("time");
    						if(condition != null)
    						{
    							if(!datestring.isEmpty())
    							{
	    							datestring = condition.getValue();
	    							datestring = datestring.trim();
	    							String[] toks = datestring.split(":");
	    							c.set(Calendar.HOUR, Integer.parseInt(toks[0]));
	    							if(toks[1].indexOf("A") != -1)
	    							{
	    								c.set(Calendar.AM_PM, Calendar.AM);
	    							}
	    							else
	    							{
	    								c.set(Calendar.AM_PM, Calendar.PM);
	    							}
	    							c.set(Calendar.MINUTE, Integer.parseInt(toks[1].substring(0,toks[1].length() - 1)));
	    							Date out_date = c.getTime();
	    							temp.setDay(out_date);
    							}
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
