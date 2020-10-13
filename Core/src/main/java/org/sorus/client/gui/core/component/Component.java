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

package org.sorus.client.gui.core.component;

import java.awt.*;
import org.sorus.client.gui.core.IContainer;
import org.sorus.client.gui.core.component.api.IRenderable;
import org.sorus.client.gui.core.util.ColorUtil;

/** The base component of the gui framework, all gui components will extend this in some way. */
public abstract class Component implements IRenderable {

  /** The component's state. */
  protected double x, y;

  protected double xScale = 1, yScale = 1;
  protected Color color = Color.WHITE;

  /** "Parent" collection used for determining the component's absolute state. */
  protected Collection collection;

  /** Called when the component's screen is closed or it is removed from anothe collection. */
  public void onRemove() {}

  /**
   * Positions the component.
   *
   * @param x the wanted x position
   * @param y the wanted y position
   * @return the component
   */
  public <T extends Component> T position(double x, double y) {
    this.x = x;
    this.y = y;
    return this.cast();
  }

  /**
   * Scales the component.
   *
   * @param xScale the wanted x scale
   * @param yScale the wanted y scale
   * @return the component
   */
  public <T extends Component> T scale(double xScale, double yScale) {
    this.xScale = xScale;
    this.yScale = yScale;
    return this.cast();
  }

  /**
   * Colors the component.
   *
   * @param color the wanted color
   * @return the component
   */
  public <T extends Component> T color(Color color) {
    this.color = color;
    return this.cast();
  }

  /**
   * Gets the absolute x based on the parents state.
   *
   * @return absolute x
   */
  public double absoluteX() {
    return this.collection.absoluteX() + x * this.collection.absoluteXScale();
  }

  /**
   * Gets the absolute y based on the parents state.
   *
   * @return absolute y
   */
  public double absoluteY() {
    return this.collection.absoluteY() + y * this.collection.absoluteYScale();
  }

  /**
   * Gets the absolute x scale based on the parents state.
   *
   * @return absolute x scale
   */
  public double absoluteXScale() {
    return this.collection.absoluteXScale() * xScale;
  }

  /**
   * Gets the absolute y scale based on the parents state.
   *
   * @return absolute y scale
   */
  public double absoluteYScale() {
    return this.collection.absoluteYScale() * yScale;
  }

  /**
   * Gets the absolute color based on the parents state.
   *
   * @return absolute color
   */
  public Color absoluteColor() {
    return ColorUtil.average(this.collection.absoluteColor(), color);
  }

  protected void setCollection(Collection collection) {
    this.collection = collection;
  }

  protected IContainer getContainer() {
    return this.collection.getContainer();
  }

  protected <T extends Component> T cast() {
    return (T) this;
  }
}
