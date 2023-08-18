package com.csse3200.game.services;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the current state of the game and facilitates state transitions
 * while ensuring thread safety.
 */
public class GameState {

    // Mutex lock
    private final Object lock = new Object();

    // Holds the current state data
    private Map<String, Object> stateData = new HashMap<>();

    // Callback listener for state changes
    private StateChangeListener stateChangeListener;

    /**
     * Initializes a new GameState instance with a state change listener.
     *
     * @param stateChangeListener Listener to be informed of state changes.
     */
    public GameState(StateChangeListener stateChangeListener) {
        this.stateChangeListener = stateChangeListener;
    }

    /**
     * Sets the new state data and triggers the state change callback.
     *
     * @param newData New state data to be set.
     */
    public void setStateData(Map<String, Object> newData) {
        synchronized (lock) {
            stateData = new HashMap<>(newData);
        }
        stateChangeListener.onStateChange(this); // Inform about state change
    }

    /**
     * Retrieves a safe copy of the current state data.
     *
     * @return A copy of the current state data.
     */
    public Map<String, Object> getStateData() {
        synchronized (lock) {
            return new HashMap<>(stateData);
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
         * @param newState The new state after the change.
         */
        void onStateChange(GameState newState);
    }
}

