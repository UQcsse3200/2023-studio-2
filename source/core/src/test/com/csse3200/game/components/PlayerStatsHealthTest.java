package com.csse3200.game.components;

import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.csse3200.game.components.player.PlayerStatsDisplay;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.csse3200.game.ui.UIComponent.skin;


@ExtendWith(GameExtension.class)
class PlayerStatsHealthTest  {

    public PlayerStatsDisplay playerStatsDisplay;
        /**
         * Test the update method of PlayerStatsHealth
         */

    /*
    @Test
    public void updateHealthTest() {
        playerStatsDisplay = new PlayerStatsDisplay();
        playerStatsDisplay.healthBar = new ProgressBar(0, 100, 1, false, skin); // Adjust the maximum value as needed

        //initial health value
        float initialHealth = playerStatsDisplay.healthBar.getMaxValue();
        Assertions.assertEquals(100, initialHealth);

    }*/
}
