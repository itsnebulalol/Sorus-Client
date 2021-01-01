package org.sorus.client.gui.screen.theme;

import org.sorus.client.Sorus;
import org.sorus.client.gui.core.ThemeableScreen;
import org.sorus.client.gui.screen.Callback;
import org.sorus.client.gui.theme.Theme;

public class SelectThemeScreen extends ThemeableScreen {

  public SelectThemeScreen(Callback<Theme> receiver) {
    super(
        Sorus.getSorus()
            .getThemeManager()
            .getTheme("theme-select", Sorus.getSorus().getThemeManager(), receiver));
  }

  @Override
  public boolean shouldOpenBlank() {
    return true;
  }
}
