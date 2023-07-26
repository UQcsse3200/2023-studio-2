package com.csse3200.game.events.listeners;

/**
 * An event listener with 1 argument
 */
@FunctionalInterface
public interface EventListener1<T> extends EventListener {
  void handle(T arg);
}
