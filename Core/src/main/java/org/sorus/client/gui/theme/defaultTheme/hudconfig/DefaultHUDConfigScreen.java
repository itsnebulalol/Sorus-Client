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

package org.sorus.client.gui.theme.defaultTheme.hudconfig;

import java.awt.*;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.Panel;
import org.sorus.client.gui.core.component.impl.*;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.font.IFontRenderer;
import org.sorus.client.gui.hud.*;
import org.sorus.client.gui.hud.Component;
import org.sorus.client.gui.screen.Callback;
import org.sorus.client.gui.screen.SelectComponentScreen;
import org.sorus.client.gui.screen.hudlist.HUDListScreen;
import org.sorus.client.gui.theme.ExitButton;
import org.sorus.client.gui.theme.ThemeBase;
import org.sorus.client.gui.theme.defaultTheme.DefaultTheme;
import org.sorus.client.version.input.Key;

public class DefaultHUDConfigScreen extends ThemeBase<HUDConfigScreen> {

  private final HUD hud;

  private Panel main;
  private Scroll scroll;

  public DefaultHUDConfigScreen(HUD hud) {
    this.hud = hud;
  }

  @Override
  public void init() {
    Sorus.getSorus().getVersion().getRenderer().enableBlur(7.5);
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
                  Sorus.getSorus().getGUIManager().open(new HUDListScreen());
                })
            .position(10, 10));
    menu.add(
        new Text()
            .fontRenderer(fontRenderer)
            .text(hud.getName())
            .position(350 - fontRenderer.getStringWidth(hud.getName()) / 2 * 4.5, 95)
            .scale(4.5, 4.5)
            .color(DefaultTheme.getForegroundLayerColor()));
    menu.add(new DefaultHUDConfigScreen.Add().position(320, 705));
    Scissor scissor = new Scissor().size(680, 600).position(10, 150);
    this.scroll = new Scroll();
    scroll.position(0, 2);
    scissor.add(scroll);
    menu.add(scissor);
    this.updateComponents();
  }

  public void updateComponents() {
    scroll.clear();
    int componentCount = 0;
    for (IComponent component : this.hud.getComponents()) {
      scroll.add(
          new ComponentComponent(this, (Component) component).position(0, 135 * componentCount));
      componentCount++;
    }
  }

  @Override
  public void render() {
    main.scale(
        Sorus.getSorus().getVersion().getScreen().getScaledWidth() / 1920,
        Sorus.getSorus().getVersion().getScreen().getScaledHeight() / 1080);
    main.onRender();
  }

  @Override
  public void exit() {
    Sorus.getSorus().getSettingsManager().save();
    Sorus.getSorus().getVersion().getRenderer().disableBlur();
    main.onRemove();
  }

  @Override
  public void keyTyped(Key key, boolean repeat) {
    if (key == Key.ESCAPE && this.screen.isInteractContainer()) {
      Sorus.getSorus().getGUIManager().close(this.screen);
    }
  }

  public HUD getHUD() {
    return hud;
  }

  public class Add extends Collection {

    private final Collection main;
    private double expandedPercent;

    private long prevRenderTime;

    public Add() {
      Sorus.getSorus().getEventManager().register(this);
      this.add(main = new Collection());
      main.add(new Rectangle().smooth(4).size(60, 60).color(new Color(35, 160, 65)));
      Collection centerCross = new Collection();
      centerCross.add(new Rectangle().smooth(3).size(10, 40).position(25, 10));
      centerCross.add(new Rectangle().smooth(3).size(40, 10).position(10, 25));
      main.add(centerCross);
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
        Sorus.getSorus().getGUIManager().close(DefaultHUDConfigScreen.this.screen);
        Sorus.getSorus()
            .getGUIManager()
            .open(
                new SelectComponentScreen(Sorus.getSorus().getHUDManager(), new OnAddComponent()));
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

    public class OnAddComponent implements Callback<IComponent> {

      @Override
      public void call(IComponent selected) {
        DefaultHUDConfigScreen.this.hud.addComponent(selected);
        DefaultHUDConfigScreen.this.hud.displaySettings(
            new HUDConfigScreen(DefaultHUDConfigScreen.this.hud));
      }

      @Override
      public void cancel() {}
    }
  }
}
