package org.sorus.client.gui.screen.hudlist;

import org.sorus.client.Sorus;
import org.sorus.client.gui.core.ThemeableScreen;

public class HUDListScreen extends ThemeableScreen {

  public HUDListScreen() {
    super(Sorus.getSorus().getThemeManager().getTheme("hud-list"));
  }

  @Override
  public boolean shouldOpenBlank() {
    return true;
  }
}
