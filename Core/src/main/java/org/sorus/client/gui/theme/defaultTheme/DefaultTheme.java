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
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.screen.profilelist.ProfileListScreen;
import org.sorus.client.gui.screen.settings.components.ColorPicker;
import org.sorus.client.gui.theme.Theme;
import org.sorus.client.gui.theme.defaultTheme.hudconfig.DefaultHUDConfigScreen;
import org.sorus.client.gui.theme.defaultTheme.hudlist.DefaultHUDListScreen;
import org.sorus.client.gui.theme.defaultTheme.menu.DefaultMenuScreen;
import org.sorus.client.gui.theme.defaultTheme.modulelist.DefaultModuleListScreen;
import org.sorus.client.gui.theme.defaultTheme.positionscreen.DefaultHUDPositionScreen;
import org.sorus.client.gui.theme.defaultTheme.profilelist.DefaultProfileListScreen;
import org.sorus.client.gui.theme.defaultTheme.theme.DefaultSelectThemeScreen;
import org.sorus.client.gui.theme.defaultTheme.theme.DefaultThemeListScreen;
import org.sorus.client.settings.Setting;

public class DefaultTheme extends Theme {

  private static DefaultTheme instance;

  private final Setting<Color> backgroundLayerColor;
  private final Setting<Color> medgroundLayerColor;
  private final Setting<Color> medforegroundLayerColor;
  private final Setting<Color> foregroundLayerColor;
  private final Setting<Color> foregroundLessLayerColor;
  private final Setting<Color> shadowStartColor;
  private final Setting<Color> shadowEndColor;

  public DefaultTheme() {
    super("DEFAULT");
    this.register("menu", DefaultMenuScreen.class);
    this.register("module-list", DefaultModuleListScreen.class);
    this.register("hud-render", DefaultHUDRenderScreen.class);
    this.register("hud-position", DefaultHUDPositionScreen.class);
    this.register("hud-list", DefaultHUDListScreen.class);
    this.register("hud-config", DefaultHUDConfigScreen.class);
    this.register("settings", DefaultSettingsScreen.class);
    this.register("settings-color-picker", DefaultColorPickerScreen.class);
    this.register("component-select", DefaultSelectComponentScreen.class);
    this.register("theme-list", DefaultThemeListScreen.class);
    this.register("theme-select", DefaultSelectThemeScreen.class);
    this.register("profile-list", DefaultProfileListScreen.class);
    this.register(
        backgroundLayerColor = new Setting<>("backgroundLayerColor", new Color(18, 18, 18)));
    this.register(
        medgroundLayerColor = new Setting<>("medgroundLayerColor", new Color(30, 30, 30)));
    this.register(
        medforegroundLayerColor = new Setting<>("medgroundLayerColor", new Color(40, 40, 40)));
    this.register(
        foregroundLayerColor = new Setting<>("foregroundLayerColor", new Color(215, 215, 215)));
    this.register(
        foregroundLessLayerColor =
            new Setting<>("foregroundLessLayerColor", new Color(160, 160, 160)));
    this.register(shadowStartColor = new Setting<>("shadowStartColor", new Color(14, 14, 14)));
    this.register(shadowEndColor = new Setting<>("shadowEndColor", new Color(14, 14, 14, 0)));
    instance = this;
  }

  @Override
  public void addConfigComponents(Collection collection) {
    collection.add(new ColorPicker(backgroundLayerColor, "Background Layer"));
    collection.add(new ColorPicker(medgroundLayerColor, "Medground Layer"));
    collection.add(new ColorPicker(medforegroundLayerColor, "Medforeground Layer"));
    collection.add(new ColorPicker(foregroundLayerColor, "Foreground Layer"));
    collection.add(new ColorPicker(foregroundLessLayerColor, "Foreground Less Layer"));
    collection.add(new ColorPicker(shadowStartColor, "Shadow Start"));
    collection.add(new ColorPicker(shadowEndColor, "Shadow End"));
  }

  @Override
  public String getDisplayName() {
    return "DEFAULT";
  }

  public static DefaultTheme getInstance() {
    return instance;
  }

  public static Color getBackgroundLayerColor() {
    return DefaultTheme.getInstance().backgroundLayerColor.getValue();
  }

  public static Color getMedgroundLayerColor() {
    return DefaultTheme.getInstance().medgroundLayerColor.getValue();
  }

  public static Color getMedforegroundLayerColor() {
    return DefaultTheme.getInstance().medforegroundLayerColor.getValue();
  }

  public static Color getForegroundLayerColor() {
    return DefaultTheme.getInstance().foregroundLayerColor.getValue();
  }

  public static Color getForegroundLessLayerColor() {
    return DefaultTheme.getInstance().foregroundLessLayerColor.getValue();
  }

  public static Color getShadowStartColor() {
    return DefaultTheme.getInstance().shadowStartColor.getValue();
  }

  public static Color getShadowEndColor() {
    return DefaultTheme.getInstance().shadowEndColor.getValue();
  }
}
