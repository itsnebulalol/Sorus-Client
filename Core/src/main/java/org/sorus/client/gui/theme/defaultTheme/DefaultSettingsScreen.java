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
import org.sorus.client.Sorus;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.Panel;
import org.sorus.client.gui.core.component.impl.*;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.font.IFontRenderer;
import org.sorus.client.gui.screen.settings.IConfigurableScreen;
import org.sorus.client.gui.screen.settings.SettingsHolder;
import org.sorus.client.gui.screen.settings.SettingsScreen;
import org.sorus.client.gui.theme.ThemeBase;
import org.sorus.client.util.MathUtil;
import org.sorus.client.version.input.Key;

public class DefaultSettingsScreen extends ThemeBase<SettingsScreen> {

  private final IConfigurableScreen configurable;

  private Panel main;
  private Scroll scroll;
  private SettingsHolder settingsHolder;

  private double targetScroll;

  public DefaultSettingsScreen(IConfigurableScreen configurable) {
    this.configurable = configurable;
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
    menu.add(
        new Text()
            .fontRenderer(fontRenderer)
            .text(configurable.getDisplayName())
            .position(
                350 - fontRenderer.getStringWidth(configurable.getDisplayName()) / 2 * 4.5, 95)
            .scale(4.5, 4.5)
            .color(new Color(175, 175, 175)));
    menu.add(new Rectangle().size(660, 620).position(20, 150).color(new Color(30, 30, 30)));
    menu.add(
        new Rectangle()
            .gradient(
                new Color(14, 14, 14, 150),
                new Color(14, 14, 14, 150),
                new Color(14, 14, 14, 30),
                new Color(14, 14, 14, 30))
            .size(660, 4)
            .position(20, 146));
    menu.add(
        new Rectangle()
            .gradient(
                new Color(14, 14, 14, 150),
                new Color(14, 14, 14, 30),
                new Color(14, 14, 14, 30),
                new Color(14, 14, 14, 30))
            .size(4, 4)
            .position(680, 146));
    menu.add(
        new Rectangle()
            .gradient(
                new Color(14, 14, 14, 150),
                new Color(14, 14, 14, 30),
                new Color(14, 14, 14, 30),
                new Color(14, 14, 14, 150))
            .size(4, 620)
            .position(680, 150));
    menu.add(
        new Rectangle()
            .gradient(
                new Color(14, 14, 14, 30),
                new Color(14, 14, 14, 30),
                new Color(14, 14, 14, 30),
                new Color(14, 14, 14, 150))
            .size(4, 4)
            .position(680, 770));
    menu.add(
        new Rectangle()
            .gradient(
                new Color(14, 14, 14, 30),
                new Color(14, 14, 14, 30),
                new Color(14, 14, 14, 150),
                new Color(14, 14, 14, 150))
            .size(660, 4)
            .position(20, 770));
    menu.add(
        new Rectangle()
            .gradient(
                new Color(14, 14, 14, 30),
                new Color(14, 14, 14, 30),
                new Color(14, 14, 14, 150),
                new Color(14, 14, 14, 30))
            .size(4, 4)
            .position(16, 770));
    menu.add(
        new Rectangle()
            .gradient(
                new Color(14, 14, 14, 30),
                new Color(14, 14, 14, 150),
                new Color(14, 14, 14, 150),
                new Color(14, 14, 14, 30))
            .size(4, 620)
            .position(16, 150));
    menu.add(
        new Rectangle()
            .gradient(
                new Color(14, 14, 14, 30),
                new Color(14, 14, 14, 150),
                new Color(14, 14, 14, 30),
                new Color(14, 14, 14, 30))
            .size(4, 4)
            .position(16, 146));
    /*menu.add(
        new Rectangle()
            .gradient(
                new Color(14, 14, 14, 0),
                new Color(14, 14, 14, 0),
                new Color(14, 14, 14),
                new Color(14, 14, 14))
            .size(650, 7)
            .position(25, 770));
    menu.add(
        new Rectangle()
            .gradient(
                new Color(14, 14, 14),
                new Color(14, 14, 14),
                new Color(14, 14, 14, 0),
                new Color(14, 14, 14, 0))
            .size(650, 7)
            .position(25, 143));
    menu.add(
        new Rectangle()
            .gradient(
                new Color(14, 14, 14, 0),
                new Color(14, 14, 14),
                new Color(14, 14, 14),
                new Color(14, 14, 14, 0))
            .size(7, 610)
            .position(13, 155));
    menu.add(
        new Rectangle()
            .gradient(
                new Color(14, 14, 14),
                new Color(14, 14, 14, 0),
                new Color(14, 14, 14, 0),
                new Color(14, 14, 14))
            .size(7, 610)
            .position(680, 155));*/
    Scissor scissor = new Scissor().size(680, 580).position(10, 170);
    this.scroll = new Scroll();
    scroll.position(0, 2);
    scissor.add(scroll);
    menu.add(scissor);
    scissor.add(scroll);
    settingsHolder = new SettingsHolder();
    configurable.addConfigComponents(settingsHolder);
    scroll.add(settingsHolder);
    menu.add(scissor);
  }

  @Override
  public void render() {
    main.scale(
        Sorus.getSorus().getVersion().getScreen().getScaledWidth() / 1920,
        Sorus.getSorus().getVersion().getScreen().getScaledHeight() / 1080);
    final int FPS = Math.max(Sorus.getSorus().getVersion().getGame().getFPS(), 1);
    double scrollValue = Sorus.getSorus().getVersion().getInput().getScroll();
    targetScroll = targetScroll + scrollValue * 0.7;
    scroll.setScroll((targetScroll - scroll.getScroll()) * 7 / FPS + scroll.getScroll());
    double maxScroll = settingsHolder.getHeight() - 580;
    scroll.addMinMaxScroll(-maxScroll, 0);
    targetScroll = MathUtil.clamp(targetScroll, scroll.getMinScroll(), scroll.getMaxScroll());
    main.onRender(this.screen);
  }

  @Override
  public void exit() {
    Sorus.getSorus().getVersion().getRenderer().disableBlur();
    Sorus.getSorus().getSettingsManager().save();
    main.onRemove();
  }

  @Override
  public void keyTyped(Key key) {
    if (key == Key.ESCAPE && this.screen.isInteractContainer()) {
      Sorus.getSorus().getGUIManager().close(this.screen);
    }
  }
}
