package myextension;

import com.martinleopold.pui.PUI;

public class FilterController {

  public FilterController(EarthquakeCityMap applet, int x, int y, int width, int height) {

    PUI ui = PUI.init(applet)
        .position(x, y)
        .size(width, height);

    ui.addLabel("Show:");
    ui.newRow();
    ui.addDivider();
    ui.newRow();

    ui.addToggle().label("Cities").isPressed(applet.getShowCities()).sets("showCities");
    ui.newRow();

    ui.addToggle().label("Land Quakes").isPressed(applet.getShowLandQuakes()).sets("showLandQuakes");
    ui.newRow();

    ui.addToggle().label("Ocean Quakes").isPressed(applet.getShowOceanQuakes()).sets("showOceanQuakes");
    ui.newRow();

    ui.addDivider();
    ui.newRow();

    ui.addToggle().label("Shallow Quakes").isPressed(applet.getShowShallowQuakes()).sets("showShallowQuakes");
    ui.newRow();

    ui.addToggle().label("Intermediate Quakes").isPressed(applet.getShowIntermediateQuakes()).sets("showIntermediateQuakes");
    ui.newRow();

    ui.addToggle().label("Deep Quakes").isPressed(applet.getShowDeepQuakes()).sets("showDeepQuakes");
    ui.newRow();

    ui.addDivider();
    ui.newRow();

    ui.addToggle().label("Only Recent Quakes").isPressed(applet.getShowOnlyRecentQuakes()).sets("showOnlyRecentQuakes");
    ui.newRow();
  }

}
