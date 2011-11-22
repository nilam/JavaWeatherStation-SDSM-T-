/*
 * 
 */
package gui.pa2;

import java.awt.Button;
import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

// TODO: Auto-generated Javadoc
/**
 * The Class SelectionWindow.
 *
 * @author 1828107
 */
@SuppressWarnings("serial")
/**
 * 
 */
public class SelectionWindow extends JFrame {
	
	/** The points. */
	private Vector<Conditions> points;
	
	/** The map. */
	private TreeMap<String, Vector<Conditions>> map;
	
	/** The day. */
	private String day;
	
	/** The lists. */
	private TreeMap<String, TreeMap<String, TreeMap<String, ArrayList<String> > > > lists;
	
	/** The panel. */
	private JTree panel;
	
	/** The graph. */
	private int graph;

	/**
	 * Instantiates a new selection window.
	 *
	 * @throws HeadlessException the headless exception
	 */
	public SelectionWindow() throws HeadlessException {
		super();
		points = null;
		map = new TreeMap<String, Vector<Conditions>>();
		day = null;
		graph = GraphWindow.DAY;
		lists = new TreeMap<String, TreeMap<String, TreeMap<String, ArrayList<String> > > >();
		panel = new JTree( new DefaultMutableTreeNode("No data entered.") );
		panel.addTreeSelectionListener(new listChooser(this));
		panel.getSelectionModel().setSelectionMode(TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);
		
		// Menu bar initialization
		MenuBar bar = new MenuBar();
		Menu m = new Menu("File");
		MenuItem i = new MenuItem("Add source file...");
		i.addActionListener(new Open(this));
		m.add(i);
		i = new MenuItem("Quit");
		i.addActionListener(new Quit(this));
		m.add(i);
		bar.add(m);
		m = new Menu("Help");
		i = new MenuItem("About the Weather Viewer...");
		i.addActionListener(new About(this));
		m.add(i);
		bar.add(m);
		setMenuBar(bar);
		
		// Content
		JPanel left = new JPanel();
		BoxLayout bl = new BoxLayout(left, BoxLayout.Y_AXIS);
		left.setLayout(bl);
		Button tempButton = new Button("Graph the interval");
		tempButton.addActionListener(new GraphAction(this));
		left.add(tempButton);
		tempButton = new Button("Show various controls");
		tempButton.addActionListener(new DialAction());
		left.add(tempButton);
		left.add(new JScrollPane(panel));
		
		getContentPane().setLayout(new FlowLayout());
		getContentPane().add(left);
		// Pack it all in.
		pack();
	}
	
	/**
	 * Update map.
	 */
	public void updateMap()
	{
		Vector<Conditions> parse = new Vector<Conditions>(points);
		for(Conditions current : parse)
		{
			Calendar c = Calendar.getInstance();
			c.setTime(current.getDay());
			day = c.get(Calendar.DAY_OF_MONTH) + " "
				+ c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US) + ", " 
				+ c.get(Calendar.YEAR);
			if(map.containsKey(day))
			{
				map.get(day).add(current);
			}
			else
			{
				Vector<Conditions> vec = new Vector<Conditions>();
				vec.add(current);
				map.put(day, vec);
			}

			String year = "" + c.get(Calendar.YEAR);
			String month = c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US) + ", " + year;
			String week = "Week " + c.get(Calendar.WEEK_OF_MONTH) + ", " + month;
			
			// If the list is empty or we're adding a new year, we need to add ALL of the maps along the way.
			if( lists.isEmpty() || !lists.containsKey(year))
			{
				TreeMap<String, TreeMap<String, ArrayList<String> > > monthMap = new TreeMap<String, TreeMap<String, ArrayList<String> > >();
				TreeMap<String, ArrayList<String> > weekMap = new TreeMap<String, ArrayList<String> >();
				ArrayList<String> days = new ArrayList<String>();
				days.add(day);
				weekMap.put(week, days);
				monthMap.put(month, weekMap);
				lists.put(year, monthMap);
			}
			else if(!lists.get(year).containsKey(month))
			{ 
				// If we're adding a new month to an existing year, there's less to allocate.
				TreeMap<String, ArrayList<String> > weekMap = new TreeMap<String, ArrayList<String> >();
				ArrayList<String> days = new ArrayList<String>();
				days.add(day);
				weekMap.put(week, days);
				lists.get(year).put(month, weekMap);
			}
			else if(!lists.get(year).get(month).containsKey(week))
			{
				// If we're adding a new week to an existing month, there's only the week to allocate.
				ArrayList<String> days = new ArrayList<String>();
				days.add(day);
				lists.get(year).get(month).put(week, days);
			}
			else if(!lists.get(year).get(month).get(week).contains(day))
			{
				lists.get(year).get(month).get(week).add(day);
			}
			
		}
		
		// newtRoad is actually a pun based off my inability to say "Root Node" six times in a row.
		DefaultMutableTreeNode newtRoad = new DefaultMutableTreeNode();
		// For each year in the lists...
		for(Map.Entry<String, TreeMap<String, TreeMap<String, ArrayList<String> > > > y : lists.entrySet())
		{
			// Create a new node for the year.
			DefaultMutableTreeNode yearNode = new DefaultMutableTreeNode(y.getKey());
			// For each month in the current year...
			for(Map.Entry<String, TreeMap<String, ArrayList<String> >  > m : lists.get(y.getKey()).entrySet())
			{
				// Create a new node for the month.
				DefaultMutableTreeNode monthNode = new DefaultMutableTreeNode(m.getKey());
				// For each week in the month...
				for(Map.Entry<String, ArrayList<String> > w : lists.get(y.getKey()).get(m.getKey()).entrySet())
				{
					DefaultMutableTreeNode weekNode = new DefaultMutableTreeNode(w.getKey());
					for(String current : lists.get(y.getKey()).get(m.getKey()).get(w.getKey()))
					{
						weekNode.add(new DefaultMutableTreeNode(current));
					}
					monthNode.add(weekNode);
				}
				yearNode.add(monthNode);
			}
			newtRoad.add(yearNode);
		}
		DefaultTreeModel model = new DefaultTreeModel(newtRoad);
		panel.setModel(model);
		
		setPoints(new Vector<Conditions>(map.get(day)));
		repaint();
	}
	
	/**
	 * Sets the points.
	 *
	 * @param points the new points
	 */
	public void setPoints(Vector<Conditions> points)
	{
		this.points = points;
	}
	
	/**
	 * Gets the points.
	 *
	 * @return the points
	 */
	public Vector<Conditions> getPoints()
	{
		if(day == null || points == null || map == null || day.isEmpty() || map.isEmpty())
				return null;
		else if(graph == GraphWindow.DAY)
		{
			points = map.get(day);
		}
		else if(graph == GraphWindow.WEEK)
		{
			points = new Vector<Conditions>();
			String tiers[] = day.split(", ");
			for(String today : lists.get(tiers[2]).
					get(tiers[1] + ", " + tiers[2]).
					get(tiers[0] + ", " + tiers[1] + ", " + tiers[2]))
			{
				// For each day in the week, get the conditions that make up the day.
				Vector<Conditions> temp = map.get(today);
				// Run our data compositor on it.
				DataCalculator dc = new DataCalculator(new Vector<Conditions>(temp));
				TreeMap<String, Object> results = dc.computeData();
				Conditions point = new Conditions(
						(Float)results.get(DataCalculator.AVG_TEMP),
						(Float)results.get(DataCalculator.AVG_PRESSURE),
						(Float)results.get(DataCalculator.AVG_HUM),
						new Float((Double)results.get(DataCalculator.AVG_UV)),
						(Float)results.get(DataCalculator.TOTAL_RAIN),
						(Float)results.get(DataCalculator.AVG_WIND),
						(String)results.get(DataCalculator.WIND_DIR),
						temp.get(0).getDay());
				points.add(point);
			}
		}
		else if(graph == GraphWindow.MONTH)
		{
			points = new Vector<Conditions>();
			String tiers[] = day.split(", ");
			for(Map.Entry<String, ArrayList<String> >week : lists.get(tiers[1]).
					get(tiers[0] + ", " + tiers[1]).entrySet())
			{
				for(String today : week.getValue())
				{
					// For each day in the week, get the conditions that make up the day.
					Vector<Conditions> temp = map.get(today);
					// Run our data compositor on it.
					DataCalculator dc = new DataCalculator(new Vector<Conditions>(temp));
					TreeMap<String, Object> results = dc.computeData();
					Conditions point = new Conditions(
							(Float)results.get(DataCalculator.AVG_TEMP),
							(Float)results.get(DataCalculator.AVG_PRESSURE),
							(Float)results.get(DataCalculator.AVG_HUM),
							new Float((Double)results.get(DataCalculator.AVG_UV)),
							(Float)results.get(DataCalculator.TOTAL_RAIN),
							(Float)results.get(DataCalculator.AVG_WIND),
							(String)results.get(DataCalculator.WIND_DIR),
							temp.get(0).getDay());
					points.add(point);
				}
			}
		}
		else if(graph == GraphWindow.YEAR)
		{
			points = new Vector<Conditions>();
			// For each month in the year we're iterating through
			for(Map.Entry<String, TreeMap<String, ArrayList<String> > > m : lists.get(day).entrySet())
			{
				// Get a full month's worth of data, and add it to its own vector.
				Vector<Conditions> month = new Vector<Conditions>();
				for(Map.Entry<String, ArrayList<String> >week : m.getValue().entrySet())
				{
					for(String today : week.getValue())
					{
						// For each day in the week, get the conditions that make up the day.
						Vector<Conditions> temp = map.get(today);
						// Run our data compositor on it.
						DataCalculator dc = new DataCalculator(new Vector<Conditions>(temp));
						TreeMap<String, Object> results = dc.computeData();
						Conditions point = new Conditions(
								(Float)results.get(DataCalculator.AVG_TEMP),
								(Float)results.get(DataCalculator.AVG_PRESSURE),
								(Float)results.get(DataCalculator.AVG_HUM),
								new Float((Double)results.get(DataCalculator.AVG_UV)),
								(Float)results.get(DataCalculator.TOTAL_RAIN),
								(Float)results.get(DataCalculator.AVG_WIND),
								(String)results.get(DataCalculator.WIND_DIR),
								temp.get(0).getDay());
						month.add(point);
					}
				}
				// Composite the entire month into its own data point.
				DataCalculator dc = new DataCalculator(new Vector<Conditions>(month));
				TreeMap<String, Object> results = dc.computeData();
				Conditions point = new Conditions(
						(Float)results.get(DataCalculator.AVG_TEMP),
						(Float)results.get(DataCalculator.AVG_PRESSURE),
						(Float)results.get(DataCalculator.AVG_HUM),
						new Float((Double)results.get(DataCalculator.AVG_UV)),
						(Float)results.get(DataCalculator.TOTAL_RAIN),
						(Float)results.get(DataCalculator.AVG_WIND),
						(String)results.get(DataCalculator.WIND_DIR),
						month.get(0).getDay());
				points.add(point);
			}
		}
		return points;
	}
	
	/**
	 * Gets the day.
	 *
	 * @return the day
	 */
	public String getDay()
	{
		return day;
	}
	
	/**
	 * Sets the day.
	 *
	 * @param newDay the new day
	 */
	public void setDay(String newDay)
	{
		day = newDay;
	}
	
	/**
	 * The Class About.
	 *
	 * @author 1828107
	 */
	private static class About implements ActionListener
	{
		
		/** The frame. */
		private JFrame frame;
		
		/**
		 * Instantiates a new about.
		 *
		 * @param ok the ok
		 */
		public About(JFrame ok)
		{
			frame = ok;
		}
		
		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent arg0) 
		{
			JOptionPane.showMessageDialog(frame, "Java XML-parsing weather station viewer, Version 0.0.1");
		}
	}
	
	/**
	 * The Class Open.
	 *
	 * @author 1828107
	 */
	private static class Open implements ActionListener
	{
		
		/** The frame. */
		private SelectionWindow frame;
		
		/**
		 * Instantiates a new open.
		 *
		 * @param frame the frame
		 */
		public Open(SelectionWindow frame)
		{
			this.frame = frame;
		}
		
		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed( ActionEvent event)
		{
			JFileChooser f = new JFileChooser();
			Vector<Conditions> points = frame.getPoints();
			f.setMultiSelectionEnabled(true);
			int answer = f.showOpenDialog(frame);
			if(answer == JFileChooser.APPROVE_OPTION)
			{
				File[] list = f.getSelectedFiles();
				for(File current : list)
				{
					XMLParser p = new XMLParser(current.getAbsolutePath());
					Vector<Conditions> ls = p.parse();
					if(points == null)
						points = new Vector<Conditions>();
					if(ls != null)
					points.addAll(ls);
				}
				frame.setPoints(points);
				frame.updateMap();
			}
			// If points isn't set, let's at least init it.
			else if(points == null)
			{
				Vector<Conditions> array = new Vector<Conditions>();
				frame.setPoints(array);
			}
		}
	}
	
	/**
	 * The Class Quit.
	 *
	 * @author 1828107
	 */
	private static class Quit implements ActionListener
	{
		
		/** The frame. */
		private JFrame frame;
		
		/**
		 * Instantiates a new quit.
		 *
		 * @param frame the frame
		 */
		public Quit(JFrame frame)
		{
			this.frame = frame;
		}
		
		/**
		 * Action performed.
		 * 
		 * @param event
		 *            the event
		 */
		public void actionPerformed( ActionEvent event)
		{
			frame.dispose();
		}
	}
	
	/**
	 * The Class GraphAction.
	 *
	 * @author 1828107
	 */
	private static class GraphAction implements ActionListener
	{
		
		/** The frame. */
		SelectionWindow frame;
		
		/**
		 * Instantiates a new graph action.
		 *
		 * @param frame the frame
		 */
		public GraphAction(SelectionWindow frame)
		{
			this.frame = frame;
		}
	    // event handler method
		/**
		 * Action performed.
		 * 
		 * @param event
		 *            the event
		 */
	    public void actionPerformed( ActionEvent event )
	    {
	    	if(frame.getDay() == null)
	    	{
	    		JOptionPane.showMessageDialog(frame, new JLabel("Oops, it looks like you forgot to initialize data! Please go to the file menu and open one or more valid XML files."));
	    	}
	    	else if( frame.getDay().isEmpty())
	    	{
	    		JOptionPane.showMessageDialog(frame, new JLabel("Please select a Year, Month, Week or Day to graph."));
	    	}
	    	else
	    	{
		    	GraphWindow g = new GraphWindow(frame.getPoints(), frame.getDay(), frame.getGraph());
		    	g.setSize(1024,768);
		    	g.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		    	g.setVisible(true);
	    	}
	    }
	}
	
	/**
	 * The Class listChooser.
	 *
	 * @author 1828107
	 */
	private static class listChooser implements javax.swing.event.TreeSelectionListener
	{
		
		/** The frame. */
		private SelectionWindow frame;
		
		/**
		 * Instantiates a new list chooser.
		 *
		 * @param frame the frame
		 */
		public listChooser(SelectionWindow frame)
		{
			this.frame = frame;
		}

		/* (non-Javadoc)
		 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
		 */
		@Override
		/**
		 * 
		 */
		public void valueChanged(TreeSelectionEvent e) 
		{
			JTree tree = (JTree)e.getSource();
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
			
			if(node != null)
			{
				switch(node.getLevel())
				{
				case 1:
					frame.setGraph(GraphWindow.YEAR);
					frame.setDay((String)node.getUserObject());
					break;
				case 2:
					frame.setGraph(GraphWindow.MONTH);
					frame.setDay((String)node.getUserObject());
					break;
				case 3:
					frame.setGraph(GraphWindow.WEEK);
					frame.setDay((String)node.getUserObject());
					break;
				case 4:
					frame.setGraph(GraphWindow.DAY);
					frame.setDay((String)node.getUserObject());
					break;
				default:
					frame.setGraph(GraphWindow.DAY);
					frame.setDay("");
					break;
				}
			}
		}

		
	}
	
	/**
	 * Gets the graph.
	 *
	 * @return the graph
	 */
	public int getGraph() {
		return graph;
	}

	/**
	 * Sets the graph.
	 *
	 * @param graph the graph to set
	 */
	public void setGraph(int graph) {
		this.graph = graph;
	}

/**
 * The Class DialAction.
 *
 * @author 1828107
 */
	private static class DialAction implements ActionListener
	{
		
		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed( ActionEvent event)
		{
			DialWindow d = new DialWindow();
			d.setSize(800,600);
			d.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			d.setVisible(true);
		}
	}

/**
 * Instantiates a new selection window.
 *
 * @param gc the gc
 */
	public SelectionWindow(GraphicsConfiguration gc) {
		super(gc);
	}

/**
 * Instantiates a new selection window.
 *
 * @param title the title
 * @throws HeadlessException the headless exception
 */
	public SelectionWindow(String title) throws HeadlessException {
		super(title);
	}

/**
 * Instantiates a new selection window.
 *
 * @param title the title
 * @param gc the gc
 */
	public SelectionWindow(String title, GraphicsConfiguration gc) {
		super(title, gc);
	}

}
