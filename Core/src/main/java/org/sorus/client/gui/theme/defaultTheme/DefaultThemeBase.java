package org.sorus.client.gui.theme.defaultTheme;

import org.sorus.client.gui.theme.ThemeBase;

public class DefaultThemeBase<T> extends ThemeBase<T> {

  protected final DefaultTheme defaultTheme;

  public DefaultThemeBase(DefaultTheme defaultTheme) {
    this.defaultTheme = defaultTheme;
  }

  public DefaultTheme getDefaultTheme() {
    return defaultTheme;
  }
}
