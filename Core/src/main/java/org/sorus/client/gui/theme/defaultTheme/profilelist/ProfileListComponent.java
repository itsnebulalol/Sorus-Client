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

package org.sorus.client.gui.theme.defaultTheme.profilelist;

import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.component.impl.Text;
import org.sorus.client.gui.core.font.IFontRenderer;
import org.sorus.client.gui.theme.defaultTheme.DefaultTheme;
import org.sorus.client.util.Axis;
import org.sorus.client.version.IGLHelper;
import org.sorus.client.version.input.IInput;

import java.awt.*;

public class ProfileListComponent extends Collection {

  private final DefaultProfileListScreen profileListScreenTheme;
  private final String profile;

  public ProfileListComponent(
      DefaultProfileListScreen profileListScreenTheme, String profile) {
    this.profileListScreenTheme = profileListScreenTheme;
    this.profile = profile;
    IFontRenderer fontRenderer =
            Sorus.getSorus().getGUIManager().getRenderer().getRubikFontRenderer();
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
    this.add(
            new Text()
                    .fontRenderer(fontRenderer)
                    .text(profile)
                    .position(125, 20)
                    .scale(4, 4)
                    .color(new Color(235, 235, 235, 210)));
    if(Sorus.getSorus().getSettingsManager().getCurrentProfile().equals(this.profile)) {
      this.add(new Rectangle().size(40, 40).position(125, 70));
    }
    /*int i = 0;
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
    }*/
    this.add(new LoadButton().position(615, 15));
  }

  public class LoadButton extends Collection {

    private double hoverPercent;

    private long prevRenderTime;

    private final Rectangle rectangle;

    public LoadButton() {
      this.add(rectangle = new Rectangle().size(50, 50));
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
      rectangle.scale(1 + hoverPercent * 0.1, 1 + hoverPercent * 0.1);
      rectangle.position(-2 * hoverPercent, -2 * hoverPercent);
      rectangle.color(new Color(235, 235, 235, (int) (210 + 45 * hoverPercent)));
      prevRenderTime = renderTime;
      double x = this.absoluteX() + 20 * this.absoluteXScale();
      double y = this.absoluteY() + 20 * this.absoluteYScale();
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
        ProfileListComponent.this.profileListScreenTheme.screen.loadProfile(ProfileListComponent.this.profile);
        ProfileListComponent.this.profileListScreenTheme.updateProfiles();
        //ProfileListComponent.this.moduleListScreenTheme.screen.displayModuleSettings(module);
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
