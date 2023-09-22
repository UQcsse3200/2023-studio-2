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
  public int lives;

    public PlayerConfig() {
    this.spritePath = "images/player.atlas";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PlayerConfig that = (PlayerConfig) o;

    if (speed != that.speed) return false;
    if (health != that.health) return false;
    if (baseAttack != that.baseAttack) return false;
    if (attackMultiplier != that.attackMultiplier) return false;
    return isImmune == that.isImmune;
  }

  @Override
  public int hashCode() {
    int result = speed;
    result = 31 * result + health;
    result = 31 * result + baseAttack;
    result = 31 * result + attackMultiplier;
    result = 31 * result + (isImmune ? 1 : 0);
    return result;
  }
}
