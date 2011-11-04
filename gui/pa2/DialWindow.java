package gui.pa2;
import javax.swing.JFrame;
import javax.swing.JLabel;


@SuppressWarnings("serial")
public class DialWindow extends JFrame {
	
	public DialWindow()
	{
		super();
		JLabel l = new JLabel("Content coming soon!",(int)CENTER_ALIGNMENT);
		getContentPane().add(l);
	}

}
