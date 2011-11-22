/*
 * 
 */

package gui.pa2;

import java.util.Calendar;
import java.util.Locale;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.CardLayout;
// TODO: Auto-generated Javadoc

/**
 * The Class InfoPane.
 *
 * @author 1828107
 */
@SuppressWarnings("serial")
public class InfoPane extends JPanel 
{
	
	/** The point. */
	private ConditionPoint point;
	
	/** The Constant FRONT_PANE. */
	public static final String FRONT_PANE = "front";
	
	/** The Constant DATA_PANE. */
	public static final String DATA_PANE = "data";
	
	/**
	 * Class Constructor;.
	 */
	public InfoPane()
	{
		super();
		point = null;
		
		// Card layout. Give 5px around the edges as a margin.
		setLayout(new CardLayout(5,5));
		
		// Labels to put on the front card.
		JLabel noSelect = new JLabel("Please click a point on the graph");
		JLabel noSelect0 = new JLabel("to see all conditions at that point.");
		
		// Front card itself.
		JPanel front = new JPanel();
		
		// Add the labels to the front card.
		front.add(noSelect);
		front.add(noSelect0);
		
		// Give the front card some kind of layout.
		BoxLayout b = new BoxLayout(front, BoxLayout.Y_AXIS);
		front.setLayout(b);
		add(front, FRONT_PANE);
	}
	
	/**
	 * Sets the point.
	 *
	 * @param point the new point
	 */
	public void setPoint(ConditionPoint point)
	{
		this.point = point;
	}
	
	/**
	 * Show info.
	 */
	public void showInfo()
	{
		// Don't let this panel try to print out a null point.
		if(point == null) 
		{
			CardLayout lay = (CardLayout)getLayout();
			lay.show(this, FRONT_PANE);
			return;
		}
		
		Conditions current = point.getData();
		Vector<JLabel> labels = new Vector<JLabel>();
		Vector<String> strings = new Vector<String>();
		Calendar c = Calendar.getInstance();
		c.setTime(current.getDay());
		
		strings.add("" + c.get(Calendar.DAY_OF_MONTH)+ " " + c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US )
				+ ", " + c.get(Calendar.YEAR) + " " + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE));
		
		if(current.getTemperature() != Conditions.INVALID_TEMP)
		{
			strings.add("Temperature: " + current.getTemperature() + " Degrees F");
		}
		
		if(current.getRain() != Conditions.INVALID_RAIN)
		{
			strings.add("Rainfall: " + current.getRain() + " inches" );
		}
		
		if(current.getWind() != Conditions.INVALID_WIND)
		{
			strings.add("Wind: " + current.getWind() + " MPH " + 
					(current.getWindAngle() == Conditions.INVALID_DIRECTION ? "" : current.getWindAngle()) );
		}
		
		if(current.getHumidity() != Conditions.INVALID_HUMIDITY)
		{
			strings.add("Humidity: " + current.getHumidity() + "%");
		}
		
		if(current.getPressure() != Conditions.INVALID_PRESSURE)
		{
			strings.add("Pressure: " + current.getPressure() + " in. Hg");
		}
		
		if(current.getUv() != Conditions.INVALID_UV)
		{
			strings.add("UV Index: " + current.getUv());
		}
		
		if(strings.isEmpty())
		{
			labels.add(new JLabel("Looks like you've selected an invalid point!"));
		}
		else
		{
			for(String str : strings)
			{
				labels.add(new JLabel(str));
			}
		}
		
		JPanel pane = new JPanel();
		
		for(JLabel lab : labels)
		{
			pane.add(lab);
		}
		BoxLayout b = new BoxLayout(pane, BoxLayout.PAGE_AXIS);
		pane.setLayout(b);
		CardLayout lay = (CardLayout)getLayout();
		add(pane, DATA_PANE);
		lay.show(this, DATA_PANE);
	}
}