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

package org.sorus.client.gui.screen.settings.components;

import java.awt.*;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.event.impl.client.input.MouseReleaseEvent;
import org.sorus.client.gui.core.component.impl.Arc;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.component.impl.Text;
import org.sorus.client.gui.screen.settings.Configurable;
import org.sorus.client.settings.Setting;
import org.sorus.client.util.MathUtil;
import org.sorus.client.version.input.IInput;

public class Slider extends Configurable {

  private final Setting<Double> setting;
  private final double minValue, maxValue;

  private boolean selected;

  protected double value;

  private final Arc selector;
  private final Rectangle selector2;

  public Slider(Setting<Double> setting, double minValue, double maxValue, String description) {
    this.setting = setting;
    this.minValue = minValue;
    this.maxValue = maxValue;
    this.value = setting.getValue();
    this.add(
        new Rectangle().smooth(3.5).size(180, 7).position(470, 36.5).color(new Color(80, 80, 80)));
    this.add(
        this.selector2 =
            new Rectangle().smooth(3.5).position(470, 36.5).color(new Color(210, 210, 210)));
    this.add(
        this.selector = new Arc().angle(0, 360).radius(10, 10).color(new Color(210, 210, 210)));
    this.add(
        new Text()
            .fontRenderer(Sorus.getSorus().getGUIManager().getRenderer().getGidoleFontRenderer())
            .text(description)
            .scale(3.5, 3.5)
            .position(30, 30)
            .color(new Color(170, 170, 170)));
    Sorus.getSorus().getEventManager().register(this);
  }

  @Override
  public void onRender() {
    if (this.selected) {
      double mouseX = Sorus.getSorus().getVersion().getData(IInput.class).getMouseX();
      double baseValue =
          (MathUtil.clamp(
                      mouseX,
                      this.absoluteX() + 470 * this.absoluteXScale(),
                      this.absoluteX() + 650 * this.absoluteXScale())
                  - (this.absoluteX() + 470 * this.absoluteXScale()))
              / (180 * this.absoluteXScale());
      value = minValue + (baseValue * (maxValue - minValue));
      this.setting.setValue(value);
    }
    this.selector.position(470 + ((value - minValue) / (maxValue - minValue)) * 180 - 10, 30);
    this.selector2.size(((value - minValue) / (maxValue - minValue)) * 180, 7);
    super.onRender();
  }

  @EventInvoked
  public void onClick(MousePressEvent e) {
    if (this.getContainer().isInteractContainer()) {
      selected = this.isHovered(e.getX(), e.getY());
    }
  }

  @EventInvoked
  public void onRelease(MouseReleaseEvent e) {
    selected = false;
  }

  private boolean isHovered(double x, double y) {
    return x > this.absoluteX() + 470 * this.absoluteXScale()
        && x < this.absoluteX() + 650 * this.absoluteXScale()
        && y > this.absoluteY() + 20 * this.absoluteYScale()
        && y < this.absoluteY() + 60 * this.absoluteYScale();
  }

  @Override
  public double getHeight() {
    return 80;
  }
}
