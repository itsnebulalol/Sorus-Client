package org.sorus.client.event.impl.client.input;

import org.sorus.client.event.Event;
import org.sorus.client.version.input.Button;

public class MouseReleaseEvent extends Event {

  private final Button button;

  public MouseReleaseEvent(Button button) {
    this.button = button;
  }

  public Button getButton() {
    return button;
  }
}
