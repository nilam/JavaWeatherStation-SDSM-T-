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
import javax.swing.tree.DefaultMutableTreeNode;

@SuppressWarnings("serial")
public class SelectionWindow extends JFrame {

	private Vector<Conditions> points;
	private TreeMap<String, Vector<Conditions>> map;
	private String day;
	private JTree tree;
	private JScrollPane scrollPane;
	private TreeMap<String, TreeMap<String, TreeMap<String, ArrayList<String>>>> yearMonthWeekDay;

	public SelectionWindow() throws HeadlessException {
		super();
		points = null;
		map = new TreeMap<String, Vector<Conditions>>();
		day = null;
		yearMonthWeekDay = new TreeMap<String, TreeMap<String, TreeMap<String, ArrayList<String>>>>();
		DefaultMutableTreeNode rootnode = new DefaultMutableTreeNode(
				"No Days Selected");
		tree = new JTree(rootnode);

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
		getContentPane().setLayout(new FlowLayout());
		Button tempButton = new Button("Graph the interval");
		tempButton.addActionListener(new GraphAction(this));
		getContentPane().add(tempButton);
		tempButton = new Button("Show various controls");
		tempButton.addActionListener(new DialAction());
		getContentPane().add(tempButton);
		scrollPane = new JScrollPane(tree);
		getContentPane().add(scrollPane);

		// Pack it all in.
		pack();
	}

	public void updateMap()
	{
		for(Conditions current : points)
		{
			Calendar c = Calendar.getInstance();
			c.setTime(current.getDay());
			day = c.get(Calendar.DAY_OF_MONTH) + " of " + c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US) + ", " + c.get(Calendar.YEAR);
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
			// First, check to see if the year is in the map.
			if(yearMonthWeekDay.isEmpty() || !yearMonthWeekDay.containsKey("" + c.get(Calendar.YEAR)) )
			{
				// It's not. Add Month, Week and Day to year.
				TreeMap<String, TreeMap<String,ArrayList<String>>> monthWeekDay = new TreeMap<String, TreeMap<String,ArrayList<String>>>(); 
				TreeMap<String, ArrayList<String>> weekDay = new TreeMap<String, ArrayList<String>>();
				ArrayList<String> day = new ArrayList<String>();
				day.add("" + c.get(Calendar.DAY_OF_MONTH) + " of " + c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US) + ", " + c.get(Calendar.YEAR));
				weekDay.put("Week " + c.get(Calendar.WEEK_OF_MONTH), day);
				monthWeekDay.put("" + c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US),weekDay);
				yearMonthWeekDay.put("" + c.get(Calendar.YEAR),monthWeekDay);
			}
			// Found the year. Is the Month already here?
			else if(!yearMonthWeekDay.get("" + c.get(Calendar.YEAR)).containsKey("" + c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US)))
			{
				// No. Add the week and day to the month, then add the month.
				TreeMap<String, ArrayList<String>> weekDay = new TreeMap<String, ArrayList<String>>();
				ArrayList<String> day = new ArrayList<String>();
				day.add("" + c.get(Calendar.DAY_OF_MONTH) + " of " + c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US) + ", " + c.get(Calendar.YEAR));
				weekDay.put("Week " + c.get(Calendar.WEEK_OF_MONTH), day);
				yearMonthWeekDay.get("" + c.get(Calendar.YEAR)).put("" + c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US),weekDay);
			}
			// Found year and month. Is the week here?
			else if(!yearMonthWeekDay.get("" + c.get(Calendar.YEAR)).get(""+ c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US)).containsKey("" + c.getDisplayName(Calendar.WEEK_OF_MONTH, Calendar.SHORT, Locale.US)))
			{
				// No week. Add the week to Year and Month.
				ArrayList<String> day = new ArrayList<String>();
				day.add("" + c.get(Calendar.DAY_OF_MONTH) + " of " + c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US) + ", " + c.get(Calendar.YEAR));
				yearMonthWeekDay.get("" + c.get(Calendar.YEAR)).get("" + c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US)).put("Week " + c.get(Calendar.WEEK_OF_MONTH),day);
			}
			else
			{
				yearMonthWeekDay.get("" + c.get(Calendar.YEAR)).get("" + c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US)).get("" + c.get(Calendar.WEEK_OF_MONTH)).add("" + c.get(Calendar.DAY_OF_MONTH) + " of " + c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US) + ", " + c.get(Calendar.YEAR));
			
			}
		}
		setPoints(new Vector<Conditions>(map.get(day)));

		DefaultMutableTreeNode newtroad = new DefaultMutableTreeNode();
		for( Map.Entry<String, TreeMap<String, TreeMap<String, ArrayList <String>>>> y : yearMonthWeekDay.entrySet())
		{
			DefaultMutableTreeNode Year = new DefaultMutableTreeNode(y.getKey());
			for( Map.Entry<String, TreeMap<String, ArrayList <String>>> m : y.getValue().entrySet())
			{
				DefaultMutableTreeNode Month = new DefaultMutableTreeNode(m.getKey()); 
				for(Map.Entry<String, ArrayList <String>> w : m.getValue().entrySet())
				{
					DefaultMutableTreeNode Week = new DefaultMutableTreeNode(w.getKey());
					for(String d : w.getValue())
					{
						DefaultMutableTreeNode Day = new DefaultMutableTreeNode(d);
						Week.add(Day);
					}
					Month.add(Week);
				}
				Year.add(Month);
			}
			newtroad.add(Year);
		}
		JViewport port = new JViewport();
		port.setView(new JTree(newtroad));
		scrollPane.setViewport(port);
		repaint();
	}

	public void setPoints(Vector<Conditions> points) {
		this.points = points;
	}

	public Vector<Conditions> getPoints() {
		return points;
	}

	public String getDay() {
		return day;
	}

	private static class About implements ActionListener {
		private JFrame frame;

		public About(JFrame ok) {
			frame = ok;
		}

		public void actionPerformed(ActionEvent arg0) {
			JOptionPane.showMessageDialog(frame,
					"Java XML-parsing weather station viewer, Version 0.0.1");
		}
	}

	private static class Open implements ActionListener {
		private SelectionWindow frame;

		public Open(SelectionWindow frame) {
			this.frame = frame;
		}

		public void actionPerformed(ActionEvent event) {
			JFileChooser f = new JFileChooser();
			Vector<Conditions> points = frame.getPoints();
			f.setMultiSelectionEnabled(true);
			int answer = f.showOpenDialog(frame);
			if (answer == JFileChooser.APPROVE_OPTION) {
				File[] list = f.getSelectedFiles();
				for (File current : list) {
					XMLParser p = new XMLParser(current.getAbsolutePath());
					Vector<Conditions> ls = p.parse();
					if (points == null)
						points = new Vector<Conditions>();
					if (ls != null)
						points.addAll(ls);
				}
				frame.setPoints(points);
				frame.updateMap();
			}
			// If points isn't set, let's at least init it.
			else if (points == null) {
				Vector<Conditions> array = new Vector<Conditions>();
				frame.setPoints(array);
			}
		}
	}

	private static class Quit implements ActionListener {
		private JFrame frame;

		public Quit(JFrame frame) {
			this.frame = frame;
		}

		public void actionPerformed(ActionEvent event) {
			frame.dispose();
		}
	}

	private static class GraphAction implements ActionListener {
		SelectionWindow frame;

		public GraphAction(SelectionWindow frame) {
			this.frame = frame;
		}

		// event handler method
		public void actionPerformed(ActionEvent event) {
			if (frame.getPoints() == null || frame.getPoints().isEmpty()) {
				JOptionPane
						.showMessageDialog(
								frame,
								new JLabel(
										"Oops, it looks like you forgot to initialize data! Please go to the file menu and open one or more valid XML files."));
			} else {
				GraphWindow g = new GraphWindow(frame.getPoints(),
						frame.getDay());
				g.setSize(800, 600);
				g.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				g.setVisible(true);
			}
		}
	}

	private static class DialAction implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			DialWindow d = new DialWindow();
			d.setSize(800, 600);
			d.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			d.setVisible(true);
		}
	}

	public SelectionWindow(GraphicsConfiguration gc) {
		super(gc);
	}

	public SelectionWindow(String title) throws HeadlessException {
		super(title);
	}

	public SelectionWindow(String title, GraphicsConfiguration gc) {
		super(title, gc);
	}

}
