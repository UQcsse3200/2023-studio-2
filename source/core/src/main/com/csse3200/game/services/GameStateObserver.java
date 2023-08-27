package com.csse3200.game.services;


import com.csse3200.game.events.EventHandler;


public class GameStateObserver {

    GameState gameState = new GameState();
    private final EventHandler eventHandler;

    public EventHandler getEvents() {
        return eventHandler;
    }


    public GameStateObserver() {
        eventHandler = new EventHandler();

    }





}
