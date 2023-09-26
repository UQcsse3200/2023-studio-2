/*package com.csse3200.game.components.structures.tools;

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
        // If no entity is clicked, return false
        if (clickedEntity == null) {
            return false;
        }

        // For checking whether the clicked entity has a CombatStatsComponent
        CombatStatsComponent combatStats = clickedEntity.getComponent(CombatStatsComponent.class);
        // If the clicked entity does not have a CombatStatsComponent, return false
        if (combatStats == null) {
            return false;
        }

        // For setting the health of the clicked entity to 100
        combatStats.setHealth(combatStats.getMaxHealth());
        return true;
    }
    // Determine the selected entity based on the player's click position
    private Entity determineSelectedEntity(GridPoint2 position) {
        return ServiceLocator.getStructurePlacementService().getStructureAt(position);
    }
}

**/

package com.csse3200.game.components.structures.tools;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.ObjectMap;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.components.resources.Resource;

public class Healing extends Tool {

    public Healing(ObjectMap<String, Integer> cost) {
        super(cost);
    }

    @Override
    public boolean interact(Entity player, GridPoint2 position) {
        // For checking whether the player has clicked on an entity
        Entity clickedEntity = determineSelectedEntity(position);
        // If no entity is clicked, return false
        if (clickedEntity == null) {
            return false;
        }

        // For checking whether the clicked entity has a CombatStatsComponent
        CombatStatsComponent combatStats = clickedEntity.getComponent(CombatStatsComponent.class);
        // If the clicked entity does not have a CombatStatsComponent, return false
        if (combatStats == null) {
            return false;
        }

        // Calculate the healing cost based on the current health deficit
        int currentHealth = combatStats.getHealth();
        int maxHealth = combatStats.getMaxHealth();
        double healthDeficit = 1 - ((double) currentHealth / maxHealth);
        int requiredDurasteel = (int) Math.ceil(healthDeficit * 100 * 2); // 2 Durasteel per 1%
        int requiredSolstite = (int) Math.ceil(healthDeficit * 100 * 2); // 2 Solstite per 1%

        // Check if the player has enough resources for healing
        if (playerHasEnoughResources(requiredDurasteel, requiredSolstite)) {
            // Deduct the required resources
            deductResources(requiredDurasteel, requiredSolstite);

            combatStats.setHealth(maxHealth);
            return true;
        } else {
            // Player doesn't have enough resources for healing
            return false;
        }
    }

    // Determine the selected entity based on the player's click position
    private Entity determineSelectedEntity(GridPoint2 position) {
        return ServiceLocator.getStructurePlacementService().getStructureAt(position);
    }

    // Check if the player has enough Durasteel and Solstite for healing
    private boolean playerHasEnoughResources(int requiredDurasteel, int requiredSolstite) {
        int playerDurasteel = (int)ServiceLocator.getGameStateObserverService().getStateData("resource/" + Resource.Durasteel.toString());
        int playerSolstite = (int)ServiceLocator.getGameStateObserverService().getStateData("resource/" + Resource.Solstite.toString());

        return playerDurasteel >= requiredDurasteel && playerSolstite >= requiredSolstite;
    }

    // Deduct the required Durasteel and Solstite from the player's resources
    private void deductResources(int requiredDurasteel, int requiredSolstite) {
        ServiceLocator.getGameStateObserverService().trigger("resourceAdd", Resource.Durasteel.toString(), -requiredDurasteel);
        ServiceLocator.getGameStateObserverService().trigger("resourceAdd", Resource.Solstite.toString(), -requiredSolstite);
    }
}

