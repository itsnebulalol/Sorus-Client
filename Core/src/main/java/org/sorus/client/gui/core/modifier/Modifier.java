package org.sorus.client.gui.core.modifier;

import org.sorus.client.gui.core.component.Component;

public class Modifier<T extends Component> {

  protected T component;

  public void onUpdate() {}

  public void onRemove() {}

  public void setComponent(T component) {
    this.component = component;
  }
}
