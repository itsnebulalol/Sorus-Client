package org.sorus.client.gui.core.modifier.impl;

import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.gui.core.component.Component;
import org.sorus.client.gui.core.modifier.Modifier;
import org.sorus.client.version.input.IInput;

public class OnClick extends Modifier<Component> {

  private final double width, height;
  private final Runnable runnable;

  public OnClick(double width, double height, Runnable runnable) {
    this.width = width;
    this.height = height;
    this.runnable = runnable;
    Sorus.getSorus().getEventManager().register(this);
  }

  @EventInvoked
  public void onClick(MousePressEvent e) {
    double mouseX = Sorus.getSorus().getVersion().getData(IInput.class).getMouseX();
    double mouseY = Sorus.getSorus().getVersion().getData(IInput.class).getMouseY();
    if (this.isHovered(mouseX, mouseY)) {
      runnable.run();
    }
  }

  private boolean isHovered(double x, double y) {
    return x > component.absoluteX()
        && x < component.absoluteX() + width * component.absoluteXScale()
        && y > component.absoluteY()
        && y < component.absoluteY() + height * component.absoluteYScale();
  }

  @Override
  public void onRemove() {
    Sorus.getSorus().getEventManager().unregister(this);
  }
}
