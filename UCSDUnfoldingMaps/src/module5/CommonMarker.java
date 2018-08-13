package module5;

import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PGraphics;
import java.util.List;

/** Implements a common marker for cities and earthquakes on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Jeff Brown
 *
 */
public abstract class CommonMarker extends SimplePointMarker {

	// Records whether this marker has been clicked (most recently)
	protected boolean clicked = false;
	
	public CommonMarker(Location location) {
		super(location);
	}
	
	public CommonMarker(Location location, java.util.HashMap<java.lang.String,java.lang.Object> properties) {
		super(location, properties);
	}
	
	// Getter method for clicked field
	public boolean getClicked() {
		return clicked;
	}
	
	// Setter method for clicked field
	public void setClicked(boolean state) {
		clicked = state;
	}
	
	// Common piece of drawing method for markers; 
	// Note that you should implement this by making calls 
	// drawMarker and showTitle, which are abstract methods 
	// implemented in subclasses
	public void draw(PGraphics pg, float x, float y) {
		// For starter code just drawMaker(...)
		if (!hidden) {
			drawMarker(pg, x, y);
			if (selected) {
				showTitle(pg, x, y);  // You will implement this in the subclasses
			}
		}
	}
	public abstract void drawMarker(PGraphics pg, float x, float y);
	public abstract void showTitle(PGraphics pg, float x, float y);
	public abstract void showThreatCircleOnly(List<Marker> quakeMarkers, List<Marker> cityMarkers,
											  List<Marker> lineMarkers);

	// Draw "title" inside a white box.
	public void drawStringAsPopup(PGraphics pg, float x, float y, String title) {
		// Save previous drawing style
		pg.pushStyle();
		int popupOffset =  5;
		int margin = 5;

		// For now, always go 5 pixels below the bottom of the marker (y+radius)
		// box itself will be 5 + textWidth() + 5 wide,
		// 5 + textAscent() + textDescent() + 5 high.
		// We don't account for newlines yet.
		pg.fill(255,255,255);
		pg.rect(x, y+radius+popupOffset,
				margin+pg.textWidth(title)+margin, margin+pg.textAscent()+pg.textDescent()+margin);
		pg.fill(0,0,0);
		// Text is indented from box left and top edges by 5 pixels.
		pg.text(title, x+margin, y+radius+popupOffset+margin+pg.textAscent());

		// Restore previous drawing style
		pg.popStyle();
	}
}