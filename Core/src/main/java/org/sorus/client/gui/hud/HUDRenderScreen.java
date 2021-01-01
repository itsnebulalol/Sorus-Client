package org.sorus.client.gui.hud;

import org.sorus.client.Sorus;
import org.sorus.client.gui.core.ThemeableScreen;

public class HUDRenderScreen extends ThemeableScreen {

  public HUDRenderScreen() {
    super(Sorus.getSorus().getThemeManager().getTheme("hud-render"));
  }
}
