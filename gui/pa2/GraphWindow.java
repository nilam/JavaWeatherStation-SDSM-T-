package gui.pa2;

import java.awt.Color;
import java.awt.Graphics;

import java.util.Vector;

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
		//JLabel l = new JLabel("Content coming soon!",(int) CENTER_ALIGNMENT);
		//getContentPane().add(l);
		GraphCanvas g = new GraphCanvas(points);
		getContentPane().add(g);
	}

}
