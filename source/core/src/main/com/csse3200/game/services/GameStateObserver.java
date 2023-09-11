package com.csse3200.game.services;

import com.csse3200.game.events.EventHandler;
import java.util.Map;

/**
 * The global game state observer class (EventHandler) that allows components
 * to interface with the stored game state data using event calls and triggers.
 */
public class GameStateObserver extends EventHandler {

    // The instance to define game state interaction. Preserved across Observers unless reset.
    private static GameStateInteraction stateInteraction = new GameStateInteraction();

    /**
     * Constructs the GameStateObserver instance.
     * Registers event listeners with Observer.
     */
    public GameStateObserver() {
        generateStateListeners();
    }

    /**
     * Constructs the GameStateObserver instance.
     * Overwrites state interactor with given gameStateInteractor and registers event listeners.
     *
     * @param gameStateInteractor  The chosen GameStateInteraction for Observers to interface with.
     */
    public  GameStateObserver(GameStateInteraction gameStateInteractor) {
        stateInteraction = gameStateInteractor;
        this.generateStateListeners();
    }

    /**
     * Generates the listeners for specific game state modification events.
     * Associates the chosen event name to listen for with corresponding state interaction method.
     */
    private void generateStateListeners() {
        this.addListener("resourceAdd", stateInteraction::updateResource);
        this.addListener("updatePlanet", stateInteraction::put);
        this.addListener("extractorsAdd", stateInteraction::updateExtractors);
        this.addListener("extractorsTotal", stateInteraction::updateTotalExtractors);
        this.addListener("resourceMax", stateInteraction::updateMaxResources);
        this.addListener("extractorsMax", stateInteraction::updateMaxExtractors);
        this.addListener("updatePlayer", stateInteraction::put);
    }

    /**
     * Returns a copy of the entire game state at the current moment.
     * @return  A map of the game state data.
     */
    public Map<String, Object> getFullStateData() {
        return stateInteraction.getStateData();
    }

    /**
     * Returns the value corresponding to the given key in the game state data.
     *
     * @param key   The key for which the value should be retrieved from
     * @return      The value at the given key, or null if key doesn't exist.
     */
    public Object getStateData(String key) {
        return stateInteraction.get(key);
    }
}
