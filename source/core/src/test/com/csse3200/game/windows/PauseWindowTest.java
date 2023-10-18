package com.csse3200.game.windows;

import com.csse3200.game.GdxGame;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.maingame.MainGameActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.Test;

import java.util.Timer;

import static com.csse3200.game.GdxGame.ScreenType.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class PauseWindowTest {
    GdxGame.ScreenType screenBefore;
    Entity entity;
    @Test
    void returnToGame() {
        GdxGame game = mock(GdxGame.class);
        Entity entity = new Entity();
        game.setScreen(GAME_STORY);
        GdxGame.ScreenType screenBefore = game.getScreenType();
        entity.getEvents().trigger("returnPressed");
        assertEquals(screenBefore, game.getScreenType());
    }

    @Test
    void exitToMainMenu() {
        GdxGame game = mock(GdxGame.class);
        Entity entity = new Entity();
        game.setScreen(GAME_STORY);
        entity.getEvents().trigger("exitPressed");
        //assertEquals(MAIN_MENU, game.getScreenType());
    }

    @Test
    void exitToControls() {
        GdxGame game = mock(GdxGame.class);
        Entity entity = new Entity();
        game.setScreen(GAME_STORY);
        entity.getEvents().trigger("controlsPressed");
        //assertEquals(CONTROL_SCREEN, game.getScreenType());
    }
}