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

package org.sorus.client.module.impl.customscreens.mainmenu;

import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.gui.core.Screen;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.Image;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.theme.defaultTheme.DefaultTheme;
import org.sorus.client.util.MathUtil;
import org.sorus.client.version.game.GUIType;
import org.sorus.client.version.game.IGame;
import org.sorus.client.version.input.IInput;

public abstract class MainMenuBottomElement extends Collection {

  protected final Collection collection = new Collection();
  private long prevRenderTime;
  private double expandedPercent;

  public MainMenuBottomElement() {
    collection.add(
        new Rectangle().size(75, 75).smooth(37.5).color(DefaultTheme.getMedgroundLayerColor()));
    this.add(collection);
    Sorus.getSorus().getEventManager().register(this);
  }

  @Override
  public void onRender() {
    long renderTime = System.currentTimeMillis();
    long deltaTime = renderTime - prevRenderTime;
    boolean hovered =
        this.isHovered(
            Sorus.getSorus().getVersion().getData(IInput.class).getMouseX(),
            Sorus.getSorus().getVersion().getData(IInput.class).getMouseY());
    expandedPercent =
        MathUtil.clamp(expandedPercent + (hovered ? 1 : -1) * deltaTime * 0.015, 0, 1);
    collection
        .position(-expandedPercent * 2, -expandedPercent * 2)
        .scale(1 + 0.05 * expandedPercent, 1 + 0.05 * expandedPercent);
    prevRenderTime = renderTime;
    super.onRender();
  }

  @Override
  public void onRemove() {
    Sorus.getSorus().getEventManager().unregister(this);
    super.onRemove();
  }

  private boolean isHovered(double x, double y) {
    return x > this.absoluteX()
        && x < this.absoluteX() + 75 * this.absoluteXScale()
        && y > this.absoluteY()
        && y < this.absoluteY() + 75 * this.absoluteYScale();
  }

  @EventInvoked
  public void onClick(MousePressEvent e) {
    if (this.isHovered(e.getX(), e.getY())) {
      this.onClick();
    }
  }

  public abstract void onClick();

  public static class LanguagesButton extends MainMenuBottomElement {

    public LanguagesButton() {
      collection.add(
          new Image()
              .resource("sorus/modules/custommenus/earth.png")
              .size(50, 50)
              .position(12.5, 12.5));
    }

    @Override
    public void onClick() {
      Sorus.getSorus().getGUIManager().close((Screen) this.getContainer());
      Sorus.getSorus().getVersion().getData(IGame.class).display(GUIType.LANGUAGES);
    }
  }

  public static class SettingsButton extends MainMenuBottomElement {

    public SettingsButton() {
      collection.add(
          new Image()
              .resource("sorus/gear.png")
              .size(50, 50)
              .position(12.5, 12.5)
              .color(DefaultTheme.getForegroundLayerColor()));
    }

    @Override
    public void onClick() {
      Sorus.getSorus().getGUIManager().close((Screen) this.getContainer());
      Sorus.getSorus().getVersion().getData(IGame.class).display(GUIType.SETTINGS);
    }
  }

  public static class ExitButton extends MainMenuBottomElement {

    public ExitButton() {
      collection.add(
          new Image()
              .resource("sorus/modules/custommenus/exit.png")
              .size(50, 50)
              .position(12.5, 12.5)
              .color(DefaultTheme.getForegroundLayerColor()));
    }

    @Override
    public void onClick() {
      Sorus.getSorus().getGUIManager().close((Screen) this.getContainer());
      Sorus.getSorus().getVersion().getData(IGame.class).shutdown();
    }
  }
}
