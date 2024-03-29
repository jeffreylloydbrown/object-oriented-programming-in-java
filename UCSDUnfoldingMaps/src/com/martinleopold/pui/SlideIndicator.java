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

	protected Label label;
	protected int paddingBeforeLabel = 5; // a pleasing default value
	protected int paddingAboveLabel;      // remember it defaults to zero

	protected int centerLabelVertically() {
		return (label.height > height) ? 0 : (height - label.height)/2;
	}

	protected void setLabelPosition(int width) {
		label.setPosition(this.x+width+paddingBeforeLabel, this.y+paddingAboveLabel);
	}

	@Override
	protected SlideIndicator getThis() {
		return this;
	}

	public SlideIndicator(PUI pui, int width, int height) {
		super(pui, width, height);
		label = new Label(pui, 0, pui.gridY2Px(PUI.DEFAULT_FONTSIZE_MEDIUM), false);
		setLabelPosition(width);
		paddingAboveLabel = centerLabelVertically();
		label.active = false; // no redraw
	}

	public SlideIndicator label(String text) {
		label.text(text);
		label.width = (int)label.textWidth()+1; // adjust label size

		label.active = true; // redraw
		setSize(width, height); // adjust layoutRect size

		pui.layout.reLayout(); // need to relayout cuz dimensions changed after first layout (in super())
		setLabelPosition(width);		// label needs to be placed again as position might have changed
		return getThis();
	}

	public SlideIndicator noLabel() {
		layoutRect = new Rect(this);
		label.active = false; // no redraw
		return getThis();
	}

	@Override
	void setSize(int w, int h) {
		super.setSize(w, h);
		if (label != null) {
			if (label.active) {
				layoutRect.width = width + label.width + paddingBeforeLabel; // joint width
				layoutRect.height = Math.max(height, label.height+paddingAboveLabel);
			}
			setLabelPosition(width);  // label needs to be replaced
		}
	}

	@Override
	void setPosition(int x, int y) {
		super.setPosition(x, y);
		if (label != null) {
			// can't use setLabelPosition here, we are changing X and Y!
			label.setPosition(x+width+paddingBeforeLabel, y+paddingAboveLabel); // label needs to be replaced
		}
	}

	@Override
	public SlideIndicator onDraw(String methodName) {
		super.onDraw(methodName);
		label.visible = false; // disable default rendering. label needs to be drawn via the onDraw callback now
		return getThis();
	}

	@Override
	void draw(PApplet p) {

		// draw background
		p.noStroke();
		p.fill(theme.overlay);
		p.rect(x, y, width, height);

		// On my phone, when the indicator is On the slide trough is green and the slide indicator is to the right.
		// When Off, the slide indicator is to the left and the slide trough is gray.  Our indicator is a circle drawn
		// within the trough, which will use the fill color always so that it matches the text.  It has a black stroke
		// outline to distinguish it from the gray trough when Off.
		int halfHeight = (height-2)/2;  // the -2 accounts for the outline width.

		int troughColor = pressed ? theme.fillHighlight : theme.background;
		p.fill(troughColor);
		p.rect(x+1, y+1, width-2, height-2);

		int centerX = x+1 + ( pressed ? 3*(width-2) : width-2 )/4;  // on: 3/4 width, off: 1/4 width
		int centerY = y+1 + halfHeight;
		int radius = halfHeight - 2; // subtract 2 to leave a small space for trough color
		p.stroke(0,0,0);
		p.fill(theme.fill);
		p.ellipse(centerX, centerY, 2 * radius, 2 * radius);

		// now add the trough outline
		if (hovered) p.stroke(theme.outlineHighlight);
		else p.stroke(theme.outline);
		p.noFill();
		p.rect(x, y, width, height);

		label.draw(p);
	}

	public SlideIndicator paddingBeforeLabel(int sizePx) {
		paddingBeforeLabel = Math.min(Math.max(0, sizePx), pui.width);
		return getThis();
	}

	public SlideIndicator paddingAboveLabel(int sizePx) {
		paddingAboveLabel = Math.min(Math.max(0, sizePx), pui.height);
		return getThis();
	}

	public SlideIndicator setFontSize(int sizePx) {
		label.setSize((int)(label.textWidth(sizePx)+1), sizePx);
		pui.layout.reLayout();
		paddingAboveLabel = centerLabelVertically();
		return getThis();
	}

	public SlideIndicator small() { return setFontSize(pui.gridY2Px(PUI.DEFAULT_FONTSIZE_SMALL));	}

	public SlideIndicator medium() { return setFontSize(pui.gridY2Px(PUI.DEFAULT_FONTSIZE_MEDIUM));}

	public SlideIndicator large() {	return setFontSize(pui.gridY2Px(PUI.DEFAULT_FONTSIZE_LARGE));	}

}
