package com.csse3200.game.components.structures.tools;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.ObjectMap;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.services.ServiceLocator;

public class Healing extends Tool {

    public Healing(ObjectMap<String, Integer> cost) {
        super(cost);
    }

    @Override
    public boolean interact(Entity player, GridPoint2 position) {
        // For checking whether the player has clicked on an entity
        Entity clickedEntity = determineSelectedEntity(position);

        if (clickedEntity == null) {
            return false;
        }

        // For checking whether the clicked entity has a CombatStatsComponent
        CombatStatsComponent combatStats = clickedEntity.getComponent(CombatStatsComponent.class);

        if (combatStats == null) {
            return false;
        }

        // For setting the health of the clicked entity to 100
        combatStats.setHealth(combatStats.getMaxHealth());
        return true;
    }

    private Entity determineSelectedEntity(GridPoint2 position) {
        return ServiceLocator.getStructurePlacementService().getStructureAt(position);
    }
}



