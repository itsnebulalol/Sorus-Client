package org.sorus.client.event.impl.client.chat;

import org.sorus.client.event.Event;

public class ChatEvent extends Event {

  private final String message;

  public ChatEvent(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
