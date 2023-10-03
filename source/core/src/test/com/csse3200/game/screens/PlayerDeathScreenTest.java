package com.csse3200.game.screens;

import com.csse3200.game.GdxGame;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import org.junit.jupiter.api.Test;

import java.util.Timer;

import static com.csse3200.game.GdxGame.ScreenType.PLAYER_DEATH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.csse3200.game.GdxGame.ScreenType.*;
import static org.mockito.Mockito.mock;
/**
 * Unit tests for the DeathScreen class.
 */
class PlayerDeathScreenTest {

    /**
     * Tests whether the game displays the coorect death screen after a player dies (health reaches zero)
     * when the player has three lives remaining.
      */
    @Test
    void testDeathBackgroundThreeLives() {
        GdxGame game = mock(GdxGame.class);
        Entity player = new Entity();
        player.setEntityType("player");
        player.addComponent(new CombatStatsComponent(0, 10, 10, false, 3));
        final Timer timer = new Timer();
        java.util.TimerTask testScreen = new java.util.TimerTask() {
            @Override
            public void run() {
                assertEquals(PLAYER_DEATH_3, game.getScreenType());
                timer.cancel();
                timer.purge();
            }
        };
        timer.schedule(testScreen, 4000);
    }

    /**
     * Tests whether the game displays the correct death screen after a player dies (health reaches zero)
     * when the player has two lives remaining.
     */
    @Test
    void testDeathBackgroundTwoLives() {
        GdxGame game = mock(GdxGame.class);
        Entity player = new Entity();
        player.setEntityType("player");
        player.addComponent(new CombatStatsComponent(0, 10, 10, false, 2));
        final Timer timer = new Timer();
        java.util.TimerTask testScreen = new java.util.TimerTask() {
            @Override
            public void run() {
                assertEquals(PLAYER_DEATH_2, game.getScreenType());
                timer.cancel();
                timer.purge();
            }
        };
        timer.schedule(testScreen, 4000);
    }

    /**
     * Tests whether the game displays the correct death screen after a player dies (health reaches zero)
     * when the player has one life remaining.
     */
    @Test
    void testDeathBackgroundOneLife() {
        GdxGame game = mock(GdxGame.class);
        Entity player = new Entity();
        player.setEntityType("player");
        player.addComponent(new CombatStatsComponent(0, 10, 10, false, 1));
        final Timer timer = new Timer();
        java.util.TimerTask testScreen = new java.util.TimerTask() {
            @Override
            public void run() {
                assertEquals(PLAYER_DEATH_1, game.getScreenType());
                timer.cancel();
                timer.purge();
            }
        };
        timer.schedule(testScreen, 4000);
    }

    /**
     * Tests whether the game displays the correct death screen after a player dies (health reaches zero)
     * when the player has zero lives remaining.
     */
    @Test
    void testDeathBackgroundZeroLives() {
        GdxGame game = mock(GdxGame.class);
        Entity player = new Entity();
        player.setEntityType("player");
        player.addComponent(new CombatStatsComponent(0, 10, 10, false, 0));
        final Timer timer = new Timer();
        java.util.TimerTask testScreen = new java.util.TimerTask() {
            @Override
            public void run() {
                assertEquals(PLAYER_DEATH_0, game.getScreenType());
                timer.cancel();
                timer.purge();
            }
        };
        timer.schedule(testScreen, 4000);
    }
}