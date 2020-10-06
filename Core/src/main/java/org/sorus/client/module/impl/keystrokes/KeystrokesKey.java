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

package org.sorus.client.module.impl.keystrokes;

import java.awt.*;
import org.sorus.client.Sorus;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.HollowRectangle;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.component.impl.Text;
import org.sorus.client.util.MathUtil;
import org.sorus.client.version.input.Key;
import org.sorus.client.version.input.KeybindType;

public class KeystrokesKey extends Collection {

  private final KeystrokesComponent keystrokes;
  private final KeyData input;
  private final Rectangle rectangle;
  private final HollowRectangle hollowRectangle;
  private final Text text;

  private double pressedPercent;
  private long prevRenderTime;

  public KeystrokesKey(KeystrokesComponent keystrokes, KeyData input) {
    this.keystrokes = keystrokes;
    this.input = input;
    this.position(input.x, input.y);
    this.add(rectangle = new Rectangle().size(input.width, input.height));
    this.add(hollowRectangle = new HollowRectangle().size(input.width, input.height));
    this.add(text = new Text().text(input.getName()).scale(1, 1));
  }

  @Override
  public void onRender() {
    long renderTime = System.currentTimeMillis();
    long deltaTime = renderTime - prevRenderTime;
    pressedPercent =
        MathUtil.clamp(
            pressedPercent
                + (input.isDown() ? 1 : -1)
                    * deltaTime
                    / Math.max(this.keystrokes.getFadeTime(), 0.01),
            0,
            1);
    this.rectangle.color(
        this.getIntermediateColor(
            this.keystrokes.getBackgroundStandardColor(),
            this.keystrokes.getBackgroundPressedColor(),
            pressedPercent));
    this.hollowRectangle
        .thickness(this.keystrokes.getBorderSize())
        .color(
            this.getIntermediateColor(
                this.keystrokes.getBorderStandardColor(),
                this.keystrokes.getBorderPressedColor(),
                pressedPercent));
    this.text
        .fontRenderer(keystrokes.getFontRenderer())
        .position(9 - text.width() / 2, 9 - text.height() / 2)
        .color(
            this.getIntermediateColor(
                this.keystrokes.getMainStandardColor(),
                this.keystrokes.getMainPressedColor(),
                pressedPercent));
    prevRenderTime = renderTime;
    super.onRender();
  }

  private Color getIntermediateColor(Color color1, Color color2, double mixPercent) {
    return new Color(
        (int) (color1.getRed() + ((color2.getRed() - color1.getRed()) * mixPercent)),
        (int) (color1.getGreen() + ((color2.getGreen() - color1.getGreen()) * mixPercent)),
        (int) (color1.getBlue() + ((color2.getBlue() - color1.getBlue()) * mixPercent)),
        (int) (color1.getAlpha() + ((color2.getAlpha() - color1.getAlpha()) * mixPercent)));
  }

  public abstract static class KeyData {

    private final double x, y;
    private final double width, height;

    public KeyData(double x, double y, double width, double height) {
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

    public abstract boolean isDown();

    public abstract String getName();
  }

  public static class Standard extends KeyData {

    private final Key input;

    public Standard(Key input, double x, double y, double width, double height) {
      super(x, y, width, height);
      this.input = input;
    }

    @Override
    public boolean isDown() {
      return input.isDown();
    }

    @Override
    public String getName() {
      return input.name();
    }
  }

  public static class Bind extends KeyData {

    private final KeybindType keybind;

    public Bind(KeybindType keybind, double x, double y, double width, double height) {
      super(x, y, width, height);
      this.keybind = keybind;
    }

    @Override
    public boolean isDown() {
      return Sorus.getSorus().getVersion().getInput().getKeybind(keybind).getKey().isDown();
    }

    @Override
    public String getName() {
      return Sorus.getSorus().getVersion().getInput().getKeybind(keybind).getKey().name();
    }
  }
}