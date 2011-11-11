package gui.pa2;

import java.awt.*;

import java.util.*;

import javax.swing.*;

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
		getContentPane().setLayout(new GridBagLayout());
		TreeSet<ConditionPoint> map = null;
		GridBagConstraints how = new GridBagConstraints();
		if(points != null) 
		{
			map = new TreeSet<ConditionPoint>();
			Calendar c = Calendar.getInstance();

			for(Conditions current : points)
			{
				c.setTime(current.getDay());
				int x = c.get(Calendar.HOUR_OF_DAY) * 1000 / 23;
				x += c.get(Calendar.MINUTE) / 6;
				int y = (int) current.getRain() * 1000 / 15;
				ConditionPoint p = new ConditionPoint(x, y, current);
				map.add(p);
			}
		}
			GraphCanvas g = new GraphCanvas(map);
			// Set it in the upper-left corner.
			how.gridx = 0;
			how.gridy = 0;
			// Make it two columns wide.
			how.gridwidth = 2;
			// Only one column tall.
			how.gridheight = 1;
			// Make it expand/contract when the window resizes.
			how.fill = GridBagConstraints.BOTH;
			how.weightx = 0.5;
			how.weighty = 0.5;
			add(g, how);
		
		if(map != null) map = new TreeSet<ConditionPoint>();
		
		if(points != null)
		{
			Calendar c = Calendar.getInstance();

			for(Conditions current : points)
			{
				c.setTime(current.getDay());
				int x = c.get(Calendar.HOUR_OF_DAY) * 1000 / 23;
				x += c.get(Calendar.MINUTE) / 6;
				int y = (int)current.getPressure() - 20;
				y *= 200 / 3;
				System.out.println("Current Pressure: " + y);
				ConditionPoint p = new ConditionPoint(x, y, current);
				map.add(p);
			}
		}
		g = new GraphCanvas(map);
		// Set it in the upper-left corner.
		how.gridx = 0;
		how.gridy = 1;
		how.weightx = 0.5;
		how.weighty = 0.5;
		// Make it two columns wide.
		how.gridwidth = 2;
		// Only one column tall.
		how.gridheight = 1;
		// Make it expand/contract when the window resizes.
		how.fill = GridBagConstraints.BOTH;
		getContentPane().add(g,how);
			
		pack();
		
	}

}
