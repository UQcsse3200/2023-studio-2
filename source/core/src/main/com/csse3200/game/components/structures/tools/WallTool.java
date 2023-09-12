package com.csse3200.game.components.structures.tools;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.ObjectMap;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.PlaceableEntity;
import com.csse3200.game.entities.buildables.WallType;
import com.csse3200.game.entities.factories.BuildablesFactory;

public class WallTool extends PlacementTool {
    public WallTool(ObjectMap<String, Integer> cost) {
        super(cost);
    }

    @Override
    public PlaceableEntity createEntity(Entity player) {
        return BuildablesFactory.createWall(WallType.basic, player);
    }

}
