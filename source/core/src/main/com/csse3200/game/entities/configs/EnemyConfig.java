package com.csse3200.game.entities.configs;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.enemies.EnemyBehaviour;
import com.csse3200.game.entities.enemies.EnemyName;
import com.csse3200.game.entities.enemies.EnemyType;

/**
 * Defines the properties stored in Enemy config files to be loaded by the NPC Factory.
 */
public class EnemyConfig extends HealthEntityConfig {
  // Load enemy information here
  public EnemyName name;
  public float speedX;
  public float speedY;
  public EnemyBehaviour behaviour;
  public EnemyType type;
  public boolean isBoss = false;
  public SoundsConfig sound;
}
