package com.csse3200.game.services;

import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.mapConfig.ConfigLoader;
import com.csse3200.game.areas.mapConfig.InvalidConfigException;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.utils.LoadUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
public class PlanetTravelTest {
    PlanetTravel planetTravel;
    GdxGame game;
    String name;

    @BeforeEach
    public void setUp() throws InvalidConfigException {
        game = mock(GdxGame.class);
        ServiceLocator.registerGameStateObserverService(new GameStateObserver(new GameStateInteraction()));
        name = LoadUtils.formatName("Earth");
        ServiceLocator.getGameStateObserverService().trigger("updatePlanet", "currentPlanet", name);
        ServiceLocator.getGameStateObserverService().trigger("updatePlanet", "nextPlanet", ConfigLoader.loadLevel(name).nextPlanet);
        planetTravel = new PlanetTravel(game);
    }

    @AfterEach
    public void cleanUp(){
        ServiceLocator.clear();
    }

    @Test
    void testServiceSetup() {
        assertNotNull(ServiceLocator.getGameStateObserverService(), "Services not correctly loaded");
    }

    @Test
    void testFullTravel(){
        planetTravel.beginFullTravel();
        verify(game).setScreen(GdxGame.ScreenType.SPACEMINI_SCREEN);
    }

    @Test
    void testInstantTravel() throws InvalidConfigException {
        planetTravel.beginInstantTravel();
        String nextPlanetName = ConfigLoader.loadLevel(LoadUtils.formatName(name)).nextPlanet;
        assertEquals(nextPlanetName, ServiceLocator.getGameStateObserverService().getStateData("currentPlanet"), "new planet Differs than expected");
    }

    @Test
    void testReturnToCurrent(){
        EntityService entityService = mock(EntityService.class);
        ServiceLocator.registerEntityService(entityService);
        when(entityService.getPlayer()).thenReturn(new Entity().addComponent(new CombatStatsComponent(1,1,1,1,false)));
        when(entityService.getCompanion()).thenReturn(new Entity().addComponent(new CombatStatsComponent(1,1,1,1,false)));
        planetTravel.returnToCurrent();
        assertEquals(name, ServiceLocator.getGameStateObserverService().getStateData("currentPlanet"), "new planet Differs than expected");
    }

    @Test
    void testPlanetTravel() {
        planetTravel.moveToNextPlanet("Earth");
        assertEquals("Earth", planetTravel.returnCurrent(), "The state data should match the set data.");
        //Skip the minigame because we are responsible for testing it.
        planetTravel.moveToNextPlanet("Mars");
        assertEquals("Mars", planetTravel.returnCurrent(), "The state data should match the set data.");
    }
}
