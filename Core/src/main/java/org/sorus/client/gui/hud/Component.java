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

package org.sorus.client.gui.hud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.screen.settings.IConfigurableScreen;
import org.sorus.client.settings.Setting;

/**
 * Implementation of {@link IComponent} to allow a component to pass in its name in the constructor.
 */
public abstract class Component implements IComponent, IConfigurableScreen {

  private final String name;

  /** The hud that the component is part of, used for rendering in the correct position. */
  protected HUD hud;

  private final List<Setting<?>> settings = new ArrayList<>();

  public Component(String name) {
    this.name = name;
  }

  @Override
  public void setHUD(HUD hud) {
    this.hud = hud;
  }

  /** Registers a setting by adding it to the list of settings stored for the component. */
  protected void register(Setting<?> setting) {
    this.settings.add(setting);
  }

  /** Unregisters a setting by adding it to the list of settings stored for the component. */
  protected void unregister(Setting<?> setting) {
    this.settings.remove(setting);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getDisplayName() {
    return this.getName();
  }

  public String getDescription() {
    return "";
  }

  protected double getScale() {
    return this.hud.getScale();
  }

  @Override
  public void addConfigComponents(Collection collection) {}

  @Override
  public Map<String, Object> getSettings() {
    Map<String, Object> settings = new HashMap<>();
    for (Setting<?> setting : this.settings) {
      settings.put(setting.getName(), setting.getValue());
    }
    return settings;
  }

  @Override
  public void setSettings(Object settings) {
    Map<String, Object> settingsMap = (Map<String, Object>) settings;
    boolean continuing = true;
    int i = 0;
    if (this.settings.size() == 0) {
      continuing = false;
    }
    while (continuing) {
      Setting<?> setting = this.settings.get(i);
      if (settingsMap.get(setting.getName()) != null) {
        setting.setValueIgnoreType(settingsMap.get(setting.getName()));
      }
      i++;
      if (i == this.settings.size()) {
        continuing = false;
      }
    }
  }

  @Override
  public String getSettingsName() {
    return this.getName();
  }
}
