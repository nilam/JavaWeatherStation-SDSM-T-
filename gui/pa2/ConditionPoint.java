/*
 * 
 */
package gui.pa2;

import gui.pa2.Conditions;
// TODO: Auto-generated Javadoc

/**
 * The Class ConditionPoint.
 *
 * @author $
 * @see
 * @version
 */
public class ConditionPoint implements Comparable<ConditionPoint> {
	
	/** The x. */
	private int x;
	
	/** The y. */
	private int y;
    
    /** The data. */
    private Conditions data;
    
    /**
     * Instantiates a new condition point.
     *
     * @param _x the _x
     * @param _y the _y
     * @param val the val
     */
    public ConditionPoint(int _x, int _y, Conditions val)
    {
    	x = _x;
    	y = _y;
    	data = val;
    }

	
	/**
	 * Compare to.
	 *
	 * @param arg0 the arg0
	 * @return the int
	 * @Override
	 */
	public int compareTo(ConditionPoint arg0) {
		return x - arg0.x;
	}

	/**
	 * Gets the x.
	 *
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Sets the x.
	 *
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Gets the y.
	 *
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sets the y.
	 *
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Gets the data.
	 *
	 * @return the point
	 */
	public Conditions getData() {
		return data;
	}

	/**
	 * Sets the data.
	 *
	 * @param point the point to set
	 */
	public void setData(Conditions point) {
		this.data = point;
	}

}
