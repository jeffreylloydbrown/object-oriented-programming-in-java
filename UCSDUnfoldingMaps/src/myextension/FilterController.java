package myextension;

import com.martinleopold.pui.PUI;

public class FilterController {

  private PUI ui;

  private String name;
  private int originX;
  private int originY;
  private int width;
  private int height;

  public FilterController(EarthquakeCityMap applet, String name, int x, int y, int width, int height) {
    this.name = name;
    this.originX = x;
    this.originY = y;
    this.width = width;
    this.height = height;

    ui = PUI.init(applet)
        .position(originX, originY)
        .size(width, height)
        ;

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
