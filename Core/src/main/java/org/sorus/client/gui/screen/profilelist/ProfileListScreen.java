package org.sorus.client.gui.screen.profilelist;

import org.sorus.client.Sorus;
import org.sorus.client.gui.core.ThemeableScreen;

public class ProfileListScreen extends ThemeableScreen {

  public ProfileListScreen() {
    super(Sorus.getSorus().getThemeManager().getTheme("profile-list"));
  }

  public void loadProfile(String profileName) {
    Sorus.getSorus().getSettingsManager().load(profileName);
  }

  @Override
  public boolean shouldOpenBlank() {
    return true;
  }
}
