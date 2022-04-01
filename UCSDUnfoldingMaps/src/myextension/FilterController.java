package myextension;

import com.martinleopold.pui.PUI;
import com.martinleopold.pui.Theme;

import static com.martinleopold.pui.Theme.color;

public class FilterController {

  private static final int offColor = color( 255, 242, 232, 200 );
  private static final int onColor = color( 0, 218, 60, 255 );
  private static final int canvasColor = color( 0, 0, 0, 255 );
  private static final int textColor = color(255, 250, 240, 255);
  private static final int outlineColor = color( 244, 243, 40, 100 );
  private static final int outlineHighlightColor = color( 253, 134, 3, 200 );

  public FilterController(EarthquakeCityMap applet, int x, int y, int width, int height) {

    Theme theme = new Theme(offColor, outlineColor, outlineHighlightColor, textColor, onColor, canvasColor);

    PUI ui = PUI.init(applet)
        .position(x, y)
        .size(width, height)
        .theme(theme);

    ui.addLabel("Show:");
    ui.newRow();
    ui.addDivider();
    ui.newRow();

    ui.addSlideIndicator().label("Cities").isPressed(applet.getShowCities()).sets("showCities");
    ui.newRow();

    ui.addSlideIndicator().label("Land Quakes").isPressed(applet.getShowLandQuakes()).sets("showLandQuakes");
    ui.newRow();

    ui.addSlideIndicator().label("Ocean Quakes").isPressed(applet.getShowOceanQuakes()).sets("showOceanQuakes");
    ui.newRow();

    ui.addDivider();
    ui.newRow();

    ui.addSlideIndicator().label("Shallow Quakes").isPressed(applet.getShowShallowQuakes()).sets("showShallowQuakes");
    ui.newRow();

    ui.addSlideIndicator().label("Intermediate Quakes").isPressed(applet.getShowIntermediateQuakes()).sets("showIntermediateQuakes");
    ui.newRow();

    ui.addSlideIndicator().label("Deep Quakes").isPressed(applet.getShowDeepQuakes()).sets("showDeepQuakes");
    ui.newRow();

    ui.addDivider();
    ui.newRow();

    ui.addSlideIndicator().label("Only Recent Quakes").isPressed(applet.getShowOnlyRecentQuakes()).sets("showOnlyRecentQuakes");
    ui.newRow();
  }

}
