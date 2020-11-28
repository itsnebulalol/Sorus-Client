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

import org.sorus.client.Sorus;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.Panel;
import org.sorus.client.gui.core.component.impl.Arc;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.component.impl.Text;
import org.sorus.client.gui.core.font.IFontRenderer;
import org.sorus.client.gui.hud.positonscreen.HUDPositionScreen;
import org.sorus.client.gui.screen.MenuScreen;
import org.sorus.client.gui.screen.hudlist.HUDListScreen;
import org.sorus.client.gui.screen.modulelist.ModuleListScreen;
import org.sorus.client.gui.screen.profilelist.ProfileListScreen;
import org.sorus.client.gui.screen.theme.ThemeListScreen;
import org.sorus.client.gui.theme.ExitButton;
import org.sorus.client.gui.theme.ThemeBase;
import org.sorus.client.gui.theme.defaultTheme.DefaultTheme;
import org.sorus.client.version.IScreen;
import org.sorus.client.version.input.Key;
import org.sorus.client.version.render.IRenderer;

public class DefaultMenuScreen extends ThemeBase<MenuScreen> {

  private Panel main;
  private Collection menu;

  private final boolean performOpenAnimation;
  private long initTime;

  public DefaultMenuScreen(boolean performOpenAnimation) {
    this.performOpenAnimation = performOpenAnimation;
  }

  @Override
  public void init() {
    Sorus.getSorus().getVersion().getData(IRenderer.class).enableBlur(7.5);
    main = new Panel();
    main.add(menu = new Collection());
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
                  Sorus.getSorus().getGUIManager().open(new HUDPositionScreen(false));
                })
            .position(10, 10));
    menu.add(
        new MenuComponent(
                "HUDs", null, () -> Sorus.getSorus().getGUIManager().open(new HUDListScreen()))
            .position(20, 90));
    menu.add(
        new MenuComponent(
                "Modules",
                null,
                () -> Sorus.getSorus().getGUIManager().open(new ModuleListScreen()))
            .position(246, 90));
    menu.add(
        new MenuComponent(
                "Themes", null, () -> Sorus.getSorus().getGUIManager().open(new ThemeListScreen()))
            .position(472, 90));
    menu.add(new MenuComponent("Settings", null, null).position(20, 322));
    menu.add(new MenuComponent("Plugins", null, null).position(246, 322));
    menu.add(
        new MenuComponent(
                "Profiles",
                null,
                () -> Sorus.getSorus().getGUIManager().open(new ProfileListScreen()))
            .position(472, 322));
    menu.add(new MenuComponent("test", null, null).position(20, 554));
    menu.add(new MenuComponent("test", null, null).position(246, 554));
    menu.add(new MenuComponent("test", null, null).position(472, 554));
    initTime =
        performOpenAnimation ? System.currentTimeMillis() : System.currentTimeMillis() - 1000;
  }

  @Override
  public void render() {
    main.scale(
        Sorus.getSorus().getVersion().getData(IScreen.class).getScaledWidth() / 1920,
        Sorus.getSorus().getVersion().getData(IScreen.class).getScaledHeight() / 1080);
    double fadeInPercent = Math.min((System.currentTimeMillis() - initTime) / 150.0, 1);
    this.menu
        .position(960 - 350 * fadeInPercent, 540 - 400 * fadeInPercent)
        .scale(fadeInPercent, fadeInPercent);
    this.main.onRender(this.screen);
  }

  @Override
  public void exit() {
    Sorus.getSorus().getVersion().getData(IRenderer.class).disableBlur();
    this.main.onRemove();
  }

  @Override
  public void keyTyped(Key key, boolean repeat) {
    if (key == Key.ESCAPE) {
      Sorus.getSorus().getGUIManager().close(this.screen);
    }
  }
}
