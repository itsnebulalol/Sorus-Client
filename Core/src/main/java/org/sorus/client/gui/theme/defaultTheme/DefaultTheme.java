package org.sorus.client.gui.theme.defaultTheme;

import java.awt.*;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.screen.settings.components.ColorPicker;
import org.sorus.client.gui.theme.Theme;
import org.sorus.client.gui.theme.defaultTheme.hudconfig.DefaultHUDConfigScreen;
import org.sorus.client.gui.theme.defaultTheme.hudlist.DefaultHUDListScreen;
import org.sorus.client.gui.theme.defaultTheme.mainmenu.DefaultMainMenuScreen;
import org.sorus.client.gui.theme.defaultTheme.modulelist.DefaultModuleListScreen;
import org.sorus.client.gui.theme.defaultTheme.positionscreen.DefaultHUDPositionScreen;
import org.sorus.client.gui.theme.defaultTheme.profilelist.DefaultProfileListScreen;
import org.sorus.client.gui.theme.defaultTheme.selectComponent.DefaultSelectComponentScreen;
import org.sorus.client.gui.theme.defaultTheme.settings.components.*;
import org.sorus.client.gui.theme.defaultTheme.theme.DefaultSelectThemeScreen;
import org.sorus.client.gui.theme.defaultTheme.theme.DefaultThemeListScreen;
import org.sorus.client.settings.Setting;

public class DefaultTheme extends Theme {

  private final Setting<Color> backgroundColorNew;
  private final Setting<Color> foregroundColorNew;
  private final Setting<Color> gradientStartColorNew;
  private final Setting<Color> gradientEndColorNew;
  private final Setting<Color> elementBackgroundColorNew;
  private final Setting<Color> elementMedgroundColorNew;
  private final Setting<Color> elementForegroundColorNew;
  private final Setting<Color> elementColorNew;
  private final Setting<Color> elementSecondColorNew;

  public DefaultTheme() {
    super("DEFAULT");
    this.register("main-menu", DefaultMainMenuScreen.class);
    this.register("module-list", DefaultModuleListScreen.class);
    this.register("hud-render", DefaultHUDRenderScreen.class);
    this.register("hud-position", DefaultHUDPositionScreen.class);
    this.register("hud-list", DefaultHUDListScreen.class);
    this.register("hud-config", DefaultHUDConfigScreen.class);
    this.register("settings", DefaultSettingsScreen.class);
    this.register("color-picker", DefaultColorPickerScreen.class);
    this.register("component-select", DefaultSelectComponentScreen.class);
    this.register("theme-list", DefaultThemeListScreen.class);
    this.register("theme-select", DefaultSelectThemeScreen.class);
    this.register("profile-list", DefaultProfileListScreen.class);
    this.register("settings-click-through", DefaultClickThrough.class);
    this.register("settings-toggle", DefaultToggle.class);
    this.register("settings-color-picker", DefaultColorPicker.class);
    this.register("settings-text-box", DefaultTextBox.class);
    this.register("settings-custom-text", DefaultCustomTextColor.class);
    this.register("settings-keybind", DefaultKeybind.class);
    this.register("settings-slider", DefaultSlider.class);
    this.register(backgroundColorNew = new Setting<>("backgroundColor", new Color(25, 25, 25)));
    this.register(foregroundColorNew = new Setting<>("foregroundColor", new Color(30, 30, 30)));
    this.register(
        gradientStartColorNew = new Setting<>("gradientStartColor", new Color(10, 10, 10, 100)));
    this.register(
        gradientEndColorNew = new Setting<>("gradientEndColor", new Color(10, 10, 10, 20)));
    this.register(
        elementBackgroundColorNew = new Setting<>("elementBackgroundColor", new Color(40, 40, 40)));
    this.register(
        elementMedgroundColorNew = new Setting<>("elementMedgroundColor", new Color(55, 55, 55)));
    this.register(
        elementForegroundColorNew = new Setting<>("elementForegroundColor", new Color(80, 80, 80)));
    this.register(elementColorNew = new Setting<>("elementColor", new Color(200, 200, 200)));
    this.register(
        elementSecondColorNew = new Setting<>("elementSecondColor", new Color(150, 150, 150)));
  }

  @Override
  public void addConfigComponents(Collection collection) {
    collection.add(new ColorPicker(backgroundColorNew, "Background Color"));
    collection.add(new ColorPicker(foregroundColorNew, "Foreground Color"));
    collection.add(new ColorPicker(gradientStartColorNew, "Gradient Start Color"));
    collection.add(new ColorPicker(gradientEndColorNew, "Gradient End Color"));
    collection.add(new ColorPicker(elementBackgroundColorNew, "Element Background Color"));
    collection.add(new ColorPicker(elementMedgroundColorNew, "Element Medground Color"));
    collection.add(new ColorPicker(elementForegroundColorNew, "Element Foreground Color"));
    collection.add(new ColorPicker(elementColorNew, "Element Color"));
  }

  @Override
  public String getDisplayName() {
    return "DEFAULT";
  }

  public Color getBackgroundColorNew() {
    return this.backgroundColorNew.getValue();
  }

  public Color getForegroundColorNew() {
    return this.foregroundColorNew.getValue();
  }

  public Color getGradientStartColorNew() {
    return this.gradientStartColorNew.getValue();
  }

  public Color getGradientEndColorNew() {
    return this.gradientEndColorNew.getValue();
  }

  public Color getElementBackgroundColorNew() {
    return this.elementBackgroundColorNew.getValue();
  }

  public Color getElementMedgroundColorNew() {
    return this.elementMedgroundColorNew.getValue();
  }

  public Color getElementForegroundColorNew() {
    return this.elementForegroundColorNew.getValue();
  }

  public Color getElementColorNew() {
    return this.elementColorNew.getValue();
  }

  public Color getElementSecondColorNew() {
    return this.elementSecondColorNew.getValue();
  }
}
