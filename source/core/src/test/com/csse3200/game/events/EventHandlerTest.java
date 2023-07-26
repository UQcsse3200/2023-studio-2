package com.csse3200.game.events;

import com.csse3200.game.events.listeners.EventListener0;
import com.csse3200.game.events.listeners.EventListener1;
import com.csse3200.game.events.listeners.EventListener2;
import com.csse3200.game.events.listeners.EventListener3;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class EventHandlerTest {
  EventHandler handler;

  @BeforeEach
  void beforeEach() {
    handler = new EventHandler();
  }

  @Test
  void shouldTriggerEvent() {
    EventListener0 listener = mock(EventListener0.class);
    handler.addListener("event", listener);
    handler.trigger("event");
    verify(listener).handle();
  }

  @Test
  void shouldTriggerMultiple() {
    EventListener0 listener = mock(EventListener0.class);
    EventListener0 listener2 = mock(EventListener0.class);
    handler.addListener("event", listener);
    handler.addListener("event", listener2);
    handler.trigger("event");
    verify(listener).handle();
    verify(listener2).handle();
  }

  @Test
  void shouldTriggerCorrectEvent() {
    EventListener0 listener = mock(EventListener0.class);
    EventListener0 listener2 = mock(EventListener0.class);
    handler.addListener("event", listener);
    handler.addListener("event2", listener2);

    handler.trigger("event2");
    verify(listener2).handle();
    verify(listener, times(0)).handle();

    handler.trigger("event");
    verify(listener).handle();
    verifyNoMoreInteractions(listener2);
  }

  @Test
  void shouldHandleNoListeners() {
    handler.trigger("not-real-event");
  }

  @Test
  void shouldTriggerOneArg() {
    EventListener1<String> listener = (EventListener1<String>)mock(EventListener1.class);
    handler.addListener("event", listener);
    handler.trigger("event", "argument");
    verify(listener).handle("argument");
  }

  @Test
  void shouldTriggerTwoArg() {
    EventListener2<Integer, Boolean> listener = (EventListener2<Integer, Boolean>)mock(EventListener2.class);
    handler.addListener("event", listener);
    handler.trigger("event", 5, true);
    verify(listener).handle(5, true);
  }

  @Test
  void shouldTriggerThreeArg() {
    EventListener3<Integer, Float, Long> listener = (EventListener3<Integer, Float, Long>)mock(EventListener3.class);
    handler.addListener("event", listener);
    handler.trigger("event", 1, 2f, 3L);
    verify(listener).handle(1, 2f, 3L);
  }

  @Test
  void shouldFailIncorrectArgs() {
    handler.addListener("stringEvent", (String s) -> {});
    assertThrows(ClassCastException.class, () -> {
      handler.trigger("stringEvent", true);
    });
  }
}