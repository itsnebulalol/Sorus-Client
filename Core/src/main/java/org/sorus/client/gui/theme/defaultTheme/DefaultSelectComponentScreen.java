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

package org.sorus.client.gui.theme.defaultTheme;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.gui.core.Screen;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.Panel;
import org.sorus.client.gui.core.component.impl.*;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.font.IFontRenderer;
import org.sorus.client.gui.hud.Component;
import org.sorus.client.gui.hud.HUDManager;
import org.sorus.client.gui.hud.IComponent;
import org.sorus.client.gui.screen.Callback;
import org.sorus.client.gui.screen.SelectComponentScreen;
import org.sorus.client.gui.theme.ThemeBase;
import org.sorus.client.util.MathUtil;
import org.sorus.client.version.IScreen;
import org.sorus.client.version.game.IGame;
import org.sorus.client.version.input.IInput;
import org.sorus.client.version.input.Key;
import org.sorus.client.version.render.IRenderer;

public class DefaultSelectComponentScreen extends ThemeBase<SelectComponentScreen> {

  private final HUDManager hudManager;
  private final Callback<IComponent> receiver;

  private Panel main;
  private Scroll scroll;

  private int componentCount;

  private double targetScroll;

  private SelectComponent selected;

  public DefaultSelectComponentScreen(HUDManager hudManager, Callback<IComponent> receiver) {
    this.hudManager = hudManager;
    this.receiver = receiver;
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
    menu.add(new Add().position(320, 705));
    Scissor scissor = new Scissor().size(700, 600).position(3, 74);
    this.scroll = new Scroll();
    scissor.add(scroll);
    menu.add(scissor);
    componentCount = 0;
    for (Class<? extends Component> componentClass : this.hudManager.getRegisteredComponents()) {
      try {
        Component component = componentClass.newInstance();
        SelectComponent selectComponent =
            new SelectComponent(component).position(0, componentCount * 107);
        scroll.add(selectComponent);
        componentCount++;
      } catch (InstantiationException | IllegalAccessException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void render() {
    final int FPS = Math.max(Sorus.getSorus().getVersion().getData(IGame.class).getFPS(), 1);
    double scrollValue = Sorus.getSorus().getVersion().getData(IInput.class).getScroll();
    targetScroll = targetScroll + scrollValue * 0.7;
    scroll.setScroll((targetScroll - scroll.getScroll()) * 7 / FPS + scroll.getScroll());
    double maxScroll = componentCount * 100 + (componentCount - 1) * 7 - 592.5;
    scroll.addMinMaxScroll(-maxScroll, 0);
    targetScroll = MathUtil.clamp(targetScroll, scroll.getMinScroll(), scroll.getMaxScroll());
    main.scale(
        Sorus.getSorus().getVersion().getData(IScreen.class).getScaledWidth() / 1920,
        Sorus.getSorus().getVersion().getData(IScreen.class).getScaledHeight() / 1080);
    main.onRender(this.screen);
  }

  @Override
  public void exit() {
    Sorus.getSorus().getVersion().getData(IRenderer.class).disableBlur();
    main.onRemove();
    Sorus.getSorus().getSettingsManager().save();
    super.exit();
  }

  @Override
  public void keyTyped(Key key, boolean repeat) {
    if (key == Key.ESCAPE) {
      receiver.cancel();
      Sorus.getSorus().getGUIManager().close(this.screen);
    }
  }

  public class SelectComponent extends Collection {

    private final Component component;

    private final HollowRectangle hollowRectangle;

    public SelectComponent(Component component) {
      this.component = component;
      IFontRenderer fontRenderer =
          Sorus.getSorus().getGUIManager().getRenderer().getRubikFontRenderer();
      final double WIDTH = 685;
      final double HEIGHT = 100;
      this.add(
              new Rectangle()
                      .size(WIDTH, HEIGHT)
                      .position(4, 4)
                      .color(DefaultTheme.getMedbackgroundLayerColor()));
      this.add(
              new Rectangle()
                      .gradient(
                              DefaultTheme.getShadowStartColor(),
                              DefaultTheme.getShadowStartColor(),
                              DefaultTheme.getShadowEndColor(),
                              DefaultTheme.getShadowEndColor())
                      .size(WIDTH, 4)
                      .position(4, 0));
      this.add(
              new Rectangle()
                      .gradient(
                              DefaultTheme.getShadowStartColor(),
                              DefaultTheme.getShadowEndColor(),
                              DefaultTheme.getShadowEndColor(),
                              DefaultTheme.getShadowEndColor())
                      .size(4, 4)
                      .position(WIDTH + 4, 0));
      this.add(
              new Rectangle()
                      .gradient(
                              DefaultTheme.getShadowStartColor(),
                              DefaultTheme.getShadowEndColor(),
                              DefaultTheme.getShadowEndColor(),
                              DefaultTheme.getShadowStartColor())
                      .size(4, HEIGHT)
                      .position(WIDTH + 4, 4));
      this.add(
              new Rectangle()
                      .gradient(
                              DefaultTheme.getShadowEndColor(),
                              DefaultTheme.getShadowEndColor(),
                              DefaultTheme.getShadowEndColor(),
                              DefaultTheme.getShadowStartColor())
                      .size(4, 4)
                      .position(WIDTH + 4, HEIGHT + 4));
      this.add(
              new Rectangle()
                      .gradient(
                              DefaultTheme.getShadowEndColor(),
                              DefaultTheme.getShadowEndColor(),
                              DefaultTheme.getShadowStartColor(),
                              DefaultTheme.getShadowStartColor())
                      .size(WIDTH, 4)
                      .position(4, HEIGHT + 4));
      this.add(
              new Rectangle()
                      .gradient(
                              DefaultTheme.getShadowEndColor(),
                              DefaultTheme.getShadowEndColor(),
                              DefaultTheme.getShadowStartColor(),
                              DefaultTheme.getShadowEndColor())
                      .size(4, 4)
                      .position(0, HEIGHT + 4));
      this.add(
              new Rectangle()
                      .gradient(
                              DefaultTheme.getShadowEndColor(),
                              DefaultTheme.getShadowStartColor(),
                              DefaultTheme.getShadowStartColor(),
                              DefaultTheme.getShadowEndColor())
                      .size(4, HEIGHT)
                      .position(0, 4));
      this.add(
              new Rectangle()
                      .gradient(
                              DefaultTheme.getShadowEndColor(),
                              DefaultTheme.getShadowStartColor(),
                              DefaultTheme.getShadowEndColor(),
                              DefaultTheme.getShadowEndColor())
                      .size(4, 4));
      Collection collection = new Collection().position(15, 15);
      this.add(collection);
      this.component.addIconElements(collection);
      this.add(
          hollowRectangle =
              new HollowRectangle()
                  .thickness(2)
                  .size(WIDTH + 8, HEIGHT + 8)
                  .position(5, 4)
                  .color(new Color(180, 180, 180, 0)));
      this.add(
          new Text()
              .fontRenderer(fontRenderer)
              .text(component.getName())
              .position(125, 20)
              .scale(4, 4)
              .color(DefaultTheme.getForegroundLayerColor()));
      int i = 0;
      for (String string :
          this.getSplitDescription(
              component.getDescription(),
              Sorus.getSorus().getGUIManager().getRenderer().getRubikFontRenderer(),
              150)) {
        this.add(
            new Text()
                .fontRenderer(Sorus.getSorus().getGUIManager().getRenderer().getRubikFontRenderer())
                .text(string)
                .position(125, 65 + i * 23)
                .scale(2, 2)
                .color(DefaultTheme.getForegroundLayerColor()));
        i++;
      }
      Sorus.getSorus().getEventManager().register(this);
    }

    public List<String> getSplitDescription(
        String description, IFontRenderer fontRenderer, double width) {
      List<String> strings = new ArrayList<>();
      StringBuilder stringBuilder = new StringBuilder();
      for (char c : description.toCharArray()) {
        stringBuilder.append(c);
        if (fontRenderer.getStringWidth(stringBuilder.toString()) > width) {
          String string = stringBuilder.toString();
          int index = string.lastIndexOf(" ");
          strings.add(string.substring(0, index));
          stringBuilder = new StringBuilder(string.substring(index + 1));
        }
      }
      strings.add(stringBuilder.toString());
      return strings;
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
      if (DefaultSelectComponentScreen.this.selected != null) {
        DefaultSelectComponentScreen.this.selected.deselect();
      }
      DefaultSelectComponentScreen.this.selected = this;
      this.hollowRectangle.color(new Color(180, 180, 180));
    }

    public void deselect() {
      this.hollowRectangle.color(new Color(180, 180, 180, 0));
    }
  }

  public class Add extends Collection {

    private final Collection main;
    private final Collection centerCross;
    private double expandedPercent;

    private long prevRenderTime;

    public Add() {
      Sorus.getSorus().getEventManager().register(this);
      this.add(main = new Collection());
      main.add(new Rectangle().smooth(4).size(60, 60).color(new Color(35, 160, 65)));
      this.centerCross = new Collection();
      this.centerCross.add(new Rectangle().smooth(3).size(10, 40).position(25, 10));
      this.centerCross.add(new Rectangle().smooth(3).size(40, 10).position(10, 25));
      main.add(centerCross);
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
        Sorus.getSorus().getGUIManager().close((Screen) this.getContainer());
        receiver.call(DefaultSelectComponentScreen.this.selected.component);
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
