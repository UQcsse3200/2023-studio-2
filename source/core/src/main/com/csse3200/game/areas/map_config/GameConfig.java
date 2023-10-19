package com.csse3200.game.areas.map_config;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GameConfig {
    public List<String> levelNames = null;
    public Map<String, Object> gameState = null;
    public AssetsConfig assets = null;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameConfig that = (GameConfig) o;
        return Objects.equals(levelNames, that.levelNames) && Objects.equals(gameState, that.gameState) && Objects.equals(assets, that.assets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(levelNames, gameState, assets);
    }
}
