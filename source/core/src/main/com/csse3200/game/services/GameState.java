package com.csse3200.game.services;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Represents the current state of the game and facilitates state transitions
 * while ensuring thread safety.
 */
public class GameState {

    // Mutex lock
    private final Object lock = new Object();

    // Holds the current state data
    private Map<String, Object> stateData = new HashMap<>();

    // Callback listeners for state changes
    private List<StateChangeListener> stateChangeListeners = new CopyOnWriteArrayList<>();

    /**
     * Initializes a new GameState instance.
     */
    public GameState() {
    }

    /**
     * Sets the new state data and triggers the state change callbacks.
     *
     * @param newData New state data to be set.
     */
    public void setStateData(Map<String, Object> newData) {
        synchronized (lock) {
            stateData = new HashMap<>(newData);
        }
        notifyStateChangeListeners(); // Inform about state change
    }

    /**
     * Retrieves a safe, immutable copy of the current state data.
     *
     * @return An immutable copy of the current state data.
     */
    public Map<String, Object> getStateData() {
        synchronized (lock) {
            return Collections.unmodifiableMap(new HashMap<>(stateData));
        }
    }

    /**
     * Registers a state change listener.
     *
     * @param listener The listener to be registered.
     */
    public void registerStateChangeListener(StateChangeListener listener) {
        stateChangeListeners.add(listener);
    }

    /**
     * Unregisters a state change listener.
     *
     * @param listener The listener to be unregistered.
     */
    public void unregisterStateChangeListener(StateChangeListener listener) {
        stateChangeListeners.remove(listener);
    }

    private void notifyStateChangeListeners() {
        // No need for synchronization here, using CopyOnWriteArrayList
        Map<String, Object> stateCopy;
        synchronized (lock) {
            stateCopy = new HashMap<>(stateData);
        }
        for (StateChangeListener listener : stateChangeListeners) {
            listener.onStateChange(stateCopy);
        }
    }

    /**
     * Callback interface to be implemented by classes interested in
     * receiving notifications about state changes.
     */
    public interface StateChangeListener {

        /**
         * Callback method triggered when the state changes.
         *
         * @param newStateData The new state data after the change.
         */
        void onStateChange(Map<String, Object> newStateData);
    }
}
