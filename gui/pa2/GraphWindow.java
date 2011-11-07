package gui.pa2;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.*;

@SuppressWarnings("serial")
public class GraphWindow extends JFrame {
	
	public GraphWindow(String title) {
		super(title);
		Graphics g = getGraphics();
		g.setColor(Color.BLUE);
		JLabel l = new JLabel("Content coming soon!");
		getContentPane().add(l);
	}
	
	public GraphWindow(Conditions[] points)
	{
		super("Rainfall Graph");
		//JLabel l = new JLabel("Content coming soon!",(int) CENTER_ALIGNMENT);
		//getContentPane().add(l);
		GraphCanvas g = new GraphCanvas(points);
		getContentPane().add(g);
	}

}
