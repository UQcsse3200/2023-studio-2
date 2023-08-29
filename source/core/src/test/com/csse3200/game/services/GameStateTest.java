package com.csse3200.game.services;

import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
class GameStateTest {
    private GameState gameState;

    @BeforeEach
    void setUp() {
        gameState = new GameState();
    }

    @Test

    void testPutAndGet() {

        gameState.put("planet", "Mars");

        assertEquals("Mars", gameState.get("planet"), "The state data should match the set data.");
    }

    @Test
    void testStateChangeListenerNotification() {
        TestStateChangeListener listener = new TestStateChangeListener();
        gameState.registerStateChangeListener(listener);

        gameState.put("planet", "Mars");

        assertTrue(listener.isNotified(), "The listener should be notified.");
        assertEquals("Mars", listener.getLastState().get("planet"), "The listener should receive the updated state data.");
    }

    @Test
    void testMultipleStateChangeListeners() {
        TestStateChangeListener listener1 = new TestStateChangeListener();
        TestStateChangeListener listener2 = new TestStateChangeListener();

        gameState.registerStateChangeListener(listener1);
        gameState.registerStateChangeListener(listener2);

        gameState.put("lives", 3);

        assertTrue(listener1.isNotified(), "The first listener should be notified.");
        assertEquals(3, listener1.getLastState().get("lives"), "The first listener should receive the updated state data.");

        assertTrue(listener2.isNotified(), "The second listener should be notified.");
        assertEquals(3, listener2.getLastState().get("lives"), "The second listener should receive the updated state data.");
    }

    @Test
    void testUnregisterStateChangeListener() {
        TestStateChangeListener listener = new TestStateChangeListener();
        gameState.registerStateChangeListener(listener);
        gameState.unregisterStateChangeListener(listener);

        gameState.put("planet", "mars");

        assertFalse(listener.isNotified(), "The unregistered listener should not be notified.");
    }

    private static class TestStateChangeListener implements GameState.StateChangeListener {
        private boolean notified = false;
        private ConcurrentHashMap<String, Object> lastState = null;

        @Override
        public void onStateChange(ConcurrentHashMap<String, Object> newStateData) {
            notified = true;
            lastState = newStateData;
        }

        public boolean isNotified() {
            return notified;
        }

        public ConcurrentHashMap<String, Object> getLastState() {
            return lastState;
        }
    }
}