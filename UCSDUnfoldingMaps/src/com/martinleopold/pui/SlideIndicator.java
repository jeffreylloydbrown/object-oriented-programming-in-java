package com.martinleopold.pui;

import processing.core.PApplet;

/**
 * The PUI Toggle works more like a checkbox than a toggle, at least in my opinion.  I also don't like
 * how Toggle's label is below the indicator instead of beside it.  My SlideIndicator looks more like the
 * indicator on a phone (so visually impaired/color-blind people can tell the value) and it
 * puts the label next to the indicator instead of below it.
 * @author Jeff Brown
 */
public class SlideIndicator extends Toggle {

	public SlideIndicator(PUI pui, int width, int height) {
		super(pui, width, height);
	}
	
	@Override
	void draw(PApplet p) {
		// draw background
		p.noStroke();
		p.fill(theme.background);
		p.rect(x, y, width, height);
		
		// draw outline
		// draw outline highlight
		if (hovered) p.stroke(theme.outlineHighlight);
		else p.stroke(theme.outline);
		
		// draw fill
		// draw fill highlight
		if (pressed) p.fill(theme.fillHighlight);
		else p.fill(theme.fill);
		
		p.rect(x,y, width-1, height-1); // stroked rect is bigger
	}

}
