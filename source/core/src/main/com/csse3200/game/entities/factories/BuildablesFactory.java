package com.csse3200.game.entities.factories;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.buildables.Wall;
import com.csse3200.game.entities.buildables.WallType;
import com.csse3200.game.entities.configs.WallConfigs;
import com.csse3200.game.files.FileLoader;

public class BuildablesFactory {
    private static final WallConfigs configs =
            FileLoader.readClass(WallConfigs.class, "configs/walls.json");

    /**
     * Creates a wall which can be placed on the map.
     * @param type the type of wall to create.
     * @return The created Wall entity.
     */
    public static Entity createCustomWall(WallType type) {
      return new Wall(configs.GetWallConfig(type));
    }
}
