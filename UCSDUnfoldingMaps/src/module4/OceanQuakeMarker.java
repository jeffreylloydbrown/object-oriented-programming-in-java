package module4;

import de.fhpotsdam.unfolding.data.PointFeature;
import processing.core.PGraphics;

/** Implements a visual marker for ocean earthquakes on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Jeff Brown
 *
 */
public class OceanQuakeMarker extends EarthquakeMarker {
	
	public OceanQuakeMarker(PointFeature quake) {
		super(quake);
		
		// setting field in earthquake marker
		isOnLand = false;
	}
	

	@Override
	public void drawEarthquake(PGraphics pg, float x, float y) {
		// Drawing a centered square for Ocean earthquakes
		// DO NOT set the fill color.  That will be set in the EarthquakeMarker
		// class to indicate the depth of the earthquake.
		// Simply draw a centered square.
		
		// HINT: Notice the radius variable in the EarthquakeMarker class
		// and how it is set in the EarthquakeMarker constructor

        float offset = radius/2;
		
		pg.rect(x-offset, y-offset, radius, radius);

		// racent quakes get a black X drawn over the marker.
        if (isRecent) {
            pg.stroke(0, 0, 0);
            pg.strokeWeight(2);
            pg.line(x-offset, y-offset, x+offset, y+offset);
            pg.line(x-offset, y+offset, x+offset, y-offset);
        }
	}
	


	

}
