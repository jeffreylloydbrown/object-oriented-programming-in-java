package module4;

import de.fhpotsdam.unfolding.data.PointFeature;
import processing.core.PConstants;
import processing.core.PGraphics;

/** Implements a visual marker for land earthquakes on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Jeff Brown
 *
 */
public class LandQuakeMarker extends EarthquakeMarker {
	
	
	public LandQuakeMarker(PointFeature quake) {
		
		// calling EarthquakeMarker constructor
		super(quake);
		
		// setting field in earthquake marker
		isOnLand = true;
	}


	@Override
	public void drawEarthquake(PGraphics pg, float x, float y) {
		// Draw a centered circle for land quakes
		// DO NOT set the fill color here.  That will be set in the EarthquakeMarker
		// class to indicate the depth of the earthquake.
		// Simply draw a centered circle.
		
		// HINT: Notice the radius variable in the EarthquakeMarker class
		// and how it is set in the EarthquakeMarker constructor

        pg.ellipseMode(PConstants.CENTER);
		pg.ellipse(x, y, radius, radius);

		if (isRecent) {
            // recent earthquakes get a black X drawn over them.  For circles,
            // we need to find the endpoints from the center.
            // with right triangle at 0,0, x would be radius*cos(45 degrees).
            // y would be radius*sin(45 degrees).
            float xOffset = radius/2 * COS_PI_4;
            float yOffset = radius/2 * SIN_PI_4;
            pg.stroke(0, 0, 0);
            pg.strokeWeight(2);
            pg.line(x-xOffset, y-yOffset, x+xOffset, y+yOffset);
            pg.line(x-xOffset, y+yOffset, x+xOffset, y-yOffset);
        }
	}
	

	// Get the country the earthquake is in
	public String getCountry() {
		return (String) getProperty("country");
	}



		
}