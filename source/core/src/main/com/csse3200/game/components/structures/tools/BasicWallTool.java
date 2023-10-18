package com.csse3200.game.components.structures.tools;

import com.badlogic.gdx.utils.ObjectMap;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.PlaceableEntity;
import com.csse3200.game.entities.buildables.WallType;
import com.csse3200.game.entities.factories.BuildablesFactory;

/**
 * A tool which allows the player to place a basic wall.
 */
public class BasicWallTool extends PlacementTool {
    /**
     * Creates a new basic wall tool with the given cost.
     *
     * @param cost     - the cost of a basic wall.
     * @param range
     * @param texture  - the texture of this tool.
     * @param ordering - the ordering of this tool.
     */
    public BasicWallTool(ObjectMap<String, Integer> cost, float range, String texture, int ordering) {
        super(cost, range, texture, ordering);
        snapX = 2;
        snapY = 2;
    }

    /**
     * Creates a new basic wall.
     * @param player - the player creating the wall.
     * @return a new basic wall to place.
     */
    @Override
    public PlaceableEntity createStructure(Entity player) {
        return BuildablesFactory.createWall(WallType.BASIC, player);
    }

}
