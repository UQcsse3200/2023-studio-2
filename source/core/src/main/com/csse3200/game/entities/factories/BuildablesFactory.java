package com.csse3200.game.entities.factories;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.PlaceableEntity;
import com.csse3200.game.entities.buildables.*;

public class BuildablesFactory {

    /**
     * Creates a wall which can be placed on the map.
     *
     * @param type the type of wall to create.
     * @return The created Wall entity.
     */
    public static PlaceableEntity createWall(WallType type, Entity player) {
        return new Wall(type, player);
    }

    public static PlaceableEntity createCustomTurret(TurretType type, Entity player) {
        return new Turret(type, new Entity());
    }

    /**
     * Creates a gate which can be placed on the map.
     *
     * @param player the player which can pass through the gate.
     * @return The created Wall entity.
     */
    public static PlaceableEntity createGate(Entity player) {
        return new Gate(player);
    }
}
