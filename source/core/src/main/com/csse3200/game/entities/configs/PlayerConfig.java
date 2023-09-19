package com.csse3200.game.entities.configs;

/**
 * Defines the properties stored in player config files to be loaded by the Player Factory.
 */
public class PlayerConfig extends BaseEntityConfig  {
  public int speed;
  public int health;
  public int baseAttack;
  public int attackMultiplier;
  public boolean isImmune;
  public String mapName;

    public PlayerConfig() {
    this.spritePath = "images/player.atlas";
  }
}
