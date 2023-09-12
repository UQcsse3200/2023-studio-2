package com.csse3200.game.events.listeners;

/**
 * An event listener with 3 arguments
 */
@FunctionalInterface
public interface EventListener3<T0, T1, T2> extends EventListener {
  void handle(T0 arg0, T1 arg1, T2 arg2);
}

