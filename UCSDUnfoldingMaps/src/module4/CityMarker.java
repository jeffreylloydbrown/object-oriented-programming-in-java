package module4;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PGraphics;

/** Implements a visual marker for cities on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Jeff Brown
 *
 */
public class CityMarker extends SimplePointMarker {
	
	// The size of the triangle marker
	// It's a good idea to use this variable in your draw method
	public static final int TRI_SIZE = 5;  
	
	public CityMarker(Location location) {
		super(location);
	}
	
	
	public CityMarker(Feature city) { super(((PointFeature)city).getLocation(), city.getProperties()); }
	
	
	
	/**
	 * Implementation of method to draw marker on the map.
	 */
	public void draw(PGraphics pg, float x, float y) {
		// Save previous drawing style
		pg.pushStyle();

		//System.out.println(getCity());
		
		// Add code to draw a triangle to represent the CityMarker
		// HINT: pg is the graphics object on which you call the graphics
		// methods.  e.g. pg.fill(255, 0, 0) will set the color to red
		// x and y are the center of the object to draw. 
		// They will be used to calculate the coordinates to pass
		// into any shape drawing methods.  
		// e.g. pg.rect(x, y, 10, 10) will draw a 10x10 square
		// whose upper left corner is at position x, y
		// Check out the processing documentation for more methods
		int red = pg.color(255, 0, 0);
		pg.fill(red);
		pg.stroke(red); // no marker outlines blotting out fill if too many markers close together.

		// Need to figure out triangle vertices for equilateral triangle with center (x,y).
        // Assignment is using marker radius as the length of the side of the triangle.
        // https://www.quora.com/What-is-the-distance-between-the-centre-of-an-equilateral-triangle-and-any-of-its-vertices
        // says distance from (x,y) to any vertex in an equilateral triangle is R=sideLength/sqrt(3).
        // It also says the center to the baseline is r=R/2.  Our triangles will "point up", so
        // the single vertex above the center (x,y) will be (x, y-R)---remember on screen Y grows down the screen---
        // and the two vertices on the base line below the center (x,y) are at (x-radius/2, y+r) & (x+radius/2, y+r).
        float R = radius/((float) Math.sqrt(3.0));
        pg.triangle(x, y-R, x-radius/2, y+R/2, x+radius/2, y+R/2);
		
		// Restore previous drawing style
		pg.popStyle();
	}
	
	/* Local getters for some city properties.  You might not need these 
	 * in module 4. 	 */
	public String getCity()
	{
		return getStringProperty("name");
	}
	
	public String getCountry()
	{
		return getStringProperty("country");
	}
	
	public float getPopulation()
	{
		return Float.parseFloat(getStringProperty("population"));
	}
	
}
