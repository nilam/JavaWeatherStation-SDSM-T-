package gui.pa2;

import java.util.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class GraphWindow extends JFrame {
	
	/**
	 * Creates a new daily graph window from the given set of conditions and day.
	 * The day should be passed in as a string in the format it should appear on
	 * the graphs and title of the window.
	 * @param points a vector of gui.pa2.Conditions describing the weather over the interval.
	 * @param today the date, as a string formatted as the date should appear on the graphs and title bar.
	 */
	public GraphWindow(Vector<Conditions> points, String today)
	{
		// Basic constructor necessity
		super("Graph for " + today);
		// We're using a hand-coded GroupLayout for this window.
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		// We want gaps inserted between the edges of the container and components.
		layout.setAutoCreateContainerGaps(true);
		// The points should be sorted by X-value. This tree gives us O(lg(n)) sort time on inserts and easy access to the elements.
		TreeSet<ConditionPoint> map = null;
		// Calculate the different values that we need. These are used in calculating proportional averages.
		DataCalculator d = new DataCalculator(points);
		TreeMap<String, Object> data = d.computeData();
		// Trying to do some basic sanity testing; caller should NOT pass in a null vector.
		if(points != null) 
		{
			map = new TreeSet<ConditionPoint>();
			// Since we used the date class for the Conditions, we need a calendar to properly access its elements.
			Calendar c = Calendar.getInstance();

			// For each Conditions called current in the collection points
			for(Conditions current : points)
			{
				if(current.getRain() != Conditions.INVALID_TEMP)
				{
					// Use the time of day for this particular point as our calendar point.
					c.setTime(current.getDay());
					/* Because the GraphCanvas class uses proportional distancing,
					 * we will want to set x as the proportion of its time in the day to the amount of time in a day.
					 * The entire thing uses a fixed-point system; I don't like to use floats or doubles unless it's
					 * a scientific program that can be +/-.0001, since FPUs are terribly imprecise. 
					 */
					int x = c.get(Calendar.HOUR_OF_DAY) * 1000 / 23;
					x += c.get(Calendar.MINUTE) / 6;
					/* The y value should also be a fixed-point proportion. 15 is used as a baseline right now, but in the
					 * future we may adjust this so it is proportional to the difference in the min and max values for
					 * the data set.
					 */
					float max = (Float)data.get(DataCalculator.MAX_RAIN);
					int y = (int) (current.getRain() * 500 / max) + 10;
					// Use the above proportions to create a new data point.
					ConditionPoint p = new ConditionPoint(x, y, current);
					// Add the point to our sorted map.
					map.add(p);
				}
			}
		}
		
		// Create a graph canvas from the formatted data.
		GraphCanvas g1 = new GraphCanvas(map, "Rainfall for " + today, "Inches");
		
		// again, a minor sanity check.
		if(map != null) map = new TreeSet<ConditionPoint>();
		if(points != null)
		{
			Calendar c = Calendar.getInstance();

			for(Conditions current : points)
			{
				// See above about proportions and using fixed vs. floating point.
				c.setTime(current.getDay());
				int x = c.get(Calendar.HOUR_OF_DAY) * 1000 / 23;
				x += c.get(Calendar.MINUTE) / 6;
				// Barometric pressure rarely, if ever, gets above 40 in Hg, and is rarely less than 25 in Hg.
				int y = (int)current.getPressure() - 20;
				y *= 200 / 3;
				ConditionPoint p = new ConditionPoint(x, y, current);
				map.add(p);
			}
		}
		// Separate variable, because we need to graph these pieces in the group window separately.
		GraphCanvas g2 = new GraphCanvas(map, "Barometric Pressure for " + today, "Inches Hg");
		
		map = new TreeSet<ConditionPoint>();
		if(points != null)
		{
			Calendar c = Calendar.getInstance();

			for(Conditions current : points)
			{
				// See above about proportions and using fixed vs. floating point.
				c.setTime(current.getDay());
				int x = c.get(Calendar.HOUR_OF_DAY) * 1000 / 23;
				x += c.get(Calendar.MINUTE) / 6;
				// Barometric pressure rarely, if ever, gets above 40 in Hg, and is rarely less than 25 in Hg.
				int y = (int)(current.getTemperature()) * 1000 / 180 + 50;
				ConditionPoint p = new ConditionPoint(x, y, current);
				map.add(p);
			}
		}
		GraphCanvas g3 = new GraphCanvas(map, "Temperature for " + today, "Degrees F");
		
		// A series of JLabels will contain the relevant data.
		JLabel[] dataValues = new JLabel[data.size()];
		int i = 0;
		// For each pair of entries in the set of data we got returned...
		for( Map.Entry<String, Object> current : data.entrySet())
		{
			// Sanity check: Avoid nullPointerExceptions
			if(current.getKey() == null)
			{
				System.err.println("Oh, snap! Found a null key in the map!");
			}
			else if(current.getValue() == null)
			{
				System.err.println("Egads! A null value in the map!");
			}
			else
			{
				// If it's all sane, concatenate the strings for output.
				dataValues[i] = new JLabel(current.getKey().trim() + ": " + current.getValue());
			}    
			++i;
		}
		// Here there be dragons. This is where I finally lay everything out. You've been warned.
		GroupLayout.SequentialGroup panel = layout.createSequentialGroup(); // Collection for the JLabels' vertical layout
		GroupLayout.ParallelGroup col = layout.createParallelGroup(); // Collection for the JLabels' horizontal layout
		
		// For each JLabel in the dataValues array...
		for( JLabel label : dataValues)
		{
			// Add the label to each half of the layout.
			panel.addComponent(label);
			panel.addGap(2,5,Short.MAX_VALUE);
			col.addComponent(label);
		}
		// Collection for the horizontal direction of the window.
		GroupLayout.SequentialGroup columns = layout.createSequentialGroup();
		// Add the graphs, one after the other.
		columns.addGroup(layout.createParallelGroup().addComponent(g1).addComponent(g2).addComponent(g3));
		columns.addGap(5);
		// Add all those little labels.
		columns.addGroup(col);
		// Make columns the layout as things go from left to right.
		layout.setHorizontalGroup(columns);
		// Get a proper layout for the individual rows.
		GroupLayout.SequentialGroup rows = layout.createSequentialGroup();
		
		/* Sorry about this; it's the easiest way to simply add all the stuff together. I know it resembles an Eldritch 
		 * horror from beyond space-time. I'll try and get a document together that explains why on earth this is
		 * necessary, but it does work.
		 */
		rows.addGroup(layout.createParallelGroup(). // note the . at the end of the line; this is a really long chain.
				addGroup(
						layout.createSequentialGroup().
						addComponent(g1, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGap(5).
						addComponent(g2, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGap(5).
						addComponent(g3, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).
				addGroup(panel));
		// Use this layout for the vertical direction.
		layout.setVerticalGroup(rows);
		
		// Pack it all in.
		pack();
		
	}

}
