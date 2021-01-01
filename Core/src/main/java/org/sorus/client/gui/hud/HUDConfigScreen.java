package org.sorus.client.gui.hud;

import org.sorus.client.Sorus;
import org.sorus.client.gui.core.ThemeableScreen;

public class HUDConfigScreen extends ThemeableScreen {

  public HUDConfigScreen(HUD hud) {
    super(Sorus.getSorus().getThemeManager().getTheme("hud-config", hud));
  }

  @Override
  public boolean shouldOpenBlank() {
    return true;
  }
}
