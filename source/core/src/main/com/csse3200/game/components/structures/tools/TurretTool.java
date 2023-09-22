package com.csse3200.game.components.structures.tools;

import com.badlogic.gdx.utils.ObjectMap;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.PlaceableEntity;
import com.csse3200.game.entities.buildables.TurretType;
import com.csse3200.game.entities.factories.BuildablesFactory;

/**
 * A tool which allows the player to place a turret.
 */
public class TurretTool extends PlacementTool {
    /**
     * Creates a new turret tool with the given cost.
     * @param cost - the cost of a turret.
     */
    public TurretTool(ObjectMap<String, Integer> cost) {
        super(cost);
    }

    /**
     * Creates a new turret.
     * @param player - the player creating the turret.
     * @return a new turret.
     */
    @Override
    public PlaceableEntity createStructure(Entity player) {
        return BuildablesFactory.createCustomTurret(TurretType.LEVEL_ONE, player);
    }

}
