package gui.pa2;

import gui.pa2.Conditions;

public class ConditionPoint implements Comparable<ConditionPoint> {
	
	private int x;
	private int y;
    private Conditions data;
    
    public ConditionPoint(int _x, int _y, Conditions val)
    {
    	x = _x;
    	y = _y;
    	data = val;
    }

	@Override
	public int compareTo(ConditionPoint arg0) {
		return x - arg0.x;
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * @return the point
	 */
	public Conditions getData() {
		return data;
	}

	/**
	 * @param point the point to set
	 */
	public void setData(Conditions point) {
		this.data = point;
	}

}
