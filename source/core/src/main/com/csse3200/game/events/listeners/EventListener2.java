package com.csse3200.game.events.listeners;

/**
 * An event listener with 2 arguments
 */
@FunctionalInterface
public interface EventListener2<T0, T1> extends EventListener {
  void handle(T0 arg0, T1 arg1);
}

