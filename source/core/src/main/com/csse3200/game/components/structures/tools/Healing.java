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
        double currentHealth = combatStats.getHealth();
        double maxHealth = combatStats.getMaxHealth();
        double healthDeficit = maxHealth - currentHealth;
        int requiredDurasteel = (int) (healthDeficit * 0.01 * 2); // 2 Durasteel per 1%
        int requiredSolstite = (int) (healthDeficit * 0.01 * 2); // 2 Solstite per 1%

        // Check if the player has enough resources for healing
        if (playerHasEnoughResources(requiredDurasteel, requiredSolstite)) {
            // Deduct the required resources
            deductResources(requiredDurasteel, requiredSolstite);

            // Calculate the healing percentage and apply healing
            double healingPercentage = healthDeficit / maxHealth;
            combatStats.setHealth((int) (currentHealth + maxHealth * healingPercentage));
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
        int playerDurasteel = ServiceLocator.getGameStateObserverService().getPlayerResource(Resource.Durasteel);
        int playerSolstite = ServiceLocator.getGameStateObserverService().getPlayerResource(Resource.Solstite);

        return playerDurasteel >= requiredDurasteel && playerSolstite >= requiredSolstite;
    }

    // Deduct the required Durasteel and Solstite from the player's resources
    private void deductResources(int requiredDurasteel, int requiredSolstite) {
        ServiceLocator.getGameStateObserverService().modifyPlayerResource(Resource.Durasteel, -requiredDurasteel);
        ServiceLocator.getGameStateObserverService().modifyPlayerResource(Resource.Solstite, -requiredSolstite);
    }
}

