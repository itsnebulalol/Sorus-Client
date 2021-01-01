package org.sorus.client.gui.screen;

import org.sorus.client.Sorus;
import org.sorus.client.gui.core.Screen;
import org.sorus.client.gui.core.ThemeableScreen;
import org.sorus.client.gui.hud.HUDManager;
import org.sorus.client.gui.hud.IComponent;

public class SelectComponentScreen extends ThemeableScreen {

  public SelectComponentScreen(Screen parent, Callback<IComponent> callback) {
    super(Sorus.getSorus().getThemeManager().getTheme("component-select", parent, callback));
  }

  @Override
  public boolean shouldOpenBlank() {
    return true;
  }
}
