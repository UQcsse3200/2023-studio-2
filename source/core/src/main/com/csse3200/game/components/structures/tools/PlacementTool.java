package com.csse3200.game.components.structures.tools;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.PlaceableEntity;
import com.csse3200.game.services.ServiceLocator;

public abstract class PlacementTool extends Tool {
    @Override
    public void interact(Entity player, GridPoint2 position) {
        PlaceableEntity newStructure = createEntity(player);

        ServiceLocator.getStructurePlacementService().PlaceStructureAt(newStructure, position, false, false);
    }

    public abstract PlaceableEntity createEntity(Entity player);
}
