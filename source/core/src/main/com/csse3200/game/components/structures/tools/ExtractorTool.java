package com.csse3200.game.components.structures.tools;

import com.csse3200.game.components.resources.Resource;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.PlaceableEntity;
import com.csse3200.game.entities.buildables.TurretType;
import com.csse3200.game.entities.factories.BuildablesFactory;
import com.csse3200.game.entities.factories.StructureFactory;

public class ExtractorTool extends PlacementTool {

    @Override
    public PlaceableEntity createEntity(Entity player) {
        return (PlaceableEntity) StructureFactory.createExtractor(100, Resource.Durasteel, 100, 10);
    }
}

