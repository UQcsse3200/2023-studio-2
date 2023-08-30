package com.csse3200.game.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.csse3200.game.GdxGame;
import com.csse3200.game.input.InputFactory;
import com.csse3200.game.input.InputService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PlanetTravelTest {
    private GameState gameState;
    private PlanetTravel planetTravel;
    private GdxGame mockGame; // Mock GdxGame

    @BeforeEach
    public void setUp() {
        InputFactory inputFactoryMock = mock(InputFactory.class);
        Input inputMock = mock(Input.class);
        Gdx.input = inputMock;
        mockGame = mock(GdxGame.class);
        InputService inputService = new InputService(inputFactoryMock);
        gameState = new GameState();
        planetTravel = new PlanetTravel(gameState, mockGame);  // Modify constructor to accept GdxGame as a parameter
    }

    @Test
    public void PlanetTravelTest() {
        gameState.put("planet", "Earth");  // Modifying the same gameState that planetTravel has
        assertEquals("Earth", planetTravel.returnCurrent(), "The state data should match the set data.");
        //Skip the minigame because we are responsible for testing it.
        doNothing().when(mockGame).setScreen(any(GdxGame.ScreenType.class)); // Stub the setScreen method
        planetTravel.moveToNextPlanet("Mars");
        assertEquals("Mars", gameState.get("planet"), "The state data should match the set data.");
    }
}
