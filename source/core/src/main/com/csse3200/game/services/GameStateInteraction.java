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
     * @param key   The key from which the value should be retrieved from.
     * @return      The value stored at the given key, or null if key found.
     */
    public Object get(String key) {
        return gameState.get(key);
    }

    /**
     * Returns a copy of the current entire game state data.
     *
     * @return  A map of game state data at the current moment.
     */
    public Map<String, Object> getStateData() {
        return gameState.getStateData();
    }

    /**
     * Increases the specified resource name by a given amount.
     * Retrieves the current amount stored and adds the given amount.
     * Note: the game state key is "resource/{resourceName}" not just the resource's name.
     *
     * @param resourceName  The name of the resource to be saved.
     * @param changeAmount  The amount the resource should increase by.
     */
    public void updateResource(String resourceName, int changeAmount){
        String resourceKey = "resource/" + resourceName;
        Object value = gameState.get(resourceKey);
        int amount = value == null ? 0 : (int) value;
        this.put(resourceKey, amount + changeAmount);
    }
}


