package com.csse3200.game.entities.configs;

import com.csse3200.game.entities.enemies.EnemyBehaviour;
import com.csse3200.game.entities.enemies.EnemyType;

/**
 * Defines all NPC configs to be loaded by Related Factories.
 */
public class NPCConfigs {
  public BaseEntityConfig ghost = new BaseEntityConfig();
  public BotanistConfig botanist = new BotanistConfig();
  // Enemies Factory
  public EnemyConfig meleeEnemyPTE = new EnemyConfig();
  public EnemyConfig meleeEnemyDTE = new EnemyConfig();
  public EnemyConfig rangeEnemyPTE = new EnemyConfig();
  public EnemyConfig rangeEnemyDTE = new EnemyConfig();
  public BossConfig meleeBossPTE = new BossConfig();
  public BossConfig meleeBossDTE = new BossConfig();
  public BossConfig rangeBossPTE = new BossConfig();
  public BossConfig rangeBossDTE = new BossConfig();

  public EnemyConfig GetEnemyConfig(EnemyType type, EnemyBehaviour behaviour) {
    EnemyConfig config = null;
    if (type == EnemyType.Ranged) {
      if (behaviour ==  EnemyBehaviour.DTE) {
        config = rangeEnemyDTE;
      }
      if (behaviour ==  EnemyBehaviour.PTE) {
        config = rangeEnemyPTE;
      }
    }
    if (type == EnemyType.Melee) {
      if (behaviour == EnemyBehaviour.DTE) {
        config = meleeEnemyDTE;
      }
      if (behaviour ==  EnemyBehaviour.PTE) {
        config = meleeEnemyPTE;
      }
    }
    if (type == EnemyType.BossRanged) {
      if (behaviour ==  EnemyBehaviour.DTE) {
        config = rangeBossDTE;
      }
      if (behaviour ==  EnemyBehaviour.PTE) {
        config = rangeBossPTE;
      }
    }
    if (type == EnemyType.BossMelee) {
      if (behaviour == EnemyBehaviour.DTE) {
        config = meleeBossDTE;
      }
      if (behaviour ==  EnemyBehaviour.PTE) {
        config = meleeBossPTE;
      }
    }
    return config;
  }
}
