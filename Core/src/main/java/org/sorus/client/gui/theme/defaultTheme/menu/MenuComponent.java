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

package org.sorus.client.gui.theme.defaultTheme.menu;

import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.gui.core.Screen;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.HollowRectangle;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.component.impl.Text;
import org.sorus.client.gui.theme.defaultTheme.DefaultTheme;

public class MenuComponent extends Collection {

  public static final double WIDTH = 193, HEIGHT = 193;

  private final Runnable runnable;

  private double hoveredPercent;

  public MenuComponent(String name, ILogoCreator logoCreator, Runnable runnable) {
    this.runnable = runnable;
    final double ROUNDING = 10;
    this.add(new Rectangle().size(WIDTH, HEIGHT).smooth(ROUNDING).color(DefaultTheme.getForegroundColorNew()));
    this.add(new HollowRectangle().thickness(3).size(WIDTH, HEIGHT).smooth(ROUNDING).color(DefaultTheme.getElementMedgroundColorNew()));
    Text text = new Text().fontRenderer(Sorus.getSorus().getGUIManager().getRenderer().getRawLineFontRenderer()).text(name).scale(3, 3);
    this.add(text.position(WIDTH / 2 - text.width() * 3 / 2, HEIGHT - text.height() * 3 * 1.75));
    Sorus.getSorus().getEventManager().register(this);
  }

  /*@Override
  public void onRender() {
    boolean hovered =
        this.isHovered(
            Sorus.getSorus().getVersion().getData(IInput.class).getMouseX(),
            Sorus.getSorus().getVersion().getData(IInput.class).getMouseY());
    double fps = Math.max(1, Sorus.getSorus().getVersion().getData(IGame.class).getFPS());
    hoveredPercent = MathUtil.clamp(hoveredPercent + (hovered ? 1 : -1) / fps * 6, 0, 1);
    main.position(-2 * hoveredPercent, -2 * hoveredPercent);
    main.scale(1 + hoveredPercent * 0.025, 1 + hoveredPercent * 0.025);
    int color = (int) (215 + 40 * hoveredPercent);
    symbol.color(new Color(color, color, color));
    super.onRender();
  }*/

  @Override
  public void onRemove() {
    Sorus.getSorus().getEventManager().unregister(this);
  }

  @EventInvoked
  public void onClick(MousePressEvent e) {
    if (this.isHovered(e.getX(), e.getY())) {
      Sorus.getSorus().getGUIManager().close((Screen) this.getContainer());
      runnable.run();
    }
  }

  private boolean isHovered(double x, double y) {
    return x > this.absoluteX()
        && x < this.absoluteX() + 206 * this.absoluteXScale()
        && y > this.absoluteY()
        && y < this.absoluteY() + 212 * this.absoluteYScale();
  }

  public interface ILogoCreator {
    void addLogoComponents();
  }
}
