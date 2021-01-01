package org.sorus.client.util;

import java.awt.*;

public class ColorUtil {

  public static Color getBetween(Color color1, Color color2, double percentBetween) {
    return new Color(
        (int) ((color2.getRed() - color1.getRed()) * percentBetween + color1.getRed()),
        (int) ((color2.getGreen() - color1.getGreen()) * percentBetween + color1.getGreen()),
        (int) ((color2.getBlue() - color1.getBlue()) * percentBetween + color1.getBlue()),
        (int) ((color2.getAlpha() - color1.getAlpha()) * percentBetween + color1.getAlpha()));
  }
}
