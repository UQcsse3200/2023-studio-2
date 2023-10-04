package com.csse3200.game.entities.configs;

import com.csse3200.game.entities.enemies.EnemyBehaviour;
import com.csse3200.game.entities.enemies.EnemyType;

/**
 * Defines the properties stored in Enemy config files to be loaded by the NPC Factory.
 */
public class EnemyConfig extends HealthEntityConfig {
  // Load enemy information here
  public int speed = 1;
  public EnemyBehaviour behaviour;
  public EnemyType type;
  public boolean isBoss = false;
  public int specialAttack;

  public SoundsConfig sound;

  public EnemyConfig() {

  }

  public EnemyConfig(boolean isBoss) {
    this.isBoss = isBoss;
  }
}
