package org.sorus.client.event.impl.client.input;

import org.sorus.client.event.Event;
import org.sorus.client.version.input.Button;

public class MousePressEvent extends Event {

  private final Button button;
  private final double x, y;

  public MousePressEvent(Button button, double x, double y) {
    this.button = button;
    this.x = x;
    this.y = y;
  }

  public Button getButton() {
    return button;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }
}
