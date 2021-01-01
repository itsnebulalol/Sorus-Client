package org.sorus.client.event;

import org.sorus.client.Sorus;

public class Event {

  public void post() {
    Sorus.getSorus().getEventManager().post(this);
  }
}
