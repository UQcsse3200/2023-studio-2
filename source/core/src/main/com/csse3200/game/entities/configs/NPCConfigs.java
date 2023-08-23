package com.csse3200.game.entities.configs;

import com.csse3200.game.entities.enemies.BossType;
import com.csse3200.game.entities.enemies.EnemyType;

/**
 * Defines all NPC configs to be loaded by Related Factories.
 */
public class NPCConfigs {
  // Enemies Factory
  public EnemyConfig meleeEnemy = new EnemyConfig();
  public EnemyConfig rangeEnemy = new EnemyConfig();
  public BossConfig meleeBoss = new BossConfig();
  public BossConfig rangeBoss = new BossConfig();

  public EnemyConfig GetEnemyConfig(EnemyType type) {
    return switch (type) {
      case Melee -> meleeEnemy;
      case Ranged -> rangeEnemy;
    };
  }

  public BossConfig GetBossConfig(BossType type) {
    return switch (type) {
      case Melee -> meleeBoss;
      case Ranged -> rangeBoss;
    };
  }
  // Add rest here
}
