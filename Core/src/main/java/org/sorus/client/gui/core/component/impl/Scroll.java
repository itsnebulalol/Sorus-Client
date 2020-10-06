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

import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.util.MathUtil;

/** Implementation of {@link Collection} to allow y scrolling. */
public class Scroll extends Collection {

  private double scroll;
  private double minScroll, maxScroll;

  /**
   * Gets the absolute y based on the parents state.
   *
   * @return absolute y
   */
  @Override
  protected double absoluteY() {
    return super.absoluteY() + scroll * this.absoluteYScale();
  }

  /**
   * Sets the current scroll and constrains it in between the min and max allowed scroll.
   *
   * @param scroll the wanted scroll
   * @return the constrained scroll
   */
  public double setScroll(double scroll) {
    this.scroll = MathUtil.clamp(scroll, this.getMinScroll(), this.getMaxScroll());
    return this.scroll;
  }

  public double getScroll() {
    return scroll;
  }

  public double getMinScroll() {
    return minScroll;
  }

  public double getMaxScroll() {
    return maxScroll;
  }

  public void addMinMaxScroll(double minScroll, double maxScroll) {
    this.minScroll = minScroll;
    this.maxScroll = maxScroll;
  }
}
