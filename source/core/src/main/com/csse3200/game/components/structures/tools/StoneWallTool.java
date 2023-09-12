package com.csse3200.game.components.structures.tools;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.PlaceableEntity;
import com.csse3200.game.entities.buildables.Wall;
import com.csse3200.game.entities.buildables.WallType;
import com.csse3200.game.entities.factories.BuildablesFactory;
import com.csse3200.game.services.ServiceLocator;

public class StoneWallTool extends PlacementTool {
    @Override
    public boolean interact(Entity player, GridPoint2 position) {
        if (super.interact(player, position)) {
            return true;
        }

        var existingStructure = ServiceLocator.getStructurePlacementService().getStructureAt(position);

        if (!(existingStructure instanceof Wall)) {
            return false;
        }

        PlaceableEntity newStructure = createEntity(player);

        ServiceLocator.getStructurePlacementService().ReplaceStructureAt(newStructure, position, false, false);

        return true;
    }

    @Override
    public PlaceableEntity createEntity(Entity player) {
        return BuildablesFactory.createWall(WallType.intermediate, player);
    }
}
