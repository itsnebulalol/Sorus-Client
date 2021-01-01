package org.sorus.client.gui.theme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.sorus.client.gui.screen.settings.IConfigurableScreen;
import org.sorus.client.settings.Setting;

public abstract class Theme implements IConfigurableScreen {

  private final Map<String, Class<? extends ITheme<?>>> themedScreens = new HashMap<>();

  private final List<Setting<?>> settings = new ArrayList<>();

  private final String name;

  public Theme(String name) {
    this.name = name;
  }

  public void register(String screenName, Class<? extends ITheme<?>> theme) {
    this.themedScreens.put(screenName, theme);
  }

  protected void register(Setting<?> setting) {
    this.settings.add(setting);
  }

  public Class<? extends ITheme<?>> getTheme(String screenName) {
    return this.themedScreens.get(screenName);
  }

  public String getName() {
    return name;
  }
}
