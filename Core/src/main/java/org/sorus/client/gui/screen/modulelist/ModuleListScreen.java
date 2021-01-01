package org.sorus.client.gui.screen.modulelist;

import org.sorus.client.Sorus;
import org.sorus.client.gui.core.ThemeableScreen;
import org.sorus.client.gui.screen.settings.SettingsScreen;
import org.sorus.client.module.ModuleConfigurable;

public class ModuleListScreen extends ThemeableScreen {

  public ModuleListScreen() {
    super(Sorus.getSorus().getThemeManager().getTheme("module-list"));
  }

  public void displayModuleSettings(ModuleConfigurable module) {
    Sorus.getSorus().getGUIManager().close(this);
    Sorus.getSorus().getGUIManager().open(new SettingsScreen(this, module));
  }

  public void enableDisableModule(ModuleConfigurable module, boolean enable) {
    module.setEnabled(enable);
  }

  @Override
  public boolean shouldOpenBlank() {
    return true;
  }
}
