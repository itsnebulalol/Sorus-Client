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

import java.util.ArrayList;
import java.util.List;
import org.sorus.client.Sorus;
import org.sorus.client.gui.core.component.Collection;

/**
 * Implementation of {@link Collection} using gl scissor to only render inside of the component's
 * bounds.
 */
public class Scissor extends Collection {

  /** Current active scissors. */
  private static final List<GLScissor> currentScissors = new ArrayList<>();

  /**
   * Adds a scissor to the list of current scissors and updates the gl scissor.
   *
   * @param x the x position of the scissor
   * @param y the y position of the scissor
   * @param width the scissor width
   * @param height the scissor height
   */
  private static void beginScissor(double x, double y, double width, double height) {
    currentScissors.add(new GLScissor(x, y, width, height));
    updateCurrentScissor();
  }

  /** Removes from the list of current scissors and updates the gl scissor. */
  private static void endScissor() {
    currentScissors.remove(currentScissors.size() - 1);
    updateCurrentScissor();
  }

  /**
   * Updates the current scissor by going through all the scissors and determining the combined
   * scissor.
   */
  private static void updateCurrentScissor() {
    if (currentScissors.size() == 0) {
      Sorus.getSorus().getVersion().getGLHelper().endScissor();
      return;
    }
    double left = Double.MIN_VALUE;
    double top = Double.MIN_VALUE;
    double right = Double.MAX_VALUE;
    double bottom = Double.MAX_VALUE;
    for (GLScissor scissor : currentScissors) {
      left = Math.max(scissor.getX(), left);
      top = Math.max(scissor.getY(), top);
      right = Math.min(scissor.getX() + scissor.getWidth(), right);
      bottom = Math.min(scissor.getY() + scissor.getHeight(), bottom);
    }
    if (left > right || top > bottom) {
      Sorus.getSorus().getVersion().getGLHelper().beginScissor(0, 0, 0, 0);
      return;
    }
    Sorus.getSorus().getVersion().getGLHelper().beginScissor(left, top, right - left, bottom - top);
  }

  /** Class used to store components such as location of a scissor. */
  private static class GLScissor {

    private final double x, y;
    private final double width, height;

    public GLScissor(double x, double y, double width, double height) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
    }

    public double getX() {
      return x;
    }

    public double getY() {
      return y;
    }

    public double getWidth() {
      return width;
    }

    public double getHeight() {
      return height;
    }
  }

  private double width, height;

  /**
   * Renders all the components stored in the collection to the screen after applying the scissor.
   */
  @Override
  public void onRender() {
    Scissor.beginScissor(
        this.absoluteX(),
        this.absoluteY(),
        this.width * this.absoluteXScale(),
        this.height * this.absoluteYScale());
    super.onRender();
    Scissor.endScissor();
  }

  /**
   * Sets the size of the rectangle.
   *
   * @param width the wanted width of the rectangle
   * @param height the wanted height of the rectangle
   * @return the rectangle
   */
  public <T extends Scissor> T size(double width, double height) {
    this.width = width;
    this.height = height;
    return this.cast();
  }
}
