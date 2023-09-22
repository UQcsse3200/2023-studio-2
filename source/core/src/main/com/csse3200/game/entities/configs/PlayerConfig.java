package com.csse3200.game.entities.configs;

/**
 * Defines the properties stored in player config files to be loaded by the Player Factory.
 */
public class PlayerConfig extends HealthEntityConfig  {
  public int speed;

  public PlayerConfig() {
    this.spritePath = "images/player.atlas";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;

    PlayerConfig that = (PlayerConfig) o;

    return speed == that.speed;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + speed;
    return result;
  }
}
