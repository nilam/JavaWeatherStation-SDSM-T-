package gui.pa2;

/* Note: I did this in Eclipse, and let Eclipse generate some of the getters/setters.
 * This is not because I don't understand how, but because it's tedious.
 */
public class Conditions {
	private float temperature;
	private float pressure;
	private float humidity;
	private float uv;
	private float rain;
	private float wind;
	private String windAngle;
	
	/**
	 * Create an invalid condition. This allows you to only partially define a condition without worrying about
	 * how to describe missing conditions at a given data point.
	 */
	public Conditions()
	{
		temperature = -500; // Absolute 0 is ~459 F
		pressure = 0; // 0 mmHg is a vacuum.
		humidity = -1; // Humidity is a percent.
		uv = -1; // UV Index is only defined between 0 and 15
		rain = -1; // Rainfall can't be less than 0
		wind = -1; // Wind in the backwards direction should go the other way.
		windAngle = "Undefined Direction"; // Normally one of the 16 values.
	}
	
	public Conditions(float temperature, float pressure, float humidity,
			float uv, float rain, float wind, String windAngle) 
	{
		this.temperature = temperature;
		this.pressure = pressure;
		this.humidity = humidity;
		this.uv = uv;
		this.rain = rain;
		this.wind = wind;
		this.windAngle = windAngle;
	}
	
	public String toString()
	{
		String out = "";
		String nl;
		
		try
		{
			nl = System.getProperty("line.separator");
		}
		catch (SecurityException e)
		{
			nl = "\n";
		}
		
		if(temperature != -999) out += "Temperature: " + temperature + nl;
		if(pressure != 0) out += "Pressure: " + pressure + nl;
		if(humidity != -1) out += "Humidity: " + humidity + nl;
		if(uv >= 0) out += "UV Index: " + uv + nl;
		if(rain >= 0) out += "There are " + rain + " Inches of rain." + nl;
		if(wind >= 0) out += "Wind: " + wind + "Miles / Hour, in " + windAngle + nl;
		
		return out;
	}

	/**
	 * @return the temperature
	 */
	public float getTemperature() {
		return temperature;
	}
	
	/**
	 * @param temperature the temperature to set
	 */
	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}
	
	/**
	 * @return the pressure
	 */
	public float getPressure() {
		return pressure;
	}
	
	/**
	 * @param pressure the pressure to set
	 */
	public void setPressure(float pressure) {
		this.pressure = pressure;
	}
	
	/**
	 * @return the humidity
	 */
	public float getHumidity() {
		return humidity;
	}
	
	/**
	 * @param humidity the humidity to set
	 */
	public void setHumidity(float humidity) {
		this.humidity = humidity;
	}
	
	/**
	 * @return the uv
	 */
	public float getUv() {
		return uv;
	}
	
	/**
	 * @param uv the UV Index to set, arbitrary number between 0 and 15
	 */
	public void setUv(float uv) {
		this.uv = uv;
	}
	
	/**
	 * @return Rainfall at this data point, in inches.
	 */
	public float getRain() {
		return rain;
	}
	
	/**
	 * @param rain The amount of rainfall at this data point, in inches.
	 */
	public void setRain(float rain) {
		this.rain = rain;
	}
	
	/**
	 * @return Wind speed at this data point, in Miles / Hour.
	 */
	public float getWind() {
		return wind;
	}
	
	/**
	 * @param wind Speed of the wind, in Miles / Hour.
	 */
	public void setWind(float wind) {
		this.wind = wind;
	}
	
	/**
	 * @return the current wind direction, as a string.
	 */
	public String getWindAngle() {
		return windAngle;
	}
	
	/**
	 * @param windAngle the windAngle to set
	 */
	public void setWindAngle(String windAngle) {
		this.windAngle = windAngle;
	}
	
	

}
