package org.sorus.client.gui.screen;

import org.sorus.client.Sorus;
import org.sorus.client.gui.core.ThemeableScreen;

public class MenuScreen extends ThemeableScreen {

  public MenuScreen(boolean performOpenAnimation) {
    super(Sorus.getSorus().getThemeManager().getTheme("menu", performOpenAnimation));
  }

  @Override
  public boolean shouldOpenBlank() {
    return true;
  }
}
