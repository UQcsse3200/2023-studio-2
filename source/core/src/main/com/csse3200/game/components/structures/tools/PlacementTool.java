package com.csse3200.game.components.structures.tools;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.PlaceableEntity;
import com.csse3200.game.services.ServiceLocator;

public abstract class PlacementTool extends Tool {
    @Override
    public void interact(Entity player, GridPoint2 position) {
        var existingStructure = ServiceLocator.getStructurePlacementService().getStructureAt(position);

        if (existingStructure == null) {
            PlaceableEntity newStructure = createEntity(player);

            ServiceLocator.getStructurePlacementService().PlaceStructureAt(newStructure, position, false, false);
        } else {
            modifyExistingEntity(player, existingStructure);
        }
    }

    public abstract PlaceableEntity createEntity(Entity player);

    public void modifyExistingEntity(Entity player, PlaceableEntity existingStructure) {
        // default implementation is to do nothing
    };
}
