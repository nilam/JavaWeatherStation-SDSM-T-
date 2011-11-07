package gui.pa2;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		SelectionWindow s = new SelectionWindow();
		s.setSize(800,600);
		s.pack();
		s.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		s.setVisible(true);
	}

}
