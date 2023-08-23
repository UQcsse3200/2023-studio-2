package com.csse3200.game.services;

import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link GameState} class.
 */
@ExtendWith(GameExtension.class)
public class GameStateTest {
    private GameState gameState;

    /**
     * Sets up the test environment by creating a new instance of {@link GameState}.
     */
    @BeforeEach
    public void setUp() {
        gameState = new GameState();
    }

    /**
     * Tests the {@link GameState#setStateData(Map)} method by setting state data and comparing it to the expected data.
     */
    @Test
    public void testSetStateData() {
        Map<String, Object> newData = new HashMap<>();
        newData.put("planet", "Mars");
        gameState.setStateData(newData);

        assertEquals(newData, gameState.getStateData(), "The state data should match the set data.");
    }

    /**
     * Tests the notification mechanism for state change listeners when the state data is updated.
     * Registers a listener, sets new state data, and verifies that the listener is notified and receives the updated data.
     */
    @Test
    public void testStateChangeListenerNotification() {
        TestStateChangeListener listener = new TestStateChangeListener();
        gameState.registerStateChangeListener(listener);

        Map<String, Object> newData = new HashMap<>();
        newData.put("planet", "Mars");
        gameState.setStateData(newData);

        assertTrue(listener.isNotified(), "The listener should be notified.");
        assertEquals(newData, listener.getLastState(), "The listener should receive the updated state data.");
    }

    /**
     * Tests the behavior of having multiple state change listeners.
     * Registers two listeners, updates the state data, and verifies that both listeners are notified and receive the updated data.
     */
    private static class TestStateChangeListener implements GameState.StateChangeListener {
        private boolean notified = false;
        private Map<String, Object> lastState = null;

        @Override
        public void onStateChange(Map<String, Object> newStateData) {
            notified = true;
            lastState = newStateData;
        }

        public boolean isNotified() {
            return notified;
        }

        public Map<String, Object> getLastState() {
            return lastState;
        }
    }

    /**
     * Tests the behavior of unregistering a state change listener.
     * Registers a listener, unregisters it, updates the state data, and verifies that the unregistered listener is not notified.
     */
    @Test
    public void testMultipleStateChangeListeners() {
        TestStateChangeListener listener1 = new TestStateChangeListener();
        TestStateChangeListener listener2 = new TestStateChangeListener();

        gameState.registerStateChangeListener(listener1);
        gameState.registerStateChangeListener(listener2);

        Map<String, Object> newData = new HashMap<>();
        newData.put("lives", 3);
        gameState.setStateData(newData);

        assertTrue(listener1.isNotified(), "The first listener should be notified.");
        assertEquals(newData, listener1.getLastState(), "The first listener should receive the updated state data.");

        assertTrue(listener2.isNotified(), "The second listener should be notified.");
        assertEquals(newData, listener2.getLastState(), "The second listener should receive the updated state data.");
    }

    /**
     * A mock implementation of the {@link GameState.StateChangeListener} interface for testing purposes.
     */
    @Test
    public void testUnregisterStateChangeListener() {
        TestStateChangeListener listener = new TestStateChangeListener();
        gameState.registerStateChangeListener(listener);
        gameState.unregisterStateChangeListener(listener);

        Map<String, Object> newData = new HashMap<>();
        newData.put("planet", "mars");
        gameState.setStateData(newData);

        assertFalse(listener.isNotified(), "The unregistered listener should not be notified.");
    }

}
