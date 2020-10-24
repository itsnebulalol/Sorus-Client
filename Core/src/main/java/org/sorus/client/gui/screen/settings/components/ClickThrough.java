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
import java.util.List;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.HollowRectangle;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.component.impl.Text;
import org.sorus.client.gui.screen.settings.Configurable;
import org.sorus.client.gui.screen.settings.SettingsHolder;
import org.sorus.client.gui.theme.defaultTheme.DefaultTheme;
import org.sorus.client.settings.Setting;
import org.sorus.client.util.MathUtil;

public class ClickThrough extends Configurable {

  private final Setting<Long> setting;
  private final List<String> options;

  private long value;

  public ClickThrough(Setting<Long> setting, List<String> options, String description) {
    this.setting = setting;
    this.options = options;
    this.value = setting.getValue();
    this.add(new ClickThroughInner().position(400, 20));
    this.add(
        new Text()
            .fontRenderer(Sorus.getSorus().getGUIManager().getRenderer().getGidoleFontRenderer())
            .text(description)
            .scale(3.5, 3.5)
            .position(30, 30)
            .color(new Color(170, 170, 170)));
  }

  @Override
  public double getHeight() {
    return 80;
  }

  private void setValue(long value) {
    this.value = value;
    if (value > this.options.size() - 1) {
      this.value = 0;
    }
    if (value < 0) {
      this.value = this.options.size() - 1;
    }
    this.setting.setValue(this.value);
    ((SettingsHolder) this.collection).refresh();
  }

  public class ClickThroughInner extends Collection {

    private final Text currentText;
    private final Text leftArrow;
    private final Text rightArrow;

    private double leftHoveredPercent;
    private double rightHoveredPercent;

    private long prevRenderTime;

    public ClickThroughInner() {
      this.add(new Rectangle().size(250, 40).color(DefaultTheme.getMedforegroundLayerColor()));
      this.add(
          new HollowRectangle()
              .size(250, 40)
              .color(DefaultTheme.getForegroundLessLayerColor()));
      this.add(new Rectangle().size(1, 38).position(40, 1).color(new Color(235, 235, 235, 210)));
      this.add(new Rectangle().size(1, 38).position(209, 1).color(new Color(235, 235, 235, 210)));
      this.add(
          currentText =
              new Text()
                  .fontRenderer(
                      Sorus.getSorus().getGUIManager().getRenderer().getGidoleFontRenderer())
                  .scale(3, 3));
      this.add(
          leftArrow =
              new Text()
                  .fontRenderer(
                      Sorus.getSorus().getGUIManager().getRenderer().getGidoleFontRenderer())
                  .text("<")
                  .scale(4, 4)
                  .position(13, 7));
      this.add(
          rightArrow =
              new Text()
                  .fontRenderer(
                      Sorus.getSorus().getGUIManager().getRenderer().getGidoleFontRenderer())
                  .text(">")
                  .scale(4, 4)
                  .position(225, 7));
      Sorus.getSorus().getEventManager().register(this);
    }

    @Override
    public void onRender() {
      currentText.text(ClickThrough.this.options.get((int) ClickThrough.this.value));
      currentText.position(125 - currentText.width() / 2 * 3, 20 - currentText.height() / 2 * 3 + 1);
      long renderTime = System.currentTimeMillis();
      long deltaTime = renderTime - prevRenderTime;
      double mouseX = Sorus.getSorus().getVersion().getInput().getMouseX();
      double mouseY = Sorus.getSorus().getVersion().getInput().getMouseY();
      this.leftHoveredPercent =
          MathUtil.clamp(
              leftHoveredPercent + (this.leftHovered(mouseX, mouseY) ? 1 : -1) * deltaTime * 0.01,
              0,
              1);
      this.leftArrow.color(new Color(235, 235, 235, (int) (205 + 50 * leftHoveredPercent)));
      this.rightHoveredPercent =
          MathUtil.clamp(
              rightHoveredPercent + (this.rightHovered(mouseX, mouseY) ? 1 : -1) * deltaTime * 0.01,
              0,
              1);
      this.rightArrow.color(new Color(235, 235, 235, (int) (205 + 50 * rightHoveredPercent)));
      prevRenderTime = renderTime;
      super.onRender();
    }

    @Override
    public void onRemove() {
      Sorus.getSorus().getEventManager().unregister(this);
      super.onRemove();
    }

    @EventInvoked
    public void onClick(MousePressEvent e) {
      if (this.leftHovered(e.getX(), e.getY())) {
        ClickThrough.this.setValue(value - 1);
      }
      if (this.rightHovered(e.getX(), e.getY())) {
        ClickThrough.this.setValue(value + 1);
      }
    }

    private boolean leftHovered(double x, double y) {
      return x > this.absoluteX()
          && x < this.absoluteX() + 40 * this.absoluteXScale()
          && y > this.absoluteY()
          && y < this.absoluteY() + 40 * this.absoluteYScale();
    }

    private boolean rightHovered(double x, double y) {
      return x > this.absoluteX() + 210 * this.absoluteXScale()
          && x < this.absoluteX() + 250 * this.absoluteXScale()
          && y > this.absoluteY()
          && y < this.absoluteY() + 40 * this.absoluteYScale();
    }
  }
}
