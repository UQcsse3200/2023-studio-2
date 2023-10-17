package com.csse3200.game.entities.factories;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.PlaceableEntity;
import com.csse3200.game.entities.buildables.*;
import com.csse3200.game.entities.configs.*;
import com.csse3200.game.files.FileLoader;

public class BuildablesFactory {
    private static final TurretConfigs turretConfigs =
            FileLoader.readClass(TurretConfigs.class, "configs/turrets.json");
    private static final WallConfigs wallConfig =
            FileLoader.readClass(WallConfigs.class, "configs/walls.json");
    private static final GateConfig gateConfig =
            FileLoader.readClass(GateConfig.class, "configs/gates.json");

    /**
     * Creates a wall which can be placed on the map.
     * @param type the type of wall to create.
     * @return The created Wall entity.
     */
    public static PlaceableEntity createWall(WallType type, Entity player) {
        return new Wall(new WallConfig(wallConfig.getWallConfig(type)), player);
    }

    /**
     * Creates a wall which can be placed on the map.
     * @param config the config for the wall to create.
     * @return The created Wall entity.
     */
    public static PlaceableEntity createWall(WallConfig config, Entity player) {
        return new Wall(config, player);
    }

    public static PlaceableEntity createCustomTurret(TurretType type, Entity player) {
        return new Turret(new TurretConfig(turretConfigs.getTurretConfig(type)), player);
    }

    public static PlaceableEntity createCustomTurret(TurretConfig config, Entity player) {
        return new Turret(config, player);
    }

    /**
     * Creates a gate which can be placed on the map.
     * @param player the player which can pass through the gate.
     * @return The created Wall entity.
     */
    public static PlaceableEntity createGate(Entity player) {
        return new Gate(player, new GateConfig(gateConfig));
    }

    /**
     * Creates a gate which can be placed on the map.
     * @param player the player which can pass through the gate.
     * @param config the config for the gate.
     * @return The created Wall entity.
     */
    public static PlaceableEntity createGate(Entity player, GateConfig config) {
        return new Gate(player, config);
    }
}
