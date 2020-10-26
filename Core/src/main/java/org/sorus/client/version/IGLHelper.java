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

package org.sorus.client.version;

import org.sorus.client.util.Axis;

/** GL interface, used to use gl functions for all versions. */
public interface IGLHelper {

  /**
   * Scales the current gl matrix.
   *
   * @param x x to scale by
   * @param y y to scale by
   * @param z z to scale by
   */
  void scale(double x, double y, double z);

  /**
   * Translates the current gl matrix.
   *
   * @param x x to translate by
   * @param y y to translate by
   * @param z z to translze by
   */
  void translate(double x, double y, double z);

  void rotate(Axis axis, double value);

  /**
   * Enables or disables blending.
   *
   * @param blend to enable or disable blend
   */
  void blend(boolean blend);

  /**
   * Colors the current gl matrix.
   *
   * @param red the red value (0-1)
   * @param green the green value (0-1)
   * @param blue the blue value (0-1)
   * @param alpha the alpha value (0-1)
   */
  void color(double red, double green, double blue, double alpha);

  void lineWidth(double lineWidth);

  /**
   * Begins a gl scissor at the specific location.
   *
   * @param x the x position of the scissor
   * @param y the y position of the scissor
   * @param width the width of the scissor
   * @param height the height of the scissor
   */
  void beginScissor(double x, double y, double width, double height);

  /** Ends the current gl scissor. */
  void endScissor();

  void unbindTexture();
}
