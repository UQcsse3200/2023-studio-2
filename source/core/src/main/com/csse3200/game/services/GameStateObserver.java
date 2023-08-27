package com.csse3200.game.services;


import com.csse3200.game.events.EventHandler;

import java.util.Map;


public class GameStateObserver {

    private final GameStateInteraction stateInteraction;
    private final EventHandler eventHandler;

    public GameStateObserver() {
        eventHandler = new EventHandler();
        stateInteraction =  new GameStateInteraction();
        this.generateStateListeners();
    }

    public EventHandler getEvents() {
        return eventHandler;
    }

    // Testing methods to get data from state
    public Map<String, Object> getStateData() {
        return stateInteraction.getStateData();
    }
    public Object getData(String key) {
        return stateInteraction.get(key);
    }

    private void generateStateListeners() {
        getEvents().addListener("resourceAdd", stateInteraction::updateResource);
    }


}
