package com.csse3200.game.components.structures.tools;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.ObjectMap;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.structures.JoinableComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.PlaceableEntity;
import com.csse3200.game.entities.factories.BuildablesFactory;

/**
 * Tool used to place gate structures.
 */
public class GateTool extends PlacementTool {
    /**
     * Creates a new tool which allows the placing of structures with the given cost.
     *
     * @param cost     - the cost of the entity being placed.
     * @param range    -
     * @param texture  - the texture of this tool.
     * @param ordering - the ordering of this tool.
     */
    public GateTool(ObjectMap<String, Integer> cost, float range, String texture, int ordering) {
        super(cost, range, texture, ordering);
        snapX = 2;
        snapY = 2;
    }

    /**
     * Creates a gate.
     * @param player - the player placing the structure.
     * @return a new gate.
     */
    @Override
    public PlaceableEntity createStructure(Entity player) {
        return BuildablesFactory.createGate(player);
    }
}
