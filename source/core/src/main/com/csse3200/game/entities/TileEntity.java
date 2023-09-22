package com.csse3200.game.entities;

import com.badlogic.gdx.math.GridPoint2;

public class TileEntity extends Entity {
    private final GridPoint2 tilePosition;
    private final Entity entity;

    public TileEntity(GridPoint2 tilePosition, Entity entity) {
        this.tilePosition = tilePosition;
        this.entity = entity;
    }

    public GridPoint2 getTilePosition() {
        return tilePosition;
    }

    public Entity getEntity() {
        return entity;
    }
}