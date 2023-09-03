package com.csse3200.game.services;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.EventHandler;

import java.util.HashMap;
import java.util.Map;

public class StructurePlacementService {
    EventHandler handler;
    private final Map<GridPoint2, Entity> placedStructures = new HashMap<>();


    public StructurePlacementService(EventHandler handler) {
        this.handler = handler;
    }

    public void PlaceStructure(Entity entity) {
        handler.trigger("placeStructure", entity);
    }

    /**
     * Gets the position of the given entity.
     *
     * @param searchEntity - the entity to get the position of.
     * @return the position of the entity.
     */
    public GridPoint2 getStructurePosition(Entity searchEntity) {
        for (var entry : placedStructures.entrySet()) {
            if (entry.getValue() == searchEntity) {
                return entry.getKey();
            }
        }

        return null;
    }

    public void PlaceStructureAt(Entity entity, GridPoint2 tilePos, boolean centerX, boolean centerY) {
        placedStructures.put(tilePos, entity);
        handler.trigger("placeStructureAt", new PlaceStructureAtArgs(entity, tilePos, centerX, centerY));
    }
    public void SpawnEntityAtVector(Entity entity, Vector2 worldPos) {
        handler.trigger("fireBullet", new SpawnEntityAtVectorArgs(entity, worldPos));
    }

    public void removeStructureAt(GridPoint2 tilePos) {
        placedStructures.remove(tilePos);
    }
    public Entity getStructureAt(GridPoint2 position) {
        return placedStructures.get(position);
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

    public static class SpawnEntityAtVectorArgs {
        public Entity entity;
        public Vector2 worldPos;

        public SpawnEntityAtVectorArgs(Entity entity, Vector2 worldPos) {
            this.entity = entity;
            this.worldPos = worldPos;
        }
    }
}
