/*package com.csse3200.game.components.structures.tools;

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
    protected ToolResponse canInteract(Entity player, GridPoint2 position) {

        // For checking whether the player has clicked on an entity
        Entity clickedEntity = determineSelectedEntity(position);
        // If no entity is clicked, return false
        if (clickedEntity == null) {
            return new ToolResponse(PlacementValidity.INVALID, "No structure to heal");
        }

        // For checking whether the clicked entity has a CombatStatsComponent
        CombatStatsComponent combatStats = clickedEntity.getComponent(CombatStatsComponent.class);
        // If the clicked entity does not have a CombatStatsComponent, return false
        if (combatStats == null) {
            return new ToolResponse(PlacementValidity.INVALID, "Cannot be healed");
        }

        return ToolResponse.valid();
    }

    @Override
    public void performInteraction(Entity player, GridPoint2 position) {
        Entity clickedEntity = determineSelectedEntity(position);
        CombatStatsComponent combatStats = clickedEntity.getComponent(CombatStatsComponent.class);
        // For setting the health of the clicked entity to 100
        combatStats.setHealth(combatStats.getMaxHealth());
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
    private int requiredSolstite;
    private int requiredDurasteel;

    //    private final Sound healingSound;
    public Healing(ObjectMap<String, Integer> cost) {
        super(cost);

       // healingSound = Gdx.audio.newSound(Gdx.files.internal("sounds/healing_sound.wav"));
    }


    /**
     * this class is meant to heal the walls by checking with enough resources
     * also trigger Sound effects while healing
     * @param player - the player interacting with the tool.
     * @param position - the position being interacted with.
     * @return
     */

    @Override
    protected ToolResponse canInteract(Entity player, GridPoint2 position) {

        // For checking whether the player has clicked on an entity
        Entity clickedEntity = determineSelectedEntity(position);
        // If no entity is clicked, return false
        if (clickedEntity == null) {
            return new ToolResponse(PlacementValidity.INVALID, "No structure to heal");
        }

        // For checking whether the clicked entity has a CombatStatsComponent
        CombatStatsComponent combatStats = clickedEntity.getComponent(CombatStatsComponent.class);
        // If the clicked entity does not have a CombatStatsComponent, return false
        if (combatStats == null) {
            return new ToolResponse(PlacementValidity.INVALID, "Cannot be healed");
        }

        // Calculate the healing cost based on the current health deficit
        int currentHealth = combatStats.getHealth();
        int maxHealth = combatStats.getMaxHealth();
        double healthDeficit = 1 - ((double) currentHealth / maxHealth);
        requiredDurasteel = (int) Math.ceil(healthDeficit * 100 * 2); // 2 Durasteel per 1%
        requiredSolstite = (int) Math.ceil(healthDeficit * 100 * 2); // 2 Solstite per 1%

        // Check if the player has enough resources for healing
        if (!playerHasEnoughResources(requiredDurasteel, requiredSolstite)) {
            return new ToolResponse(PlacementValidity.INVALID, "Not enough resources");
        }

        return ToolResponse.valid();
    }

    @Override
    public void performInteraction(Entity player, GridPoint2 position) {// Deduct the required resources
        deductResources(requiredDurasteel, requiredSolstite);

        Entity entity = new Entity();

        Entity clickedEntity = determineSelectedEntity(position);

        entity.getEvents().trigger("playSound","wallHeal");

        CombatStatsComponent combatStats = clickedEntity.getComponent(CombatStatsComponent.class);
        // For setting the health of the clicked entity to 100
        combatStats.setHealth(combatStats.getMaxHealth());
    }

    // Determine the selected entity based on the player's click position
    private Entity determineSelectedEntity(GridPoint2 position) {
        return ServiceLocator.getStructurePlacementService().getStructureAt(position);
    }

    // Check if the player has enough Durasteel and Solstite for healing
    private boolean playerHasEnoughResources(int requiredDurasteel, int requiredSolstite) {
        try {
            int playerDurasteel = (int)ServiceLocator.getGameStateObserverService()
                    .getStateData("resource/" + Resource.Durasteel);
            int playerSolstite = (int)ServiceLocator.getGameStateObserverService()
                    .getStateData("resource/" + Resource.Solstite);

            return playerDurasteel >= requiredDurasteel && playerSolstite >= requiredSolstite;
        } catch (NullPointerException e) {
            return false;
        }
    }

    // Deduct the required Durasteel and Solstite from the player's resources
    private void deductResources(int requiredDurasteel, int requiredSolstite) {
        ServiceLocator.getGameStateObserverService().trigger("resourceAdd", Resource.Durasteel.toString(), -requiredDurasteel);
        ServiceLocator.getGameStateObserverService().trigger("resourceAdd", Resource.Solstite.toString(), -requiredSolstite);
    }
}

