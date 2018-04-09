package module4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractShapeMarker;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MultiMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;
import parsing.ParseFeed;
import processing.core.PApplet;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Jeff Brown
 * Date: April 6, 2018
 * */
public class EarthquakeCityMap extends PApplet {
	
	// We will use member variables, instead of local variables, to store the data
	// that the setUp and draw methods will need to access (as well as other methods)
	// You will use many of these variables, but the only one you should need to add
	// code to modify is countryQuakes, where you will store the number of earthquakes
	// per country.
	
	// You can ignore this.  It's to get rid of eclipse warnings
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFILINE, change the value of this variable to true
	private static final boolean offline = false;
	
	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	

	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";

	//feed with all earthquakes in the last MONTH.
    //private String earthquakesURL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_month.atom";
	
	// The files containing city names and info and country names and info
	private String cityFile = "data/city-data.json";
	private String countryFile = "data/countries.geo.json";
	
	// The map
	private UnfoldingMap map;
	
	// Markers for each city
	private List<Marker> cityMarkers;
	// Markers for each earthquake
	private List<Marker> quakeMarkers;

	// A List of country markers
	private List<Marker> countryMarkers;
	
	public void setup() {		
		// (1) Initializing canvas and map tiles
		size(900, 700, OPENGL);
		if (offline) {
		    map = new UnfoldingMap(this, 200, 50, 650, 600, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "data/2.5_week.atom";  // The same feed, but saved August 7, 2015
		}
		else {
			//map = new UnfoldingMap(this, 200, 50, 650, 600, new Google.GoogleMapProvider());
			map = new UnfoldingMap(this, 200, 50, 650, 600, new Microsoft.RoadProvider());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
		    //earthquakesURL = "data/2.5_week.atom";
		}
		MapUtils.createDefaultEventDispatcher(this, map);
		
		// FOR TESTING: Set earthquakesURL to be one of the testing files by uncommenting
		// one of the lines below.  This will work whether you are online or offline
		//earthquakesURL = "data/test1.atom";
		//earthquakesURL = "data/test2.atom";
		
		// WHEN TAKING THIS QUIZ: Uncomment the next line
		//earthquakesURL = "data/quiz1.atom";
		
		
		// (2) Reading in earthquake data and geometric properties
	    //     STEP 1: load country features and markers
		List<Feature> countries = GeoJSONReader.loadData(this, countryFile);
		countryMarkers = MapUtils.createSimpleMarkers(countries);
		
		//     STEP 2: read in city data
		List<Feature> cities = GeoJSONReader.loadData(this, cityFile);
		cityMarkers = new ArrayList<Marker>();
		for(Feature city : cities) {
		  cityMarkers.add(new CityMarker(city));
		}
	    
		//     STEP 3: read in earthquake RSS feed
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    quakeMarkers = new ArrayList<Marker>();
	    
	    for(PointFeature feature : earthquakes) {
		  //check if LandQuake
		  if(isLand(feature)) {
		    quakeMarkers.add(new LandQuakeMarker(feature));
		  }
		  // OceanQuakes
		  else {
		    quakeMarkers.add(new OceanQuakeMarker(feature));
		  }
	    }

	    // could be used for debugging
	    //printQuakes();
	 		
	    // (3) Add markers to map
	    //     NOTE: Country markers are not added to the map.  They are used
	    //           for their geometric properties
	    map.addMarkers(quakeMarkers);
	    map.addMarkers(cityMarkers);
	    
	}  // End setup
	
	
	public void draw() {
		background(0);
		map.draw();
		addKey();
		
	}
	
	// helper method to draw key in GUI
	// TODO: Update this method as appropriate
	private void addKey() {	
		// Remember you can use Processing's graphics methods here
        int black = color(0,0,0);
        int beige = color(245,245,220);
        int red = color(255,0,0);
        int yellow = color(255, 255,0);
        int blue = color(0,0,255);
        int white = color(255, 255, 255);

		fill(beige);
		rect(25, 50, 150, 295);
		
		fill(black);
		textAlign(LEFT, CENTER);
		textSize(12);
		text("Earthquake Key", 50, 75);

		int textLeft = 72;

		fill(red);
		stroke(red);
		triangle(58, 96, 53, 105, 63, 105);
		fill(black);
		text("City Marker", textLeft, 100);

		fill(black);
		stroke(black);
		ellipse(58, 121, 10, 10);
		text("Land quake", textLeft, 120);
		rectMode(CENTER);
		rect(58, 141, 10, 10, 0);
		rectMode(CORNER);
		text("Ocean quake", textLeft, 140);
		text("Size - Magnitude", 50, 170);

		fill(red);
		stroke(red);
		rect(50, 200, 100, 20);
		fill(blue);
		stroke(blue);
		rect(50, 230, 100, 20);
		fill(yellow);
		stroke(yellow);
		rect(50, 260, 100, 20);

        fill(white);
        textAlign(CENTER);
        text("Deep", 100, 214);
        text("Moderate", 100, 244);
        fill(black);
        text("Shallow", 100, 274);

        fill(black);
        stroke(black);
        strokeWeight(2);
        textAlign(LEFT, CENTER);
        line(58-5, 310-5, 58+5, 310+5);
        line(58-5, 310+5, 58+5, 310-5);
        text("Recent", textLeft, 309);
	}

	
	
	// Checks whether this quake occurred on land.  If it did, it sets the 
	// "country" property of its PointFeature to the country where it occurred
	// and returns true.  Notice that the helper method isInCountry will
	// set this "country" property already.  Otherwise it returns false.
	private boolean isLand(PointFeature earthquake) {
		
		
		// Loop over all the country markers.  
		// For each, check if the earthquake PointFeature is in the 
		// country in m.  Notice that isInCountry takes a PointFeature
		// and a Marker as input.  
		// If isInCountry ever returns true, isLand should return true.
		for (Marker m : countryMarkers) {
			if (isInCountry(earthquake, m))
			    return true;
		}
		
		
		// not inside any country
		return false;
	}

	// Add 1 to the count stored in the hashmap `counts`.  If `name` doesn't exist in `counts`,
    // add it to the hashmap with the count 1.
    private void bumpQuakeCount (HashMap<String, Integer> counts, String name) {
        counts.put(name, counts.getOrDefault(name, 0) + 1);
    }
	
	/* prints countries with number of earthquakes as
	 * Country1: numQuakes1
	 * Country2: numQuakes2
	 * ...
	 * OCEAN QUAKES: numOceanQuakes
	 * */
	private void printQuakes() 
	{
	    // build a hashmap of country names and counts.
        // while it is suggested to loop over countries, we will instead
        // loop over the quakeMarkers as they already know if they are land or sea.
        // We know everything in that list is a subtype of EarthquakeMarker,
        // although we will still be safe about it.  Even so that means they all
        // have an isOnLand() method.  Ocean quakes will go into the hashmap under
        // "OCEAN QUAKES".
        HashMap<String, Integer> quakeCounts = new HashMap<String, Integer>();
        String OCEAN_NAME = "OCEAN QUAKES";
        for (Marker m : quakeMarkers) {
            if (m instanceof EarthquakeMarker) {
                EarthquakeMarker em = (EarthquakeMarker) m;
                bumpQuakeCount(quakeCounts, em.isOnLand() ? (String) em.getProperty("country") : OCEAN_NAME);
            }
        }

        // OK, now walk the map.  When we see the OCEAN_NAME key, skip it
        // during the loop.  After the loop, then print the OCEAN_NAME count so
        // it always appears at the end of the report.
        for (String name : quakeCounts.keySet())
            if (! name.equals(OCEAN_NAME))
                System.out.println(name + ": " + quakeCounts.get(name));
        System.out.println(OCEAN_NAME + ": " + quakeCounts.get(OCEAN_NAME));
	}


	
	// helper method to test whether a given earthquake is in a given country
	// This will also add the country property to the properties of the earthquake 
	// feature if it's in one of the countries.
	// You should not have to modify this code
	private boolean isInCountry(PointFeature earthquake, Marker country) {
		// getting location of feature
		Location checkLoc = earthquake.getLocation();

		// some countries represented it as MultiMarker
		// looping over SimplePolygonMarkers which make them up to use isInsideByLoc
		if(country.getClass() == MultiMarker.class) {
				
			// looping over markers making up MultiMarker
			for(Marker marker : ((MultiMarker)country).getMarkers()) {
					
				// checking if inside
				if(((AbstractShapeMarker)marker).isInsideByLocation(checkLoc)) {
					earthquake.addProperty("country", country.getProperty("name"));
						
					// return if is inside one
					return true;
				}
			}
		}
			
		// check if inside country represented by SimplePolygonMarker
		else if(((AbstractShapeMarker)country).isInsideByLocation(checkLoc)) {
			earthquake.addProperty("country", country.getProperty("name"));
			
			return true;
		}
		return false;
	}

}
