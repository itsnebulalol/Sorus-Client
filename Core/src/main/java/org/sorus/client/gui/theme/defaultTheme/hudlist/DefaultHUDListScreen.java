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

package org.sorus.client.gui.theme.defaultTheme.hudlist;

import java.awt.*;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.Panel;
import org.sorus.client.gui.core.component.impl.*;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.font.IFontRenderer;
import org.sorus.client.gui.hud.HUD;
import org.sorus.client.gui.hud.HUDManager;
import org.sorus.client.gui.hud.SingleHUD;
import org.sorus.client.gui.screen.MenuScreen;
import org.sorus.client.gui.screen.hudlist.HUDListScreen;
import org.sorus.client.gui.theme.ExitButton;
import org.sorus.client.gui.theme.ThemeBase;
import org.sorus.client.gui.theme.defaultTheme.DefaultTheme;
import org.sorus.client.util.MathUtil;
import org.sorus.client.version.IScreen;
import org.sorus.client.version.game.IGame;
import org.sorus.client.version.input.IInput;
import org.sorus.client.version.input.Key;
import org.sorus.client.version.render.IRenderer;

public class DefaultHUDListScreen extends ThemeBase<HUDListScreen> {

  private final HUDManager hudManager;

  private Panel main;
  private Scroll scroll;

  private int hudCount;

  private double targetScroll;

  public DefaultHUDListScreen(HUDManager hudManager) {
    this.hudManager = hudManager;
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
    menu.add(new Add().position(240, 702.5));
    Scissor scissor = new Scissor().size(700, 600).position(3, 74);
    this.scroll = new Scroll();
    scissor.add(scroll);
    menu.add(scissor);
    this.updateHUDS();
  }

  public void updateHUDS() {
    scroll.clear();
    hudCount = 0;
    for (HUD hud : this.hudManager.getHUDs()) {
      scroll.add(new HUDListComponent(this, hud).position(0, hudCount * 110));
      hudCount++;
    }
  }

  @Override
  public void render() {
    final int FPS = Math.max(Sorus.getSorus().getVersion().getData(IGame.class).getFPS(), 1);
    double scrollValue = Sorus.getSorus().getVersion().getData(IInput.class).getScroll();
    targetScroll = targetScroll + scrollValue * 0.7;
    scroll.setScroll((targetScroll - scroll.getScroll()) * 7 / FPS + scroll.getScroll());
    double maxScroll = hudCount * 100 + (hudCount - 1) * 10 - 590;
    scroll.addMinMaxScroll(-maxScroll, 0);
    targetScroll = MathUtil.clamp(targetScroll, scroll.getMinScroll(), scroll.getMaxScroll());
    main.scale(
        Sorus.getSorus().getVersion().getData(IScreen.class).getScaledWidth() / 1920,
        Sorus.getSorus().getVersion().getData(IScreen.class).getScaledHeight() / 1080);
    main.onRender();
  }

  @Override
  public void exit() {
    Sorus.getSorus().getVersion().getData(IRenderer.class).disableBlur();
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

    private final Collection collection;
    private final Scissor scissor;
    private double expandedPercent;

    private long prevRenderTime;

    public Add() {
      Sorus.getSorus().getEventManager().register(this);
      this.add(new Rectangle().smooth(4).size(220, 60).color(new Color(35, 160, 65)));
      this.scissor = new Scissor();
      this.add(scissor);
      this.add(collection = new Collection());
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
      scissor
          .size(220, 60)
          .color(new Color(235, 235, 235, 255));
      prevRenderTime = renderTime;
      if (hovered) {
        this.collection.clear();
        Rectangle selectedSide =
            new Rectangle().size(110, 60).color(new Color(0, 0, 0, 30));
        this.collection.add(selectedSide);
        if (mouseX > this.absoluteX() + 110 * this.absoluteXScale()) {
          selectedSide.position(110, 0);
        } else {
          selectedSide.position(0, 0);
        }
      }
      scissor.clear();
      scissor.add(new HollowArc().thickness(2).radius(10, 10).angle(0, 360).position(45, 10));
      scissor.add(
          new Text()
              .fontRenderer(Sorus.getSorus().getGUIManager().getRenderer().getRubikFontRenderer())
              .text("SINGLE")
              .scale(2, 2)
              .position(30, 40));
      scissor.add(
          new HollowArc().thickness(2).radius(10, 10).angle(0, 360).position(130, 10));//22.375
      scissor.add(
          new HollowArc().thickness(2).radius(10, 10).angle(0, 360).position(152.375, 10));
      scissor.add(
          new HollowArc().thickness(2).radius(10, 10).angle(0, 360).position(174.75, 10));
      scissor.add(
          new Text()
              .fontRenderer(Sorus.getSorus().getGUIManager().getRenderer().getRubikFontRenderer())
              .text("MULTI")
              .scale(2, 2)
              .position(137.5, 40));
      super.onRender();
    }

    @EventInvoked
    public void onClick(MousePressEvent e) {
      if (this.isHovered(e.getX(), e.getY())) {
        HUD hud;
        if (e.getX() > this.absoluteX() + 110 * this.absoluteXScale()) {
          hud = new HUD();
        } else {
          hud = new SingleHUD();
        }
        Sorus.getSorus().getGUIManager().close(DefaultHUDListScreen.this.screen);
        hud.onCreation();
      }
    }

    private boolean isHovered(double mouseX, double mouseY) {
      double x = this.absoluteX();
      double y = this.absoluteY();
      double width = 220 * this.absoluteXScale();
      double height = 60 * this.absoluteYScale();
      return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }

    @Override
    public void onRemove() {
      Sorus.getSorus().getEventManager().unregister(this);
      super.onRemove();
    }
  }
}
