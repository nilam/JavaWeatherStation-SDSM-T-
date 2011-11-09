package gui.pa2;

import java.awt.Button;
import javax.swing.JFileChooser;
import java.awt.FlowLayout;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class SelectionWindow extends JFrame {
	
	private Vector<Conditions> points;

	public SelectionWindow() throws HeadlessException {
		super();
		points = null;
		
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
		setMenuBar(bar);
		
		// Content
		getContentPane().setLayout(new FlowLayout());
		Button tempButton = new Button("Graph the interval");
		tempButton.addActionListener(new GraphAction(this));
		getContentPane().add(tempButton);
		tempButton = new Button("Show various controls");
		tempButton.addActionListener(new DialAction());
		getContentPane().add(tempButton);
		
		// Pack it all in.
		pack();
	}
	
	public void setPoints(Vector<Conditions> points)
	{
		this.points = points;
	}
	
	public Vector<Conditions> getPoints()
	{
		return points;
	}
	
	private static class Open implements ActionListener
	{
		private SelectionWindow frame;
		public Open(SelectionWindow frame)
		{
			this.frame = frame;
		}
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
			}
			// If points isn't set, let's at least init it.
			else if(points == null)
			{
				Vector<Conditions> array = new Vector<Conditions>();
				frame.setPoints(array);
			}
		}
	}
	
	private static class Quit implements ActionListener
	{
		private JFrame frame;
		public Quit(JFrame frame)
		{
			this.frame = frame;
		}
		public void actionPerformed( ActionEvent event)
		{
			frame.dispose();
		}
	}
	
	private static class GraphAction implements ActionListener
	{
		SelectionWindow frame;
		public GraphAction(SelectionWindow frame)
		{
			this.frame = frame;
		}
	    // event handler method
	    public void actionPerformed( ActionEvent event )
	    {
	    	GraphWindow g = new GraphWindow(frame.getPoints());
	    	g.setSize(800,600);
	    	g.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	    	g.setVisible(true);
	    }
	}
	private static class DialAction implements ActionListener
	{
		public void actionPerformed( ActionEvent event)
		{
			DialWindow d = new DialWindow();
			d.setSize(800,600);
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
