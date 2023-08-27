package com.csse3200.game.services;

import java.util.Map;

public class GameStateInteraction {

    private GameState gameState = new GameState();

    public void put(String key, Object value) {
        gameState.put(key, value);
    }

    public Object get(String key) {
        return gameState.get(key);
    }
//
    public Map<String, Object> getStateData() {
        return gameState.getStateData();
    }

    public void updateResource(String resourceKey, int changeAmount){
        Object value = gameState.get(resourceKey);
        int amount = value == null ? 0 : (int) value;
        gameState.put(resourceKey, amount + changeAmount);
    }


}


