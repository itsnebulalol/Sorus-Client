package org.sorus.client.gui.core.font;

import java.awt.*;
import org.sorus.client.Sorus;
import org.sorus.client.version.render.IRenderer;

public class MinecraftFontRenderer implements IFontRenderer {

  private final String fontLocation;

  public MinecraftFontRenderer(String fontLocation) {
    this.fontLocation = fontLocation;
  }

  @Override
  public void drawString(
      String string,
      double x,
      double y,
      double xScale,
      double yScale,
      boolean shadow,
      Color color) {
    Sorus.getSorus()
        .getVersion()
        .getData(IRenderer.class)
        .drawString(fontLocation, string, x, y, xScale, yScale, shadow, color);
  }

  @Override
  public double getStringWidth(String string) {
    return Sorus.getSorus()
        .getVersion()
        .getData(IRenderer.class)
        .getStringWidth(fontLocation, string);
  }

  @Override
  public double getFontHeight() {
    return Sorus.getSorus().getVersion().getData(IRenderer.class).getFontHeight(fontLocation);
  }

  @Override
  public int getSpacing() {
    return 1;
  }
}
