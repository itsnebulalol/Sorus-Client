package org.sorus.client.gui.hud.positonscreen;

import org.sorus.client.Sorus;
import org.sorus.client.gui.core.ThemeableScreen;

public class HUDPositionScreen extends ThemeableScreen {

  public HUDPositionScreen(boolean animateIn) {
    super(Sorus.getSorus().getThemeManager().getTheme("hud-position", animateIn));
  }

  @Override
  public boolean shouldOpenBlank() {
    return true;
  }
}
