package gui.pa2;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class SelectionWindow extends JFrame {

	public SelectionWindow() throws HeadlessException {
		super();
		getContentPane().setLayout(new FlowLayout());
		Button tempButton = new Button("Graph the interval");
		tempButton.addActionListener(new GraphAction());
		getContentPane().add(tempButton);
		tempButton = new Button("Show various controls");
		tempButton.addActionListener(new DialAction());
		getContentPane().add(tempButton);
		pack();
	}
	
	
	private static class GraphAction implements ActionListener
	{
	    // event handler method
	    public void actionPerformed( ActionEvent event )
	    {
	    	GraphWindow g = new GraphWindow();
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
