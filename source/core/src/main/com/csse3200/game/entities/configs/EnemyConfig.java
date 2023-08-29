package com.csse3200.game.entities.configs;

/**
 * Defines the properties stored in Enemy config files to be loaded by the NPC Factory.
 */
public class EnemyConfig extends BaseEntityConfig {
  // Load enemy information here
  public int speed = 1;
  public String behaviour = "default";
  public String atlas = ".atlas";
}
