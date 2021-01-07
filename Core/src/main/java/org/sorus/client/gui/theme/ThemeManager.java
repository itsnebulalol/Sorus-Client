package org.sorus.client.gui.theme;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.sorus.client.gui.theme.defaultTheme.DefaultTheme;

public class ThemeManager {

  private final List<Class<? extends Theme>> registeredThemes = new ArrayList<>();
  private final List<Theme> currentThemes = new ArrayList<>();

  public ThemeManager() {
    this.register(DefaultTheme.class);
    this.currentThemes.add(new DefaultTheme());
  }

  public void register(Class<? extends Theme> theme) {
    this.registeredThemes.add(theme);
  }

  public void add(Theme theme) {
    this.currentThemes.add(theme);
  }

  public void remove(Theme theme) {
    this.currentThemes.remove(theme);
  }

  public <T> T getTheme(String screenName, Object... args) {
    Theme theme = null;
    Class<? extends ITheme<?>> themeClass = null;
    int i = 0;
    while (themeClass == null) {
      theme = currentThemes.get(i);
      themeClass = theme.getTheme(screenName);
      i++;
    }
    try {
      Object[] newArgs = new Object[args.length + 1];
      newArgs[0] = theme;
      System.arraycopy(args, 0, newArgs, 1, args.length);
      return (T) themeClass.getConstructors()[0].newInstance(newArgs);
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
      return null;
    }
  }

  public List<Theme> getCurrentThemes() {
    return currentThemes;
  }

  public List<Class<? extends Theme>> getRegisteredThemes() {
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
