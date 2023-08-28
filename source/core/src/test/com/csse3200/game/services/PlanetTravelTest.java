package com.csse3200.game.services;



import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

public class PlanetTravelTest {
    private GameState gameState;
    private PlanetTravel planetTravel;

    @BeforeEach
    public void setUp() {
        gameState = new GameState();
        planetTravel = new PlanetTravel(gameState);  // Passing the same gameState
    }

    @Test
    public void PlanetTravelTest(){
        gameState.put("planet", "Earth");  // Modifying the same gameState that planetTravel has
        assertEquals("Earth", planetTravel.returnCurrent(), "The state data should match the set data.");

        planetTravel.moveToNextPlanet("Mars");
        assertEquals("Mars", gameState.get("planet"), "The state data should match the set data.");
    }
}
