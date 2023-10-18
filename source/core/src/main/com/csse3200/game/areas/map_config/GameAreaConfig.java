package com.csse3200.game.areas.map_config;

import com.csse3200.game.entities.configs.BaseEntityConfig;
import com.csse3200.game.entities.configs.CompanionConfig;
import com.csse3200.game.entities.configs.PlayerConfig;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Configuration class representing a game area.
 * Holds information related to assets, map properties, and entity configurations for the game area.
 */
public class GameAreaConfig {
    //Map Assets - all entity specific paths should be defined within the levelConfig file
    public AssetsConfig assets = null;

    //Map Properties
    public String mapName = "Planet";
    public String planetImage = "images/planets/space_navigation_planet_0.png";
    public String terrainPath = "map/base.tmx";
    public List<ResourceCondition> winConditions = null;
    public PlayerConfig playerConfig = null;
    public CompanionConfig companionConfig = null;
    public AreaEntityConfig areaEntityConfig = null;

    /**
     * Retrieves a list of texture paths associated with the entities defined in the areaEntityConfig.
     *
     * @return An array of distinct texture paths for entities.
     */
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

    /**
     * Determines if the provided object is equal to this GameAreaConfig instance.
     *
     * @param o Object to be compared.
     * @return true if objects are equal, otherwise false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameAreaConfig that = (GameAreaConfig) o;

        if (!Objects.equals(assets, that.assets)) return false;
        if (!Objects.equals(mapName, that.mapName)) return false;
        if (!Objects.equals(planetImage, that.planetImage)) return false;
        if (!Objects.equals(terrainPath, that.terrainPath)) return false;
        if (!Objects.equals(winConditions, that.winConditions))
            return false;
        return Objects.equals(areaEntityConfig, that.areaEntityConfig);
    }
    /**
     * Computes the hash code for this GameAreaConfig instance based on its attributes.
     *
     * @return The hash code of the object.
     */
    @Override
    public int hashCode() {
        int result = assets != null ? assets.hashCode() : 0;
        result = 31 * result + (mapName != null ? mapName.hashCode() : 0);
        result = 31 * result + (planetImage != null ? planetImage.hashCode() : 0);
        result = 31 * result + (terrainPath != null ? terrainPath.hashCode() : 0);
        result = 31 * result + (winConditions != null ? winConditions.hashCode() : 0);
        result = 31 * result + (areaEntityConfig != null ? areaEntityConfig.hashCode() : 0);
        return result;
    }
}