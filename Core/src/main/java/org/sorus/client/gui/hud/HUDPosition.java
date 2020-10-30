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

package org.sorus.client.gui.hud;

public class HUDPosition {

  private double x, y;
  private double offsetX, offsetY;

  public HUDPosition() {
    this.x = 0.5;
    this.y = 0.5;
  }

  /**
   * Sets the x position of the hud relative to the screen width, and also chooses a point based on
   * the hud location to make the hud location "based" off of. This allows huds to not just expand
   * to the bottom right when they get bigger.
   */
  public void setX(HUD hud, double x, double screenWidth) {
    double hudWidth = hud.getWidth() * hud.getScale();
    double leftDistance = Math.abs(0 - (x - hudWidth / 2));
    double centerDistance = Math.abs(screenWidth / 2 - x);
    double rightDistance = Math.abs(screenWidth - (x + hudWidth / 2));
    if (leftDistance < centerDistance && leftDistance < rightDistance) {
      this.offsetX = -0.5;
    }
    if (centerDistance < leftDistance && centerDistance < rightDistance) {
      this.offsetX = 0;
    }
    if (rightDistance < leftDistance && rightDistance < centerDistance) {
      this.offsetX = 0.5;
    }
    this.x = (x + hudWidth * offsetX) / screenWidth;
  }

  /**
   * Sets the y position of the hud relative to the screen height, and also chooses a point based on
   * the hud location to make the hud location "based" off of. This allows huds to not just expand
   * to the bottom right when they get bigger.
   */
  public void setY(HUD hud, double y, double screenHeight) {
    double hudHeight = hud.getHeight() * hud.getScale();
    double topDistance = Math.abs(0 - (y - hudHeight / 2));
    double centerDistance = Math.abs(screenHeight / 2 - y);
    double bottomDistance = Math.abs(screenHeight - (y + hudHeight / 2));
    if (topDistance < centerDistance && topDistance < bottomDistance) {
      this.offsetY = -0.5;
    }
    if (centerDistance < topDistance && centerDistance < bottomDistance) {
      this.offsetY = 0;
    }
    if (bottomDistance < topDistance && bottomDistance < centerDistance) {
      this.offsetY = 0.5;
    }
    this.y = (y + hudHeight * offsetY) / screenHeight;
  }

  /** Returns the absolute x of the hud for the given screen width. */
  public double getX(HUD hud, double screenWidth) {
    return (this.x - (this.offsetX * hud.getWidth() * hud.getScale()) / screenWidth) * screenWidth;
  }

  /** Returns the absolute y of the hud for the given screen width. */
  public double getY(HUD hud, double screenHeight) {
    return (this.y - (this.offsetY * hud.getHeight() * hud.getScale()) / screenHeight)
        * screenHeight;
  }
}
