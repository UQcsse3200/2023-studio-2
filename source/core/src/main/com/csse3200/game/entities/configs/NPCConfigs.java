package com.csse3200.game.entities.configs;

import com.csse3200.game.entities.enemies.EnemyBehaviour;
import com.csse3200.game.entities.enemies.EnemyType;

/**
 * Defines all NPC configs to be loaded by Related Factories.
 */
public class NPCConfigs {
  public BaseEntityConfig ghost = new BaseEntityConfig();
  public SoundsConfig sound;
  public BotanistConfig botanist = new BotanistConfig();
  public AstroConfig Astro = new AstroConfig();
  public TutnpcConfig Tutnpc = new TutnpcConfig();
  public AstronautConfig astronautConfig = new AstronautConfig();

  public JailConfig Jail = new JailConfig();
  //   Enemies Factory
  public EnemyConfig meleeEnemyPTE = new EnemyConfig();
  public EnemyConfig meleeEnemyDTE = new EnemyConfig();
  public EnemyConfig rangeEnemyPTE = new EnemyConfig();
  public EnemyConfig rangeEnemyDTE = new EnemyConfig();
  public EnemyConfig meleeBossPTE = new EnemyConfig();
  public EnemyConfig meleeBossDTE = new EnemyConfig();
  public EnemyConfig rangeBossPTE = new EnemyConfig();
  public EnemyConfig rangeBossDTE = new EnemyConfig();

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