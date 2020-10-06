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

package org.sorus.client.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.sorus.client.Sorus;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.font.IFontRenderer;
import org.sorus.client.gui.screen.settings.IConfigurableScreen;
import org.sorus.client.settings.ISettingHolder;
import org.sorus.client.settings.Setting;

public abstract class ModuleConfigurable extends Module
    implements ISettingHolder, IConfigurableScreen {

  private final List<Setting<?>> settings = new ArrayList<>();

  private final Setting<Boolean> enabled;

  public ModuleConfigurable(String name) {
    super(name);
    this.register(enabled = new Setting<>("enabled", false));
    Sorus.getSorus().getSettingsManager().register(this);
  }

  protected void register(Setting<?> setting) {
    this.settings.add(setting);
  }

  /** Overridable method called when the module is enabled. */
  public void onEnable() {}

  /** Overridable method called when the module is disabled. */
  public void onDisable() {}

  public boolean isEnabled() {
    return enabled.getValue();
  }

  /**
   * Enables / disables the mod and calls the respective methods if necessary.
   *
   * @param enabled whether or not to enable the mod
   */
  public void setEnabled(boolean enabled) {
    if (enabled != this.enabled.getValue()) {
      if (enabled) {
        this.onEnable();
      } else {
        this.onDisable();
      }
    }
    this.enabled.setValue(enabled);
  }

  @Override
  public String getDisplayName() {
    return this.getName();
  }

  public void addIconElements(Collection collection) {}

  public String getDescription() {
    return "";
  }

  private final List<String> strings = new ArrayList<>();

  public List<String> getSplitDescription(IFontRenderer fontRenderer, double width) {
    if (!strings.isEmpty()) {
      return strings;
    }
    StringBuilder stringBuilder = new StringBuilder();
    for (char c : this.getDescription().toCharArray()) {
      stringBuilder.append(c);
      if (fontRenderer.getStringWidth(stringBuilder.toString()) > width) {
        String string = stringBuilder.toString();
        int index = string.lastIndexOf(" ");
        strings.add(string.substring(0, index));
        stringBuilder = new StringBuilder(string.substring(index + 1));
      }
    }
    strings.add(stringBuilder.toString());
    return strings;
  }

  @Override
  public void addConfigComponents(Collection collection) {}

  @Override
  public void updateConfigComponents() {}

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
    for (Setting<Object> setting : (List<Setting<Object>>) (List) this.settings) {
      if (settingsMap.get(setting.getName()) != null) {
        setting.setValueIgnoreType(settingsMap.get(setting.getName()));
      }
    }
  }

  @Override
  public String getSettingsName() {
    return this.getName();
  }
}
