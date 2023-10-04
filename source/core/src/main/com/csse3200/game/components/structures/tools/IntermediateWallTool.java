package com.csse3200.game.components.structures.tools;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.ObjectMap;
import com.csse3200.game.components.structures.CostComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.PlaceableEntity;
import com.csse3200.game.entities.buildables.Wall;
import com.csse3200.game.entities.buildables.WallType;
import com.csse3200.game.entities.factories.BuildablesFactory;
import com.csse3200.game.services.ServiceLocator;

/**
 * A tool which allows the player to place an intermediate wall or upgrade basic walls.
 */
public class IntermediateWallTool extends PlacementTool {

    /**
     * Creates a new intermediate wall tool with the given cost.
     * @param cost - the cost of an intermediate wall.
     */
    public IntermediateWallTool(ObjectMap<String, Integer> cost) {
        super(cost);

        snapX = 2;
        snapY = 2;
    }

    /**
     * Attempts to place the wall at the given position.
     * If there is an existing structure at the given position, and it is a wall, replaces
     * it with an intermediate wall.
     *
     * @param player - the player interacting with the tool.
     * @param position - the position to place the structure.
     * @return whether the intermediate wall was successfully placed.
     */
    @Override
    public boolean interact(Entity player, GridPoint2 position) {
        if (super.interact(player, position)) {
            return true;
        }

        position = getSnapPosition(position);

        var existingStructure = ServiceLocator.getStructurePlacementService().getStructureAt(position);

        if (!(existingStructure instanceof Wall)) {
            return false;
        }

        if (!hasEnoughResources()) {
            return false;
        }

        PlaceableEntity newStructure = createStructure(player);
        newStructure.addComponent(new CostComponent(cost));

        ServiceLocator.getStructurePlacementService().replaceStructureAt(newStructure, position, false, false);

        return true;
    }

    /**
     * Creates an intermediate wall to place.
     *
     * @param player - the player placing the structure.
     * @return the intermediate wall to place.
     */
    @Override
    public PlaceableEntity createStructure(Entity player) {
        return BuildablesFactory.createWall(WallType.intermediate, player);
    }
}
