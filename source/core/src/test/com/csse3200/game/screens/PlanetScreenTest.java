package com.csse3200.game.screens;


import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.mapConfig.ConfigLoader;
import com.csse3200.game.areas.mapConfig.InvalidConfigException;
import com.csse3200.game.areas.mapConfig.LevelConfig;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.utils.LoadUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
public class PlanetScreenTest {
    GdxGame game;
    PlanetScreen screen;

    @BeforeEach
    void setup(){
        game = mock(GdxGame.class);
        screen = new PlanetScreen(game, "Earth");
    }

    @Test
    void testConstructionName(){
        String name = "Earth";
        screen = new PlanetScreen(game, name);
        assertEquals(name, screen.name, "Name of planet is different than expected " + name);
    }

    @Test
    void testNextPlanet() throws InvalidConfigException {
        String name = "Earth";
        LevelConfig levelConfig = ConfigLoader.loadLevel(LoadUtils.formatName(name));
        screen = new PlanetScreen(game, name);
        assertEquals(levelConfig.nextPlanet, screen.getNextPlanetName(), "Next planet differs");
    }

    @Test
    void testStartGameArea(){
        assertEquals("main_area", screen.getCurentAreaName(), "Starting Game Area differs from expected");
    }

    @Test
    void testSetInvalidGameArea(){
        assertFalse(screen.setCurrentArea("Invalid Name"));
    }
}
