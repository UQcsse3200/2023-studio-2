package com.csse3200.game.services;


import com.csse3200.game.events.EventHandler;

import java.util.Map;


public class GameStateObserver {

    private final GameStateInteraction stateInteraction;
    private final EventHandler eventHandler;

    public GameStateObserver() {
        eventHandler = new EventHandler();
        stateInteraction =  new GameStateInteraction();
    }

    public EventHandler getEvents() {
        return eventHandler;
    }


    public Map<String, Object> getStateData() {

        Map<String, Object> clone = stateInteraction.getStateData();
        System.out.println(clone);
        clone.put("Test", 1);

        stateInteraction.put("Actual", 10);
        System.out.println(clone);

        System.out.println(stateInteraction.getStateData());


        return stateInteraction.getStateData();
    }



}
