package gui.pa2;

import java.awt.*;
import java.util.*;

@SuppressWarnings("serial")
public class GraphCanvas extends Canvas {
	
	// The conditions to graph
	private TreeSet<ConditionPoint> map;
	private Vector<ConditionPoint> relativeMap;
	// The title of the graph.
	private String title;
    private String timeUnits;
	private Point pt;
	private float numDivs;
	
	public TreeSet<ConditionPoint> getMap()
	{
		return map;
	}
	
	public Vector<ConditionPoint> getRelative()
	{
		return relativeMap;
	}
	
	public void setPoint(Point p)
	{
		pt = p;
	}
	
	public GraphCanvas(TreeSet<ConditionPoint> points, String title, String unit, String timeUnits, float numDivs)
	{
		super();
		this.map = points;
		this.title = title;
        this.timeUnits = timeUnits;
        this.numDivs = numDivs;
		pt = new Point(0,0);
		relativeMap = new Vector<ConditionPoint>();
	}
	
	public void paint(Graphics g)
	{

		int width = getSize().width;
		int height = getSize().height;
		// Set the background.
		//g.setColor(new Color(0xCC, 0xCC, 0xFF));
		//g.fillRect(0, 0, getSize().width, getSize().height);

		/* Sets the background to a gradient. Pretty, but makes the redraw really slow.
		for(int i = 0; i < height / 10 + 10; ++i)
		{
			g.setColor(new Color(0xCC - 3 * i, 0xCC - 3 * i, 0xFF - 3 * i) );
			g.fillRect(0, 10 * i, width, 10);
		}
		*/
		g.setColor(Color.BLACK);
		g.drawRect(0,0,width - 1, height - 1);
		
		// Draw the y-axis
		g.setColor(Color.BLACK);
		
		g.drawLine(35, 25, 35, getSize().height - 45);
		
		// Draw the x-axis
		g.drawLine(35, getSize().height - 45, getSize().width - 35, getSize().height - 45);
		for(int i = 0; i <= numDivs; ++i)
		{
			int x = (int)(i / (numDivs) * (width - 70) ) + 35;
			g.drawString(""+i, x, height - 25);
		}
		//g.drawString(units, width / 2, height - 5);
		g.drawString(timeUnits, width / 2, height - 6);
		// Draw the title.
		g.drawString(title, getSize().width / 4, 20);
		
		// Check to see if we have points.
		if(map == null || map.isEmpty())
		{
			// Looks like we don't. Print an error message.
			g.setColor(Color.BLACK);
			g.fillRect(getSize().width / 2 - 90, getSize().height / 2 - 25, 150, 40);
			g.setColor(Color.PINK);
			g.drawString("No valid data for this day.", getSize().width / 2 - 80, getSize().height / 2);
		}
		// Draw any points we do have.
		else
		{
			Iterator<ConditionPoint> points = map.descendingIterator();
			g.setColor(Color.BLUE);
			
			ConditionPoint now = points.next();
			if(relativeMap != null) relativeMap = new Vector<ConditionPoint>();

			// Graph the points.
			if(points.hasNext())
			while (points.hasNext())
			{
				int ax = now.getX();
				int ay = now.getY();
				ConditionPoint net = points.next();
				int bx = net.getX();
				int by = net.getY();
				ax = (int)(ax / 1000.0 * (width - 75) + 35);
				ay = height - (int)( ay / 1000.00 * height) - 45;
				bx = (int)(bx / 1000.0 * (width - 75) + 35);
				by = height - (int)( by / 1000.0 * height) - 45;
				g.drawLine(ax, ay, bx, by);
				g.fillOval(ax, ay, 3, 3);
				relativeMap.add( new ConditionPoint( ax, ay, now.getData() ) );
				now = net;
			}
			int ax = now.getX();
			int ay = now.getY();
			ax = (int)(ax/1000.0 * (width - 75) + 35);
			g.fillRect(ax, ay, 3, 3);
			relativeMap.add( new ConditionPoint( ax, ay, now.getData()) );
		
			if(pt.x != 0 && pt.y != 0)
			{
				ConditionPoint mouse = new ConditionPoint(pt.x, pt.y, new Conditions());
				ConditionPoint closest = new ConditionPoint(now.getX(), now.getY(), now.getData());
				for( ConditionPoint current : relativeMap)
				{
					if( Math.abs( current.compareTo(mouse) ) < Math.abs(closest.compareTo(mouse) ) )
					{
						closest = current;
					}
				}
				g.setColor(Color.RED);
				g.drawLine(closest.getX(), closest.getY(), mouse.getX(), mouse.getY());
				g.setColor(Color.CYAN);
				g.fillRect(width - 85, 0, width, 20);
				g.setColor(Color.BLACK);
				g.drawString(closest.getX() + ", " + closest.getY(), width - 80, 15);
				
				g.setColor(Color.PINK);
				for(ConditionPoint p : relativeMap)
				{
					g.drawOval(p.getX(), p.getY(), 3, 3);
				}
			}
		}
	}
}