package gui.pa2;

import javax.swing.JFrame;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class GraphWindow extends JFrame {

	public GraphWindow(String title) {
		super(title);
		JLabel l = new JLabel("Content coming soon!");
		getContentPane().add(l);
	}
	
	public GraphWindow()
	{
		super("Rainfall Graph");
		JLabel l = new JLabel("Content coming soon!",(int) CENTER_ALIGNMENT);
		getContentPane().add(l);
	}

}
