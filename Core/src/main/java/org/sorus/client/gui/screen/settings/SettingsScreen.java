package org.sorus.client.gui.screen.settings;

import org.sorus.client.Sorus;
import org.sorus.client.gui.core.Screen;
import org.sorus.client.gui.core.ThemeableScreen;

public class SettingsScreen extends ThemeableScreen {

  public SettingsScreen(Screen parent, IConfigurableScreen configurable) {
    this(parent, parent, configurable);
  }

  public SettingsScreen(Screen parent, Screen returnScreen, IConfigurableScreen configurable) {
    super(
        Sorus.getSorus()
            .getThemeManager()
            .getTheme("settings", parent, returnScreen, configurable));
  }

  @Override
  public boolean shouldOpenBlank() {
    return true;
  }
}
