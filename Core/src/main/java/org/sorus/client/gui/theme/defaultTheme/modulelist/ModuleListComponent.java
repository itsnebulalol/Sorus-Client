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

import java.awt.*;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.Image;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.component.impl.Text;
import org.sorus.client.gui.core.font.IFontRenderer;
import org.sorus.client.gui.theme.defaultTheme.DefaultTheme;
import org.sorus.client.module.ModuleConfigurable;
import org.sorus.client.util.Axis;
import org.sorus.client.version.IGLHelper;

public class ModuleListComponent extends Collection {

  private final DefaultModuleListScreen moduleListScreenTheme;

  public ModuleListComponent(
      DefaultModuleListScreen moduleListScreenTheme, ModuleConfigurable module) {
    this.moduleListScreenTheme = moduleListScreenTheme;
    IFontRenderer fontRenderer =
        Sorus.getSorus().getGUIManager().getRenderer().getGidoleFontRenderer();
    this.add(
        new Rectangle().size(670, 125).position(5, 4).color(DefaultTheme.getMedgroundLayerColor()));
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
    Collection collection = new Collection().position(15, 15);
    this.add(collection);
    module.addIconElements(collection);
    this.add(
        new Text()
            .fontRenderer(fontRenderer)
            .text(module.getName())
            .position(125, 20)
            .scale(4, 4)
            .color(new Color(235, 235, 235, 210)));
    int i = 0;
    for (String string :
        module.getSplitDescription(
            Sorus.getSorus().getGUIManager().getRenderer().getRubikFontRenderer(), 150)) {
      this.add(
          new Text()
              .fontRenderer(Sorus.getSorus().getGUIManager().getRenderer().getRubikFontRenderer())
              .text(string)
              .position(125, 65 + i * 23)
              .scale(2, 2)
              .color(new Color(190, 190, 190, 210)));
      i++;
    }
    this.add(new ToggleButton(module).position(450, 15));
    this.add(new SettingsButton(module).position(615, 15));
  }

  public class ToggleButton extends Collection {

    private final ModuleConfigurable module;
    private boolean enabled;

    private final Collection main;
    private final Rectangle rectangle;
    private final Text text;

    private double hoverPercent;
    private double switchPercent;

    private long prevRenderTime;

    public ToggleButton(ModuleConfigurable module) {
      this.module = module;
      this.enabled = module.isEnabled();
      this.add(main = new Collection());
      main.add(rectangle = new Rectangle().size(150, 40).smooth(4));
      main.add(
          text =
              new Text()
                  .fontRenderer(
                      Sorus.getSorus().getGUIManager().getRenderer().getGidoleFontRenderer())
                  .scale(3, 3));
      Sorus.getSorus().getEventManager().register(this);
    }

    @Override
    public void onRender() {
      this.text.text(enabled ? "ENABLED" : "DISABLED").position(75 - text.width() / 2 * 3, 12.5);
      long renderTime = System.currentTimeMillis();
      long deltaTime = renderTime - prevRenderTime;
      switchPercent =
          Math.max(0, Math.min(1, switchPercent + (enabled ? 1 : -1) * deltaTime * 0.005));
      this.rectangle.color(
          new Color(
              (int) (160 - switchPercent * 135),
              (int) (35 + switchPercent * 125),
              (int) (35 + switchPercent * 30)));
      boolean hovered =
          this.isHovered(
              Sorus.getSorus().getVersion().getInput().getMouseX(),
              Sorus.getSorus().getVersion().getInput().getMouseY());
      hoverPercent =
          Math.max(0, Math.min(1, hoverPercent + (hovered ? 1 : -1) * deltaTime * 0.008));
      this.main
          .position(-hoverPercent * 2.25, -hoverPercent * 0.75)
          .scale(1 + hoverPercent * 0.035, 1 + hoverPercent * 0.035);
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
        enabled = !enabled;
        ModuleListComponent.this.moduleListScreenTheme.screen.enableDisableModule(module, enabled);
      }
    }

    private boolean isHovered(double x, double y) {
      return x > this.absoluteX()
          && x < this.absoluteX() + 150 * this.absoluteXScale()
          && y > this.absoluteY()
          && y < this.absoluteY() + 40 * this.absoluteYScale();
    }
  }

  public class SettingsButton extends Collection {

    private double hoverPercent;

    private long prevRenderTime;

    private final ModuleConfigurable module;

    private final org.sorus.client.gui.core.component.impl.Image image;

    public SettingsButton(ModuleConfigurable module) {
      this.module = module;
      this.add(image = new Image().resource("sorus/gear.png").size(40, 40));
      Sorus.getSorus().getEventManager().register(this);
    }

    @Override
    public void onRender() {
      long renderTime = System.currentTimeMillis();
      long deltaTime = renderTime - prevRenderTime;
      boolean hovered =
          module.isEnabled()
              && this.isHovered(
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
      if (module.isEnabled() && this.isHovered(e.getX(), e.getY())) {
        ModuleListComponent.this.moduleListScreenTheme.screen.displayModuleSettings(module);
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
