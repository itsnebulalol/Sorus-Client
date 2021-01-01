package org.sorus.client.event.impl.client;

import org.sorus.client.event.Event;
import org.sorus.client.version.game.GUIType;

public class GuiSwitchEvent extends Event {

  private final GUIType type;

  public GuiSwitchEvent(GUIType type) {
    this.type = type;
  }

  public GUIType getType() {
    return type;
  }
}
