package org.sorus.client.event;

public class EventCancelable extends Event {

  private boolean cancelled;

  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }

  public boolean isCancelled() {
    return cancelled;
  }
}
