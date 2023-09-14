package com.csse3200.game.components.structures.tools;

import com.badlogic.gdx.utils.ObjectMap;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.PlaceableEntity;
import com.csse3200.game.entities.factories.BuildablesFactory;
import com.csse3200.game.entities.factories.StructureFactory;

public class GateTool extends PlacementTool {
    /**
     * Creates a new tool which allows the placing of structures with the given cost.
     *
     * @param cost - the cost of the entity being placed.
     */
    public GateTool(ObjectMap<String, Integer> cost) {
        super(cost);
    }

    @Override
    public PlaceableEntity createStructure(Entity player) {
        return BuildablesFactory.createGate(player);
    }
}
