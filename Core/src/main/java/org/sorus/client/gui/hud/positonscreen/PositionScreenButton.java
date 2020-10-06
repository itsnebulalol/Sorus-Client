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

import java.awt.*;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.HollowRectangle;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.component.impl.Text;
import org.sorus.client.util.MathUtil;

public class PositionScreenButton extends Collection {

  private final Runnable runnable;
  private final double width, height;
  private final Collection main;

  private long prevRenderTime;
  private double expandedPercent;

  public PositionScreenButton(Runnable runnable, String textIn, double width, double height) {
    this.runnable = runnable;
    this.width = width;
    this.height = height;
    this.add(main = new Collection());
    main.add(new Rectangle().smooth(5).size(width, height).color(new Color(20, 20, 20, 200)));
    main.add(
        new HollowRectangle()
            .thickness(2)
            .smooth(5)
            .size(width, height)
            .color(new Color(235, 235, 235, 210)));
    Text text;
    main.add(
        text =
            new Text()
                .fontRenderer(
                    Sorus.getSorus().getGUIManager().getRenderer().getGidoleFontRenderer())
                .text(textIn)
                .scale(4, 4)
                .color(new Color(235, 235, 235, 210)));
    text.position(width / 2 - text.width() / 2 * 4, height / 2 - text.height() / 2 * 4);
    Sorus.getSorus().getEventManager().register(this);
  }

  @Override
  public void onRender() {
    long renderTime = System.currentTimeMillis();
    long deltaTime = renderTime - prevRenderTime;
    boolean hovered =
        this.isHovered(
            Sorus.getSorus().getVersion().getInput().getMouseX(),
            Sorus.getSorus().getVersion().getInput().getMouseY());
    expandedPercent = MathUtil.clamp(expandedPercent + (hovered ? 1 : -1) * deltaTime * 0.01, 0, 1);
    main.scale(1 + 0.05 * expandedPercent, 1 + 0.05 * expandedPercent)
        .position(-0.02 * width * expandedPercent, -0.02 * height * expandedPercent);
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
    if (this.isHovered(e.getX(), e.getY())) {
      runnable.run();
    }
  }

  private boolean isHovered(double x, double y) {
    return x > this.absoluteX()
        && x < this.absoluteX() + width * this.absoluteXScale()
        && y > this.absoluteY()
        && y < this.absoluteY() + height * this.absoluteYScale();
  }
}
