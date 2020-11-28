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

package org.sorus.client.event.impl.client.render;

import org.sorus.client.event.EventCancelable;

public class RenderObjectEvent extends EventCancelable {

  public static class Crosshair extends RenderObjectEvent {}

  public static class BlockOverlay extends RenderObjectEvent {}

  public static class Text extends RenderObjectEvent {

    private String text;

    public Text(String text) {
      this.text = text;
    }

    public String getText() {
      return text;
    }

    public void setText(String text) {
      this.text = text;
    }
  }

  public static class GradientRectangle extends RenderObjectEvent {

    private final int left, right;
    private final int top, bottom;
    private final int color1, color2;

    public GradientRectangle(int left, int top, int right, int bottom, int color1, int color2) {
      this.left = left;
      this.top = top;
      this.right = right;
      this.bottom = bottom;
      this.color1 = color1;
      this.color2 = color2;
    }

    public int getLeft() {
      return left;
    }

    public int getRight() {
      return right;
    }

    public int getTop() {
      return top;
    }

    public int getBottom() {
      return bottom;
    }

    public int getColor1() {
      return color1;
    }

    public int getColor2() {
      return color2;
    }
  }

  public static class Sidebar extends RenderObjectEvent {}
}
