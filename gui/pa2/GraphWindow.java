/*
 * 
 */
package gui.pa2;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;

import javax.swing.*;

// TODO: Auto-generated Javadoc
/**
 * The Class GraphWindow.
 *
 * @author 1828107
 */
@SuppressWarnings("serial")
/**
 * 
 */
public class GraphWindow extends JFrame {
	

    /** The Constant DAY. */
    public final static int DAY = 0;
    
    /** The Constant WEEK. */
    public final static int WEEK = 1;
    
    /** The Constant MONTH. */
    public final static int MONTH = 2;
    
    /** The Constant YEAR. */
    public final static int YEAR = 3;
    
    /** The time strings. */
    private String[] timeStrings = {"Hour", "Day", "Day", "Day"};
    
    /**
     * Instantiates a new graph window.
     *
     * @param in_pts the in_pts
     * @param today the today
     */
    public GraphWindow(Vector<Conditions> in_pts, String today)
    {
        this( in_pts, today, GraphWindow.DAY);
    }
	
	/**
	 * Creates a new daily graph window from the given set of conditions and day.
	 * The day should be passed in as a string in the format it should appear on
	 * the graphs and title of the window.
	 *
	 * @param in_pts a vector of gui.pa2.Conditions describing the weather over the interval.
	 * @param today the date, as a string formatted as the date should appear on the graphs and title bar.
	 * @param timePeriod the time period
	 */
	public GraphWindow(Vector<Conditions> in_pts, String today, int timePeriod )
	{
		// Basic constructor necessity
		super("Graph for " + today);
		// Prevent concurrency issues
		Vector<Conditions> points = new Vector<Conditions> (in_pts);
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
                
        int divisor = 23;
        int numerator = Calendar.HOUR_OF_DAY;
                
                
		// Trying to do some basic sanity testing; caller should NOT pass in a null vector.
		if(points != null) 
		{
			map = new TreeSet<ConditionPoint>();
			// Since we used the date class for the Conditions, we need a calendar to properly access its elements.
			Calendar c = Calendar.getInstance();

            switch( timePeriod )
            {
                case WEEK:
                	numerator = Calendar.DAY_OF_WEEK;
                    divisor = 7;
                    break;
                case MONTH:
                	numerator = Calendar.DAY_OF_MONTH;
                    Conditions first = points.firstElement();
                    c.setTime(first.getDay());
                    divisor = c.getActualMaximum(Calendar.DAY_OF_MONTH);
                    break;
                case YEAR:
                	numerator = Calendar.MONTH;
                    first = points.firstElement();
                    c.setTime(first.getDay());
                    divisor = c.getActualMaximum(Calendar.MONTH);
                    break;
            }
                        
			// For each Conditions called current in the collection points
			for(Conditions current : points)
			{
				if(current.getRain() != Conditions.INVALID_RAIN)
				{
					// Use the time of day for this particular point as our calendar point.
					c.setTime(current.getDay());
					/* Because the GraphCanvas class uses proportional distancing,
					 * we will want to set x as the proportion of its time in the day to the amount of time in a day.
					 * The entire thing uses a fixed-point system; I don't like to use floats or doubles unless it's
					 * a scientific program that can be +/-.0001, since FPUs are terribly imprecise. 
					 */
					int x = c.get(numerator) * 1000 / divisor;
					if(timePeriod == DAY)
						x += c.get(Calendar.MINUTE) / 2;
					/* The y value should also be a fixed-point proportion. 15 is used as a baseline right now, but in the
					 * future we may adjust this so it is proportional to the difference in the min and max values for
					 * the data set.
					 */
					float max = (Float)data.get(DataCalculator.MAX_RAIN);
					float min = (Float)data.get(DataCalculator.MIN_RAIN);
					int y = (int) ((current.getRain() - min) / (max - min) * 500);
					// Use the above proportions to create a new data point.
					ConditionPoint p = new ConditionPoint(x, y, current);
					// Add the point to our sorted map.
					map.add(p);
				}
			}
		}
		
		// Create a graph canvas from the formatted data.
		GraphCanvas g1 = new GraphCanvas(map, "Rainfall for " + today, "Inches", timeStrings[timePeriod], new Float(divisor));
		
		// again, a minor sanity check.
		if(map != null) map = new TreeSet<ConditionPoint>();
		if(points != null)
		{
			Calendar c = Calendar.getInstance();

			for(Conditions current : points)
			{
				// See above about proportions and using fixed vs. floating point.
				c.setTime(current.getDay());
				int x = c.get(numerator) * 1000 / divisor;
				if(timePeriod == DAY)
					x += c.get(Calendar.MINUTE) / 2;
				float max = (Float)data.get(DataCalculator.MAX_PRESSURE);
				float min = (Float)data.get(DataCalculator.MIN_PRESSURE);
				// Barometric pressure rarely, if ever, gets above 40 in Hg, and is rarely less than 25 in Hg.
				int y = (int)((current.getPressure() - min) / (max - min) * 500);
				ConditionPoint p = new ConditionPoint(x, y, current);
				map.add(p);
			}
		}
		// Separate variable, because we need to graph these pieces in the group window separately.
		GraphCanvas g2 = new GraphCanvas(map, "Barometric Pressure for " + today, "Inches Hg", timeStrings[timePeriod], divisor);
		
		map = new TreeSet<ConditionPoint>();
		if(points != null)
		{
			Calendar c = Calendar.getInstance();

			for(Conditions current : points)
			{
				// See above about proportions and using fixed vs. floating point.
				c.setTime(current.getDay());
				int x = c.get(numerator) * 1000 / divisor;
				if(timePeriod == DAY)
					x += c.get(Calendar.MINUTE) / 2;
				float max = (Float)data.get(DataCalculator.MAX_TEMP);
				float min = (Float)data.get(DataCalculator.MIN_TEMP);
				int y = (int)((current.getTemperature() - min) / (max - min) * 500);
				ConditionPoint p = new ConditionPoint(x, y, current);
				map.add(p);
			}
		}
		GraphCanvas g3 = new GraphCanvas(map, "Temperature for " + today, "Degrees F", timeStrings[timePeriod], divisor);
		
        
        map = new TreeSet<ConditionPoint>();
        if(points != null)
        {
            Calendar c = Calendar.getInstance();
            
            for( Conditions current : points )
            {
                
                c.setTime(current.getDay());
				int x = c.get(numerator) * 1000 / divisor;
				if(timePeriod == DAY)
					x += c.get(Calendar.MINUTE) / 2;
                float max = (Float)data.get(DataCalculator.MAX_WIND);
                int y = (int)(current.getWind() / max * 500);
                
                ConditionPoint p = new ConditionPoint(x, y, current);
                map.add(p);
            }
            
        }
        GraphCanvas g4 = new GraphCanvas(map,"Wind Speed for " + today, "MPH", timeStrings[timePeriod], divisor);
        
        map = new TreeSet<ConditionPoint>();
        if(points != null)
        {
            Calendar c = Calendar.getInstance();
            
            for( Conditions current : points )
            {
                c.setTime(current.getDay());
				int x = c.get(numerator) * 1000 / divisor;
				if(timePeriod == DAY)
					x += c.get(Calendar.MINUTE) / 2;
                double max = (Double)data.get(DataCalculator.MAX_HUM);
                double min = (Double)data.get(DataCalculator.MIN_HUM);
                int y = (int)((current.getHumidity() - min) / (max - min) * 500);
                
                ConditionPoint p = new ConditionPoint(x, y, current);
                map.add(p);
            }
        }
        GraphCanvas g5 = new GraphCanvas(map, "Humidity for " + today, "%", timeStrings[timePeriod], divisor);
       
        map = new TreeSet<ConditionPoint>();
        if(points != null)
        {
            Calendar c = Calendar.getInstance();
            
            for( Conditions current : points )
            {
                c.setTime(current.getDay());
				int x = c.get(numerator) * 1000 / divisor;
				if(timePeriod == DAY)
					x += c.get(Calendar.MINUTE) / 2;
                float max = (Float)data.get(DataCalculator.MAX_UV);
                float min = (Float)data.get(DataCalculator.MIN_UV);
                int y = (int)((current.getUv() - min) / (max - min) * 500);
                
                ConditionPoint p = new ConditionPoint(x, y, current);
                map.add(p);
            }
        }
        GraphCanvas g6 = new GraphCanvas(map, "UV Index for " + today, "", timeStrings[timePeriod], divisor);
                
		// A special informational pane.
		InfoPane pane = new InfoPane();
		pane.setSize(new java.awt.Dimension(100,100));
        g6.addMouseListener(new PointClick(pane));                
        g5.addMouseListener(new PointClick(pane));
        g4.addMouseListener(new PointClick(pane));
        g3.addMouseListener(new PointClick(pane));
		g2.addMouseListener(new PointClick(pane));
		g1.addMouseListener(new PointClick(pane));
		
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
		columns.addGroup(layout.createParallelGroup().addComponent(g1).addComponent(g2).addComponent(g3).addComponent(g4).addComponent(g5).addComponent(g6));
		columns.addGap(5);
		// Add all those little labels.
		columns.addGroup(col);
		columns.addGap(5);
		// Add in the info pane.
		columns.addComponent(pane);
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
						addComponent(g3, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGap(5).
                                                addComponent(g4, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGap(5).
                                                addComponent(g5, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGap(5).
                                                addComponent(g6, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).
				addGroup(panel).
				addComponent(pane));
		// Use this layout for the vertical direction.
		layout.setVerticalGroup(rows);
		
		// Pack it all in.
		pack();
		
	}
	
	/**
	 * The Class PointClick.
	 *
	 * @author 1828107
	 */
	private class PointClick implements MouseListener
	{
		
		/** The target. */
		private InfoPane target;
		
		/**
		 * Instantiates a new point click.
		 *
		 * @param pane the pane
		 */
		public PointClick(InfoPane pane)
		{
			target = pane;
		}
		
		/**
		 * Mouse clicked.
		 *
		 * @param arg0 the arg0
		 */
		public void mouseClicked(MouseEvent arg0) 
		{
			int x = arg0.getX();
			int y = arg0.getY();
			Conditions dummy = new Conditions();
			ConditionPoint pt = new ConditionPoint(x, y, dummy);
			GraphCanvas canvas = (GraphCanvas)arg0.getSource();
			if(canvas.getRelative() != null)
			{
			    Iterator<ConditionPoint> it = canvas.getRelative().iterator();
			    boolean cont = true;
			    ConditionPoint holder = null;
			    while(it.hasNext() && cont)
			    {
			    	holder = it.next();
			    	if( Math.abs( holder.compareTo(pt) )  < 5)
			    	{
			    		cont = false;
			    	}
			    }
			    
			    if(cont == true) holder = null;
			    
			    target.setPoint(holder);
			    target.showInfo();
			}
		}
		
		/**
		 * Mouse entered.
		 *
		 * @param arg0 the arg0
		 */
		public void mouseEntered(MouseEvent arg0) 
		{
		}
		
		/**
		 * Mouse exited.
		 *
		 * @param arg0 the arg0
		 */
		public void mouseExited(MouseEvent arg0) 
		{
		}
		
		/**
		 * Mouse pressed.
		 *
		 * @param arg0 the arg0
		 */
		public void mousePressed(MouseEvent arg0) 
		{
		}
		
		/**
		 * Mouse released.
		 *
		 * @param arg0 the arg0
		 */
		public void mouseReleased(MouseEvent arg0) 
		{
		}
		
	}

}
