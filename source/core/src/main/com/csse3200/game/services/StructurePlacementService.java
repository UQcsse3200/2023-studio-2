package com.csse3200.game.services;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.EventHandler;

public class StructurePlacementService {
    EventHandler handler;

    public StructurePlacementService(EventHandler handler) {
        this.handler = handler;
    }

    public void PlaceStructure(Entity entity) {
        handler.trigger("placeStructure", entity);
    }

    public void PlaceStructureAt(Entity entity, GridPoint2 tilePos, boolean centerX, boolean centerY) {
        handler.trigger("placeStructureAt", new PlaceStructureAtArgs(entity, tilePos, centerX, centerY));
    }

    public static class PlaceStructureAtArgs {
        public Entity entity;
        public GridPoint2 tilePos;
        public boolean centerX;
        public boolean centerY;

        public PlaceStructureAtArgs(Entity entity, GridPoint2 tilePos, boolean centerX, boolean centerY) {
            this.entity = entity;
            this.tilePos = tilePos;
            this.centerX = centerX;
            this.centerY = centerY;
        }
    }
}
