package com.csse3200.game.components.structures.tools;

import com.badlogic.gdx.utils.ObjectMap;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.PlaceableEntity;
import com.csse3200.game.entities.factories.ExplosivesFactory;

/**
 * Used by the player to place explosive barrels.
 */
public class ExplosiveBarrelTool extends PlacementTool {
    /**
     * Creates a new tool which allows the placing of structures with the given cost.
     *
     * @param cost - the cost of the entity being placed.
     * @param ordering - the ordering of this tool.
     * @param texture - the texture of this tool.
     */
    public ExplosiveBarrelTool(ObjectMap<String, Integer> cost, int ordering, String texture) {
        super(cost, ordering, texture);
    }

    /**
     * Creates a new explosive barrel.
     * @param player - the player placing the structure.
     * @return a new explosive barrel.
     */
    @Override
    public PlaceableEntity createStructure(Entity player) {
        return ExplosivesFactory.createExplosiveBarrel();
    }
}
