package com.csse3200.game.components.structures.tools;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.ObjectMap;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;

public class Healing extends Tool {

    public Healing(ObjectMap<String, Integer> cost) {
        super(cost);
    }

    @Override
    public boolean interact(Entity player, GridPoint2 position) {
        // For checking whether the player has clicked on an entity
        Entity clickedEntity = determineSelectedEntity(position);

        if (clickedEntity != null) {
            // For checking whether the clicked entity has a CombatStatsComponent
            CombatStatsComponent combatStats = clickedEntity.getComponent(CombatStatsComponent.class);

            if (combatStats != null) {
                // For setting the health of the clicked entity to 100
                combatStats.setHealth(combatStats.getMaxHealth());
                return true;

            }
        }
        return false;
    }

    private Entity determineSelectedEntity(GridPoint2 position) {
        var existingStructure = ServiceLocator.getStructurePlacementService().getStructureAt(position);

        return existingStructure;
        // For iterating through all entities in the game
       /* for (Entity entity : ServiceLocator.getEntityService().getEntitiesByComponent((CombatStatsComponent.class))) {
            // for getting the position and scale of the entity
            Vector2 entityPosition = entity.getPosition();
            Vector2 entityScale = entity.getScale();

            // for calculating the boundaries of the entity's bounding box
            float left = entityPosition.x - entityScale.x / 2;
            float right = entityPosition.x + entityScale.x / 2;
            float top = entityPosition.y + entityScale.y / 2;
            float bottom = entityPosition.y - entityScale.y / 2;

            // For checking whether the click position is within the boundaries of the entity
            if (position.x >= left && position.x <= right && position.y >= bottom && position.y <= top) {
                // The click position is within this entity's boundaries, so it's selected
                return entity;
            }
        }

        // if no entity was found at the click position
        return null;*/
    }

}



