package module1;

import de.fhpotsdam.unfolding.providers.Microsoft;
import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.*;
import de.fhpotsdam.unfolding.utils.MapUtils;

/** HelloWorld
  * An application with two maps side-by-side zoomed in on different locations.
  * Author: UC San Diego Coursera Intermediate Programming team
  * @author Jeff Brown
  * Date: April 2, 2018
  * */
public class HelloWorld extends PApplet
{
	/** Your goal: add code to display second map, zoom in, and customize the background.
	 * Feel free to copy and use this code, adding to it, modifying it, etc.  
	 * Don't forget the import lines above. */

	// You can ignore this.  It's to keep eclipse from reporting a warning
	private static final long serialVersionUID = 1L;

	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "data/blankLight-1-3.mbtiles";
	
	// IF YOU ARE WORKING OFFLINE: Change the value of this variable to true
	private static final boolean offline = false;
	
	/** The map we use to display our home town: La Jolla, CA */
	UnfoldingMap map1;
	
	/** The map you will use to display your home town; McKinney, TX */
	UnfoldingMap map2;

	public void setup() {
		//size(800, 600, P2D);  // Set up the Applet window to be 800x600
		                      // The OPENGL argument indicates to use the 
		                      // Processing library's 2D drawing
		                      // You'll learn more about processing in Module 3
		size(850, 600, P2D);  // we need 850 to show 2 maps, see below.

		// This sets the background color for the Applet.  
		// Play around with these numbers and see what happens!
		this.background(200, 200, 200);
		
		// Select a map provider
		// Google maps are in German only it seems, no one knows how to make them English.
		// So I'm using Microsoft.  (The Yahoo providers appear broken now.)
		//AbstractMapProvider provider = new Google.GoogleTerrainProvider();
		AbstractMapProvider provider = new Microsoft.HybridProvider();

		// Set a zoom level
		int zoomLevel = 10;
		
		if (offline) {
			// If you are working offline, you need to use this provider 
			// to work with the maps that are local on your computer.  
			provider = new MBTilesMapProvider(mbTilesString);
			// 3 is the maximum zoom level for working offline
			zoomLevel = 3;
		}
		
		// Create a new UnfoldingMap to be displayed in this window.  
		// The 2nd-5th arguments give the map's x, y, width and height
		// When you create your map we want you to play around with these 
		// arguments to get your second map in the right place.
		// The 6th argument specifies the map provider.  
		// There are several providers built-in.
		// Note if you are working offline you must use the MBTilesMapProvider
		map1 = new UnfoldingMap(this, 50, 50, 350, 500, provider);

		// The next line zooms in and centers the map at 
	    // 32.9 (latitude) and -117.2 (longitude): UC San Diego
	    map1.zoomAndPanTo(zoomLevel, new Location(32.9f, -117.2f));

		// This line makes the map interactive
		MapUtils.createDefaultEventDispatcher(this, map1);
		
		// My hometown is McKinney, TX: at 33.215604,-96.7935086
		// Second map makes the overall width = 50+350+50+350+50 = 850.  Set it above.
		map2 = new UnfoldingMap(this, 450, 50, 350, 500, provider);
		Location mcKinneyTX = new Location(33.215604f, -96.7935086f);
		map2.zoomAndPanTo(zoomLevel, mcKinneyTX);
		MapUtils.createDefaultEventDispatcher(this, map2);
	}

	/** Draw the Applet window.  */
	public void draw() {
		map1.draw();
		map2.draw();
	}

	
}
