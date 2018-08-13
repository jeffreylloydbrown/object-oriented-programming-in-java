package module5;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PConstants;
import processing.core.PGraphics;

import java.util.List;

/** Implements a visual marker for cities on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Jeff Brown
 *
 */
// Change SimplePointMarker to CommonMarker as the very first thing you do
// in module 5 (i.e. CityMarker extends CommonMarker).  It will cause an error.
// That's what's expected.
public class CityMarker extends CommonMarker {
	
	public static int TRI_SIZE = 5;  // The size of the triangle marker
	
	public CityMarker(Location location) {
		super(location);
	}
	
	
	public CityMarker(Feature city) {
		super(((PointFeature)city).getLocation(), city.getProperties());
		// Cities have properties: "name" (city name), "country" (country name)
		// and "population" (population, in millions)
	}

	
	/**
	 * Implementation of method to draw marker on the map.
	 */
	public void drawMarker(PGraphics pg, float x, float y) {
		// Save previous drawing style
		pg.pushStyle();
		
		// IMPLEMENT: drawing triangle for each city
		pg.fill(150, 30, 30);
		pg.triangle(x, y-TRI_SIZE, x-TRI_SIZE, y+TRI_SIZE, x+TRI_SIZE, y+TRI_SIZE);
		
		// Restore previous drawing style
		pg.popStyle();
	}
	
	/** Show the title of the city if this marker is selected */
	public void showTitle(PGraphics pg, float x, float y)
	{
        // title is 'City, Country: pop. NNN million'
	    drawStringAsPopup(pg, x, y, getCity()+", "+getCountry()+": pop. "+getPopulation()+" million");
	}

	public void showThreatCircleOnly(List<Marker> quakeMarkers, List<Marker> cityMarkers, List<Marker> lineMarkers) {
        // Where am I?
        Location myLocation = getLocation();

        // I'm a city marker.  That means I hide all other cities and
        // all earthquakes farther away than their individual threat circle.
		// I don't do anything (yet) with line markers.
        for (Marker m : cityMarkers)
            m.setHidden(m != this);
        for (Marker m : quakeMarkers) {
            EarthquakeMarker quake = (EarthquakeMarker) m;
            quake.setHidden(quake.getDistanceTo(myLocation) > quake.threatCircle());
        }
	}
	
	
	/* Local getters for some city properties.  
	 */
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
