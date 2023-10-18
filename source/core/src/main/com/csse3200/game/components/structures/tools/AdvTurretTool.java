package com.csse3200.game.components.structures.tools;

import com.badlogic.gdx.utils.ObjectMap;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.PlaceableEntity;
import com.csse3200.game.entities.buildables.TurretType;
import com.csse3200.game.entities.factories.BuildablesFactory;

/**
 * A tool which allows the player to place a turret.
 */
public class AdvTurretTool extends PlacementTool {
    /**
     * Creates a new turret tool with the given cost.
     *
     * @param cost     - the cost of a turret.
     * @param range
     * @param texture  - the texture of this tool.
     * @param ordering - the ordering of this tool.
     */
    public AdvTurretTool(ObjectMap<String, Integer> cost, float range, String texture, int ordering) {
        super(cost, range, texture, ordering);
        snapX = 2;
        snapY = 2;
    }

    /**
     * Creates a new turret.
     * @param player - the player creating the turret.
     * @return a new turret.
     */
    @Override
    public PlaceableEntity createStructure(Entity player) {
        return BuildablesFactory.createCustomTurret(TurretType.LEVEL_TWO, player);
    }

}
