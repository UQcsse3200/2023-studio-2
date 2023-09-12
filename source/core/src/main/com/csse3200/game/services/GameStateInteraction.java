package com.csse3200.game.services;

import java.util.Map;


/**
 * A utility interaction class to manage and modify game state
 */
public class GameStateInteraction {

    // Corresponding GameState to be managed by interactions
    private final GameState gameState;

    /**
     * Constructs a GameStateInteraction instance.
     * Initialises a GameState.
     */
    public GameStateInteraction() {
        this.gameState = new GameState();
    }

    /**
     * Constructs a GameStateInteraction instance.
     * Manages the given GameState.
     *
     * @param gameState The chosen GameState to modify and manage.
     */
    public GameStateInteraction(GameState gameState) {
        this.gameState = gameState;
    }

    /**
     * Updates or adds the given key-value pair in the game state data.
     *
     * @param key   The key to associate the value with.
     * @param value The corresponding value to save to the key.
     */
    public void put(String key, Object value) {
        gameState.put(key, value);
    }

    /**
     * Returns the value stored at the give key in the game state.
     *
     * @param key The key from which the value should be retrieved from.
     * @return The value stored at the given key, or null if key found.
     */
    public Object get(String key) {
        return gameState.get(key);
    }

    /**
     * Clears the game state data.
     */
    public void clear() {
        gameState.clear();
    }

    /**
     * Returns a copy of the current entire game state data.
     *
     * @return A map of game state data at the current moment.
     */
    public Map<String, Object> getStateData() {
        return gameState.getStateData();
    }

    /**
     * Increases the specified resource name by a given amount.
     * Retrieves the current amount stored and adds the given amount.
     * Note: the game state key is "resource/{resourceName}" not just the resource's name.
     *
     * @param resourceName The name of the resource to be saved.
     * @param changeAmount The amount the resource should increase by.
     */
    public void updateResource(String resourceName, int changeAmount) {
        String resourceKey = "resource/" + resourceName;
        Object value = gameState.get(resourceKey);
        int amount = value == null ? 0 : (int) value;
        value = this.get("resourceMax/" + resourceName);
        if (value != null) {
            int max = (int) value;
            if (amount + changeAmount > max) {
                changeAmount = max - amount;
            }
        }
        this.put(resourceKey, amount + changeAmount);
    }

    /**
     * Sets the maximum amount of a resource
     * Note: the game state key is "resourceMax/{resourceName}" not just the resource's name.
     *
     * @param resourceName The name of the resource produced by the extractor.
     * @param amount       The amount to cap the resource by
     */
    public void updateMaxResources(String resourceName, int amount) {
        String resourceKey = "resourceMax/" + resourceName;
        this.put(resourceKey, amount);
    }

    /**
     * Increases the specified extractor count by a given amount.
     * Retrieves the current amount stored and adds the given amount.
     * Note: the game state key is "extractors/{resourceName}" not just the resource's name.
     *
     * @param resourceName The name of the resource produced by the extractor.
     * @param changeAmount The amount the count should change by.
     */
    public void updateExtractors(String resourceName, int changeAmount) {
        String resourceKey = "extractors/" + resourceName;
        Object value = gameState.get(resourceKey);
        int amount = value == null ? 0 : (int) value;
        this.put(resourceKey, amount + changeAmount);
    }

    /**
     * Sets the maximum amount of extractors for a resource
     *
     * @param resourceName The name of the resource produced by the extractor.
     * @param amount       The amount to set the max to
     */
    public void updateMaxExtractors(String resourceName, int amount) {
        String resourceKey = "extractorsMax/" + resourceName;
        this.put(resourceKey, amount);
    }

    /**
     * Sets the total amount of tracked extractors of the resource name to amount
     *
     * @param resourceName The name of the resource produced by the extractor/s
     * @param amount       The amount of extractors producing the resource
     */
    public void updateTotalExtractors(String resourceName, int amount) {
        String resourceKey = "extractorsTotal/" + resourceName;
        this.put(resourceKey, amount);
    }
}


