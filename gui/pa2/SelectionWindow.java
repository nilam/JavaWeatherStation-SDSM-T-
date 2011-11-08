package gui.pa2;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class SelectionWindow extends JFrame {
	
	private Conditions[] points;

	public SelectionWindow() throws HeadlessException {
		super();
		
		// Menu bar initialization
		MenuBar bar = new MenuBar();
		Menu m = new Menu("File");
		MenuItem i = new MenuItem("Quit");
		i.addActionListener(new Quit(this));
		m.add(i);
		i = new MenuItem("Add source file...");
		i.addActionListener(new Open(this));
		m.add(i);
		bar.add(m);
		setMenuBar(bar);
		
		// Content
		getContentPane().setLayout(new FlowLayout());
		Button tempButton = new Button("Graph the interval");
		tempButton.addActionListener(new GraphAction(points));
		getContentPane().add(tempButton);
		tempButton = new Button("Show various controls");
		tempButton.addActionListener(new DialAction());
		getContentPane().add(tempButton);
		
		// Pack it all in.
		pack();
	}
	
	public void setPoints(Conditions[] points)
	{
		this.points = points;
	}
	
	public Conditions[] getPoints()
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
			Conditions[] array = new Conditions[5];
			frame.setPoints(array);
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
		Conditions[] points;
		public GraphAction(Conditions[] points)
		{
			this.points = points;
		}
	    // event handler method
	    public void actionPerformed( ActionEvent event )
	    {
	    	if(points == null) points = new Conditions[5];
	    	for(int i = 0; i < points.length; ++i)
	    	{
	    		points[i] = new Conditions();
	    	}
	    	GraphWindow g = new GraphWindow(points);
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
