package module3;

//Java utilities libraries
import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.List;

//Processing library
import de.fhpotsdam.unfolding.providers.Microsoft;
import processing.core.PApplet;

//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;

//Parsing library
import parsing.ParseFeed;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Jeff Brown
 * Date: April 2, 2018
 * */
public class EarthquakeCityMap extends PApplet {

	// You can ignore this.  It's to keep eclipse from generating a warning.
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFLINE, change the value of this variable to true
	private static final boolean offline = false;
	
	// Less than this threshold is a light earthquake
	public static final float THRESHOLD_MODERATE = 5;
	// Less than this threshold is a minor earthquake
	public static final float THRESHOLD_LIGHT = 4;

	// Various colors we'll use.
	private int black = color(0,0,0);
	private int beige = color(245,245,220);
	private int red = color(255,0,0);
	private int yellow = color(255, 255,0);
	private int blue = color(0,0,255);

	// Various marker sizes
	private int baseRadius = 5;
	private int minorRadius = 1*baseRadius;
	private int lightRadius = 2*baseRadius;
	private int largeRadius = 3*baseRadius;

	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "data/blankLight-1-3.mbtiles";
	
	// The map
	private UnfoldingMap map;

	// canvas size information.
	private int canvasWidth = 950;
	private int canvasHeight = 600;

	// map's size information.
	private int borderSize = 50;
	private int mapX = 4*borderSize;
	private int mapY = borderSize;
	private int mapWidth = canvasWidth-mapX-borderSize;
	private int mapHeight = canvasHeight-(2*borderSize);

	// legend's size information.
	private int legendX = borderSize;
	private int legendY = borderSize;
	private int legendWidth = mapX - (2*borderSize);
	private int legendHeight = mapHeight/3;
	
	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";

	
	public void setup() {
		size(canvasWidth, canvasHeight, OPENGL);

		if (offline) {
		    map = new UnfoldingMap(this, mapX, mapY, mapWidth, mapHeight, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "data/2.5_week.atom"; 	// Same feed, saved Aug 7, 2015, for working offline
		}
		else {
			//map = new UnfoldingMap(this, mapX, mapY, mapWidth, mapHeight, new Google.GoogleMapProvider());
			map = new UnfoldingMap(this, mapX, mapY, mapWidth, mapHeight, new Microsoft.RoadProvider());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
			//earthquakesURL = "data/2.5_week.atom";
		}
		
	    map.zoomToLevel(2);
	    MapUtils.createDefaultEventDispatcher(this, map);	
			
	    // The List you will populate with new SimplePointMarkers
	    List<Marker> markers = new ArrayList<Marker>();

	    //Use provided parser to collect properties for each earthquake
	    //PointFeatures have a getLocation method
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    
	    //(Step 3): Add a loop here that calls createMarker (see below)
	    // to create a new SimplePointMarker for each PointFeature in 
	    // earthquakes.  Then add each new SimplePointMarker to the 
	    // List markers (so that it will be added to the map in the line below)
	    for (PointFeature eq : earthquakes)
	    	markers.add(createMarker(eq));
	    
	    // Add the markers to the map so that they are displayed
	    map.addMarkers(markers);
	}
		
	/* createMarker: A suggested helper method that takes in an earthquake 
	 * feature and returns a SimplePointMarker for that earthquake
	 * 
	 * In step 3 You can use this method as-is.  Call it from a loop in the 
	 * setp method.  
	 * 
	 * (Step 4): Add code to this method so that it adds the proper
	 * styling to each marker based on the magnitude of the earthquake.  
	*/
	private SimplePointMarker createMarker(PointFeature feature)
	{  
		// To print all of the features in a PointFeature (so you can see what they are)
		// uncomment the line below.  Note this will only print if you call createMarker 
		// from setup
		//System.out.println(feature.getProperties());
		
		// Create a new SimplePointMarker at the location given by the PointFeature
		SimplePointMarker marker = new SimplePointMarker(feature.getLocation());
		
		Object magObj = feature.getProperty("magnitude");
		float mag = Float.parseFloat(magObj.toString());
		
		// (Step 4): Add code below to style the marker's size and color
	    // according to the magnitude of the earthquake.  
	    // Don't forget about the constants THRESHOLD_MODERATE and 
	    // THRESHOLD_LIGHT, which are declared above.
	    // Rather than comparing the magnitude to a number directly, compare 
	    // the magnitude to these variables (and change their value in the code 
	    // above if you want to change what you mean by "moderate" and "light")

	    if (mag < THRESHOLD_LIGHT) {
	    	// minor earthquakes are blue with small markers
			marker.setColor(blue);
			marker.setStrokeColor(blue);
			marker.setRadius(minorRadius);
		} else if (mag < THRESHOLD_MODERATE) {
	    	// light earthquakes are yellow with medium markers
			marker.setColor(yellow);
			marker.setStrokeColor(yellow);
			marker.setRadius(lightRadius);
		} else {
	    	// high magnitude earthquakes are red with large markers
			marker.setColor(red);
			marker.setStrokeColor(red);
			marker.setRadius(largeRadius);
		}
	    
	    // Finally return the marker
	    return marker;
	}
	
	public void draw() {
	    background(10);
	    map.draw();
	    addKey();
	}


	// helper method to draw key in GUI
	// Implement this method to draw the key
	private void addKey() 
	{	
		// draw the key background.
		fill(beige);
		rect(legendX, legendY, legendWidth, legendHeight);

		// lay out the key text
		fill(black);
		textSize(10);
		int textLeft = legendX+10;
		int textRight = legendX+legendWidth-10;
		int textTop = legendY+10;
		int textBottom = legendY+legendHeight-10;
		int textCenter = (textLeft+textRight)/2;

		// `line` will track our current output (y coord) line.
		int line = textTop+10;
		int lineHeight = 10;

		// draw the key title.
		textAlign(CENTER, BASELINE);
		text("Earthquake Key", textCenter, line);

		// skip 4 lines.
		line += 4*lineHeight;

		// Rest of the text is left-aligned.
		textAlign(LEFT, BASELINE);
		textLeft = textLeft + 20;
		textSize(8);

		stroke(black);
		fill(black);
		text(String.format("%2.1f+ Magnitude", THRESHOLD_MODERATE), textLeft, line);
		stroke(red);
		fill(red);
		ellipse(textLeft-15, line-3, largeRadius, largeRadius);

		line += 3*lineHeight;
		stroke(black);
		fill(black);
		text(String.format("%2.1f+ Magnitude", THRESHOLD_LIGHT), textLeft, line);
		stroke(yellow);
		fill(yellow);
		ellipse(textLeft-15, line-3, lightRadius, lightRadius);

		line += 3*lineHeight;
		stroke(black);
		fill(black);
		text(String.format("Below %2.1f", THRESHOLD_LIGHT), textLeft, line);
		stroke(blue);
		fill(blue);
		ellipse(textLeft-15, line-3, minorRadius, minorRadius);
	}
}
