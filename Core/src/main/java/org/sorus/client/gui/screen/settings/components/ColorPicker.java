package org.sorus.client.gui.screen.settings.components;

import java.awt.*;
import org.sorus.client.Sorus;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.screen.settings.ThemeableConfigurable;
import org.sorus.client.settings.Setting;

public class ColorPicker extends ThemeableConfigurable {

  private static Collection main;

  public ColorPicker(Setting<Color> setting, String description) {
    super(
        Sorus.getSorus()
            .getThemeManager()
            .getTheme("settings-color-picker", main = new Collection(), setting, description));
    this.add(main);
  }
}
