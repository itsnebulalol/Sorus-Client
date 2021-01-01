package org.sorus.client.gui.core.component.impl;

import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.Component;

public class MultiText extends Collection {

  @Override
  public void onRender() {
    double x = 0;
    for (Component component : this.components) {
      Text text = (Text) component;
      text.position(x, 0);
      x += text.width() + text.fontRenderer().getSpacing();
    }
    super.onRender();
  }

  public double getWidth() {
    double width = 0;
    for (int i = 0; i < components.size(); i++) {
      Text text = (Text) components.get(i);
      width += text.width();
      if (i < components.size() - 1) {
        width += text.fontRenderer().getSpacing();
      }
    }
    return width;
  }

  public double getHeight() {
    double height = 0;
    for (Component component : components) {
      Text text = (Text) component;
      height = Math.max(height, text.height());
    }
    return height;
  }
}
