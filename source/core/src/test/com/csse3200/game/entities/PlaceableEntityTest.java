package com.csse3200.game.entities;

import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.GameStateObserver;
import com.csse3200.game.services.ServiceLocator;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class PlaceableEntityTest {
    PlaceableEntity placeableEntity;

    @Mock
    private GameStateObserver stateObserver;

    @Test
    void placed() {
        placeableEntity.placed();

        verify(stateObserver).trigger("resourceAdd", "unobtanium", -10);
        verify(stateObserver).trigger("resourceAdd", "cobolt", -20);
    }

    @Test
    void removed() {
    }

    @Test
    void getCost() {
        var cost = new HashMap<String, Integer>();
        cost.put("unobtanium", 10);
        cost.put("cobolt", 20);

        assertEquals(cost, placeableEntity.getCost());
    }

    @BeforeEach
    void before() {
        ServiceLocator.registerGameStateObserverService(stateObserver);
        var cost = new HashMap<String, Integer>();
        cost.put("unobtanium", 10);
        cost.put("cobolt", 20);
        placeableEntity = new PlaceableEntity(cost);
    }

    @AfterEach
    void after() {
        ServiceLocator.clear();
    }
}