package org.sorus.client.module.impl.customscreens.mainmenu;

import org.sorus.client.Sorus;
import org.sorus.client.gui.core.ThemeableScreen;

public class MainMenuScreen extends ThemeableScreen {

  public MainMenuScreen() {
    super(Sorus.getSorus().getThemeManager().getTheme("main-menu"));
  }
}
