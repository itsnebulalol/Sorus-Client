package org.sorus.client.gui.core.component.impl;

import java.awt.*;
import org.sorus.client.Sorus;
import org.sorus.client.gui.core.component.Component;
import org.sorus.client.gui.core.component.api.IScreenRenderer;
import org.sorus.client.gui.core.util.ColorUtil;

public class Rectangle extends Component {

  protected double width, height;
  protected double cornerRadius;

  protected Color bottomLeftColor, bottomRightColor, topRightColor, topLeftColor;

  @Override
  public void onRender() {
    double x = this.absoluteX();
    double y = this.absoluteY();
    double width = this.width * this.absoluteXScale();
    double height = this.height * this.absoluteYScale();
    double xCornerRadius = this.cornerRadius * this.absoluteXScale();
    double yCornerRadius = this.cornerRadius * this.absoluteYScale();
    Color color = this.absoluteColor();
    IScreenRenderer renderer = Sorus.getSorus().getGUIManager().getRenderer();
    if (xCornerRadius > 0 && yCornerRadius > 0) {
      renderer.drawArc(x, y, xCornerRadius, yCornerRadius, 180, 270, color);
      renderer.drawArc(
          x + width - xCornerRadius * 2, y, xCornerRadius, yCornerRadius, 90, 180, color);
      renderer.drawArc(
          x, y + height - yCornerRadius * 2, xCornerRadius, yCornerRadius, 270, 360, color);
      renderer.drawArc(
          x + width - xCornerRadius * 2,
          y + height - yCornerRadius * 2,
          xCornerRadius,
          yCornerRadius,
          0,
          90,
          color);
      renderer.drawRect(
          x + xCornerRadius,
          y,
          width - xCornerRadius * 2,
          yCornerRadius,
          color,
          color,
          color,
          color);
      renderer.drawRect(
          x,
          y + yCornerRadius,
          xCornerRadius,
          height - yCornerRadius * 2,
          color,
          color,
          color,
          color);
      renderer.drawRect(
          x + xCornerRadius,
          y + height - yCornerRadius,
          width - xCornerRadius * 2,
          yCornerRadius,
          color,
          color,
          color,
          color);
      renderer.drawRect(
          x + width - xCornerRadius,
          y + yCornerRadius,
          xCornerRadius,
          height - yCornerRadius * 2,
          color,
          color,
          color,
          color);
      renderer.drawRect(
          x + xCornerRadius,
          y + yCornerRadius,
          width - xCornerRadius * 2,
          height - yCornerRadius * 2,
          color,
          color,
          color,
          color);
    } else {
      if (bottomLeftColor != null) {
        renderer.drawRect(
            this.absoluteX(),
            this.absoluteY(),
            this.width * this.absoluteXScale(),
            this.height * this.absoluteYScale(),
            ColorUtil.average(this.absoluteColor(), bottomLeftColor),
            ColorUtil.average(this.absoluteColor(), bottomRightColor),
            ColorUtil.average(this.absoluteColor(), topRightColor),
            ColorUtil.average(this.absoluteColor(), topLeftColor));
      } else {
        renderer.drawRect(
            this.absoluteX(),
            this.absoluteY(),
            this.width * this.absoluteXScale(),
            this.height * this.absoluteYScale(),
            this.absoluteColor());
      }
    }
  }

  public <T extends Rectangle> T size(double width, double height) {
    this.width = width;
    this.height = height;
    return this.cast();
  }

  public <T extends Rectangle> T smooth(double radius) {
    this.cornerRadius = radius;
    return this.cast();
  }

  public <T extends Rectangle> T gradient(
      Color bottomLeftColor, Color bottomRightColor, Color topRightColor, Color topLeftColor) {
    this.bottomLeftColor = bottomLeftColor;
    this.bottomRightColor = bottomRightColor;
    this.topRightColor = topRightColor;
    this.topLeftColor = topLeftColor;
    return this.cast();
  }
}
