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

package org.sorus.client.gui.hud.positonscreen;

public class SelectedHUDState {

  private final InteractType interactType;
  private final double initialX;
  private final double initialY;
  private final double initialScale;
  private final double initialMouseX;
  private final double initialMouseY;

  public SelectedHUDState(
      InteractType interactType,
      double initialX,
      double initialY,
      double initialScale,
      double initialMouseX,
      double initialMouseY) {
    this.interactType = interactType;
    this.initialX = initialX;
    this.initialY = initialY;
    this.initialScale = initialScale;
    this.initialMouseX = initialMouseX;
    this.initialMouseY = initialMouseY;
  }

  public InteractType getInteractType() {
    return interactType;
  }

  public double getInitialX() {
    return initialX;
  }

  public double getInitialY() {
    return initialY;
  }

  public double getInitialMouseX() {
    return initialMouseX;
  }

  public double getInitialMouseY() {
    return initialMouseY;
  }

  public double getInitialScale() {
    return initialScale;
  }

  public enum InteractType {
    DRAG,
    RESIZE_LEFT,
    RESIZE_RIGHT
  }
}
