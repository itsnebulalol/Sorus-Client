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
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.Panel;
import org.sorus.client.gui.core.component.impl.*;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.font.IFontRenderer;
import org.sorus.client.gui.screen.Callback;
import org.sorus.client.gui.screen.MenuScreen;
import org.sorus.client.gui.screen.theme.SelectThemeScreen;
import org.sorus.client.gui.screen.theme.ThemeListScreen;
import org.sorus.client.gui.theme.ExitButton;
import org.sorus.client.gui.theme.Theme;
import org.sorus.client.gui.theme.ThemeBase;
import org.sorus.client.gui.theme.ThemeManager;
import org.sorus.client.gui.theme.defaultTheme.DefaultTheme;
import org.sorus.client.util.MathUtil;
import org.sorus.client.version.IScreen;
import org.sorus.client.version.input.IInput;
import org.sorus.client.version.input.Key;
import org.sorus.client.version.render.IRenderer;

public class DefaultThemeListScreen extends ThemeBase<ThemeListScreen> {

  private final ThemeManager themeManager;

  private Panel main;
  private Scroll scroll;

  private int themeCount;

  private double targetScroll;

  private ThemeListComponent draggedComponent;
  private double initialMouseX, initialMouseY;
  private double initialX, initialY;

  public DefaultThemeListScreen(ThemeManager themeManager) {
    this.themeManager = themeManager;
  }

  @Override
  public void init() {
    Sorus.getSorus().getVersion().getData(IRenderer.class).enableBlur(7.5);
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
    menu.add(
        new ExitButton(
                () -> {
                  Sorus.getSorus().getGUIManager().close(this.screen);
                  Sorus.getSorus().getGUIManager().open(new MenuScreen(false));
                })
            .position(10, 10));
    menu.add(new Add().position(320, 705));
    Scissor scissor = new Scissor().size(680, 690).position(10, 85);
    this.scroll = new Scroll();
    scroll.position(0, 2);
    scissor.add(scroll);
    menu.add(scissor);
    this.updateThemes();
  }

  public void updateThemes() {
    scroll.clear();
    themeCount = 0;
    double yRatio = Sorus.getSorus().getVersion().getData(IScreen.class).getScaledHeight() / 1080;
    for (Theme theme : this.themeManager.getCurrentThemes()) {
      boolean added = false;
      while (!added) {
        boolean add = true;
        if (draggedComponent != null) {
          double y = MathUtil.clamp(draggedComponent.absoluteY(), 225 * yRatio, 1000 * yRatio);
          if (Math.abs(y - ((themeCount * 135) + 225) * yRatio) < 62.5 * yRatio) {
            add = false;
          }
        }
        if (add) {
          scroll.add(new ThemeListComponent(this, theme).position(0, themeCount * 135));
          added = true;
        }
        themeCount++;
      }
    }
  }

  @Override
  public void render() {
    double xRatio = Sorus.getSorus().getVersion().getData(IScreen.class).getScaledWidth() / 1920;
    double yRatio = Sorus.getSorus().getVersion().getData(IScreen.class).getScaledHeight() / 1080;
    if (draggedComponent != null) {
      double mouseX = Sorus.getSorus().getVersion().getData(IInput.class).getMouseX();
      double mouseY = Sorus.getSorus().getVersion().getData(IInput.class).getMouseY();
      draggedComponent.position(
          (mouseX - initialMouseX + initialX) * 1 / xRatio,
          (mouseY - initialMouseY + initialY) * 1 / yRatio);
      this.updateThemes();
    }
    main.scale(xRatio, yRatio);
    main.onRender();
  }

  @Override
  public void exit() {
    Sorus.getSorus().getVersion().getData(IRenderer.class).disableBlur();
    Sorus.getSorus().getSettingsManager().save();
    main.onRemove();
    super.exit();
  }

  @Override
  public void keyTyped(Key key, boolean repeat) {
    if (key == Key.ESCAPE) {
      Sorus.getSorus().getGUIManager().close(this.screen);
    }
  }

  public void onComponentDrag(ThemeListComponent draggedComponent, double mouseX, double mouseY) {
    this.draggedComponent = new ThemeListComponent(this, draggedComponent.getTheme());
    this.initialMouseX = mouseX;
    this.initialMouseY = mouseY;
    this.initialX = draggedComponent.absoluteX();
    this.initialY = draggedComponent.absoluteY();
    themeManager.remove(draggedComponent.getTheme());
    main.add(this.draggedComponent);
    this.updateThemes();
  }

  public void onComponentRelease(ThemeListComponent component) {
    double yRatio = Sorus.getSorus().getVersion().getData(IScreen.class).getScaledHeight() / 1080;
    int size = themeManager.getCurrentThemes().size();
    int index = 0;
    boolean added = false;
    while (!added) {
      if (draggedComponent != null) {
        double y =
            MathUtil.clamp(draggedComponent.absoluteY(), 225 * yRatio, (225 + size * 135) * yRatio);
        if (Math.abs(y - ((index * 135) + 225) * yRatio) < 62.5 * yRatio) {
          themeManager.getCurrentThemes().add(index, component.getTheme());
          added = true;
        }
      }
      index++;
    }
    main.remove(draggedComponent);
    draggedComponent = null;
    this.updateThemes();
  }

  public ThemeListComponent getDraggedComponent() {
    return draggedComponent;
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
      double mouseX = Sorus.getSorus().getVersion().getData(IInput.class).getMouseX();
      double mouseY = Sorus.getSorus().getVersion().getData(IInput.class).getMouseY();
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
        Sorus.getSorus().getGUIManager().close(DefaultThemeListScreen.this.screen);
        Sorus.getSorus().getGUIManager().open(new SelectThemeScreen(new ThemeReceiver()));
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

    public class ThemeReceiver implements Callback<Theme> {

      @Override
      public void call(Theme selected) {
        themeManager.add(selected);
        Sorus.getSorus().getGUIManager().open(DefaultThemeListScreen.this.screen);
        DefaultThemeListScreen.this.updateThemes();
      }

      @Override
      public void cancel() {
        Sorus.getSorus().getGUIManager().open(new ThemeListScreen());
      }
    }
  }
}
