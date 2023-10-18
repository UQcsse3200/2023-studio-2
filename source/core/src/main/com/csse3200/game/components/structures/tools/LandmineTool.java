package com.csse3200.game.components.structures.tools;

import com.badlogic.gdx.utils.ObjectMap;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.PlaceableEntity;
import com.csse3200.game.entities.factories.ExplosivesFactory;

/**
 * A tool used by the player to place landmines.
 */
public class LandmineTool extends PlacementTool {
    /**
     * Creates a new tool which allows the placing of structures with the given cost.
     *
     * @param cost     - the cost of the entity being placed.
     * @param range
     * @param texture  - the texture of this tool.
     * @param ordering - the ordering of this tool.
     */
    public LandmineTool(ObjectMap<String, Integer> cost, float range, String texture, int ordering) {
        super(cost, range, texture, ordering);
    }

    /**
     * Creates a new landmine.
     * @param player - the player placing the structure.
     * @return a new landmine.
     */
    @Override
    public PlaceableEntity createStructure(Entity player) {
        return ExplosivesFactory.createLandmine();
    }
}
