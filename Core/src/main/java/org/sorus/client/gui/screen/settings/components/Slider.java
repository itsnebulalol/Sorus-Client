package org.sorus.client.gui.screen.settings.components;

import org.sorus.client.Sorus;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.screen.settings.ThemeableConfigurable;
import org.sorus.client.settings.Setting;

public class Slider extends ThemeableConfigurable {

  private static Collection main;

  public Slider(Setting<Double> setting, double minValue, double maxValue, String description) {
    super(
        Sorus.getSorus()
            .getThemeManager()
            .getTheme(
                "settings-slider",
                main = new Collection(),
                setting,
                minValue,
                maxValue,
                description));
    this.add(main);
  }
}
