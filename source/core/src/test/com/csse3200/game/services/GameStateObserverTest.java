package com.csse3200.game.services;

import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.atomic.AtomicInteger;

@ExtendWith(GameExtension.class)
class GameStateObserverTest {

    GameStateObserver stateObserver;
    GameStateInteraction stateInteraction;

    @BeforeEach
    void setUp() {
        stateInteraction = new GameStateInteraction();
        stateObserver = new GameStateObserver(stateInteraction);
    }

    @Test
    void testAddListener() {
        int setValue = 100;

        AtomicInteger value = new AtomicInteger();
        stateObserver.addListener("testListener1", (amount) -> value.set((Integer) amount));

        stateObserver.trigger("testListener1", setValue);

        assertEquals(setValue, value.get());
    }

    @Test
    void testTriggerCallback() {
        String name = "testResource1";
        int amount = 100;

        stateObserver.trigger("resourceAdd", name, amount);

        assertEquals(stateInteraction.get("resource/" + name), amount);
    }

    @Test
    void testGetStateData() {
        String key1 = "testKey1";
        int value1 = 200;
        String key2 = "testKey2";
        String value2 = "testValue";

        stateInteraction.put(key1, value1);
        stateInteraction.put(key2, value2);

        assertEquals(stateObserver.getStateData(key1), value1);
        assertEquals(stateObserver.getStateData(key2), value2);
        assertNull(stateObserver.getStateData("Not found key value"));
    }
}
