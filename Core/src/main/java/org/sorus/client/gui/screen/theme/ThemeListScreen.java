package org.sorus.client.gui.screen.theme;

import org.sorus.client.Sorus;
import org.sorus.client.gui.core.ThemeableScreen;

public class ThemeListScreen extends ThemeableScreen {

  public ThemeListScreen() {
    super(
        Sorus.getSorus()
            .getThemeManager()
            .getTheme("theme-list", Sorus.getSorus().getThemeManager()));
  }

  @Override
  public boolean shouldOpenBlank() {
    return true;
  }
}
