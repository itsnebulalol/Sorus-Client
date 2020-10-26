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
import org.sorus.client.gui.core.Screen;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.Panel;
import org.sorus.client.gui.core.component.impl.*;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.font.IFontRenderer;
import org.sorus.client.gui.screen.Callback;
import org.sorus.client.gui.screen.theme.SelectThemeScreen;
import org.sorus.client.gui.theme.Theme;
import org.sorus.client.gui.theme.ThemeBase;
import org.sorus.client.gui.theme.ThemeManager;
import org.sorus.client.gui.theme.defaultTheme.DefaultTheme;
import org.sorus.client.version.input.Key;

public class DefaultSelectThemeScreen extends ThemeBase<SelectThemeScreen> {

  private final ThemeManager themeManager;
  private final Callback<Theme> receiver;

  private Panel main;
  private Scroll scroll;

  private int themeCount;

  private double targetScroll;

  private SelectComponent selected;

  public DefaultSelectThemeScreen(ThemeManager themeManager, Callback<Theme> receiver) {
    this.themeManager = themeManager;
    this.receiver = receiver;
  }

  @Override
  public void init() {
    Sorus.getSorus().getVersion().getRenderer().enableBlur();
    main = new Panel();
    Collection menu = new Collection().position(610, 140);
    main.add(menu);
    menu.add(
        new Rectangle()
            .smooth(5)
            .size(700, 720)
            .position(0, 70)
            .color(DefaultTheme.getBackgroundLayerColor()));
    menu.add(
        new Rectangle().size(700, 65).position(0, 5).color(DefaultTheme.getMedgroundLayerColor()));
    menu.add(
        new Arc()
            .radius(5, 5)
            .angle(180, 270)
            .position(0, 0)
            .color(DefaultTheme.getMedgroundLayerColor()));
    menu.add(
        new Arc()
            .radius(5, 5)
            .angle(90, 180)
            .position(690, 0)
            .color(DefaultTheme.getMedgroundLayerColor()));
    menu.add(
        new Rectangle().size(690, 5).position(5, 0).color(DefaultTheme.getMedgroundLayerColor()));
    menu.add(
        new Rectangle()
            .gradient(
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowStartColor(),
                DefaultTheme.getShadowStartColor())
            .size(700, 7)
            .position(0, 70));
    IFontRenderer fontRenderer =
        Sorus.getSorus().getGUIManager().getRenderer().getRubikFontRenderer();
    menu.add(
        new Text()
            .fontRenderer(fontRenderer)
            .text("SORUS")
            .position(350 - fontRenderer.getStringWidth("SORUS") / 2 * 5.5, 17.5)
            .scale(5.5, 5.5)
            .color(DefaultTheme.getForegroundLayerColor()));
    menu.add(new Add().position(320, 705));
    Scissor scissor = new Scissor().size(680, 690).position(10, 85);
    this.scroll = new Scroll();
    scroll.position(0, 2);
    scissor.add(scroll);
    menu.add(scissor);
    this.updateThemes();
  }

  private void updateThemes() {
    scroll.clear();
    themeCount = 0;
    for (Theme theme : this.themeManager.getRegisteredThemes()) {
      scroll.add(new SelectComponent(theme).position(0, themeCount * 135));
      themeCount++;
    }
  }

  @Override
  public void render() {
    main.scale(
        Sorus.getSorus().getVersion().getScreen().getScaledWidth() / 1920,
        Sorus.getSorus().getVersion().getScreen().getScaledHeight() / 1080);
    main.onRender(this.screen);
  }

  @Override
  public void exit() {
    Sorus.getSorus().getVersion().getRenderer().disableBlur();
    Sorus.getSorus().getSettingsManager().save();
    main.onRemove();
  }

  @Override
  public void keyTyped(Key key, boolean repeat) {
    if (key == Key.ESCAPE) {
      Sorus.getSorus().getGUIManager().close(this.screen);
    }
  }

  public class Add extends Collection {

    private final Collection main;
    private double expandedPercent;

    private long prevRenderTime;

    public Add() {
      Sorus.getSorus().getEventManager().register(this);
      this.add(main = new Collection());
      main.add(new Rectangle().smooth(4).size(60, 60).color(new Color(35, 160, 65)));
      main.add(new Collection().add(new Rectangle().smooth(3).size(10, 40).position(25, 10)))
          .add(new Rectangle().smooth(3).size(40, 10).position(10, 25));
    }

    @Override
    public void onRender() {
      long renderTime = System.currentTimeMillis();
      long deltaTime = System.currentTimeMillis() - prevRenderTime;
      double mouseX = Sorus.getSorus().getVersion().getInput().getMouseX();
      double mouseY = Sorus.getSorus().getVersion().getInput().getMouseY();
      boolean hovered = this.isHovered(mouseX, mouseY);
      expandedPercent =
          Math.min(Math.max(0, expandedPercent + (hovered ? 1 : -1) * deltaTime / 100.0), 1);
      main.scale(1 + 0.1 * expandedPercent, 1 + 0.1 * expandedPercent)
          .position(-2.5 * expandedPercent, -2.5 * expandedPercent);
      prevRenderTime = renderTime;
      super.onRender();
    }

    @EventInvoked
    public void onClick(MousePressEvent e) {
      if (this.isHovered(e.getX(), e.getY())) {
        Sorus.getSorus().getGUIManager().close((Screen) this.getContainer());
        receiver.call(DefaultSelectThemeScreen.this.selected.theme);
      }
    }

    private boolean isHovered(double mouseX, double mouseY) {
      double x = this.absoluteX() - 80 * expandedPercent * this.absoluteXScale();
      double y = this.absoluteY();
      double width = (60 + 160 * expandedPercent) * this.absoluteXScale();
      double height = 60 * this.absoluteYScale();
      return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }

    @Override
    public void onRemove() {
      Sorus.getSorus().getEventManager().unregister(this);
      super.onRemove();
    }
  }

  public class SelectComponent extends Collection {

    private final Theme theme;

    private final HollowRectangle hollowRectangle;

    public SelectComponent(Theme theme) {
      this.theme = theme;
      IFontRenderer fontRenderer =
          Sorus.getSorus().getGUIManager().getRenderer().getGidoleFontRenderer();
      this.add(
          new Rectangle()
              .size(670, 125)
              .position(5, 4)
              .color(DefaultTheme.getMedgroundLayerColor()));
      this.add(
          new Rectangle()
              .gradient(
                  DefaultTheme.getShadowStartColor(),
                  DefaultTheme.getShadowStartColor(),
                  DefaultTheme.getShadowEndColor(),
                  DefaultTheme.getShadowEndColor())
              .size(670, 4)
              .position(5, 0));
      this.add(
          new Rectangle()
              .gradient(
                  DefaultTheme.getShadowStartColor(),
                  DefaultTheme.getShadowEndColor(),
                  DefaultTheme.getShadowEndColor(),
                  DefaultTheme.getShadowEndColor())
              .size(4, 4)
              .position(675, 0));
      this.add(
          new Rectangle()
              .gradient(
                  DefaultTheme.getShadowStartColor(),
                  DefaultTheme.getShadowEndColor(),
                  DefaultTheme.getShadowEndColor(),
                  DefaultTheme.getShadowStartColor())
              .size(4, 125)
              .position(675, 4));
      this.add(
          new Rectangle()
              .gradient(
                  DefaultTheme.getShadowEndColor(),
                  DefaultTheme.getShadowEndColor(),
                  DefaultTheme.getShadowEndColor(),
                  DefaultTheme.getShadowStartColor())
              .size(4, 4)
              .position(675, 129));
      this.add(
          new Rectangle()
              .gradient(
                  DefaultTheme.getShadowEndColor(),
                  DefaultTheme.getShadowEndColor(),
                  DefaultTheme.getShadowStartColor(),
                  DefaultTheme.getShadowStartColor())
              .size(670, 4)
              .position(5, 129));
      this.add(
          new Rectangle()
              .gradient(
                  DefaultTheme.getShadowEndColor(),
                  DefaultTheme.getShadowEndColor(),
                  DefaultTheme.getShadowStartColor(),
                  DefaultTheme.getShadowEndColor())
              .size(4, 4)
              .position(2, 129));
      this.add(
          new Rectangle()
              .gradient(
                  DefaultTheme.getShadowEndColor(),
                  DefaultTheme.getShadowStartColor(),
                  DefaultTheme.getShadowStartColor(),
                  DefaultTheme.getShadowEndColor())
              .size(4, 125)
              .position(2, 4));
      this.add(
          new Rectangle()
              .gradient(
                  DefaultTheme.getShadowEndColor(),
                  DefaultTheme.getShadowStartColor(),
                  DefaultTheme.getShadowEndColor(),
                  DefaultTheme.getShadowEndColor())
              .size(4, 4)
              .position(1, 0));
      this.add(
          hollowRectangle =
              new HollowRectangle()
                  .thickness(2)
                  .size(670, 125)
                  .position(5, 4)
                  .color(new Color(180, 180, 180, 0)));
      this.add(
          new Text()
              .fontRenderer(fontRenderer)
              .text(theme.getName())
              .position(125, 20)
              .scale(4, 4)
              .color(new Color(235, 235, 235, 210)));
      Sorus.getSorus().getEventManager().register(this);
    }

    @Override
    public void onRemove() {
      Sorus.getSorus().getEventManager().unregister(this);
      super.onRemove();
    }

    @EventInvoked
    public void onClick(MousePressEvent e) {
      if (e.getX() > this.absoluteX()
          && e.getX() < this.absoluteX() + 680 * this.absoluteXScale()
          && e.getY() > this.absoluteY()
          && e.getY() < this.absoluteY() + 125 * this.absoluteYScale()) {
        this.select();
      }
    }

    public void select() {
      if (DefaultSelectThemeScreen.this.selected != null) {
        DefaultSelectThemeScreen.this.selected.deselect();
      }
      DefaultSelectThemeScreen.this.selected = this;
      this.hollowRectangle.color(new Color(180, 180, 180));
    }

    public void deselect() {
      this.hollowRectangle.color(new Color(180, 180, 180, 0));
    }
  }
}
