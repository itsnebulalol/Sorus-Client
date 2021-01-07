package org.sorus.client.gui.screen.settings.components;

import java.util.List;
import org.sorus.client.Sorus;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.screen.settings.ThemeableConfigurable;
import org.sorus.client.settings.Setting;

public class ClickThrough extends ThemeableConfigurable {

  private static Collection main;

  public ClickThrough(Setting<Long> setting, List<String> options, String description) {
    super(
        Sorus.getSorus()
            .getThemeManager()
            .getTheme(
                "settings-click-through", main = new Collection(), setting, options, description));
    this.add(main);
  }
}
