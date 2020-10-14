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

import org.sorus.client.gui.screen.settings.IConfigurableScreen;
import org.sorus.client.settings.Setting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
