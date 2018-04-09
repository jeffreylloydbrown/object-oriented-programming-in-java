package module4;

import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PGraphics;

/** Implements a visual marker for earthquakes on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Jeff Brown
 *
 */
public abstract class EarthquakeMarker extends SimplePointMarker
{
	
	// Did the earthquake occur on land?  This will be set by the subclasses.
	protected boolean isOnLand;

	// SimplePointMarker has a field "radius" which is inherited
	// by Earthquake marker:
	// protected float radius;
	//
	// You will want to set this in the constructor, either
	// using the thresholds below, or a continuous function
	// based on magnitude. 

	// Here are my radius sizes to use.
    private static final int baseRadius = 5;
    public static final float SMALL_MAGNITUDE_RADIUS = 1*baseRadius;
    public static final float LIGHT_MAGNITUDE_RADIUS = 2*baseRadius;
    public static final float MODERATE_MAGNITUDE_RADIUS = 3*baseRadius;

	/** Greater than or equal to this threshold is a moderate earthquake */
	public static final float THRESHOLD_MODERATE = 5;
	/** Greater than or equal to this threshold is a light earthquake */
	public static final float THRESHOLD_LIGHT = 4;

	/** Greater than or equal to this threshold is an intermediate depth */
	public static final float THRESHOLD_INTERMEDIATE = 70;
	/** Greater than or equal to this threshold is a deep depth */
	public static final float THRESHOLD_DEEP = 300;

	// ADD constants for colors

	
	// abstract method implemented in derived classes
	public abstract void drawEarthquake(PGraphics pg, float x, float y);
		
	
	// constructor
	public EarthquakeMarker (PointFeature feature) 
	{
		super(feature.getLocation());

		// Add a radius property and then set the properties
		java.util.HashMap<String, Object> properties = feature.getProperties();
		float magnitude = Float.parseFloat(properties.get("magnitude").toString());
		if (magnitude >= THRESHOLD_MODERATE) {
		    this.radius = MODERATE_MAGNITUDE_RADIUS;
        } else if (magnitude >= THRESHOLD_LIGHT) {
		    this.radius = LIGHT_MAGNITUDE_RADIUS;
        } else {
		    this.radius = SMALL_MAGNITUDE_RADIUS;
        }
		properties.put("radius", this.radius );
		setProperties(properties);
	}

	// calls abstract method drawEarthquake and then checks age and draws X if needed
	public void draw(PGraphics pg, float x, float y) {
		// save previous styling
		pg.pushStyle();
			
		// determine color of marker from depth
		colorDetermine(pg);
		
		// call abstract method implemented in child class to draw marker shape
		drawEarthquake(pg, x, y);
		
		// OPTIONAL TODO: draw X over marker if within past day		
		
		// reset to previous styling
		pg.popStyle();
		
	}
	
	// determine color of marker from depth, and set pg's fill color 
	// using the pg.fill method.
	// We suggest: Deep = red, intermediate = blue, shallow = yellow
	// But this is up to you, of course.
	// You might find the getters below helpful.
	private void colorDetermine(PGraphics pg) {
		// it is silly to re-execute this over and over from draw().
        // the color doesn't change so it should be set in the constructor.
        // but the color method isn't defined in the constructor since
        // we don't inherit from PApplet.  I thought long about writing
        // my own shifter to build a correctly formatted int for a color code,
        // so I could set values in a private field and this method would just
        // be a call to pg.fill and pg.stroke to use that field.
        float depth = getDepth();
        int red = pg.color(255,0,0);
        int yellow = pg.color(255, 255,0);
        int blue = pg.color(0,0,255);
        if (depth >= THRESHOLD_DEEP) {
            pg.fill(red);
            pg.stroke(red);
        } else if (depth >= THRESHOLD_INTERMEDIATE) {
            pg.fill(blue);
            pg.stroke(blue);
        } else { // shallow
            pg.fill(yellow);
            pg.stroke(yellow);
        }
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
		return (String) getProperty("title");	
		
	}
	
	public float getRadius() {
		return Float.parseFloat(getProperty("radius").toString());
	}
	
	public boolean isOnLand()
	{
		return isOnLand;
	}
	
	
}
