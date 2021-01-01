package org.sorus.client.gui.core.util;

import java.awt.*;

public class ColorUtil {

  public static Color average(Color color1, Color color2) {
    return new Color(
        (int) (color1.getRed() / 255.0 * color2.getRed()),
        (int) (color1.getGreen() / 255.0 * color2.getGreen()),
        (int) (color1.getBlue() / 255.0 * color2.getBlue()),
        (int) (color1.getAlpha() / 255.0 * color2.getAlpha()));
  }
}
