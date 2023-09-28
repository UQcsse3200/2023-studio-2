package com.csse3200.game.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.PlaceableEntity;
import com.csse3200.game.events.EventHandler;

import java.util.HashMap;
import java.util.Map;

public class StructurePlacementService {
    EventHandler handler;
    private final Map<GridPoint2, PlaceableEntity> placedStructures = new HashMap<>();


    public StructurePlacementService(EventHandler handler) {
        this.handler = handler;
    }

    public void placeStructure(Entity entity) {
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
    public void placeStructureAt(PlaceableEntity entity, GridPoint2 tilePos, boolean centerX, boolean centerY) {
        entity.willPlace();
        enterIntoMap(entity, tilePos);
        handler.trigger("placeStructureAt", new placeStructureAtArgs(entity, tilePos, centerX, centerY));
        entity.placed();
    }

    public boolean canPlaceStructureAt(PlaceableEntity entity, GridPoint2 tilePos) {
        for (int x = tilePos.x; x < (tilePos.x + entity.getWidth()); x++) {
            for (int y = tilePos.y; y < (tilePos.y + entity.getHeight()); y++) {
                if (getStructureAt(new GridPoint2(x, y)) != null) {
                    return false;
                }
            }
        }

        return true;
    }

    public void enterIntoMap(PlaceableEntity entity, GridPoint2 tilePos) {
        entity.setGridPosition(tilePos);

        for (int x = tilePos.x; x < (tilePos.x + entity.getWidth()); x++) {
            for (int y = tilePos.y; y < (tilePos.y + entity.getHeight()); y++) {
                placedStructures.put(new GridPoint2(x, y), entity);
            }
        }
    }

    public void removeFromMap(GridPoint2 tilePos) {
        var entity = getStructureAt(tilePos);

        tilePos = entity.getGridPosition();

        for (int x = tilePos.x; x < (tilePos.x + entity.getWidth()); x++) {
            for (int y = tilePos.y; y < (tilePos.y + entity.getHeight()); y++) {
                placedStructures.remove(new GridPoint2(x, y));
            }
        }
    }

    public void replaceStructureAt(PlaceableEntity entity, GridPoint2 tilePos, boolean centerX, boolean centerY) {
        removeStructureAt(tilePos);

        entity.willPlace();
        enterIntoMap(entity, tilePos);
        handler.trigger("placeStructureAt", new placeStructureAtArgs(entity, tilePos, centerX, centerY));
        entity.placed();
    }

    public void spawnEntityAtVector(Entity entity, Vector2 worldPos) {
        handler.trigger("fireBullet", new spawnEntityAtVectorArgs(entity, worldPos));
    }

    public void removeStructureAt(GridPoint2 tilePos) {
        var entity = placedStructures.get(tilePos);

        if (entity == null || entity.is_irremovable()) {
            return;
        }

        entity.willRemove();
        removeFromMap(tilePos);
        entity.removed();

        Gdx.app.postRunnable(entity::dispose);
    }
    public PlaceableEntity getStructureAt(GridPoint2 position) {
        return placedStructures.get(position);
    }

    public static class placeStructureAtArgs {
        private Entity entity;
        private GridPoint2 tilePos;
        private boolean centerX;
        private boolean centerY;

        public placeStructureAtArgs(Entity entity, GridPoint2 tilePos, boolean centerX, boolean centerY) {
            this.setEntity(entity);
            this.setTilePos(tilePos);
            this.setCenterX(centerX);
            this.setCenterY(centerY);
        }

        public Entity getEntity() {
            return entity;
        }

        public void setEntity(Entity entity) {
            this.entity = entity;
        }

        public GridPoint2 getTilePos() {
            return tilePos;
        }

        public void setTilePos(GridPoint2 tilePos) {
            this.tilePos = tilePos;
        }

        public boolean isCenterX() {
            return centerX;
        }

        public void setCenterX(boolean centerX) {
            this.centerX = centerX;
        }

        public boolean isCenterY() {
            return centerY;
        }

        public void setCenterY(boolean centerY) {
            this.centerY = centerY;
        }
    }

    public static class spawnEntityAtVectorArgs {
        private Entity entity;
        private Vector2 worldPos;

        public spawnEntityAtVectorArgs(Entity entity, Vector2 worldPos) {
            this.setEntity(entity);
            this.setWorldPos(worldPos);
        }

        public Entity getEntity() {
            return entity;
        }

        public void setEntity(Entity entity) {
            this.entity = entity;
        }

        public Vector2 getWorldPos() {
            return worldPos;
        }

        public void setWorldPos(Vector2 worldPos) {
            this.worldPos = worldPos;
        }
    }
}
