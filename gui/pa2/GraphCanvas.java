package gui.pa2;

import java.awt.*;
import java.util.*;

@SuppressWarnings("serial")
public class GraphCanvas extends Canvas {
	
	// The conditions to graph
	TreeSet<ConditionPoint> map;
	// The title of the graph.
	String title;
	String units;
	
	public GraphCanvas(TreeSet<ConditionPoint> points, String title, String unit)
	{
		super();
		this.map = points;
		this.title = title;
		units = unit;
	}
	
	public void paint(Graphics g)
	{

		int width = getSize().width;
		int height = getSize().height;
		// Set the background.
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getSize().width, getSize().height);
		g.setColor(Color.BLACK);
		g.drawRect(0,0,width - 1, height - 1);
		
		// Draw the y-axis
		g.setColor(Color.BLACK);
		
		g.drawLine(35, 25, 35, getSize().height - 45);
		
		// Draw the x-axis
		g.drawLine(35, getSize().height - 45, getSize().width - 35, getSize().height - 45);
		for(int i = 0; i < 24; ++i)
		{
			int x = (int)(i / 23.0 * (width - 70) ) + 35;
			g.drawString(""+i, x, height - 25);
		}
		//g.drawString(units, width / 2, height - 5);
		g.drawString("Hour", width / 2, height - 6);
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
			if(points.hasNext())
			do
			{
				int ax = now.getX();
				int ay = now.getY();
				ConditionPoint net = points.next();
				int bx = net.getX();
				int by = net.getY();
				ax = (int)(ax / 1000.0 * (width - 75) + 35);
				ay = height - (int)( (ay / 1000.00 * height) ) - 45;
				bx = (int)(bx / 1000.0 * (width - 75) + 35);
				by = height - (int)( by / 1000.0 * height) - 45;
				g.drawLine(ax, ay, bx, by);
				now = net;
			}while (points.hasNext());
			else
			{
				int ax = now.getX();
				int ay = now.getY();
				ax = (int)(ax/1000.0 * (width - 75) + 35);
				g.fillRect(ax, ay, 3, 3);
			}
		}
	}
}