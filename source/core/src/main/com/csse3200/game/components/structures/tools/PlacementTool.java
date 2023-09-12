package com.csse3200.game.components.structures.tools;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.ObjectMap;
import com.csse3200.game.components.structures.CostComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.PlaceableEntity;
import com.csse3200.game.services.GameStateObserver;
import com.csse3200.game.services.ServiceLocator;

public abstract class PlacementTool extends Tool {
    public PlacementTool(ObjectMap<String, Integer> cost) {
        super(cost);
    }

    @Override
    public boolean interact(Entity player, GridPoint2 position) {
        if (!isPositionValid(position)) {
            return false;
        }

        PlaceableEntity newStructure = createEntity(player);
        newStructure.addComponent(new CostComponent(cost));

        ServiceLocator.getStructurePlacementService().PlaceStructureAt(newStructure, position, false, false);

        return true;
    }

    public abstract PlaceableEntity createEntity(Entity player);

    public boolean isPositionValid(GridPoint2 position) {
        var existingStructure = ServiceLocator.getStructurePlacementService().getStructureAt(position);

        if (existingStructure != null) {
            return false;
        }

        GameStateObserver stateObserver = ServiceLocator.getGameStateObserverService();

        for (var resourceCost : cost.entries()) {
            var availableResources = stateObserver.getStateData("resource/" + resourceCost.key);

            if (!(availableResources instanceof Integer)) {
                return false;
            }

            if ((int) availableResources < resourceCost.value) {
                return false;
            }
        }

        return true;
    }
}
