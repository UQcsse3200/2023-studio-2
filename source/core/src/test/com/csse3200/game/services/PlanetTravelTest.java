package com.csse3200.game.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Plane;
import com.csse3200.game.GdxGame;
import com.csse3200.game.input.InputFactory;
import com.csse3200.game.input.InputService;
import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PlanetTravelTest {
    PlanetTravel planetTravel;
    GameStateObserver stateObserver;
    GdxGame mockGame;
    GameStateInteraction stateInteraction;
    @BeforeEach
    public void setUp() {
        InputFactory inputFactoryMock = mock(InputFactory.class);
        Input inputMock = mock(Input.class);
        Gdx.input = inputMock;
        mockGame = mock(GdxGame.class);
        ServiceLocator.registerGameStateObserverService(new GameStateObserver());
        planetTravel = new PlanetTravel(mockGame);
    }

    @AfterEach
    public void Final(){
        ServiceLocator.clear();
    }

    @Test
    public void PlanetTravelTest() {
        planetTravel.moveToNextPlanet("Earth");
        assertEquals("Earth", planetTravel.returnCurrent(), "The state data should match the set data.");
        //Skip the minigame because we are responsible for testing it.
        planetTravel.moveToNextPlanet("Mars");
        assertEquals("Mars", planetTravel.returnCurrent(), "The state data should match the set data.");
    }
}
