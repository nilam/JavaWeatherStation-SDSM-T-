package gui.pa2;

import java.awt.*;

import java.util.*;

import javax.swing.JFrame;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class GraphWindow extends JFrame {
	
	public GraphWindow(String title) {
		super(title);
		Graphics g = getGraphics();
		g.setColor(Color.BLUE);
		JLabel l = new JLabel("Content coming soon!");
		getContentPane().add(l);
	}
	
	public GraphWindow(Vector<Conditions> points)
	{
		super("Rainfall Graph");
		getContentPane().setLayout(new GridLayout());
		TreeSet<ConditionPoint> map = null;
		if(points != null) 
		{
			map = new TreeSet<ConditionPoint>();
			Calendar c = Calendar.getInstance();

			for(Conditions current : points)
			{
				c.setTime(current.getDay());
				int x = c.get(Calendar.HOUR_OF_DAY) * 1000 / 23;
				x += c.get(Calendar.MINUTE) / 6;
				int y = (int) current.getRain();
				ConditionPoint p = new ConditionPoint(x, y, current);
				map.add(p);
			}
	
			GraphCanvas g = new GraphCanvas(map);
			g.setSize(getSize().width / 2, getSize().height / 2 - 25);
			getContentPane().add(g);
			
			map.clear();
			for(Conditions current : points)
			{
				c.setTime(current.getDay());
				int x = c.get(Calendar.HOUR_OF_DAY) * 1000 / 23;
				x += c.get(Calendar.MINUTE) / 6;
				int y = (int) current.getPressure();
				ConditionPoint p = new ConditionPoint(x, y, current);
				map.add(p);
			}
			g = new GraphCanvas(map);
			g.setSize(getSize().width / 2, getSize().height / 2 - 25);
			getContentPane().add(g);
			getContentPane().validate();
		}
	}

}
