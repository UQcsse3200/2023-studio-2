package com.csse3200.game.services;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.EventHandler;
import com.badlogic.gdx.math.Vector2;
import java.util.Map;

/**
 * Service to allow entities to be placed after game initilisation
 * Code is a modified version of StructurePlacementServic written by team 7
 * Kept Seperate due to differences in implementation and functionality
 */
public class EntityPlacementService {
    EventHandler handler;

    public EntityPlacementService(EventHandler handler) {
        this.handler = handler;
    }

    /**
     * Function relay place entity call to listeners - passes provided entity
     * @param entity - entity to be relayed
     */
    public void PlaceEntity(Entity entity) {
        handler.trigger("placeEntity", entity);
    }

    /**
     * FUnction to relay an entity and a position
     * @param entity - the entity to be relayed
     * @param position - a Vector 2 indicatoring a position to be relayed
     */
    public void PlaceEntityAt(Entity entity, Vector2 position) {
        handler.trigger("placeEntityAt", entity, position);
    }
}
