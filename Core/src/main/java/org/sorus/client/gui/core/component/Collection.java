package org.sorus.client.gui.core.component;

import java.util.ArrayList;
import java.util.List;

public class Collection extends Component {

  protected final List<Component> components = new ArrayList<>();

  @Override
  public void onUpdate() {
    for (Component component : new ArrayList<>(this.components)) {
      component.onUpdate();
    }
    super.onUpdate();
  }

  @Override
  public void onRender() {
    for (Component component : components) {
      component.onRender();
    }
  }

  @Override
  public void onRemove() {
    for (Component component : this.components) {
      component.onRemove();
    }
  }

  public <T extends Collection> T add(Component component) {
    this.components.add(component);
    component.setParent(this);
    return this.cast();
  }

  public <T extends Collection> T insert(Component component, int index) {
    this.components.add(index, component);
    component.setParent(this);
    return this.cast();
  }

  public <T extends Collection> T addAtFront(Component component) {
    this.insert(component, 0);
    return this.cast();
  }

  public void remove(Component component) {
    component.onRemove();
    this.components.remove(component);
  }

  public void clear() {
    for (Component component : new ArrayList<>(this.components)) {
      this.remove(component);
    }
  }

  public List<Component> getChildren() {
    return components;
  }
}
