package com.csse3200.game.events;

import com.badlogic.gdx.utils.Array;
import com.csse3200.game.events.listeners.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Send and receive events between objects. EventHandler provides an implementation of the Observer
 * pattern, also known as an event system or publish/subscribe. When an event is triggered with
 * trigger(), all listeners are notified of the event.
 *
 * <p>Currently supports up to 3 arguments for an event. More can be added, but consider instead
 * passing a class with required fields.
 *
 * <p>If you get a ClassCastException from an event, trigger is being called with different
 * arguments than the listeners expect.
 */
public class EventHandler {
  private static final Logger logger = LoggerFactory.getLogger(EventHandler.class);
  Map<String, Array<EventListener>> listeners;

  public EventHandler() {
    // Assume no events by default, which will be the case for most entities
    listeners = new HashMap<>(0);
  }

  /**
   * Add a listener to an event with zero arguments
   *
   * @param eventName name of the event
   * @param listener function to call when event fires
   */
  public void addListener(String eventName, EventListener0 listener) {
    registerListener(eventName, listener);
  }

  /**
   * Add a listener to an event with one argument
   *
   * @param eventName name of the event
   * @param listener function to call when event fires
   * @param <T> argument type
   */
  public <T> void addListener(String eventName, EventListener1<T> listener) {
    registerListener(eventName, listener);
  }

  /**
   * Add a listener to an event with two arguments
   *
   * @param eventName name of the event
   * @param listener function to call when event fires
   * @param <T0> Type of arg 0
   * @param <T1> Type of arg 1
   */
  public <T0, T1> void addListener(String eventName, EventListener2<T0, T1> listener) {
    registerListener(eventName, listener);
  }

  /**
   * Add a listener to an event with three arguments
   *
   * @param eventName name of the event
   * @param listener function to call when event fires
   * @param <T0> Type of arg 0
   * @param <T1> Type of arg 1
   * @param <T2> Type of arg 2
   */
  public <T0, T1, T2> void addListener(String eventName, EventListener3<T0, T1, T2> listener) {
    registerListener(eventName, listener);
  }

  /**
   * Trigger an event with no arguments
   *
   * @param eventName name of the event
   */
  public void trigger(String eventName) {
    logTrigger(eventName);
    forEachListener(eventName, (EventListener listener) -> ((EventListener0) listener).handle());
  }

  /**
   * Trigger an event with one argument
   *
   * @param eventName name of the event
   * @param arg0 arg to pass to event
   * @param <T> argument type
   */
  @SuppressWarnings("unchecked")
  public <T> void trigger(String eventName, T arg0) {
    logTrigger(eventName);
    forEachListener(
        eventName, (EventListener listener) -> ((EventListener1<T>) listener).handle(arg0));
  }

  /**
   * Trigger an event with one argument
   *
   * @param eventName name of the event
   * @param arg0 arg 0 to pass to event
   * @param arg1 arg 1 to pass to event
   * @param <T0> Type of arg 0
   * @param <T1> Type of arg 1
   */
  @SuppressWarnings("unchecked")
  public <T0, T1> void trigger(String eventName, T0 arg0, T1 arg1) {
    logTrigger(eventName);
    forEachListener(
        eventName,
        (EventListener listener) -> ((EventListener2<T0, T1>) listener).handle(arg0, arg1));
  }

  /**
   * Trigger an event with one argument
   *
   * @param eventName name of the event
   * @param arg0 arg 0 to pass to event
   * @param arg1 arg 1 to pass to event
   * @param arg2 arg 2 to pass to event
   * @param <T0> Type of arg 0
   * @param <T1> Type of arg 1
   * @param <T2> Type of arg 2
   */
  @SuppressWarnings("unchecked")
  public <T0, T1, T2> void trigger(String eventName, T0 arg0, T1 arg1, T2 arg2) {
    logTrigger(eventName);
    forEachListener(
        eventName,
        (EventListener listener) ->
            ((EventListener3<T0, T1, T2>) listener).handle(arg0, arg1, arg2));
  }

  private void registerListener(String eventName, EventListener listener) {
    logger.debug("Adding listener {} to event {}", listener, eventName);
    Array<EventListener> eventListeners = listeners.getOrDefault(eventName, null);
    if (eventListeners == null) {
      eventListeners = new Array<>(1);
      listeners.put(eventName, eventListeners);
    }
    eventListeners.add(listener);
  }

  private void forEachListener(String eventName, Consumer<EventListener> func) {
    Array<EventListener> eventListeners = listeners.getOrDefault(eventName, null);
    if (eventListeners != null) {
      eventListeners.forEach(func);
    }
  }

  private static void logTrigger(String eventName) {
    logger.debug("Triggering event {}", eventName);
  }
}
