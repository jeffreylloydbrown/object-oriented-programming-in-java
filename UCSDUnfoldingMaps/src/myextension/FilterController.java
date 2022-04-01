package myextension;

import com.martinleopold.pui.*;

import static com.martinleopold.pui.Theme.color;

public class FilterController {

  private static final int offColor = color( 255, 242, 232, 200 );
  private static final int onColor = color( 0, 218, 60, 255 );
  private static final int canvasColor = color( 0, 0, 0, 255 );
  private static final int textColor = color(255, 250, 240, 255);
  private static final int outlineColor = color( 244, 243, 40, 100 );
  private static final int outlineHighlightColor = color(255, 255, 0, 255);

  public FilterController(EarthquakeCityMap applet, int x, int y, int width, int height) {

    Theme theme = new Theme(offColor, outlineColor, outlineHighlightColor, textColor, onColor, canvasColor);

    PUI ui = PUI.init(applet)
        .position(x, y)
        .size(width, height)
        .theme(theme);

    Label header = ui.addLabel("Show:").medium();
    int newWidth = Math.max(width, header.width());
    ui.newRow();

    SlideIndicator sl = ui.addSlideIndicator();
    sl.label("Cities").isPressed(applet.getShowCities()).sets("showCities");
    newWidth = Math.max(newWidth, sl.width());
    ui.newRow();

    sl = ui.addSlideIndicator();
    sl.label("Land Quakes").isPressed(applet.getShowLandQuakes()).sets("showLandQuakes");
    newWidth = Math.max(newWidth, sl.width());
    ui.newRow();

    sl = ui.addSlideIndicator();
    sl.label("Ocean Quakes").isPressed(applet.getShowOceanQuakes()).sets("showOceanQuakes");
    newWidth = Math.max(newWidth, sl.width());
    ui.newRow();

    Divider div1 = ui.addDivider();
    ui.newRow();

    sl = ui.addSlideIndicator();
    sl.label("Shallow Quakes").isPressed(applet.getShowShallowQuakes()).sets("showShallowQuakes");
    newWidth = Math.max(newWidth, sl.width());
    ui.newRow();

    sl = ui.addSlideIndicator();
    sl.label("Intermediate Quakes").isPressed(applet.getShowIntermediateQuakes()).sets("showIntermediateQuakes");
    newWidth = Math.max(newWidth, sl.width());
    ui.newRow();

    sl = ui.addSlideIndicator();
    sl.label("Deep Quakes").isPressed(applet.getShowDeepQuakes()).sets("showDeepQuakes");
    newWidth = Math.max(newWidth, sl.width());
    ui.newRow();

    Divider div2 = ui.addDivider();
    ui.newRow();

    sl = ui.addSlideIndicator();
    sl.label("Only Recent Quakes").isPressed(applet.getShowOnlyRecentQuakes()).sets("showOnlyRecentQuakes");
    newWidth = Math.max(newWidth, sl.width());
    ui.newRow();

    // resize to match width of the widest control.  We aren't adjusting height on purpose.  We could if we chose.
    // Since it's not turned off, there are 2 layers of padding (around the top UI and around each SlideIndicator).
    // That's why the 4*padding addition to the width.
    ui.size(newWidth + 4*ui.paddingXPx(), height);
    div1.size(ui.px2gridX(newWidth), ui.px2gridY(div1.height()));
    div2.size(ui.px2gridX(newWidth), ui.px2gridY(div2.height()));
  }

}
