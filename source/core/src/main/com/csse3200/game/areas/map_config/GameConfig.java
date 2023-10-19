package com.csse3200.game.areas.map_config;

import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * Configuration class for representing the overall game settings.
 * Contains level names, game state data, and associated assets configurations.
 */
public class GameConfig {
    public List<String> levelNames = null;
    public Map<String, Object> gameState = null;
    public AssetsConfig assets = null;

    /**
     * Determines if the provided object is equal to this GameConfig instance.
     *
     * @param o Object to be compared.
     * @return true if objects are equal, otherwise false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameConfig that = (GameConfig) o;
        return Objects.equals(levelNames, that.levelNames) && Objects.equals(gameState, that.gameState) && Objects.equals(assets, that.assets);
    }

    /**
     * Computes a hash code for the GameConfig instance.
     *
     * @return Hash code of the object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(levelNames, gameState, assets);
    }
}
