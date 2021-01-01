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

  protected void unregister(Setting<?> setting) {
    this.settings.remove(setting);
  }

  @Override
  public void onLoad() {
    if (this.isEnabled()) {
      this.onEnable();
    }
    super.onLoad();
  }

  public void onEnable() {}

  public void onDisable() {}

  public boolean isEnabled() {
    return enabled.getValue();
  }

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
