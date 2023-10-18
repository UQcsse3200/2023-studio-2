package com.csse3200.game.entities.configs;

import com.csse3200.game.files.FileLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Defines the properties stored in player config files to be loaded by the Player Factory.
 */
public class PlayerConfig extends HealthEntityConfig  {
  public int speed;
  public int lives;
  public SoundsConfig sounds;
  public List<Object> unlocks;

  public PlayerConfig() {
    this.spritePath = "images/player.atlas";
    this.sounds = FileLoader.readClass(SoundsConfig.class, "configs/player_sounds.json");
    unlocks = new ArrayList<>();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;

    PlayerConfig that = (PlayerConfig) o;

    if (speed != that.speed) return false;
    if (lives != that.lives) return false;
    return Objects.equals(sounds, that.sounds);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + speed;
    result = 31 * result + lives;
    result = 31 * result + (sounds != null ? sounds.hashCode() : 0);
    return result;
  }
}
