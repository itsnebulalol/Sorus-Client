package org.sorus.client.version.render;

import java.awt.*;
import org.sorus.client.cosmetic.TexturedQuad;
import org.sorus.client.version.game.IItemStack;

public interface IRenderer {

  void drawRect(
      double x,
      double y,
      double width,
      double height,
      Color bottomLeftColor,
      Color bottomRightColor,
      Color topRightColor,
      Color topLeftColor);

  void drawArc(
      double x,
      double y,
      double xRadius,
      double yRadius,
      int startAngle,
      int endAngle,
      Color color);

  void drawHollowArc(
      double x,
      double y,
      double xRadius,
      double yRadius,
      int startAngle,
      int endAngle,
      double thickness,
      Color color);

  void drawString(
      String fontLocation,
      String string,
      double x,
      double y,
      double xScale,
      double yScale,
      boolean shadow,
      Color color);

  double getStringWidth(String fontLocation, String string);

  double getFontHeight(String fontLocation);

  void drawPartialImage(
      String resource,
      double x,
      double y,
      double width,
      double height,
      double textureX,
      double textureY,
      double textureWidth,
      double textureHeight,
      Color color);

  void drawFullImage(String resource, double x, double y, double width, double height, Color color);

  void enableBlur(double amount);

  void disableBlur();

  ITTFFontRenderer getFont(String location);

  void drawItem(IItemStack itemStack, double x, double y, Color color);

  void drawQuad(TexturedQuad quad);
}
