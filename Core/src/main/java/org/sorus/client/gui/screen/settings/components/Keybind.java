package org.sorus.client.gui.screen.settings.components;

import org.sorus.client.Sorus;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.screen.settings.ThemeableConfigurable;
import org.sorus.client.settings.Setting;
import org.sorus.client.version.input.Input;

public class Keybind extends ThemeableConfigurable {

  private static Collection main;

  public Keybind(Setting<Input> setting, String description) {
    super(
        Sorus.getSorus()
            .getThemeManager()
            .getTheme("settings-keybind", main = new Collection(), setting, description));
    this.add(main);
  }
}
