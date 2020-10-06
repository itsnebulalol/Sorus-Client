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
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.component.impl.Text;
import org.sorus.client.gui.screen.settings.Configurable;
import org.sorus.client.settings.Setting;

/** Config element to change the state of a boolean setting. */
public class Toggle extends Configurable {

  private final Setting<Boolean> setting;

  private boolean value;
  private double switchPercent;

  private final Rectangle background;
  private final Rectangle selector;

  private long prevRenderTime;

  public Toggle(Setting<Boolean> setting, String description) {
    this.setting = setting;
    this.value = setting.getValue();
    this.background = new Rectangle().size(80, 40).smooth(20).position(570, 20);
    this.add(background);
    this.selector = new Rectangle().size(30, 30).smooth(15).color(new Color(235, 235, 235));
    this.add(selector);
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
    long renderTime = System.currentTimeMillis();
    long deltaTime = renderTime - prevRenderTime;
    switchPercent = Math.max(0, Math.min(1, switchPercent + (value ? 1 : -1) * deltaTime * 0.007));
    this.background.color(
        new Color(
            (int) (160 - switchPercent * 135),
            (int) (35 + switchPercent * 125),
            (int) (35 + switchPercent * 30)));
    this.selector.position(575 + 40 * switchPercent, 25);
    prevRenderTime = renderTime;
    super.onRender();
  }

  @Override
  public void onRemove() {
    Sorus.getSorus().getEventManager().unregister(this);
  }

  /**
   * Disables or enables the setting based on its value before.
   *
   * @param e the mouse press event
   */
  @EventInvoked
  public void onClick(MousePressEvent e) {
    if (e.getX() > this.absoluteX() + 570 * this.absoluteXScale()
        && e.getX() < this.absoluteX() + 650 * this.absoluteXScale()
        && e.getY() > this.absoluteY() + 20 * this.absoluteYScale()
        && e.getY() < this.absoluteY() + 70 * this.absoluteYScale()) {
      this.value = !value;
      this.setting.setValue(value);
    }
  }

  @Override
  public double getHeight() {
    return 80;
  }
}
