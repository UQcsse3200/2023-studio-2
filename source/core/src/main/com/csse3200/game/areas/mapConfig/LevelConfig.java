package com.csse3200.game.areas.mapConfig;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LevelConfig {
    public List<GameAreaConfig> gameAreas;

    public String[] getTextures() {
        return gameAreas.stream()
                 .flatMap(gameAreaConfig -> Arrays.stream(gameAreaConfig.getEntityTextures()))
                 .distinct()
                 .collect(Collectors.toList())
                 .toArray(new String[]{});
    }
}
