package com.csse3200.game.services;

import java.util.Map;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Represents the current state of the game whilst ensuring thread safety.
 */
public class GameState {

    // Holds the current state's information.
    private final ConcurrentHashMap<String, Object> stateData = new ConcurrentHashMap<>();


    // Callback list of state changes' listeners.
    private final CopyOnWriteArrayList<StateChangeListener> stateChangeListeners = new CopyOnWriteArrayList<>();

    /**
     * Add or update the state data and trigger the state change callbacks.
     *
     * @param key The key of the state to be set.
     * @param newValue The new value to be set.
     */
    public void put(String key, Object newValue) {
        stateData.put(key, newValue);
        notifyStateChangeListeners();
    }

    /**
     * Retrieves the value corresponding to the input key from the current state data.
     *
     * @param key The key of the data to be fetched.
     * @return The data corresponding to the provided key.
     */
    public Object get(String key) {
        return stateData.get(key);
    }

    /**
     * Returns a copy of the current game state data
     *
     * @return The copy of current state data
     */
    public Map<String, Object> getStateData() {
        return new ConcurrentHashMap<>(stateData);
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
        for (StateChangeListener listener : stateChangeListeners) {
            listener.onStateChange(stateData);
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
        void onStateChange(ConcurrentHashMap<String, Object> newStateData);
    }
}