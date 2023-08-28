package com.csse3200.game.services;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.EventHandler;
import com.badlogic.gdx.math.Vector2;
import java.util.HashMap;
import java.util.Map;

public class StructurePlacementService {
    EventHandler handler;
    private final Map<GridPoint2, Entity> placedStructures = new HashMap<>();


    public StructurePlacementService(EventHandler handler) {
        this.handler = handler;
    }

    public void PlaceStructure(Entity entity) {
        handler.trigger("rtEntity", entity);
    }

    public void PlaceStructureAt(Entity entity, Vector2 position) {
        handler.trigger("rtEntityAt", new PlaceStructureAtArgs(entity, position));
    }

    public static class PlaceStructureAtArgs {
        public Entity entity;
        public Vector2 position;

        public PlaceStructureAtArgs(Entity entity, Vector2 position) {
            this.entity = entity;
            this.position = position;
        }
    }
}
