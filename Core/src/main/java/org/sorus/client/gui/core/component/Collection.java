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

package org.sorus.client.gui.core.component;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link Component} used for storing a component that contains multiple other
 * components. Can be used recursively to have a collection inside of a collection.
 */
public class Collection extends Component {

  /** The components stored in the collection. */
  protected final List<Component> components;

  public Collection() {
    components = new ArrayList<>();
  }

  /** Renders all the components stored in the collection to the screen. */
  @Override
  public void onRender() {
    for (Component component : new ArrayList<>(this.components)) {
      component.onRender();
    }
  }

  /** Called when the component's screen is closed or it is removed from another collection. */
  @Override
  public void onRemove() {
    for (Component component : this.components) {
      component.onRemove();
    }
  }

  /**
   * Adds a component to the collection.
   *
   * @param component the component to add
   * @return the collection
   */
  public <T extends Collection> T add(Component component) {
    this.components.add(component);
    component.setCollection(this);
    return this.cast();
  }

  public <T extends Collection> T addAtFront(Component component) {
    this.components.add(0, component);
    component.setCollection(this);
    return this.cast();
  }

  /**
   * Removes a component from the list of components.
   *
   * @param component the component to remove
   */
  public void remove(Component component) {
    component.onRemove();
    this.components.remove(component);
  }

  /** Clears all components from the list. */
  public void clear() {
    for (Component component : new ArrayList<>(this.components)) {
      this.remove(component);
    }
  }

  public List<Component> getComponents() {
    return components;
  }
}
