package org.sorus.client.gui.screen.settings.components;

import java.awt.*;
import java.util.List;
import org.sorus.client.Sorus;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.screen.settings.ThemeableConfigurable;
import org.sorus.client.settings.Setting;
import org.sorus.client.util.Pair;

public class CustomTextColor extends ThemeableConfigurable {

  private static Collection main;

  public CustomTextColor(Setting<List<List<Pair<String, Color>>>> setting, String description) {
    super(
        Sorus.getSorus()
            .getThemeManager()
            .getTheme("settings-custom-text", main = new Collection(), setting, description));
    this.add(main);
  }
}
