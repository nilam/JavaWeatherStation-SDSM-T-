package gui.pa2;

import java.awt.*;
import java.util.*;

@SuppressWarnings("serial")
public class GraphCanvas extends Canvas {
	
	// A string describing the day of the year we want to look at:
	String viewDay;
	// The conditions that we want to graph. Required input.
	Vector<Conditions> points;
	// The rainfall at various times throughout the day, mapped to a given day.
	TreeMap<String, Vector<Point>> rainByDay;
	/* This actually needs some explanation: the map is two-layered. It first takes a string, which should be the day of the year
	 * converted to a String. This will return a map of Points. The second map, if given a point that we placed on that day in the
	 * graph, will return the conditions at that point. Thus, Given a day of the year and the specific point that was used to
	 * graph the rainfall at that time during the year, this rather ugly-looking structure gives us all of the available information
	 * on that day. Thank you, Dr. Weiss, for giving me a problem that could be solved almost elegantly with something this ghastly.
	 */
	TreeMap<String, TreeMap<String,Conditions>> conditionsByRain;
	
	public GraphCanvas(Vector<Conditions> points)
	{
		super();
		this.points = points;
		// For what we're doing here, empty is good, while null is dangerous. Memory's cheap. Late-runtime null checking is tricky.
		this.conditionsByRain = new TreeMap<String, TreeMap<String, Conditions>>();
		this.rainByDay = new TreeMap<String, Vector<Point>>();
		viewDay = "";
		/* We want to access members of a Date, so we need a Calendar. Unfortunately, 
		 * there's a small bug in Java that returns a null Calendar if the time zone is
		 * at a negative offset (i.e. the time zone is west of Greenwich), so we have to find an ok timezone to
		 * use as the default until we can get the calendar and correct it.
		 
		TimeZone tz = TimeZone.getDefault();
		tz.setRawOffset(+1000);
		TimeZone.setDefault(tz);
		*/
		Calendar c = Calendar.getInstance();
		if(c == null)
		{
			System.err.println("Aw, man! We've got a null calendar from Calendar.getInstance!");
		}
		else
		{
			// We will make this the day that we're working on. At the end, this becomes the default view day.
			String day = "";
			// If we were given points to graph, process them here.
			if(points != null)
			{
				// for Conditions current in points
				for(Conditions current : points)
				{
					// We want to work with the date/time specified in the data point.
					Date today = current.getDay();
					if(today != null)
					{
						c.setTime(today);
						// Get a proportion of the timeline we will be working on for the x and y directions.
						double xf = c.get(Calendar.HOUR_OF_DAY) / 23.0;
						// Minutes should be a smaller value and have less impact than minutes.
						xf += c.get(Calendar.MINUTE) / 600.0;
						float yf = current.getRain();
						
						// Convert those to actual points on the x/y axis
						int x = (int)(xf * 100);
						int y = (int)(yf * 100); 
						
						// We need *SOMETHING* to sort this stuff by day.
						day = "" + c.get(Calendar.DAY_OF_YEAR);
						
						// Stuff x and y into an actual point.
						Point p = new Point(x,y);
						
						// Check to see if we already have a vector for the given day.
						if(rainByDay.containsKey(day))
						{
							// We do, so just stick this point on the end.
							rainByDay.get(day).add(p);
						}
						else
						{
							// Looks like we don't. Create a vector for today, and stuff this point in there.
							Vector<Point> vec = new Vector<Point>();
							vec.add(p);
							rainByDay.put(day, vec);
						}
						String key = "" + p.x + "," + p.y;
						// Now we have a point associated with this day. Let's associate all of these conditions with this date and point.
						if(conditionsByRain.containsKey(day))
						{
							// Check to make sure we don't have a perfectly overlapping point (We shouldn't, but it *IS* possible).
							if(conditionsByRain.get(day).containsKey(key))
							{
								System.err.println("Error: Overlapping point on day " + day);
								System.err.println("Point in question is " + p);
								System.err.println("Possible data loss.");
							}
							else
							{
								// We're good. Today already exists, but this point doesn't. 
								conditionsByRain.get(day).put(key,current);
							}
						}
						// If the day doesn't already exist, we need to create it.
						else
						{
							TreeMap<String, Conditions> temp = new TreeMap<String, Conditions>();
							temp.put(key, current);
							conditionsByRain.put(day, temp);
						}
					}
				} // We're doing this for each set of conditions that we were given.
				viewDay = day;
			}
		}
	}
	
	public void paint(Graphics g)
	{
		// Get all the points for whatever day we are on.
		Vector<Point> pika = rainByDay.get(viewDay);
		// Set the background.
		g.setColor(Color.WHITE);
		g.drawRect(0, 0, getSize().width, getSize().height);
		// Draw the y-axis
		g.setColor(Color.BLACK);
		g.drawLine(35, 25, 35, getSize().height - 25);
		// Draw the x-axis
		g.drawLine(35, getSize().height - 25, getSize().width - 35, getSize().height - 25);
		// Check to see if we have points.
		if(points == null || points.isEmpty())
		{
			// Looks like we don't. Print an error message.
			g.setColor(Color.BLACK);
			g.drawString("Oops, it looks like you forgot to provide an input file. Please go back and do so.", 50, 50);
			g.setColor(Color.BLUE);
			g.fillRect(getSize().width / 2 - 90, getSize().height / 2 - 25, 150, 40);
			g.setColor(Color.PINK);
			g.drawString("I don't think so, Tim.", getSize().width / 2 - 80, getSize().height / 2);
		}
		// Draw any points we do have.
		else
		{
			g.setColor(Color.GREEN);
			if(pika.size() == 1)
			{
				g.drawRect(pika.get(0).x, pika.get(0).y, 1, 1);
			}
			int height = getSize().height;
			
			for(int i = 0; i < pika.size() - 1; ++i)
			{
				int ax = pika.get(i).x;
				int ay = pika.get(i).y;
				int bx = pika.get(i+1).x;
				int by = pika.get(i+1).y;
				ax = (int)(ax / 100.0 * (getSize().width - 120) + 35);
				ay = (int)(height - (ay / 100.0 * height) - 27);
				bx = (int)(bx / 100.0 * (getSize().width - 120) + 35);
				by = (int)(height - (by / 100.0 * height) - 27);
				g.drawLine(ax, ay, bx, by);
				System.out.println("From " + ax + ", " + ay + " to " + bx + ", " + by);
			}
		}
	}
}