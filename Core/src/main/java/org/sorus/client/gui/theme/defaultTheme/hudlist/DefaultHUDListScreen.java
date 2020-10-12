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
import org.sorus.client.gui.theme.base.HUDListScreenTheme;
import org.sorus.client.util.MathUtil;
import org.sorus.client.version.input.Key;

public class DefaultHUDListScreen extends HUDListScreenTheme {

  private Panel main;
  private Scroll scroll;

  private int hudCount;

  private double targetScroll;

  public DefaultHUDListScreen(HUDManager hudManager) {
    super(hudManager);
  }

  @Override
  public void init() {
    Sorus.getSorus().getVersion().getRenderer().enableBlur();
    main = new Panel();
    Collection menu = new Collection().position(610, 140);
    main.add(menu);
    menu.add(new Rectangle().smooth(5).size(700, 720).position(0, 70).color(new Color(18, 18, 18)));
    menu.add(new Rectangle().size(700, 65).position(0, 5).color(new Color(30, 30, 30)));
    menu.add(new Arc().radius(5, 5).angle(180, 270).position(0, 0).color(new Color(30, 30, 30)));
    menu.add(new Arc().radius(5, 5).angle(90, 180).position(690, 0).color(new Color(30, 30, 30)));
    menu.add(new Rectangle().size(690, 5).position(5, 0).color(new Color(30, 30, 30)));
    menu.add(
        new Rectangle()
            .gradient(
                new Color(14, 14, 14, 0),
                new Color(14, 14, 14, 0),
                new Color(14, 14, 14),
                new Color(14, 14, 14))
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
            .color(new Color(215, 215, 215)));
    menu.add(new Add().position(320, 705));
    Scissor scissor = new Scissor().size(680, 600).position(10, 85);
    this.scroll = new Scroll();
    scroll.position(0, 2);
    scissor.add(scroll);
    menu.add(scissor);
    this.updateHUDS();
  }

  public void updateHUDS() {
    scroll.clear();
    hudCount = 0;
    for (HUD hud : this.hudManager.getHUDs()) {
      scroll.add(new HUDComponent(this, hud).position(0, 135 * hudCount));
      hudCount++;
    }
  }

  @Override
  public void render() {
    final int FPS = Math.max(Sorus.getSorus().getVersion().getGame().getFPS(), 1);
    double scrollValue = Sorus.getSorus().getVersion().getInput().getScroll();
    targetScroll = targetScroll + scrollValue * 0.7;
    scroll.setScroll((targetScroll - scroll.getScroll()) * 7 / FPS + scroll.getScroll());
    double maxScroll = hudCount * 135 - 690;
    scroll.addMinMaxScroll(-maxScroll, 0);
    targetScroll = MathUtil.clamp(targetScroll, scroll.getMinScroll(), scroll.getMaxScroll());
    main.scale(
        Sorus.getSorus().getVersion().getScreen().getScaledWidth() / 1920,
        Sorus.getSorus().getVersion().getScreen().getScaledHeight() / 1080);
    main.onRender();
  }

  @Override
  public void exit() {
    Sorus.getSorus().getVersion().getRenderer().disableBlur();
    Sorus.getSorus().getSettingsManager().save();
    main.onRemove();
  }

  @Override
  public void keyTyped(Key key) {
    if (key == Key.ESCAPE) {
      Sorus.getSorus().getGUIManager().close(this.screen);
    }
  }

  public class Add extends Collection {

    private final Collection collection;
    private final Rectangle rectangle;
    private final Scissor scissor;
    private final Collection centerCross;
    private double expandedPercent;

    private long prevRenderTime;

    public Add() {
      Sorus.getSorus().getEventManager().register(this);
      this.add(rectangle = new Rectangle().smooth(4).color(new Color(35, 160, 65)));
      collection = new Collection();
      this.scissor = new Scissor();
      this.add(scissor);
      this.centerCross = new Collection();
      this.centerCross.add(new Rectangle().smooth(3).size(10, 40).position(25, 10));
      this.centerCross.add(new Rectangle().smooth(3).size(40, 10).position(10, 25));
      this.add(centerCross);
    }

    @Override
    public void onRender() {
      long renderTime = System.currentTimeMillis();
      long deltaTime = System.currentTimeMillis() - prevRenderTime;
      double mouseX = Sorus.getSorus().getVersion().getInput().getMouseX();
      double mouseY = Sorus.getSorus().getVersion().getInput().getMouseY();
      boolean hovered = this.isHovered(mouseX, mouseY);
      double x = -80 * expandedPercent;
      expandedPercent =
          Math.min(Math.max(0, expandedPercent + (hovered ? 1 : -1) * deltaTime / 100.0), 1);
      rectangle.size(60 + 160 * expandedPercent, 60).position(x, 0);
      scissor
          .size(60 + 160 * expandedPercent, 60)
          .position(x, 0)
          .color(new Color(235, 235, 235, (int) (255 * expandedPercent)));
      centerCross.color(new Color(235, 235, 235, (int) (Math.max(0, 255 - 400 * expandedPercent))));
      prevRenderTime = renderTime;
      this.remove(collection);
      if (hovered) {
        this.collection.clear();
        Rectangle selectedSide =
            new Rectangle().size(30 + 80 * expandedPercent, 60).color(new Color(0, 0, 0, 30));
        this.collection.add(selectedSide);
        if (mouseX > this.absoluteX() + 30 * this.absoluteXScale()) {
          selectedSide.position(30, 0);
        } else {
          selectedSide.position(-80 * expandedPercent, 0);
        }
        this.add(collection);
      }
      scissor.clear();
      scissor.add(new HollowArc().thickness(2).radius(10, 10).angle(0, 360).position(-x - 35, 10));
      scissor.add(
          new Text()
              .fontRenderer(Sorus.getSorus().getGUIManager().getRenderer().getRubikFontRenderer())
              .text("SINGLE")
              .scale(2, 2)
              .position(-x - 50, 40));
      scissor.add(
          new HollowArc().thickness(2).radius(10, 10).angle(0, 360).position(-x + 51.25, 10));
      scissor.add(
          new HollowArc().thickness(2).radius(10, 10).angle(0, 360).position(-x + 73.625, 10));
      scissor.add(
          new HollowArc().thickness(2).radius(10, 10).angle(0, 360).position(-x + 96.625, 10));
      scissor.add(
          new Text()
              .fontRenderer(Sorus.getSorus().getGUIManager().getRenderer().getRubikFontRenderer())
              .text("MULTI")
              .scale(2, 2)
              .position(-x + 64, 40));
      super.onRender();
    }

    @EventInvoked
    public void onClick(MousePressEvent e) {
      if (this.isHovered(e.getX(), e.getY())) {
        HUD hud;
        if (e.getX() > this.absoluteX() + 30 * this.absoluteXScale()) {
          hud = new HUD();
        } else {
          hud = new SingleHUD();
        }
        Sorus.getSorus().getHUDManager().register(hud);
        Sorus.getSorus().getGUIManager().close(DefaultHUDListScreen.this.screen);
        hud.onCreation();
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
}
