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

public class Image extends Component {

  private String resource;
  private double width, height;
  private double textureX, textureY;
  private double textureWidth, textureHeight;
  private boolean fullImage = true;

  @Override
  public void onRender() {
    double x = this.absoluteX();
    double y = this.absoluteY();
    double width = this.width * this.absoluteXScale();
    double height = this.height * this.absoluteYScale();
    Color color = this.absoluteColor();
    IScreenRenderer renderer = Sorus.getSorus().getGUIManager().getRenderer();
    if(this.fullImage) {
      renderer.drawFullImage(resource, x, y, width, height, color);
    } else {
      renderer.drawPartialImage(resource, x, y, width, height, textureX, textureY, textureWidth, textureHeight, color);
    }
  }

  public <T extends Image> T size(double width, double height) {
    this.width = width;
    this.height = height;
    return this.cast();
  }

  public <T extends Image> T resource(String resource) {
    this.resource = resource;
    return this.cast();
  }

  public <T extends Image> T texturePosition(double textureX, double textureY) {
    this.textureX = textureX;
    this.textureY = textureY;
    this.fullImage = false;
    return this.cast();
  }

  public <T extends Image> T textureSize(double textureWidth, double textureHeight) {
    this.textureWidth = textureWidth;
    this.textureHeight = textureHeight;
    this.fullImage = false;
    return this.cast();
  }

}
