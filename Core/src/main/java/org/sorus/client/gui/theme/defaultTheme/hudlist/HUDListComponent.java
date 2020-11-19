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
import java.util.ArrayList;
import java.util.List;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.HollowArc;
import org.sorus.client.gui.core.component.impl.Image;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.component.impl.Text;
import org.sorus.client.gui.core.font.IFontRenderer;
import org.sorus.client.gui.hud.HUD;
import org.sorus.client.gui.hud.SingleHUD;
import org.sorus.client.gui.theme.defaultTheme.DefaultTheme;
import org.sorus.client.gui.theme.defaultTheme.modulelist.ModuleListComponent;
import org.sorus.client.util.Axis;
import org.sorus.client.version.IGLHelper;
import org.sorus.client.version.input.IInput;

public class HUDListComponent extends Collection {

  private final DefaultHUDListScreen screen;

  public HUDListComponent(DefaultHUDListScreen screen, HUD hud) {
    this.screen = screen;
    IFontRenderer fontRenderer =
        Sorus.getSorus().getGUIManager().getRenderer().getRubikFontRenderer();
    this.add(
            new Rectangle().size(680, 100).position(4, 4).color(DefaultTheme.getMedbackgroundLayerColor()));
    this.add(
            new Rectangle()
                    .gradient(
                            DefaultTheme.getShadowStartColor(),
                            DefaultTheme.getShadowStartColor(),
                            DefaultTheme.getShadowEndColor(),
                            DefaultTheme.getShadowEndColor())
                    .size(680, 4)
                    .position(4, 0));
    this.add(
            new Rectangle()
                    .gradient(
                            DefaultTheme.getShadowStartColor(),
                            DefaultTheme.getShadowEndColor(),
                            DefaultTheme.getShadowEndColor(),
                            DefaultTheme.getShadowEndColor())
                    .size(4, 4)
                    .position(684, 0));
    this.add(
            new Rectangle()
                    .gradient(
                            DefaultTheme.getShadowStartColor(),
                            DefaultTheme.getShadowEndColor(),
                            DefaultTheme.getShadowEndColor(),
                            DefaultTheme.getShadowStartColor())
                    .size(4, 100)
                    .position(684, 4));
    this.add(
            new Rectangle()
                    .gradient(
                            DefaultTheme.getShadowEndColor(),
                            DefaultTheme.getShadowEndColor(),
                            DefaultTheme.getShadowEndColor(),
                            DefaultTheme.getShadowStartColor())
                    .size(4, 4)
                    .position(684, 104));
    this.add(
            new Rectangle()
                    .gradient(
                            DefaultTheme.getShadowEndColor(),
                            DefaultTheme.getShadowEndColor(),
                            DefaultTheme.getShadowStartColor(),
                            DefaultTheme.getShadowStartColor())
                    .size(680, 4)
                    .position(4, 104));
    this.add(
            new Rectangle()
                    .gradient(
                            DefaultTheme.getShadowEndColor(),
                            DefaultTheme.getShadowEndColor(),
                            DefaultTheme.getShadowStartColor(),
                            DefaultTheme.getShadowEndColor())
                    .size(4, 4)
                    .position(0, 104));
    this.add(
            new Rectangle()
                    .gradient(
                            DefaultTheme.getShadowEndColor(),
                            DefaultTheme.getShadowStartColor(),
                            DefaultTheme.getShadowStartColor(),
                            DefaultTheme.getShadowEndColor())
                    .size(4, 100)
                    .position(0, 4));
    this.add(
            new Rectangle()
                    .gradient(
                            DefaultTheme.getShadowEndColor(),
                            DefaultTheme.getShadowStartColor(),
                            DefaultTheme.getShadowEndColor(),
                            DefaultTheme.getShadowEndColor())
                    .size(4, 4)
                    .position(0, 0));
    Collection collection = new Collection().position(15, 15);
    this.add(collection);
    // hud.addIconElements(collection);
    this.add(
        new Text()
            .fontRenderer(fontRenderer)
            .text(hud.getName())
            .position(105, 15)
            .scale(4, 4)
            .color(new Color(235, 235, 235, 210)));
    this.add(new SettingsButton(hud).position(615, 32.5));
    this.add(new RemoveButton(hud).position(545, 32.5));
    if (hud instanceof SingleHUD) {
      this.add(
          new HollowArc()
              .thickness(2)
              .radius(10, 10)
              .angle(0, 360)
              .position(110 + fontRenderer.getStringWidth(hud.getName()) * 4 + 15, 14.5)
              .color(new Color(235, 235, 235, 210)));
    } else {
      this.add(
          new HollowArc()
              .thickness(2)
              .radius(10, 10)
              .angle(0, 360)
              .position(110 + fontRenderer.getStringWidth(hud.getName()) * 4 + 15, 14.5)
              .color(new Color(235, 235, 235, 210)));
      this.add(
          new HollowArc()
              .thickness(2)
              .radius(10, 10)
              .angle(0, 360)
              .position(110 + fontRenderer.getStringWidth(hud.getName()) * 4 + 37.375, 14.5)
              .color(new Color(235, 235, 235, 210)));
      this.add(
          new HollowArc()
              .thickness(2)
              .radius(10, 10)
              .angle(0, 360)
              .position(110 + fontRenderer.getStringWidth(hud.getName()) * 4 + 60.375, 14.5)
              .color(new Color(235, 235, 235, 210)));
    }
    int i = 0;
    for (String string :
        this.getSplitDescription(
            hud.getDescription(),
            Sorus.getSorus().getGUIManager().getRenderer().getRubikFontRenderer(),
            150)) {
      this.add(
          new Text()
              .fontRenderer(Sorus.getSorus().getGUIManager().getRenderer().getRubikFontRenderer())
              .text(string)
              .position(105, 60 + i * 23)
              .scale(2, 2)
              .color(new Color(190, 190, 190, 210)));
      i++;
    }
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

  public class SettingsButton extends Collection {

    private double hoverPercent;

    private long prevRenderTime;

    private final HUD hud;

    private final org.sorus.client.gui.core.component.impl.Image image;

    public SettingsButton(HUD hud) {
      this.hud = hud;
      this.add(image = new Image().resource("sorus/gear.png").size(45, 45));
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
      hoverPercent =
          Math.max(0, Math.min(1, hoverPercent + (hovered ? 1 : -1) * deltaTime * 0.008));
      image.scale(1 + hoverPercent * 0.1, 1 + hoverPercent * 0.1);
      image.position(-2 * hoverPercent, -2 * hoverPercent);
      image.color(new Color(235, 235, 235, (int) (210 + 45 * hoverPercent)));
      prevRenderTime = renderTime;
      double x = this.absoluteX() + 22.5 * this.absoluteXScale();
      double y = this.absoluteY() + 22.5 * this.absoluteYScale();
      IGLHelper glHelper = Sorus.getSorus().getVersion().getData(IGLHelper.class);
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
        Sorus.getSorus().getGUIManager().close(HUDListComponent.this.screen.screen);
        hud.displaySettings(HUDListComponent.this.screen.screen);
      }
    }

    private boolean isHovered(double x, double y) {
      return x > this.absoluteX()
          && x < this.absoluteX() + 50 * this.absoluteXScale()
          && y > this.absoluteY()
          && y < this.absoluteY() + 50 * this.absoluteYScale();
    }
  }

  public class RemoveButton extends Collection {

    private final HUD hud;

    private final Collection main;

    private double hoverPercent;

    private long prevRenderTime;

    public RemoveButton(HUD hud) {
      this.hud = hud;
      this.add(main = new Collection());
      main.add(new Rectangle().size(45, 45).smooth(5).color(new Color(160, 35, 35)));
      main.add(
              new Rectangle().size(35, 10).smooth(3).position(5, 17.5).color(new Color(210, 210, 210)));
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
        Sorus.getSorus().getHUDManager().unregister(hud);
        HUDListComponent.this.screen.updateHUDS();
      }
    }

    private boolean isHovered(double x, double y) {
      return x > this.absoluteX()
          && x < this.absoluteX() + 40 * this.absoluteXScale()
          && y > this.absoluteY()
          && y < this.absoluteY() + 40 * this.absoluteYScale();
    }
  }
}
