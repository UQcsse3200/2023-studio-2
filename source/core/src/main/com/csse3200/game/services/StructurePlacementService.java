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
    private final Map<PlaceableEntity, GridPoint2> position = new HashMap<>();


    public StructurePlacementService(EventHandler handler) {
        this.handler = handler;
    }

    /**
     * Gets the position of the given entity.
     *
     * @param searchEntity - the entity to get the position of.
     * @return the position of the entity.
     */
    public GridPoint2 getStructurePosition(PlaceableEntity searchEntity) {
        return position.get(searchEntity);
    }

    public void placeStructureAt(PlaceableEntity entity, GridPoint2 tilePos) {
        if (!canPlaceStructureAt(entity, tilePos)) {
            return;
        }

        entity.willPlace();

        position.put(entity, tilePos);

        for (int x = tilePos.x; x < (tilePos.x + entity.getWidth()); x++) {
            for (int y = tilePos.y; y < (tilePos.y + entity.getHeight()); y++) {
                placedStructures.put(new GridPoint2(x, y), entity);
            }
        }

        handler.trigger("placeStructureAt", new placeStructureAtArgs(entity, tilePos));
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

    public void replaceStructureAt(PlaceableEntity entity, GridPoint2 tilePos) {
        removeStructureAt(tilePos, true);

        placeStructureAt(entity, tilePos);
    }

    public void spawnEntityAtVector(Entity entity, Vector2 worldPos) {
        handler.trigger("fireBullet", new spawnEntityAtVectorArgs(entity, worldPos));
    }

    public void removeStructureAt(GridPoint2 tilePos) {
        removeStructureAt(tilePos, false);
    }

    public void removeStructureAt(GridPoint2 tilePos, boolean force) {
        var entity = placedStructures.get(tilePos);

        if (entity == null || entity.isIrremovable() && !force) {
            return;
        }

        entity.willRemove();

        tilePos = position.get(entity);

        if (tilePos != null) {
            for (int x = tilePos.x; x < (tilePos.x + entity.getWidth()); x++) {
                for (int y = tilePos.y; y < (tilePos.y + entity.getHeight()); y++) {
                    placedStructures.remove(new GridPoint2(x, y));
                }
            }
        }

        entity.removed();

        Gdx.app.postRunnable(entity::dispose);
    }

    public PlaceableEntity getStructureAt(GridPoint2 position) {
        return placedStructures.get(position);
    }

    public void removeStructure(PlaceableEntity entity) {
        var position = getStructurePosition(entity);

        if (position != null) {
            removeStructureAt(position);
        }
    }



    public static class placeStructureAtArgs {
        private Entity entity;
        private GridPoint2 tilePos;

        public placeStructureAtArgs(Entity entity, GridPoint2 tilePos) {
            this.setEntity(entity);
            this.setTilePos(tilePos);
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
