/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.pa2;

import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.util.Vector;
import java.util.TreeMap;
import javax.swing.BoxLayout;

/**
 *
 * @author jmk
 */
public class InfoWindow extends JFrame {
    
    public InfoWindow( Vector<Conditions> points, int graph, String time )
    {
        DataCalculator calc = new DataCalculator(points);
        TreeMap<String,Object> data = calc.computeData();
        
        JPanel contents = new JPanel();
        
        contents.setLayout( new BoxLayout(contents, BoxLayout.PAGE_AXIS));
        
        for( Map.Entry<String, Object> current : data.entrySet() )
        {
            if( current.getKey() != null && current.getValue() != null)
            {
                JLabel temp = new JLabel( current.getKey().trim() + ": " + current.getValue());
                contents.add(temp);
            }
        }
        
        setContentPane(contents);
        setTitle("Summary for: " + time );
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        pack();
    }
}
