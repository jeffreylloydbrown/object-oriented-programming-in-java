package module5;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.ScreenPosition;
import processing.core.PGraphics;

import java.util.ArrayList;
import java.util.List;

/** Implements a visual marker for ocean earthquakes on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Jeff Brown
 *
 */
public class OceanQuakeMarker extends EarthquakeMarker {

    // These are for drawing lines from me to all cities in the threat circle.
    // I was surprised to discover that I have to offset the screen coordinates!
    // The values passed into drawEarthquake() DO NOT MATCH the screen coordinate
    // of the quake epicenter, even when you look it up in drawEarthquake()!
    // So I have to translate the line based on the difference of the two positions.
    private List<Location> cityCoords;
    private UnfoldingMap map;
    private Location myLocation;  // DO NOT SAVE ScreenPosition here!!!

	public OceanQuakeMarker(PointFeature quake) {
		super(quake);
		
		// setting field in earthquake marker
		isOnLand = false;

		// Now figure out the screen position of each city within our
        // threat radius.  Remember those positions in `cityCoords`,
        // so we can draw lines to them later.
        List<Marker> cityMarkers = (List<Marker>) getProperty("cityMarkers");
        map = (UnfoldingMap) getProperty("map");
        myLocation = getLocation();
        double threatRadius = threatCircle();
        cityCoords = new ArrayList<Location>();
        for (Marker m : cityMarkers) {
            CityMarker city = (CityMarker) m;
            if (city.getDistanceTo(myLocation) <= threatRadius) {
                cityCoords.add(city.getLocation());
            }
        }
	}
	

	/** Draw the earthquake as a square */
	@Override
	public void drawEarthquake(PGraphics pg, float x, float y) {

        // save previous styling
        pg.pushStyle();

		pg.rect(x-radius, y-radius, 2*radius, 2*radius);

        // Draw lines from my coordinates to every city in my threat circle.
        // Coordinates got saved in the constructor, so this boils down to
        // drawing (or clearing if not clicked) a line from (x,y) to every
        // screen position in `cityCoords`.

        if (! cityCoords.isEmpty()) {
            pg.strokeWeight(3);
            // only show the line when we've clicked on this quake.  Otherwise
            // make it be "undrawn".
            if (clicked) {
                pg.stroke(0, 250, 0);
            } else {
                pg.noStroke();
            }

            // Here is where I figure out how to translate the screen position for
            // this quake and its drawn location.  This translation is applied below.
            ScreenPosition myPosition = map.getScreenPosition(myLocation);
            float offsetX = x - myPosition.x;
            float offsetY = y - myPosition.y;

            // Now draw the line between me and each city location.
            // I started by saving the ScreenPosition in the constructor.  But that saves the
            // original position, which might be very different from the current position if we've
            // panned or zoomed the map!
            for (Location l : cityCoords) {
                ScreenPosition p = map.getScreenPosition(l);
                pg.line(myPosition.x+offsetX, myPosition.y+offsetY, p.x+offsetX, p.y+offsetY);
            }
        }

        // reset to previous styling
        pg.popStyle();
	}

}
