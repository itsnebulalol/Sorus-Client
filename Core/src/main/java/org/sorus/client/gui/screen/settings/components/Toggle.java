package org.sorus.client.gui.screen.settings.components;

import org.sorus.client.Sorus;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.screen.settings.ThemeableConfigurable;
import org.sorus.client.settings.Setting;

public class Toggle extends ThemeableConfigurable {

  private static Collection main;

  public Toggle(Setting<Boolean> setting, String description) {
    super(Sorus.getSorus().getThemeManager().getTheme("settings-toggle", main = new Collection(), setting, description));
    this.add(main);
  }
}
