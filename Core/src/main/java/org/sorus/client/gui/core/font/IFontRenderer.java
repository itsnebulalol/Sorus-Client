package org.sorus.client.gui.core.font;

import java.awt.*;

public interface IFontRenderer {

  void drawString(
      String string, double x, double y, double xScale, double yScale, boolean shadow, Color color);

  double getStringWidth(String string);

  double getFontHeight();

  int getSpacing();
}
