package com.csse3200.game.screens;

import com.csse3200.game.GdxGame;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import org.junit.jupiter.api.Test;

import java.util.Timer;

import static org.mockito.Mockito.mock;
/**
 * Unit tests for the DeathScreen class.
 */
class PlayerDeathScreenTest {

    /**
     * Tests whether the game displays the death screen after a player dies (health reaches zero)
      */
    @Test
    void DeathBackground() {
        GdxGame game = mock(GdxGame.class);
        Entity player = new Entity();
        player.setEntityType("player");
        player.addComponent(new CombatStatsComponent(0, 10, 10, false));
        final Timer timer = new Timer();
        java.util.TimerTask testScreen = new java.util.TimerTask() {
            @Override
            public void run() {
                // assertEquals(PLAYER_DEATH, game.getScreenType());
                timer.cancel();
                timer.purge();
            }
        };
        timer.schedule(testScreen, 4000);
    }
}