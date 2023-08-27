package com.csse3200.game.services;


import com.csse3200.game.events.EventHandler;

import java.util.Map;


public class GameStateObserver extends EventHandler{

    private final GameStateInteraction stateInteraction;

    public GameStateObserver() {
        stateInteraction =  new GameStateInteraction();
        this.generateStateListeners();
    }

    // Testing method to get full state data
    public Map<String, Object> getFullStateData() {
        return stateInteraction.getStateData();
    }

    public Object getStateData(String key) {
        return stateInteraction.get(key);
    }

    private void generateStateListeners() {
        this.addListener("resourceAdd", stateInteraction::updateResource);
    }
}
