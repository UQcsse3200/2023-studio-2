package com.csse3200.game.areas.mapConfig;

import com.csse3200.game.entities.configs.BaseEntityConfig;
import com.csse3200.game.entities.configs.PlayerConfig;

import java.util.*;
import java.util.stream.Collectors;

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

    public String[] getEntityTextures() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameAreaConfig that = (GameAreaConfig) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(texturePaths, that.texturePaths)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(textureAtlasPaths, that.textureAtlasPaths)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(soundPaths, that.soundPaths)) return false;
        if (!Objects.equals(backgroundMusicPath, that.backgroundMusicPath))
            return false;
        if (!Objects.equals(mapName, that.mapName)) return false;
        if (!Objects.equals(terrainPath, that.terrainPath)) return false;
        if (!Objects.equals(winConditions, that.winConditions))
            return false;
        if (!Objects.equals(playerConfig, that.playerConfig)) return false;
        return Objects.equals(areaEntityConfig, that.areaEntityConfig);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(texturePaths);
        result = 31 * result + Arrays.hashCode(textureAtlasPaths);
        result = 31 * result + Arrays.hashCode(soundPaths);
        result = 31 * result + (backgroundMusicPath != null ? backgroundMusicPath.hashCode() : 0);
        result = 31 * result + (mapName != null ? mapName.hashCode() : 0);
        result = 31 * result + (terrainPath != null ? terrainPath.hashCode() : 0);
        result = 31 * result + (winConditions != null ? winConditions.hashCode() : 0);
        result = 31 * result + (playerConfig != null ? playerConfig.hashCode() : 0);
        result = 31 * result + (areaEntityConfig != null ? areaEntityConfig.hashCode() : 0);
        return result;
    }
}