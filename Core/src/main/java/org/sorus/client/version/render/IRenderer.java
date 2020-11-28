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

package org.sorus.client.version.render;

import java.awt.*;
import org.sorus.client.version.game.IItemStack;

/** Renderer interface, used when rendering objects to the screen. */
public interface IRenderer {

  /**
   * Draws a rectangle.
   *
   * @param x the x position
   * @param y the y position
   * @param width the width
   * @param height the height
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
   * Draws an arc.
   *
   * @param x the x position
   * @param y the y position
   * @param xRadius the x radius
   * @param yRadius the y radius
   * @param startAngle the angle the arc starts at
   * @param endAngle the angle the arc ends at
   * @param color the color
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
   * @param x the x position
   * @param y the y position
   * @param xRadius the x radius
   * @param yRadius the y radius
   * @param startAngle the angle the arc starts at
   * @param endAngle the angle the arc ends at
   * @param thickness the thickness of the line
   * @param color the color
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

  /**
   * Draws a string using the default minecraft font renderer.
   *
   * @param fontLocation the location of the font
   * @param string the string to draw
   * @param x the x position
   * @param y the y position
   * @param xScale the x scale
   * @param yScale the y scale
   * @param color the color
   */
  void drawString(
      String fontLocation,
      String string,
      double x,
      double y,
      double xScale,
      double yScale,
      boolean shadow,
      Color color);

  /**
   * Gets the width of a string from the default minecraft font renderer.
   *
   * @param fontLocation the location of the font
   * @param string the string
   * @return the width of the string
   */
  double getStringWidth(String fontLocation, String string);

  /**
   * Gets the character height from the default minecraft font renderer.
   *
   * @param fontLocation the location of the font
   * @return the font height
   */
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
}
