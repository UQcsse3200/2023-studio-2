package com.csse3200.game.components.structures.tools;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.PlaceableEntity;
import com.csse3200.game.entities.buildables.TurretType;
import com.csse3200.game.entities.factories.BuildablesFactory;

import java.util.ArrayList;

public class TurretTool extends PlacementTool {

    @Override
    public PlaceableEntity createEntity(Entity player) {
        return BuildablesFactory.createCustomTurret(TurretType.levelOne, player);
    }

}
