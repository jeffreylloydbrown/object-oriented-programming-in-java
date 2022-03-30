package myextension;

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
import de.fhpotsdam.unfolding.utils.MapUtils;
import parsing.ParseFeed;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 * Date: July 17, 2015
 * */
public class EarthquakeCityMap extends PApplet {

	// We will use member variables, instead of local variables, to store the data
	// that the setUp and draw methods will need to access (as well as other methods)
	// You will use many of these variables, but the only one you should need to add
	// code to modify is countryQuakes, where you will store the number of earthquakes
	// per country.

	// You can ignore this.  It's to get rid of eclipse warnings
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFLINE, change the value of this variable to true
	private static final boolean offline = false;

	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";

	// The map
	private UnfoldingMap map;

	// Markers for each city
	private List<Marker> cityMarkers;
	// Markers for each earthquake
	private List<Marker> quakeMarkers;

	// A List of country markers
	private List<Marker> countryMarkers;

	// NEW IN MODULE 5
	private CommonMarker lastSelected;
	private CommonMarker lastClicked;

	// my extension: visibility controls
	// PUI's event subsystem is expecting "connected" variables to be here.
	// That means I cannot put them in FilterController, which makes more sense,
	// but then I have to replicate much of the event subsystem and that just
	// isn't worth it for this assignment.  I thought about making FilterController
	// a subclass of Toggle and rewriting sets(), but that also meant extending
	// the event manager.  The real solution is for the event subsystem to let you
	// pass in the reference to the class holding these variables instead of assuming
	// they are in the child of PApplet.
	// We start with everything visible, which means we do NOT want to show only recent quakes.
	private boolean showCities = true;
	public boolean getShowCities() { return showCities; }
	private boolean showLandQuakes = true;
	public boolean getShowLandQuakes() { return showLandQuakes; }
	private boolean showOceanQuakes = true;
	public boolean getShowOceanQuakes() { return showOceanQuakes; }
	private boolean showShallowQuakes = true;
	public boolean getShowShallowQuakes() { return showShallowQuakes; }
	private boolean showIntermediateQuakes = true;
	public boolean getShowIntermediateQuakes() { return showIntermediateQuakes; }
	private boolean showDeepQuakes = true;
	public boolean getShowDeepQuakes() { return showDeepQuakes; }
	private boolean showOnlyRecentQuakes = false;
	public boolean getShowOnlyRecentQuakes() { return showOnlyRecentQuakes; }

	public void setup() {
		// The files containing city names and info and country names and info
		String cityFile = "city-data.json";
		String countryFile = "countries.geo.json";

		//feed with magnitude 2.5+ Earthquakes
		String earthquakesURL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";

		// (1) Initializing canvas and map tiles
		size(900, 700, OPENGL);
		if (offline) {
			map = new UnfoldingMap(this, 200, 50, 650, 600, new MBTilesMapProvider(mbTilesString));
			earthquakesURL = "2.5_week.atom";  // The same feed, but saved August 7, 2015
		}
		else {
			map = new UnfoldingMap(this, 200, 50, 650, 600, new Google.GoogleMapProvider());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
			//earthquakesURL = "2.5_week.atom";
		}
		MapUtils.createDefaultEventDispatcher(this, map);

		// FOR TESTING: Set earthquakesURL to be one of the testing files by uncommenting
		// one of the lines below.  This will work whether you are online or offline
		//earthquakesURL = "test1.atom";
		//earthquakesURL = "test2.atom";

		// Uncomment this line to take the quiz
		//earthquakesURL = "quiz2.atom";


		// (2) Reading in earthquake data and geometric properties
		//     STEP 1: load country features and markers
		List<Feature> countries = GeoJSONReader.loadData(this, countryFile);
		countryMarkers = MapUtils.createSimpleMarkers(countries);

		//     STEP 2: read in city data
		List<Feature> cities = GeoJSONReader.loadData(this, cityFile);
		cityMarkers = new ArrayList<>();
		for(Feature city : cities) {
			cityMarkers.add(new CityMarker(city));
		}

		//     STEP 3: read in earthquake RSS feed
		List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
		quakeMarkers = new ArrayList<>();

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
		if (earthquakesURL.equals("quiz2.atom")) {
			System.out.println("QUESTION 7 (adjust numQuakes to number in question 7, answer is the last item):");
			sortAndPrint(6, 1);

			System.out.println("\nQUESTION 8 (adjust minimumRepeated to number in question 8, manually count items == magnitude of first item):");
			sortAndPrint(10, 3);
		} else {
			printQuakes();
		}

		// (3) Add markers to map
		//     NOTE: Country markers are not added to the map.  They are used
		//           for their geometric properties
		map.addMarkers(quakeMarkers);
		map.addMarkers(cityMarkers);

		new FilterController(this, "Filters", 20, 300, 150, 350);

	}  // End setup


	public void draw() {
		background(0);
		setVisibility(cityMarkers);
		setVisibility(quakeMarkers);
		map.draw();
		addKey();
	}

	private void setVisibility(List<Marker> markers) {
		// only change the visibility if we ARE NOT examining a specific marker.
		if (lastClicked == null) {
			for (Marker m : markers)
				((CommonMarker) m).setVisibility(this);
		}
	}

	private void sortAndPrint(int numQuakes, int minimumRepeated)
	{
		if (minimumRepeated <= 1) minimumRepeated = 1;
		if (numQuakes <= 1) numQuakes = 1;

		List<EarthquakeMarker> earthquakes = new ArrayList<>();
		for (Marker m: quakeMarkers)
			earthquakes.add((EarthquakeMarker) m);

		// God, I miss Scala.
		Map<Float, List<EarthquakeMarker>> groupedByMagnitude = groupByMagnitude(earthquakes);
		Map<Float, List<EarthquakeMarker>> filteredByMinRepetition = filterByRepeatedMagnitude(groupedByMagnitude, minimumRepeated);
		List<Float> magnitudes = new ArrayList<>(filteredByMinRepetition.keySet());
		magnitudes.sort(null);  // smallest to largest, we want largest to smallest
		int count = 0;
		for (int i = magnitudes.size() - 1; i >= 0 && count < numQuakes; i--) {  // largest to smallest
			List<EarthquakeMarker> quakes = filteredByMinRepetition.get(magnitudes.get(i));
			for (EarthquakeMarker em: quakes) {
				count++;
				System.out.println(count + ": " + em);
				if (count >= numQuakes) break;
			}
		}
	}

	private Map<Float, List<EarthquakeMarker>> groupByMagnitude(List<EarthquakeMarker> earthquakeMarkers)
	{
		Map<Float, List<EarthquakeMarker>> groupedByMagnitude = new HashMap<>();

		for (EarthquakeMarker em: earthquakeMarkers) {
			float magnitude = em.getMagnitude();
			List<EarthquakeMarker> list = groupedByMagnitude.getOrDefault(magnitude, new ArrayList<>());
			list.add(em);
			groupedByMagnitude.put(magnitude, list);
		}

		return groupedByMagnitude;
	}

	private Map<Float, List<EarthquakeMarker>>
	filterByRepeatedMagnitude(Map<Float, List<EarthquakeMarker>> input, int minimumRepeated)
	{
		// minimumRepeated <= 0 is silly, so we return an empty filtered map in that case.
		// minimumRepeated == 1 means to include every earthquake we received, so just return the input.
		if (minimumRepeated < 1)
			return new HashMap<>();
		else if (minimumRepeated == 1)
			return input;
		else {
			Map<Float, List<EarthquakeMarker>> filtered = new HashMap<>();
			for (Float magnitude : input.keySet()) {
				List<EarthquakeMarker> quakes = input.get(magnitude);
				if (quakes.size() >= minimumRepeated)
					filtered.put(magnitude, quakes);
			}
			return filtered;
		}
	}

	/** Event handler that gets called automatically when the 
	 * mouse moves.
	 */
	@Override
	public void mouseMoved()
	{
		// clear the last selection
		if (lastSelected != null) {
			lastSelected.setSelected(false);
			lastSelected = null;

		}
		selectMarkerIfHover(quakeMarkers);
		selectMarkerIfHover(cityMarkers);
		//loop();
	}

	// If there is a marker selected 
	private void selectMarkerIfHover(List<Marker> markers)
	{
		// Abort if there's already a marker selected
		if (lastSelected != null) {
			return;
		}

		for (Marker m : markers)
		{
			CommonMarker marker = (CommonMarker)m;
			if (marker.isInside(map,  mouseX, mouseY)) {
				lastSelected = marker;
				marker.setSelected(true);
				return;
			}
		}
	}

	/** The event handler for mouse clicks
	 * It will display an earthquake and its threat circle of cities
	 * Or if a city is clicked, it will display all the earthquakes 
	 * where the city is in the threat circle
	 */
	@Override
	public void mouseClicked()
	{
		if (lastClicked != null) {
			unhideMarkers();
			lastClicked = null;
		}
		else {
			checkEarthquakesForClick();
			if (lastClicked == null) {
				checkCitiesForClick();
			}
		}
	}

	// Helper method that will check if a city marker was clicked on
	// and respond appropriately
	private void checkCitiesForClick()
	{
		if (lastClicked != null) return;
		// Loop over the earthquake markers to see if one of them is selected
		for (Marker marker : cityMarkers) {
			if (!marker.isHidden() && marker.isInside(map, mouseX, mouseY)) {
				lastClicked = (CommonMarker)marker;
				// Hide all the other earthquakes and hide
				for (Marker mHide : cityMarkers) {
					if (mHide != lastClicked) {
						mHide.setHidden(true);
					}
				}
				for (Marker mHide : quakeMarkers) {
					EarthquakeMarker quakeMarker = (EarthquakeMarker)mHide;
					if (quakeMarker.getDistanceTo(marker.getLocation())
							> quakeMarker.threatCircle()) {
						quakeMarker.setHidden(true);
					}
				}
				return;
			}
		}
	}

	// Helper method that will check if an earthquake marker was clicked on
	// and respond appropriately
	private void checkEarthquakesForClick()
	{
		if (lastClicked != null) return;
		// Loop over the earthquake markers to see if one of them is selected
		for (Marker m : quakeMarkers) {
			EarthquakeMarker marker = (EarthquakeMarker)m;
			if (!marker.isHidden() && marker.isInside(map, mouseX, mouseY)) {
				lastClicked = marker;
				// Hide all the other earthquakes and hide
				for (Marker mHide : quakeMarkers) {
					if (mHide != lastClicked) {
						mHide.setHidden(true);
					}
				}
				for (Marker mHide : cityMarkers) {
					if (mHide.getDistanceTo(marker.getLocation())
							> marker.threatCircle()) {
						mHide.setHidden(true);
					}
				}
				return;
			}
		}
	}

	// loop over and unhide all markers
	private void unhideMarkers() {
		for(Marker marker : quakeMarkers) {
			marker.setHidden(false);
		}

		for(Marker marker : cityMarkers) {
			marker.setHidden(false);
		}
	}

	// helper method to draw key in GUI
	private void addKey() {
		// Remember you can use Processing's graphics methods here
		fill(255, 250, 240);

		int xBase = 25;
		int yBase = 50;

		rect(xBase, yBase, 150, 250);

		fill(0);
		textAlign(LEFT, CENTER);
		textSize(12);
		text("Earthquake Key", xBase+25, yBase+25);

		fill(150, 30, 30);
		int tri_xBase = xBase + 35;
		int tri_yBase = yBase + 50;
		triangle(tri_xBase, tri_yBase- CityMarker.TRI_SIZE, tri_xBase- CityMarker.TRI_SIZE,
				tri_yBase+ CityMarker.TRI_SIZE, tri_xBase+ CityMarker.TRI_SIZE,
				tri_yBase+ CityMarker.TRI_SIZE);

		fill(0, 0, 0);
		textAlign(LEFT, CENTER);
		text("City Marker", tri_xBase + 15, tri_yBase);

		text("Land Quake", xBase+50, yBase+70);
		text("Ocean Quake", xBase+50, yBase+90);
		text("Size ~ Magnitude", xBase+25, yBase+110);

		fill(255, 255, 255);
		ellipse(xBase+35,
				yBase+70,
				10,
				10);
		rect(xBase+35-5, yBase+90-5, 10, 10);

		fill(color(255, 255, 0));
		ellipse(xBase+35, yBase+140, 12, 12);
		fill(color(0, 0, 255));
		ellipse(xBase+35, yBase+160, 12, 12);
		fill(color(255, 0, 0));
		ellipse(xBase+35, yBase+180, 12, 12);

		textAlign(LEFT, CENTER);
		fill(0, 0, 0);
		text("Shallow", xBase+50, yBase+140);
		text("Intermediate", xBase+50, yBase+160);
		text("Deep", xBase+50, yBase+180);

		text("Past hour", xBase+50, yBase+200);

		fill(255, 255, 255);
		int centerX = xBase+35;
		int centerY = yBase+200;
		ellipse(centerX, centerY, 12, 12);

		strokeWeight(2);
		line(centerX-8, centerY-8, centerX+8, centerY+8);
		line(centerX-8, centerY+8, centerX+8, centerY-8);


	}



	// Checks whether this quake occurred on land.  If it did, it sets the 
	// "country" property of its PointFeature to the country where it occurred
	// and returns true.  Notice that the helper method isInCountry will
	// set this "country" property already.  Otherwise, it returns false.
	private boolean isLand(PointFeature earthquake) {

		// IMPLEMENT THIS: loop over all countries to check if location is in any of them
		// If it is, add 1 to the entry in countryQuakes corresponding to this country.
		for (Marker country : countryMarkers) {
			if (isInCountry(earthquake, country)) {
				return true;
			}
		}

		// not inside any country
		return false;
	}

	// prints countries with number of earthquakes
	// You will want to loop through the country markers or country features
	// (either will work) and then for each country, loop through
	// the quakes to count how many occurred in that country.
	// Recall that the country markers have a "name" property, 
	// And LandQuakeMarkers have a "country" property set.
	private void printQuakes() {
		int totalWaterQuakes = quakeMarkers.size();
		for (Marker country : countryMarkers) {
			String countryName = country.getStringProperty("name");
			int numQuakes = 0;
			for (Marker marker : quakeMarkers)
			{
				EarthquakeMarker eqMarker = (EarthquakeMarker)marker;
				if (eqMarker.isOnLand()) {
					if (countryName.equals(eqMarker.getStringProperty("country"))) {
						numQuakes++;
					}
				}
			}
			if (numQuakes > 0) {
				totalWaterQuakes -= numQuakes;
				System.out.println(countryName + ": " + numQuakes);
			}
		}
		System.out.println("OCEAN QUAKES: " + totalWaterQuakes);
	}



	// helper method to test whether a given earthquake is in a given country
	// This will also add the country property to the properties of the earthquake feature if 
	// it's in one of the countries.
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
