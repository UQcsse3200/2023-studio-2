package com.csse3200.game.components;

import com.csse3200.game.areas.mapConfig.GameAreaConfig;
import com.csse3200.game.extensions.GameExtension;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;





/**
 * Unit tests for the {@link EnvironmentStatsComponent} class.
 */
@ExtendWith(GameExtension.class)
class EnvironmentStatsComponentTest {
  private EnvironmentStatsComponent environmentStatsComponent;

  @BeforeEach
  void setUp() {
    environmentStatsComponent = new EnvironmentStatsComponent();
  }

  /**
   * Test setting damage over time effect
   */
  @Test
  void damage() throws InterruptedException {

    CombatStatsComponent player = new CombatStatsComponent(10, 10, 1, false);
    EnvironmentStatsComponent environmentStatsComponent = new EnvironmentStatsComponent();


    environmentStatsComponent.damage(player);

    // Wait for 2 seconds
    Thread.sleep(2000);


    assertEquals(9, player.getHealth(), "Health should be reduced after the timer delay");
  }

  /**
   * test setting player immunity
   */
  @Test
  void setImmunity() {

    GameAreaConfig earthMapConfig = new GameAreaConfig();
    earthMapConfig.mapName = "Earth";


    environmentStatsComponent.setImmunity(earthMapConfig);
    assertFalse(environmentStatsComponent.getImmunity(), "Entity should not be immune on Earth");
  }


}