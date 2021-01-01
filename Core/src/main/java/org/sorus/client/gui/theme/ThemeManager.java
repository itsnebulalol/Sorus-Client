package org.sorus.client.gui.theme;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.sorus.client.gui.theme.defaultTheme.DefaultTheme;

public class ThemeManager {

  private final List<Theme> registeredThemes = new ArrayList<>();
  private final List<Theme> currentThemes = new ArrayList<>();

  public ThemeManager() {
    Theme defaultTheme = new DefaultTheme();
    this.register(defaultTheme);
    this.currentThemes.add(defaultTheme);
  }

  public void register(Theme theme) {
    this.registeredThemes.add(theme);
  }

  public void add(Theme theme) {
    this.currentThemes.add(theme);
  }

  public void remove(Theme theme) {
    this.currentThemes.remove(theme);
  }

  public <T> T getTheme(String screenName, Object... args) {
    Class<? extends ITheme<?>> themeClass = null;
    int i = 0;
    while (themeClass == null) {
      themeClass = currentThemes.get(i).getTheme(screenName);
      i++;
    }
    try {
      return (T) themeClass.getConstructors()[0].newInstance(args);
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
      return null;
    }
  }

  public List<Theme> getCurrentThemes() {
    return currentThemes;
  }

  public List<Theme> getRegisteredThemes() {
    return registeredThemes;
  }

  public <T extends Theme> T getTheme(Class<T> clazz) {
    for (Theme theme : this.getCurrentThemes()) {
      if (clazz.isAssignableFrom(theme.getClass())) {
        return (T) theme;
      }
    }
    return null;
  }
}
