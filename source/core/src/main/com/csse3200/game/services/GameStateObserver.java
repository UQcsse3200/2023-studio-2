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

    public Map<String, Object> getStateData() {
        return stateInteraction.getStateData();
    }

    private void generateStateListeners() {

        getEvents().addListener("resourceAddition", stateInteraction::updateResource);



    }


}
