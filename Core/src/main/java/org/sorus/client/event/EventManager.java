package org.sorus.client.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class EventManager {

  private final Map<Class<Event>, List<EventData>> eventData = new HashMap<>();

  public void register(Object object) {
    for (Method method : this.getCompleteMethods(object.getClass())) {
      if (method.getDeclaredAnnotation(EventInvoked.class) != null) {
        Class<Event> caller = (Class<Event>) method.getParameterTypes()[0];
        EventData eventData = new EventData(object, method);
        this.addEventData(caller, eventData);
      }
    }
  }

  private List<Method> getCompleteMethods(Class<?> clazz) {
    List<Method> methods =
        new ArrayList<>(new ArrayList<>(Arrays.asList(clazz.getDeclaredMethods())));
    if (clazz.getSuperclass() != null) {
      methods.addAll(this.getCompleteMethods(clazz.getSuperclass()));
    }
    return methods;
  }

  public void unregister(Object object) {
    for (List<EventData> eventDatas : this.eventData.values()) {
      eventDatas.removeIf(eventData -> eventData.getObject().equals(object));
    }
  }

  private void addEventData(Class<Event> caller, EventData eventData) {
    this.eventData.computeIfAbsent(caller, k -> new ArrayList<>());
    this.eventData.get(caller).add(eventData);
  }

  public void post(Event event) {
    List<EventData> eventDatas = new ArrayList<>();
    for (Class<Event> caller : this.eventData.keySet()) {
      if (event.getClass().isAssignableFrom(caller)) {
        eventDatas.addAll(this.eventData.get(caller));
      }
    }
    for (EventData eventData : eventDatas) {
      Object object = eventData.getObject();
      Method target = eventData.getTarget();
      try {
        target.invoke(object, event);
      } catch (IllegalAccessException | InvocationTargetException e) {
        e.printStackTrace();
      }
    }
  }
}
