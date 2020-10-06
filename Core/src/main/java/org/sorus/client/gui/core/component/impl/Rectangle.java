/*
 * MIT License
 *
 * Copyright (c) 2020 Danterus
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.sorus.client.gui.core.component.impl;

import java.awt.*;
import org.sorus.client.Sorus;
import org.sorus.client.gui.core.component.Component;
import org.sorus.client.gui.core.component.api.IScreenRenderer;
import org.sorus.client.gui.core.util.ColorUtil;

/** Implementation of {@link Component} for a rectangle. */
public class Rectangle extends Component {

  protected double width, height;
  protected double cornerRadius;

  protected Color bottomLeftColor, bottomRightColor, topRightColor, topLeftColor;

  /** Draws a rectangle to the screen. */
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

  /**
   * Sets the size of the rectangle.
   *
   * @param width the wanted width of the rectangle
   * @param height the wanted height of the rectangle
   * @return the rectangle
   */
  public <T extends Rectangle> T size(double width, double height) {
    this.width = width;
    this.height = height;
    return this.cast();
  }

  /**
   * Sets how smooth the corners of the rectangle will be.
   *
   * @param radius the radius of the arcs
   * @return the rectangle
   */
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
