package com.csse3200.game.components.structures.tools;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.ObjectMap;
import com.csse3200.game.components.structures.CostComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.PlaceableEntity;
import com.csse3200.game.services.GameStateObserver;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.StructurePlacementService;

/**
 * An abstract tool which allows the player to place a structure.
 * This class must be inherited and the createEntity method implemented to function.
 */
public abstract class PlacementTool extends Tool {
    protected int snapX = 1;
    protected int snapY = 1;
    protected StructurePlacementService structurePlacementService;

    /**
     * Creates a new tool which allows the placing of structures with the given cost.
     *
     * @param cost     - the cost of the entity being placed.
     * @param range
     * @param texture  - the texture of this tool.
     * @param ordering - the ordering of this tool.
     */
    protected PlacementTool(ObjectMap<String, Integer> cost, float range, String texture, int ordering) {
        super(cost, range, texture, ordering);
        structurePlacementService = ServiceLocator.getStructurePlacementService();
    }

    /**
     * Places the structure returned by createStructure() at the given position.
     *
     * @param player - the player interacting with the tool.
     * @param position - the position to place the structure.
     */
    @Override
    protected void performInteraction(Entity player, GridPoint2 position) {
        PlaceableEntity newStructure = createStructure(player);
        newStructure.addComponent(new CostComponent(cost));

        ServiceLocator.getStructurePlacementService().placeStructureAt(newStructure, position);
    }

    /**
     * Returns ToolResponse.valid() if the position is not occupied and the player
     * has insufficient resources, otherwise returns an invalid response.
     *
     * @param player - the player attempting to interact.
     * @param position - the position to interact.
     * @return whether the position can be interacted with.
     */
    @Override
    protected ToolResponse canInteract(Entity player, GridPoint2 position) {
        var validity = super.canInteract(player, position);

        if (!validity.isValid()) {
            return validity;
        }

        var positionValidity = isPositionValid(position);
        if (!positionValidity.isValid()) {
            return positionValidity;
        }

        var resourceValidity = hasEnoughResources();
        if (!resourceValidity.isValid()) {
            return resourceValidity;
        }

        return ToolResponse.valid();
    }

    /**
     * Snaps the position to the tools individual grid and then calls the parents implementation.
     *
     * @param player - the player interacting with the tool.
     * @param position - the position being interacted with.
     */
    @Override
    public void interact(Entity player, GridPoint2 position) {
        position = getSnapPosition(position);

        super.interact(player, position);
    }

    /**
     * Snaps the position to the tools individual grid.
     *
     * @param position - the position unsnapped.
     * @return the snapped position.
     */
    public GridPoint2 getSnapPosition(GridPoint2 position) {
        var diffX = position.x % snapX;
        var diffY = position.y % snapY;

        return new GridPoint2(position.x - diffX, position.y - diffY);
    }

    /**
     * Creates the structure to be placed. This must be implemented in subclasses to function.
     *
     * @param player - the player placing the structure.
     * @return the structure to be placed.
     */
    public abstract PlaceableEntity createStructure(Entity player);

    /**
     * Checks whether the structure can be placed at the given position.
     * By default, checks if the grid position is empty.
     *
     * @param position - the position the structure is trying to be placed at.
     * @return whether the structure can be placed at the given position.
     */
    public ToolResponse isPositionValid(GridPoint2 position) {
        var dummyInstance = createStructure(new Entity());

        return structurePlacementService.canPlaceStructureAt(dummyInstance, position) ? ToolResponse.valid()
                : new ToolResponse(PlacementValidity.INVALID_POSITION, "Cannot place on an existing structure");
    }

    /**
     * Checks whether the player has sufficient resources to place the structure.
     *
     * @return whether the player has sufficient resources to place the structure.
     */
    public ToolResponse hasEnoughResources() {
        GameStateObserver stateObserver = ServiceLocator.getGameStateObserverService();

        for (var resourceCost : cost.entries()) {
            var availableResources = stateObserver.getStateData("resource/" + resourceCost.key);

            if (!(availableResources instanceof Integer)) {
                return new ToolResponse(PlacementValidity.ERROR, "Resource %s does not have an integer state");
            }

            if ((int)availableResources < resourceCost.value) {
                return new ToolResponse(PlacementValidity.INSUFFICIENT_RESOURCES,
                        String.format("Not enough %s", resourceCost.key));
            }
        }

        return ToolResponse.valid();
    }
}
