package org.sorus.client.event.impl.client.input;

import org.sorus.client.event.Event;
import org.sorus.client.version.input.Key;

public class KeyReleaseEvent extends Event {

  private final Key key;

  public KeyReleaseEvent(Key key) {
    this.key = key;
  }

  public Key getKey() {
    return key;
  }
}
