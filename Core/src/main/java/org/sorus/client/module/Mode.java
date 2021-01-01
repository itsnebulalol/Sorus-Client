package org.sorus.client.module;

import java.util.ArrayList;
import java.util.List;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.settings.Setting;

public class Mode {

  private final List<Setting<?>> settings = new ArrayList<>();

  public void register(Setting<?> setting) {
    this.settings.add(setting);
  }

  public List<Setting<?>> getSettings() {
    return settings;
  }

  public String getName() {
    return null;
  }

  public void addConfigComponents(Collection collection) {}
}
