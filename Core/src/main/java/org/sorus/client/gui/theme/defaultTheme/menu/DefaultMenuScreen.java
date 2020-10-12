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

package org.sorus.client.gui.theme.defaultTheme.menu;

import java.awt.*;
import org.sorus.client.Sorus;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.Panel;
import org.sorus.client.gui.core.component.impl.Arc;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.component.impl.Text;
import org.sorus.client.gui.core.font.IFontRenderer;
import org.sorus.client.gui.screen.hudlist.HUDListScreen;
import org.sorus.client.gui.screen.modulelist.ModuleListScreen;
import org.sorus.client.gui.theme.base.MenuScreenTheme;
import org.sorus.client.version.input.Key;

public class DefaultMenuScreen extends MenuScreenTheme {

  private Panel main;

  @Override
  public void init() {
    main = new Panel();
    Collection menu = new Collection().position(690, 200); // +60
    main.add(menu);
    menu.add(new Rectangle().smooth(5).size(580, 600).position(0, 70).color(new Color(18, 18, 18)));
    menu.add(new Rectangle().size(580, 65).position(0, 5).color(new Color(30, 30, 30)));
    menu.add(new Arc().radius(5, 5).angle(180, 270).position(0, 0).color(new Color(30, 30, 30)));
    menu.add(new Arc().radius(5, 5).angle(90, 180).position(570, 0).color(new Color(30, 30, 30)));
    menu.add(new Rectangle().size(570, 5).position(5, 0).color(new Color(30, 30, 30)));
    menu.add(
        new Rectangle()
            .gradient(
                new Color(14, 14, 14, 0),
                new Color(14, 14, 14, 0),
                new Color(14, 14, 14),
                new Color(14, 14, 14))
            .size(580, 7)
            .position(0, 70));
    IFontRenderer fontRenderer =
        Sorus.getSorus().getGUIManager().getRenderer().getRubikFontRenderer();
    menu.add(
        new Text()
            .fontRenderer(fontRenderer)
            .text("SORUS")
            .position(290 - fontRenderer.getStringWidth("SORUS") / 2 * 5.5, 17.5)
            .scale(5.5, 5.5)
            .color(new Color(215, 215, 215)));
    menu.add(new MenuComponent("HUDs", null, HUDListScreen.class).position(20, 90));
    menu.add(new MenuComponent("Modules", null, ModuleListScreen.class).position(207.5, 90));
    menu.add(new MenuComponent("Themes", null, null).position(395, 90));
    menu.add(new MenuComponent("Settings", null, null).position(20, 282.5));
    menu.add(new MenuComponent("Plugins", null, null).position(207.5, 282.5));
    menu.add(new MenuComponent("test", null, null).position(395, 282.5));
    menu.add(new MenuComponent("test", null, null).position(20, 475));
    menu.add(new MenuComponent("test", null, null).position(207.5, 475));
    menu.add(new MenuComponent("test", null, null).position(395, 475));
  }

  @Override
  public void render() {
    main.scale(
        Sorus.getSorus().getVersion().getScreen().getScaledWidth() / 1920,
        Sorus.getSorus().getVersion().getScreen().getScaledHeight() / 1080);
    this.main.onRender(this.screen);
  }

  @Override
  public void exit() {
    this.main.onRemove();
  }

  @Override
  public void keyTyped(Key key) {
    if (key == Key.ESCAPE) {
      Sorus.getSorus().getGUIManager().close(this.screen);
    }
  }
}
