package com.csse3200.game.areas.mapConfig;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.entities.configs.BaseEntityConfig;
import com.csse3200.game.entities.configs.PlayerConfig;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameAreaConfig {
    //Map Assets - all entity specific paths should be defined within the levelConfig file
    public String[] texturePaths = null;
    public String[] textureAtlasPaths = null;
    public String[] soundPaths = null;
    public String backgroundMusicPath = null;

    //Map Properties
    public String mapName = "Planet";
    public String terrainPath = "map/base.tmx";
    public List<ResourceCondition> winConditions = null;
    public PlayerConfig playerConfig = null;

    public AreaEntityConfig areaEntityConfig = null;

    public String[] getTextures() {
        List<String> textures = new ArrayList<>();

        if (areaEntityConfig != null) {
            textures = areaEntityConfig.getAllConfigs()
                    .stream()
                    .map(BaseEntityConfig::getTextures)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
        }
        return textures.stream().distinct().toArray(String[]::new);
    }
}