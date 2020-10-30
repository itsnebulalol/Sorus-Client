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

package org.sorus.client.gui.core.component.api;

import java.awt.*;
import org.sorus.client.gui.core.font.IFontRenderer;
import org.sorus.client.version.game.IItemStack;

/** Interface used to perform drawing operations. */
public interface IScreenRenderer {

  default void drawRect(double x, double y, double width, double height, Color color) {
    this.drawRect(x, y, width, height, color, color, color, color);
  }

  /**
   * Draws a rectangle with specified rounding.
   *
   * @param x the x position of the rectangle
   * @param y the y position of the rectangle
   * @param width the width of the rectangle
   * @param height the height of the rectangle
   */
  void drawRect(
      double x,
      double y,
      double width,
      double height,
      Color bottomLeftColor,
      Color bottomRightColor,
      Color topRightColor,
      Color topLeftColor);

  /**
   * Draws a hollow rectangle.
   *
   * @param x the x position of the rectangle
   * @param y the y position of the rectangle
   * @param width the width of the rectangle
   * @param height the height of the rectangle
   * @param thickness the size of the line
   * @param color the color of the rectangle
   */
  default void drawHollowRect(
      double x, double y, double width, double height, double thickness, Color color) {
    this.drawHollowRect(x, y, width, height, thickness, 0, color);
  }

  /**
   * Draws a hollow rectangle with specified rounding.
   *
   * @param x the x position of the rectangle
   * @param y the y position of the rectangle
   * @param width the width of the rectangle
   * @param height the height of the rectangle
   * @param thickness the size of the line
   * @param cornerRadius the radius of the rounded portion of the corners
   * @param color the color of the rectangle
   */
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

  /**
   * Draws a hollow rectangle with specified rounding.
   *
   * @param x the x position of the rectangle
   * @param y the y position of the rectangle
   * @param width the width of the rectangle
   * @param height the height of the rectangle
   * @param thickness the size of the line
   * @param xCornerRadius the x radius of the rounded portion of the corners
   * @param yCornerRadius the y radius of the rounded portion of the corners
   * @param color the color of the rectangle
   */
  void drawHollowRect(
      double x,
      double y,
      double width,
      double height,
      double thickness,
      double xCornerRadius,
      double yCornerRadius,
      Color color);

  /**
   * Draws a string.
   *
   * @param string the string
   * @param x the x position of the string
   * @param y the y position of the string
   * @param xScale the x scale of the string
   * @param yScale the y scale of the string
   * @param color the color of the string
   */
  void drawString(
      IFontRenderer fontRenderer,
      String string,
      double x,
      double y,
      double xScale,
      double yScale,
      boolean shadow,
      Color color);

  /**
   * Draws an arc.
   *
   * @param x the x position of the arc
   * @param y the y position of the arc
   * @param xRadius the x radius of the arc
   * @param yRadius the y radius of the arc
   * @param startAngle the angle the arc starts at
   * @param endAngle the angle the arc ends at
   * @param color the color of the arc
   */
  void drawArc(
      double x,
      double y,
      double xRadius,
      double yRadius,
      int startAngle,
      int endAngle,
      Color color);

  /**
   * Draws a hollow arc.
   *
   * @param x the x position of the arc
   * @param y the y position of the arc
   * @param xRadius the x radius of the arc
   * @param yRadius the y radius of the arc
   * @param startAngle the angle the arc starts at
   * @param endAngle the angle the arc ends at
   * @param thickness the thickness of the arc
   * @param color the color of the arc
   */
  void drawHollowArc(
      double x,
      double y,
      double xRadius,
      double yRadius,
      int startAngle,
      int endAngle,
      double thickness,
      Color color);

  void drawImage(
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

  void drawItem(IItemStack itemStack, double x, double y, Color color);
}
