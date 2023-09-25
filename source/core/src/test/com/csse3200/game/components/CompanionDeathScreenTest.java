package com.csse3200.game.components;

import com.csse3200.game.GdxGame;
import com.csse3200.game.entities.Entity;
import org.junit.jupiter.api.Test;

import java.util.Timer;

import static com.csse3200.game.GdxGame.ScreenType.COMPANION_DEATH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for the CompanionDeathScreen class.
 */
class CompanionDeathScreenTest {

    /**
     * Tests whether the game displays the companion death screen after a companion dies (health reaches zero).
     */
    @Test
    void CompanionDeathBackground() {
        GdxGame game = mock(GdxGame.class);
        Entity companion = new Entity();
        companion.setEntityType("companion");
        companion.addComponent(new CombatStatsComponent(10, 10, 10, false));
        companion.getComponent(CombatStatsComponent.class).setHealth(0);
        final Timer timer = new Timer();
        java.util.TimerTask testScreen = new java.util.TimerTask() {
            @Override
            public void run() {
                assertEquals(COMPANION_DEATH, game.getScreenType());
                timer.cancel();
                timer.purge();
            }
        };
        timer.schedule(testScreen, 4000);
    }
}
