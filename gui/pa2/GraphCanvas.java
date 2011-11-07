package gui.pa2;

import java.awt.*;

@SuppressWarnings("serial")
public class GraphCanvas extends Canvas {
	
	Conditions[] points;
	
	public GraphCanvas(Conditions[] points)
	{
		super();
		this.points = points;
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
	}

}
