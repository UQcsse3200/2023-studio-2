package com.csse3200.game.services;

import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

@ExtendWith(GameExtension.class)
class GameStateInteractionTest {

    GameState gameState;
    GameStateInteraction stateInteraction;

    @BeforeEach
    void setUp(){
        gameState = new GameState();
        stateInteraction = new GameStateInteraction(gameState);
    }

    @Test
    void testPutData() {
        String key1 = "testKey1";
        int value1 = 100;
        String key2 = "testKey2";
        String value2 = "testValue";

        stateInteraction.put(key1, value1);
        stateInteraction.put(key2, value2);

        assertEquals(gameState.get(key1), value1);
        assertEquals(gameState.get(key2), value2);
    }

    @Test
    void testGetData() {
        String key1 = "testKey1";
        int value1 = 100;
        String key2 = "testKey2";
        String value2 = "testValue";

        gameState.put(key1, value1);
        gameState.put(key2, value2);

        assertEquals(stateInteraction.get(key1), value1);
        assertEquals(stateInteraction.get(key2), value2);
        assertNull(stateInteraction.get("Not found key"));
    }

    @Test
    void testGetAllStateData() {
        String key1 = "testKey1";
        int value1 = 100;
        String key2 = "testKey2";
        String value2 = "testValue";
        gameState.put(key1, value1);
        gameState.put(key2, value2);

        Map<String, Object> map = new HashMap<>();
        map.put(key1, value1);
        map.put(key2, value2);

        assertEquals(stateInteraction.getStateData(), map);
    }

    @Test
    void testUpdateResource() {
        String resourceName = "testResource1";
        int startAmount = 1000;
        int changeAmount = 500;

        stateInteraction.updateResource(resourceName, startAmount);
        assertEquals(gameState.get("resource/" + resourceName), startAmount);

        stateInteraction.updateResource(resourceName, changeAmount);
        assertEquals(gameState.get("resource/" + resourceName), startAmount + changeAmount);
    }
}
