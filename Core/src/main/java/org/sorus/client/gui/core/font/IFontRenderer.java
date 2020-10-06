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

package org.sorus.client.gui.core.font;

import java.awt.*;

/**
 * Font renderer interface that allows for any type of font renderer to be implemented and to work
 * in Sorus.
 */
public interface IFontRenderer {

  /**
   * Draws the specified text.
   *
   * @param string the text to draw
   * @param x the x position of the text
   * @param y the y position of the text
   * @param xScale the x scale of the text
   * @param yScale the y scale of the text
   * @param color the color of the text
   */
  void drawString(
      String string, double x, double y, double xScale, double yScale, boolean shadow, Color color);

  /**
   * Gets the width of a certain string.
   *
   * @param string the input string
   * @return the width of the string
   */
  double getStringWidth(String string);

  /**
   * Gets the height of the font.
   *
   * @return the height of the font
   */
  double getFontHeight();

  int getSpacing();
}
