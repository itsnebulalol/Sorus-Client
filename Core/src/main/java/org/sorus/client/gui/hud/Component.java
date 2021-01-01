package org.sorus.client.gui.hud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.screen.settings.IConfigurableScreen;
import org.sorus.client.settings.Setting;

public abstract class Component implements IComponent, IConfigurableScreen {

  private final String name;

  protected HUD hud;

  private final List<Setting<?>> settings = new ArrayList<>();

  public Component(String name) {
    this.name = name;
  }

  @Override
  public void setHUD(HUD hud) {
    this.hud = hud;
  }

  protected void register(Setting<?> setting) {
    this.settings.add(setting);
  }

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

  public HUD getHUD() {
    return hud;
  }

  @Override
  public String getSettingsName() {
    return this.getName();
  }
}
