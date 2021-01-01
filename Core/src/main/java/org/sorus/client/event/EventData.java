package org.sorus.client.event;

import java.lang.reflect.Method;

public class EventData {

  private final Object object;

  private final Method target;

  public EventData(Object object, Method target) {
    this.object = object;
    this.target = target;
  }

  public Object getObject() {
    return object;
  }

  public Method getTarget() {
    return target;
  }
}
