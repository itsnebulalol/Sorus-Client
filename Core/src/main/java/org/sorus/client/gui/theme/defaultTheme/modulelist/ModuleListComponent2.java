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

package org.sorus.client.gui.theme.defaultTheme.modulelist;

import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.gui.core.Screen;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.*;
import org.sorus.client.gui.core.component.impl.Image;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.screen.settings.SettingsScreen;
import org.sorus.client.gui.theme.defaultTheme.DefaultTheme;
import org.sorus.client.module.ModuleConfigurable;
import org.sorus.client.util.Axis;
import org.sorus.client.util.MathUtil;
import org.sorus.client.version.IGLHelper;
import org.sorus.client.version.input.IInput;

import java.awt.*;

public class ModuleListComponent2 extends Collection {

  public static final double WIDTH = 830, HEIGHT = 110;

  private final DefaultModuleListScreen2 moduleListScreenTheme;
  private final ModuleConfigurable module;

  private final HollowRectangle border;

  public ModuleListComponent2(
      DefaultModuleListScreen2 moduleListScreenTheme, ModuleConfigurable module) {
    this.moduleListScreenTheme = moduleListScreenTheme;
    this.module = module;
    final double ROUNDING = 10;
    this.add(new Rectangle().size(WIDTH, HEIGHT).smooth(ROUNDING).color(DefaultTheme.getForegroundColorNew()));
    this.add(border = new HollowRectangle().thickness(3).size(WIDTH, HEIGHT).smooth(ROUNDING));
    this.add(new Image().resource("sorus/modules/test_icon.png").size(70, 70).position(20, 20));
    this.add(new Text().fontRenderer(Sorus.getSorus().getGUIManager().getRenderer().getRubikFontRenderer()).text(module.getName()).scale(3, 3).position(110, 25).color(DefaultTheme.getElementColorNew()));
    int i = 0;
    for (String string :
            module.getSplitDescription(
                    Sorus.getSorus().getGUIManager().getRenderer().getRubikFontRenderer(), 200)) {
      this.add(
              new Text()
                      .fontRenderer(Sorus.getSorus().getGUIManager().getRenderer().getRubikFontRenderer())
                      .text(string)
                      .position(110, 60 + i * 20)
                      .scale(2, 2)
                      .color(DefaultTheme.getElementSecondColorNew()));
      i++;
    }
    this.add(new Settings().position(WIDTH - 60, HEIGHT / 2 - 22.5));
    this.add(new Toggle().position(WIDTH - 120, HEIGHT / 2 - 22.5));
  }

  @Override
  public void onRender() {
    if(this.module.isEnabled()) {
      border.color(new Color(47, 138, 3));
    } else {
      border.color(new Color(170, 29, 29));
    }
    super.onRender();
  }

  public class Settings extends Collection {

    private final Collection main;
    private final Rectangle rectangle;
    private final Image image;
    private double hoverPercent;
    private long prevRenderTime;

    public Settings() {
      this.add(main = new Collection());
      main.add(rectangle = new Rectangle().size(45, 45).smooth(22.5).color(DefaultTheme.getBackgroundColorNew()));
      main.add(image = new Image().resource("sorus/gear.png").size(27.5, 27.5).position(8.75, 8.75));
      Sorus.getSorus().getEventManager().register(this);
    }

    @Override
    public void onRender() {
      IInput input = Sorus.getSorus().getVersion().getData(IInput.class);
      boolean hovered = this.isHovered(input.getMouseX(), input.getMouseY());
      long renderTime = System.currentTimeMillis();
      long deltaTime = renderTime - prevRenderTime;
      hoverPercent = MathUtil.clamp(hoverPercent + (hovered ? 1 : -1) * deltaTime * 0.01, 0, 1);
      main.position(-hoverPercent, -hoverPercent).scale(1 + hoverPercent * 0.05, 1 + hoverPercent * 0.05);
      this.prevRenderTime = renderTime;
      rectangle.onRender();
      IGLHelper glHelper = Sorus.getSorus().getVersion().getData(IGLHelper.class);
      double x = main.absoluteX() + 22.5 * main.absoluteXScale(), y = main.absoluteY() + 22.5 * main.absoluteYScale();
      glHelper.translate(x, y, 0);
      glHelper.rotate(Axis.Z, hoverPercent * 40);
      glHelper.translate(-x, -y, 0);
      image.onRender();
      glHelper.translate(x, y, 0);
      glHelper.rotate(Axis.Z, -hoverPercent * 40);
      glHelper.translate(-x, -y, 0);
    }

    @Override
    public void onRemove() {
      Sorus.getSorus().getEventManager().unregister(this);
      super.onRemove();
    }

    @EventInvoked
    public void onClick(MousePressEvent e) {
      if(this.isHovered(e.getX(), e.getY())) {
        Screen current = ModuleListComponent2.this.moduleListScreenTheme.screen;
        Sorus.getSorus().getGUIManager().close(current);
        Sorus.getSorus().getGUIManager().open(new SettingsScreen(current, ModuleListComponent2.this.module));
        ModuleListComponent2.this.module.setEnabled(!ModuleListComponent2.this.module.isEnabled());
      }
    }

    private boolean isHovered(double x, double y) {
      return x > this.absoluteX() && x < this.absoluteX() + 45 * this.absoluteXScale() && y > this.absoluteY() && y < this.absoluteY() + 45 * this.absoluteYScale();
    }

  }

  public class Toggle extends Collection {

    private final Collection main;
    private final Image image;

    private double expandedPercent;
    private long prevRenderTime;

    public Toggle() {
      this.add(main = new Collection());
      main.add(new Rectangle().size(45, 45).smooth(22.5).color(DefaultTheme.getBackgroundColorNew()));
      main.add(image = new Image().size(27.5, 27.5).position(8.75, 8.75));
      Sorus.getSorus().getEventManager().register(this);
    }

    @Override
    public void onRender() {
      if(ModuleListComponent2.this.module.isEnabled()) {
        image.resource("sorus/modules/enabled.png");
      } else {
        image.resource("sorus/modules/disabled.png");
      }
      IInput input = Sorus.getSorus().getVersion().getData(IInput.class);
      boolean hovered = this.isHovered(input.getMouseX(), input.getMouseY());
      long renderTime = System.currentTimeMillis();
      long deltaTime = renderTime - prevRenderTime;
      expandedPercent = MathUtil.clamp(expandedPercent + (hovered ? 1 : -1) * deltaTime * 0.01, 0, 1);
      main.position(-expandedPercent, -expandedPercent).scale(1 + expandedPercent * 0.05, 1 + expandedPercent * 0.05);
      this.prevRenderTime = renderTime;
      super.onRender();
    }

    @Override
    public void onRemove() {
      Sorus.getSorus().getEventManager().unregister(this);
      super.onRemove();
    }

    @EventInvoked
    public void onClick(MousePressEvent e) {
      if(this.isHovered(e.getX(), e.getY())) {
        ModuleListComponent2.this.module.setEnabled(!ModuleListComponent2.this.module.isEnabled());
      }
    }

    private boolean isHovered(double x, double y) {
      return x > this.absoluteX() && x < this.absoluteX() + 45 * this.absoluteXScale() && y > this.absoluteY() && y < this.absoluteY() + 45 * this.absoluteYScale();
    }

  }

}
