/*
 * 
 */
package gui.pa2;
import javax.swing.UIManager;
import javax.swing.UIManager.*;
// TODO: Auto-generated Javadoc

/**
 * The Class Main.
 *
 * @author 1828107
 */
public class Main {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) 
	{

		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		    // If Nimbus is not available, you can set the GUI to another look and feel.
		}
		
		SelectionWindow s = new SelectionWindow();
		s.setSize(800,600);
		s.pack();
		s.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		s.setVisible(true);
	}

}
