package com.csse3200.game.components.structures.tools;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.components.resources.Resource;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.PlaceableEntity;
import com.csse3200.game.entities.factories.StructureFactory;

public class ExtractorTool extends PlacementTool {

    @Override
    public PlaceableEntity createEntity(Entity player) {
        return StructureFactory.createExtractor(100, Resource.Durasteel, 100, 10);
    }

}

