package org.sorus.client.gui.screen.settings;

import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.Component;

public class SettingsHolder extends Collection {

  private final IConfigurableScreen configurable;

  private double height;

  public SettingsHolder(IConfigurableScreen configurable) {
    this.configurable = configurable;
    this.refresh();
  }

  @Override
  public void onRender() {
    double y = 15;
    for (Component component : this.components) {
      Configurable configurable = (Configurable) component;
      configurable.position(15, y);
      y += configurable.getHeight();
    }
    this.height = y;
    super.onRender();
  }

  public double getHeight() {
    return height;
  }

  public void refresh() {
    this.clear();
    this.configurable.addConfigComponents(this);
  }
}
