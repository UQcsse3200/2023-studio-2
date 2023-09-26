package com.csse3200.game.entities.configs;

import com.csse3200.game.files.FileLoader;

/**
 * Defines the properties stored in player config files to be loaded by the Player Factory.
 */
public class PlayerConfig extends HealthEntityConfig  {
  public int speed;
  public int lives;
  public SoundsConfig sounds;

  public PlayerConfig() {
    this.spritePath = "images/player.atlas";
    this.sounds = FileLoader.readClass(SoundsConfig.class, "configs/player_sounds.json");
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
