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

/** Implementation of {@link Component} to draw a hollow rectangle. */
public class HollowRectangle extends Rectangle {

  private double thickness = 1;

  /** Draws a hollow rectangle based on current state. */
  @Override
  public void onRender() {
    double x = this.absoluteX();
    double y = this.absoluteY();
    double width = this.width * this.absoluteXScale();
    double height = this.height * this.absoluteYScale();
    double thickness = this.thickness * this.absoluteXScale();
    double xCornerRadius = this.cornerRadius * this.absoluteXScale();
    double yCornerRadius = this.cornerRadius * this.absoluteYScale();
    Color color = this.absoluteColor();
    IScreenRenderer renderer = Sorus.getSorus().getGUIManager().getRenderer();
    renderer.drawHollowRect(x, y, width, height, thickness, xCornerRadius, yCornerRadius, color);
  }

  /**
   * Sets the thickness of the border.
   *
   * @param thickness the thickness of the border
   * @return the hollow rectangle
   */
  public <T extends HollowRectangle> T thickness(double thickness) {
    this.thickness = thickness;
    return this.cast();
  }
}
