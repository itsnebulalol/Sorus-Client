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

package org.sorus.client.gui.theme.defaultTheme.theme;

import java.awt.*;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.event.impl.client.input.MouseReleaseEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.Image;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.component.impl.Text;
import org.sorus.client.gui.core.font.IFontRenderer;
import org.sorus.client.gui.hud.HUD;
import org.sorus.client.gui.screen.settings.SettingsScreen;
import org.sorus.client.gui.theme.Theme;
import org.sorus.client.gui.theme.defaultTheme.hudlist.HUDComponent;
import org.sorus.client.module.ModuleConfigurable;
import org.sorus.client.util.Axis;
import org.sorus.client.version.IGLHelper;

public class ThemeListComponent extends Collection {

  private final DefaultThemeListScreen themeListScreen;
  private final Theme theme;

  public ThemeListComponent(DefaultThemeListScreen themeListScreen, Theme theme) {
    this.themeListScreen = themeListScreen;
    this.theme = theme;
    IFontRenderer fontRenderer =
        Sorus.getSorus().getGUIManager().getRenderer().getGidoleFontRenderer();
    this.add(new Rectangle().size(670, 125).position(5, 4).color(new Color(30, 30, 30)));
    this.add(
        new Rectangle()
            .gradient(
                new Color(14, 14, 14, 150),
                new Color(14, 14, 14, 150),
                new Color(14, 14, 14, 30),
                new Color(14, 14, 14, 30))
            .size(670, 4)
            .position(5, 0));
    this.add(
        new Rectangle()
            .gradient(
                new Color(14, 14, 14, 150),
                new Color(14, 14, 14, 30),
                new Color(14, 14, 14, 30),
                new Color(14, 14, 14, 30))
            .size(4, 4)
            .position(675, 0));
    this.add(
        new Rectangle()
            .gradient(
                new Color(14, 14, 14, 150),
                new Color(14, 14, 14, 30),
                new Color(14, 14, 14, 30),
                new Color(14, 14, 14, 150))
            .size(4, 125)
            .position(675, 4));
    this.add(
        new Rectangle()
            .gradient(
                new Color(14, 14, 14, 30),
                new Color(14, 14, 14, 30),
                new Color(14, 14, 14, 30),
                new Color(14, 14, 14, 150))
            .size(4, 4)
            .position(675, 129));
    this.add(
        new Rectangle()
            .gradient(
                new Color(14, 14, 14, 30),
                new Color(14, 14, 14, 30),
                new Color(14, 14, 14, 150),
                new Color(14, 14, 14, 150))
            .size(670, 4)
            .position(5, 129));
    this.add(
        new Rectangle()
            .gradient(
                new Color(14, 14, 14, 30),
                new Color(14, 14, 14, 30),
                new Color(14, 14, 14, 150),
                new Color(14, 14, 14, 30))
            .size(4, 4)
            .position(2, 129));
    this.add(
        new Rectangle()
            .gradient(
                new Color(14, 14, 14, 30),
                new Color(14, 14, 14, 150),
                new Color(14, 14, 14, 150),
                new Color(14, 14, 14, 30))
            .size(4, 125)
            .position(2, 4));
    this.add(
        new Rectangle()
            .gradient(
                new Color(14, 14, 14, 30),
                new Color(14, 14, 14, 150),
                new Color(14, 14, 14, 30),
                new Color(14, 14, 14, 30))
            .size(4, 4)
            .position(1, 0));
    Collection collection = new Collection().position(15, 15);
    this.add(collection);
    // module.addIconElements(collection);
    this.add(
        new Text()
            .fontRenderer(fontRenderer)
            .text(theme.getName())
            .position(125, 20)
            .scale(4, 4)
            .color(new Color(235, 235, 235, 210)));
    int i = 0;
    this.add(new SettingsButton(theme).position(615, 22));
    this.add(new RemoveButton(theme).position(555, 22));
    Sorus.getSorus().getEventManager().register(this);
  }

  @Override
  public void onRemove() {
    Sorus.getSorus().getEventManager().unregister(this);
  }

  @EventInvoked
  public void onClick(MousePressEvent e) {
    if(this.collection == null) {
      return;
    }
    if(e.getX() > this.absoluteX() && e.getY() < this.absoluteX() + 670 * this.absoluteXScale() && e.getY() > this.absoluteY() && e.getY() < this.absoluteY() + 125 * this.absoluteYScale()) {
      this.themeListScreen.onComponentDrag(this, e.getX(), e.getY());
    }
  }

  @EventInvoked
  public void onRelease(MouseReleaseEvent e) {
    if(this.equals(this.themeListScreen.getDraggedComponent())) {
      this.themeListScreen.onComponentRelease(this);
    }
  }

  public Theme getTheme() {
    return theme;
  }

  public class RemoveButton extends Collection {

    private final Theme theme;

    private final Collection main;

    private double hoverPercent;

    private long prevRenderTime;

    public RemoveButton(Theme theme) {
      this.theme = theme;
      this.add(main = new Collection());
      main.add(new Rectangle().size(40, 40).smooth(5).color(new Color(160, 35, 35)));
      main.add(
              new Rectangle().size(30, 10).smooth(3).position(5, 15).color(new Color(210, 210, 210)));
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
      hoverPercent =
              Math.max(0, Math.min(1, hoverPercent + (hovered ? 1 : -1) * deltaTime * 0.008));
      this.main
              .position(-hoverPercent * 2.25, -hoverPercent * 2.25)
              .scale(1 + hoverPercent * 0.1, 1 + hoverPercent * 0.1);
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
        Sorus.getSorus().getThemeManager().remove(theme);
        ThemeListComponent.this.themeListScreen.updateThemes();
      }
    }

    private boolean isHovered(double x, double y) {
      return x > this.absoluteX()
              && x < this.absoluteX() + 40 * this.absoluteXScale()
              && y > this.absoluteY()
              && y < this.absoluteY() + 40 * this.absoluteYScale();
    }
  }

  public class SettingsButton extends Collection {

    private double hoverPercent;

    private long prevRenderTime;

    private final Theme theme;

    private final Image image;

    public SettingsButton(Theme theme) {
      this.theme = theme;
      this.add(image = new Image().resource("sorus/gear.png").size(40, 40));
      Sorus.getSorus().getEventManager().register(this);
    }

    @Override
    public void onRender() {
      long renderTime = System.currentTimeMillis();
      long deltaTime = renderTime - prevRenderTime;
      boolean hovered = this.isHovered(
                  Sorus.getSorus().getVersion().getInput().getMouseX(),
                  Sorus.getSorus().getVersion().getInput().getMouseY());
      hoverPercent =
          Math.max(0, Math.min(1, hoverPercent + (hovered ? 1 : -1) * deltaTime * 0.008));
      image.scale(1 + hoverPercent * 0.1, 1 + hoverPercent * 0.1);
      image.position(-2 * hoverPercent, -2 * hoverPercent);
      image.color(new Color(235, 235, 235, (int) (210 + 45 * hoverPercent)));
      prevRenderTime = renderTime;
      double x = this.absoluteX() + 20 * this.absoluteXScale();
      double y = this.absoluteY() + 20 * this.absoluteYScale();
      IGLHelper glHelper = Sorus.getSorus().getVersion().getGLHelper();
      glHelper.translate(x, y, 0);
      glHelper.rotate(Axis.Z, hoverPercent * 50);
      glHelper.translate(-x, -y, 0);
      super.onRender();
      glHelper.translate(x, y, 0);
      glHelper.rotate(Axis.Z, -hoverPercent * 50);
      glHelper.translate(-x, -y, 0);
    }

    @Override
    public void onRemove() {
      Sorus.getSorus().getEventManager().unregister(this);
      super.onRemove();
    }

    @EventInvoked
    public void onClick(MousePressEvent e) {
      if (this.isHovered(e.getX(), e.getY())) {
        Sorus.getSorus().getGUIManager().open(new SettingsScreen(theme));
        // ThemeListComponent.this.themeListScreen.screen.displayModuleSettings(module);
      }
    }

    private boolean isHovered(double x, double y) {
      return x > this.absoluteX()
          && x < this.absoluteX() + 50 * this.absoluteXScale()
          && y > this.absoluteY()
          && y < this.absoluteY() + 50 * this.absoluteYScale();
    }
  }
}
