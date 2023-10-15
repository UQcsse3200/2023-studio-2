package com.csse3200.game.components.structures.tools;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.ObjectMap;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.PlaceableEntity;
import com.csse3200.game.entities.buildables.Wall;
import com.csse3200.game.entities.buildables.WallType;
import com.csse3200.game.entities.factories.BuildablesFactory;
import com.csse3200.game.services.ServiceLocator;

/**
 * A tool which allows the player to place an intermediate wall or upgrade basic walls.
 */
public class IntermediateWallTool extends ReplacementTool {

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
     * Creates an intermediate wall to place.
     *
     * @param player - the player placing the structure.
     * @return the intermediate wall to place.
     */
    @Override
    public PlaceableEntity createStructure(Entity player) {
        return BuildablesFactory.createWall(WallType.INTERMEDIATE, player);
    }

    /**
     * Returns whether the position is valid. It is valid if it is replacing no structure,
     * or is replacing a wall.
     * @param position - the position the structure is trying to be placed at.
     * @return whether the position is valid.
     */
    @Override
    public ToolResponse isPositionValid(GridPoint2 position) {
        var validity = super.isPositionValid(position);

        if (!validity.isValid()) {
            return validity;
        }

        var existingStructure = ServiceLocator.getStructurePlacementService().getStructureAt(position);

        if (!(existingStructure instanceof Wall) && existingStructure != null) {
            return new ToolResponse(PlacementValidity.INVALID_POSITION, "You can only upgrade walls");
        }

        return ToolResponse.valid();
    }
}
