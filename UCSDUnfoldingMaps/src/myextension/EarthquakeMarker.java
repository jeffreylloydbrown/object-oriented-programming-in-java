package myextension;

import de.fhpotsdam.unfolding.data.PointFeature;
import processing.core.PConstants;
import processing.core.PGraphics;

/** Implements a visual marker for earthquakes on an earthquake map
 *
 * @author UC San Diego Intermediate Software Development MOOC team
 *
 */
public abstract class EarthquakeMarker extends CommonMarker implements Comparable<EarthquakeMarker>
{
	
	// Did the earthquake occur on land?  This will be set by the subclasses.
	protected boolean isOnLand;

	protected Depth depth;

	protected boolean isRecent;

	// The radius of the Earthquake marker
	// You will want to set this in the constructor, either
	// using the thresholds below, or a continuous function
	// based on magnitude. 
	protected float radius;
	
	// constants for distance
	protected static final float kmPerMile = 1.6f;

	/** Greater than or equal to this threshold is an intermediate depth */
	public static final float THRESHOLD_INTERMEDIATE = 70;
	/** Greater than or equal to this threshold is a deep depth */
	public static final float THRESHOLD_DEEP = 300;

	// abstract method implemented in derived classes
	public abstract void drawEarthquake(PGraphics pg, float x, float y);
		
	
	// constructor
	public EarthquakeMarker (PointFeature feature) 
	{
		super(feature.getLocation());
		// Add a radius property and then set the properties
		java.util.HashMap<String, Object> properties = feature.getProperties();
		float magnitude = Float.parseFloat(properties.get("magnitude").toString());
		properties.put("radius", 2*magnitude );
		setProperties(properties);
		this.radius = 1.75f*getMagnitude();
		this.depth = calculateDepth();
		this.isRecent = isRecent();
	}

	private Depth calculateDepth() {
		float depth = this.getDepth();

		if (depth < THRESHOLD_INTERMEDIATE) {
			return Depth.SHALLOW;
		}
		else if (depth < THRESHOLD_DEEP) {
			return Depth.INTERMEDIATE;
		}
		else {
			return Depth.DEEP;
		}
	}

	private boolean isRecent() {
		String age = getStringProperty("age");
		return "Past Hour".equals(age) || "Past Day".equals(age);
	}

	// Used by subclasses to help them ultimately figure our their marker visibility.
	protected boolean depthAndRecencyVisibility(EarthquakeCityMap ecm) {
		boolean withDepth =
				(ecm.getShowShallowQuakes() && depth == Depth.SHALLOW) ||
						(ecm.getShowIntermediateQuakes() && depth == Depth.INTERMEDIATE) ||
						(ecm.getShowDeepQuakes() && depth == Depth.DEEP)
		;
		return ecm.getShowOnlyRecentQuakes() ? withDepth && isRecent : withDepth;
	}

	// this is largest to the smallest order
	public int compareTo(EarthquakeMarker m)
	{
		return Float.compare(m.getMagnitude(), this.getMagnitude());
	}
	
	// calls abstract method drawEarthquake and then checks age and draws X if needed
	@Override
	public void drawMarker(PGraphics pg, float x, float y) {
		// save previous styling
		pg.pushStyle();
			
		// determine color of marker from depth
		pg.fill(depth.toColor());
		
		// call abstract method implemented in child class to draw marker shape
		drawEarthquake(pg, x, y);
		
		// IMPLEMENT: add X over marker if within past day
		if (isRecent) {
			pg.strokeWeight(2);
			int buffer = 2;
			pg.line(x-(radius+buffer), 
					y-(radius+buffer), 
					x+radius+buffer, 
					y+radius+buffer);
			pg.line(x-(radius+buffer), 
					y+(radius+buffer), 
					x+radius+buffer, 
					y-(radius+buffer));
		}
		
		// reset to previous styling
		pg.popStyle();
	}

	/** Show the title of the earthquake if this marker is selected */
	public void showTitle(PGraphics pg, float x, float y)
	{
		String title = getTitle();
		pg.pushStyle();
		
		pg.rectMode(PConstants.CORNER);
		
		pg.stroke(110);
		pg.fill(255,255,255);
		pg.rect(x, y + 15, pg.textWidth(title) +6, 18, 5);
		
		pg.textAlign(PConstants.LEFT, PConstants.TOP);
		pg.fill(0);
		pg.text(title, x + 3 , y +18);

		pg.popStyle();
	}

	
	/**
	 * Return the "threat circle" radius, or distance up to 
	 * which this earthquake can affect things, for this earthquake.   
	 * DISCLAIMER: this formula is for illustration purposes
	 *  only and is not intended to be used for safety-critical 
	 *  or predictive applications.
	 */
	public double threatCircle() {	
		double miles = 20.0f * Math.pow(1.8, 2*getMagnitude()-5);
		return (miles * kmPerMile);
	}

	/** toString
	 * Returns an earthquake marker's string representation
	 * @return the string representation of an earthquake marker.
	 */
	public String toString()
	{
		return getTitle();
	}
	/*
	 * getters for earthquake properties
	 */
	
	public float getMagnitude() {
		return Float.parseFloat(getProperty("magnitude").toString());
	}
	
	public float getDepth() {
		return Float.parseFloat(getProperty("depth").toString());	
	}
	
	public String getTitle() {
		return getProperty("title") + ", depth " + getDepth();
	}
	
	public float getRadius() {
		return Float.parseFloat(getProperty("radius").toString());
	}
	
	public boolean isOnLand()
	{
		return isOnLand;
	}
	
} // EarthquakeMarker
