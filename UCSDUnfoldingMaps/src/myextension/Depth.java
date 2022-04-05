package myextension;

import static com.martinleopold.pui.Theme.color;

public enum Depth {
  SHALLOW(255, 255, 0),     // yellow
  INTERMEDIATE(0, 0, 255),  // blue
  DEEP(255, 0, 0);          // red

  private final int depthColor;

  Depth(int red, int green, int blue, int alpha) {
    depthColor = color(red, green, blue, alpha);
  }

  Depth(int red, int green, int blue) { this(red, green, blue, 255); }

  public int toColor() { return depthColor; }
}
