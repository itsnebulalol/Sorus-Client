/*
 * MIT License
 *
 * Copyright (c) 2020 Danterus
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.sorus.client.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import org.sorus.client.Sorus;

/**
 * The main event manager that stores and handles all events and event-invoked methods.
 * Implementation in {@link Sorus}.
 */
public class EventManager {

  /** Data for all the registered event-invoked methods. */
  private final Map<Class<Event>, List<EventData>> eventData = new HashMap<>();

  /**
   * Searches through all the methods in the object and registers all of the event-invoked with the
   * {@link EventManager}.
   *
   * @param object object to search for event-invoked methods
   */
  public void register(Object object) {
    for (Method method : this.getCompleteMethods(object.getClass())) {
      if (method.getDeclaredAnnotation(EventInvoked.class) != null) {
        Class<Event> caller = (Class<Event>) method.getParameterTypes()[0];
        EventData eventData = new EventData(object, method);
        this.addEventData(caller, eventData);
      }
    }
  }

  /**
   * Internal method that searches through a class, and all its superclasses to get all of it's
   * methods.
   *
   * @param clazz the class to search
   * @return the methods in the class and it's superclasses
   */
  private List<Method> getCompleteMethods(Class<?> clazz) {
    List<Method> methods =
        new ArrayList<>(new ArrayList<>(Arrays.asList(clazz.getDeclaredMethods())));
    if (clazz.getSuperclass() != null) {
      methods.addAll(this.getCompleteMethods(clazz.getSuperclass()));
    }
    return methods;
  }

  /**
   * Unregisters all the event-invoked methods associated with the object from the event manager.
   * All unregistered events will not continue to be called.
   *
   * @param object the object being unregistered
   */
  public void unregister(Object object) {
    for (List<EventData> eventDatas : this.eventData.values()) {
      eventDatas.removeIf(eventData -> eventData.getObject().equals(object));
    }
  }

  /**
   * Adds the event data to the list of datas for all the events.
   *
   * @param caller event that the event-invoked method is listening for
   * @param eventData data for the method being registered with the {@link EventManager}
   */
  private void addEventData(Class<Event> caller, EventData eventData) {
    this.eventData.computeIfAbsent(caller, k -> new ArrayList<>());
    this.eventData.get(caller).add(eventData);
  }

  /**
   * Calls all the registered event-invoked methods associated with the posted event.
   *
   * @param event event that is being posted
   */
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
