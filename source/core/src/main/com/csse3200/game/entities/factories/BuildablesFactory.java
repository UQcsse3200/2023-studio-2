package com.csse3200.game.entities.factories;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.buildables.Gate;
import com.csse3200.game.entities.buildables.Wall;
import com.csse3200.game.entities.buildables.WallType;
import com.csse3200.game.entities.configs.WallConfigs;
import com.csse3200.game.files.FileLoader;

public class BuildablesFactory {

    /**
     * Creates a wall which can be placed on the map.
     * @param type the type of wall to create.
     * @return The created Wall entity.
     */
    public static Entity createCustomWall(WallType type, Entity player) {
      return new Wall(type, player);
    }

    /**
     * Creates a gate which can be placed on the map.
     * @param player the player which can pass through the gate.
     * @return The created Wall entity.
     */
    public static Entity createGate(Entity player) {
        return new Gate(player);
    }
}
