/*
 * MIT License
 *
 * Copyright (c) 2020 Danterus
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
