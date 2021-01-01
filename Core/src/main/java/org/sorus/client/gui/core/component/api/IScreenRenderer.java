package org.sorus.client.gui.core.component.api;

import java.awt.*;
import org.sorus.client.gui.core.font.IFontRenderer;
import org.sorus.client.version.game.IItemStack;

public interface IScreenRenderer {

  default void drawRect(double x, double y, double width, double height, Color color) {
    this.drawRect(x, y, width, height, color, color, color, color);
  }

  void drawRect(
      double x,
      double y,
      double width,
      double height,
      Color bottomLeftColor,
      Color bottomRightColor,
      Color topRightColor,
      Color topLeftColor);

  default void drawHollowRect(
      double x, double y, double width, double height, double thickness, Color color) {
    this.drawHollowRect(x, y, width, height, thickness, 0, color);
  }

  default void drawHollowRect(
      double x,
      double y,
      double width,
      double height,
      double thickness,
      double cornerRadius,
      Color color) {
    this.drawHollowRect(x, y, width, height, thickness, cornerRadius, cornerRadius, color);
  }

  void drawHollowRect(
      double x,
      double y,
      double width,
      double height,
      double thickness,
      double xCornerRadius,
      double yCornerRadius,
      Color color);

  void drawString(
      IFontRenderer fontRenderer,
      String string,
      double x,
      double y,
      double xScale,
      double yScale,
      boolean shadow,
      Color color);

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

  void drawItem(IItemStack itemStack, double x, double y, Color color);
}
