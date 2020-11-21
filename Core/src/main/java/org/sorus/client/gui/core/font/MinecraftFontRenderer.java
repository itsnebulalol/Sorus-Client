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
import org.sorus.client.Sorus;
import org.sorus.client.version.render.IRenderer;

/** Implementation of the {@link IFontRenderer} for a default minecraft font renderer. */
public class MinecraftFontRenderer implements IFontRenderer {

  /** ResourceLocation of the font. */
  private final String fontLocation;

  public MinecraftFontRenderer(String fontLocation) {
    this.fontLocation = fontLocation;
  }

  /**
   * Draws the specified text.
   *
   * @param string the text to draw
   * @param x the x position of the text
   * @param y the y position of the text
   * @param color the color of the text
   */
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

  /**
   * Gets the width of a certain string.
   *
   * @param string the input string
   * @return the width of the string
   */
  @Override
  public double getStringWidth(String string) {
    return Sorus.getSorus()
        .getVersion()
        .getData(IRenderer.class)
        .getStringWidth(fontLocation, string);
  }

  /**
   * Gets the height of the font.
   *
   * @return the height of the font
   */
  @Override
  public double getFontHeight() {
    return Sorus.getSorus().getVersion().getData(IRenderer.class).getFontHeight(fontLocation);
  }

  @Override
  public int getSpacing() {
    return 1;
  }
}
