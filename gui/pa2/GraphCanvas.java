package gui.pa2;

import java.awt.*;
import java.util.*;

@SuppressWarnings("serial")
public class GraphCanvas extends Canvas {
	public enum CONDS {RAIN, PRESSURE};
	
	// The conditions to graph
	TreeSet<ConditionPoint> map;
	
	public GraphCanvas(TreeSet<ConditionPoint> points)
	{
		super();
		this.map = points;
	}
	
	public void paint(Graphics g)
	{
		// Set the background.
		g.setColor(Color.WHITE);
		g.drawRect(0, 0, getSize().width, getSize().height);
		// Draw the y-axis
		g.setColor(Color.BLACK);
		g.drawLine(35, 25, 35, getSize().height - 25);
		// Draw the x-axis
		g.drawLine(35, getSize().height - 25, getSize().width - 35, getSize().height - 25);
		// Check to see if we have points.
		if(map == null || map.isEmpty())
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
			Iterator<ConditionPoint> points = map.descendingIterator();
			g.setColor(Color.BLACK);
			int height = getSize().height;
			int width = getSize().width;
			
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
				ay = (int)(height - ay - 30);
				bx = (int)(bx / 1000.0 * (width - 75) + 35);
				by = (int)(height - by - 30);
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
			g.setColor(Color.BLUE);
			for(int i = 1; i < 24; ++i)
			{
				g.drawLine((int)((i / 23.0) * (width - 75)) + 35, height - 25, (int)((i / 23.0) * (width - 75)) + 35, 25);
			}
		}
	}
}