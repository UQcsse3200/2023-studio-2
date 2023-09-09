package com.csse3200.game.areas.MapConfig;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.dataaccessobjects.EntityDAO;

import java.util.List;
import java.util.stream.Collectors;

public class LevelConfig {
    public List<GameAreaConfig> gameAreas;
    public EntityDAO player;    //TODO: Update to playerDAO

    public String[] GetTextures() {
        //Get all entities from all game areas and get each of their sprite paths, then collect to an array
        return gameAreas.stream()
                 .flatMap(gameAreaConfig -> gameAreaConfig.entityConfig.getAllDAOs()
                         .stream().map(entityDAO -> entityDAO.spritePath))
                 .collect(Collectors.toList())
                 .toArray(new String[]{});
    }
}
