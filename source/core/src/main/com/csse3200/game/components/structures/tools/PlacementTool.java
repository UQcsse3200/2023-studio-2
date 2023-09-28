package com.csse3200.game.components.structures.tools;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.csse3200.game.components.structures.CostComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.PlaceableEntity;
import com.csse3200.game.services.GameStateObserver;
import com.csse3200.game.services.ServiceLocator;

/**
 * An abstract tool which allows the player to place a structure.
 * This class must be inherited and the createEntity method implemented to function.
 */
public abstract class PlacementTool extends Tool {

    /**
     * Creates a new tool which allows the placing of structures with the given cost.
     * @param cost - the cost of the entity being placed.
     */
    protected PlacementTool(ObjectMap<String, Integer> cost) {
        super(cost);
    }

    /**
     * Attempts to place a structure at the specified position.
     * If the position is already occupied or the player has insufficient resources, does nothing.
     *
     * @param player - the player interacting with the tool.
     * @param position - the position to place the structure.
     * @return whether the structure was successfully placed.
     */
    @Override
    public boolean interact(Entity player, GridPoint2 position) {
        if (!isPositionValid(position)) {
            if (player != null) {
                player.getEvents().trigger("displayWarningAtPosition", "Invalid position",
                        new Vector2((float) position.x / 2, (float) position.y / 2));
            }

            return false;
        }

        if (!hasEnoughResources()) {
            if (player != null ) {
                player.getEvents().trigger("displayWarningAtPosition", "Insufficient resources",
                        new Vector2((float) position.x / 2, (float) position.y / 2));
            }
            return false;
        }

        PlaceableEntity newStructure = createStructure(player);
        newStructure.addComponent(new CostComponent(cost));

        ServiceLocator.getStructurePlacementService().placeStructureAt(newStructure, position, false, false);

        return true;
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
    public boolean isPositionValid(GridPoint2 position) {
        var existingStructure = ServiceLocator.getStructurePlacementService().getStructureAt(position);

        return existingStructure == null;
    }

    /**
     * Checks whether the player has sufficient resources to place the structure.
     *
     * @return whether the player has sufficient resources to place the structure.
     */
    public boolean hasEnoughResources() {
        GameStateObserver stateObserver = ServiceLocator.getGameStateObserverService();

        for (var resourceCost : cost.entries()) {
            var availableResources = stateObserver.getStateData("resource/" + resourceCost.key);

            if (!(availableResources instanceof Integer)) {
                return false;
            }

            if ((int)availableResources < resourceCost.value) {
                return false;
            }
        }

        return true;
    }
}
