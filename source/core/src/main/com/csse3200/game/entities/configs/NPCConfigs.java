package com.csse3200.game.entities.configs;

import com.csse3200.game.entities.enemies.EnemyBehaviour;
import com.csse3200.game.entities.enemies.EnemyName;
import com.csse3200.game.entities.enemies.EnemyType;

import java.util.Objects;

/**
 * Defines all NPC configs to be loaded by Related Factories.
 */
public class NPCConfigs {

  public SoundsConfig sound;
  public BotanistConfig botanist = new BotanistConfig();
  public AstroConfig Astro = new AstroConfig();
  public TutnpcConfig Tutnpc = new TutnpcConfig();
  public AstronautConfig astronautConfig = new AstronautConfig();

  public JailConfig Jail = new JailConfig();
  //   Enemies Factory
  public EnemyConfig redGhost = new EnemyConfig();
  public EnemyConfig roboMan = new EnemyConfig();
  public EnemyConfig chain = new EnemyConfig();
  public EnemyConfig necromancer = new EnemyConfig();
  public EnemyConfig Knight = new EnemyConfig();
  public EnemyConfig rangeBossPTE = new EnemyConfig();

  public EnemyConfig GetEnemyConfig(EnemyName name) {
      EnemyConfig config = null;
      switch (name) {
          case redGhost:
               config = redGhost;
              break;
          case chain:
              config = chain;
              break;
          case necromancer:
              config = necromancer;
              break;
          case roboMan:
              config = roboMan;
              break;
          case Knight:
              config = Knight;
              break;
          case rangeBossPTE:
              config = rangeBossPTE;
              break;
      }
      return config;
  }
}