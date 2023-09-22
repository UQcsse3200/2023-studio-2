package com.csse3200.game.areas.mapConfig;

import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class LevelConfig {
    public List<GameAreaConfig> gameAreas;

    public String[] getTextures() {
        return gameAreas.stream()
                 .flatMap(gameAreaConfig -> Arrays.stream(gameAreaConfig.getEntityTextures()))
                 .distinct()
                 .toList()
                 .toArray(new String[]{});
    }
}
