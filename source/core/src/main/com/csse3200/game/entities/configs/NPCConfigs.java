package com.csse3200.game.entities.configs;

import com.csse3200.game.entities.enemies.EnemyBehaviour;
import com.csse3200.game.entities.enemies.EnemyType;

/**
 * Defines all NPC configs to be loaded by Related Factories.
 */
public class NPCConfigs {

  // Enemies Factory
  public EnemyConfig meleeEnemyPTE = new EnemyConfig();
  public EnemyConfig meleeEnemyDTE = new EnemyConfig();
  public EnemyConfig rangeEnemyPTE = new EnemyConfig();
  public EnemyConfig rangeEnemyDTE = new EnemyConfig();
  public BossConfig meleeBossPTE = new BossConfig();
  public BossConfig meleeBossDTE = new BossConfig();
  public BossConfig rangeBossPTE = new BossConfig();
  public BossConfig rangeBossDTE = new BossConfig();

  public BulletConfig bullet = new BulletConfig();

  public EnemyConfig GetEnemyConfig(EnemyType type, EnemyBehaviour behaviour) {
    if (type == EnemyType.Ranged) {
      if (behaviour ==  EnemyBehaviour.DTE) {
        return rangeEnemyDTE;
      } else {
        return rangeEnemyPTE;
      }
    }
    if (type == EnemyType.Melee) {
      if (behaviour == EnemyBehaviour.DTE) {
        return meleeEnemyDTE;
      } else {
        return meleeEnemyPTE;
      }
    }
    if (type == EnemyType.BossRanged) {
      if (behaviour ==  EnemyBehaviour.DTE) {
        return rangeBossDTE;
      } else {
        return rangeBossPTE;
      }
    } else {
      if (behaviour == EnemyBehaviour.DTE) {
        return meleeBossDTE;
      } else {
        return meleeBossPTE;
      }
    }
  }

  public BulletConfig GetBulletConfig() {
    return bullet;
  }
  public BaseEntityConfig ghost = new BaseEntityConfig();
  public BotanistConfig botanist = new BotanistConfig();


}
