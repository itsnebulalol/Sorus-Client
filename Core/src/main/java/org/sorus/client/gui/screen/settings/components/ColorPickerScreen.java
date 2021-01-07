package org.sorus.client.gui.screen.settings.components;

import java.awt.*;
import org.sorus.client.Sorus;
import org.sorus.client.gui.core.ThemeableScreen;
import org.sorus.client.gui.screen.Callback;

public class ColorPickerScreen extends ThemeableScreen {

  public ColorPickerScreen(Color color, Callback<Color> callback) {
    super(Sorus.getSorus().getThemeManager().getTheme("color-picker", color, callback));
  }

  @Override
  public boolean shouldOpenBlank() {
    return true;
  }
}
