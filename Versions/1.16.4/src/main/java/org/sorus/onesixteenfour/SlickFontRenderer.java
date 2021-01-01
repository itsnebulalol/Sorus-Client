

package org.sorus.onesixteenfour;

import org.sorus.client.Sorus;
import org.sorus.client.version.IGLHelper;
import org.sorus.client.version.render.ITTFFontRenderer;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class SlickFontRenderer implements ITTFFontRenderer {



  @Override
  public void drawString(
      String string,
      double x,
      double y,
      double xScale,
      double yScale,
      boolean shadow,
      Color color) {
    if(string.trim().isEmpty()) {
      return;
    }
    Sorus.getSorus().getVersion().getData(IGLHelper.class).translate(x - 0.25, y - 2, 0);
    Sorus.getSorus().getVersion().getData(IGLHelper.class).scale(1.0 / 9 * xScale, 1.0 / 9 * yScale, 1);
    Sorus.getSorus().getVersion().getData(IGLHelper.class).blend(true);
    Sorus.getSorus()
        .getVersion()
            .getData(IGLHelper.class)
        .color(
            color.getRed() / 255.0,
            color.getGreen() / 255.0,
            color.getBlue() / 255.0,
            color.getAlpha() / 255.0);

    //super.drawString(0, 0, string, color1);
    Sorus.getSorus().getVersion().getData(IGLHelper.class).blend(false);
    Sorus.getSorus().getVersion().getData(IGLHelper.class).scale(9 / xScale, 9 / yScale, 1);
    Sorus.getSorus().getVersion().getData(IGLHelper.class).translate(-x + 0.25, -y + 2, 0);
    Sorus.getSorus().getVersion().getData(IGLHelper.class).unbindTexture();
  }

  @Override
  public double getStringWidth(String string) {
    return 0;//super.getWidth(string) / 9.0;
  }

  @Override
  public double getFontHeight() {
    return 0;//super.getLineHeight() / 15.0;
  }

  @Override
  public int getSpacing() {
    return 0;
  }

}
