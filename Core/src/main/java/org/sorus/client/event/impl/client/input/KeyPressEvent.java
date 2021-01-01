package org.sorus.client.event.impl.client.input;

import org.sorus.client.event.Event;
import org.sorus.client.version.input.Key;

public class KeyPressEvent extends Event {

  private final Key key;
  private final char character;
  private final boolean repeat;

  public KeyPressEvent(Key key, char character, boolean repeat) {
    this.key = key;
    this.character = character;
    this.repeat = repeat;
  }

  public Key getKey() {
    return key;
  }

  public char getCharacter() {
    return character;
  }

  public boolean isRepeat() {
    return repeat;
  }
}
