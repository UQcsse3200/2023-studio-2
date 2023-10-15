package com.csse3200.game.components.structures.tools;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.ObjectMap;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.components.resources.Resource;

/**
 * This tool allows the user to heal structures.
 */
public class Healing extends Tool {
    private int requiredSolstite;
    private int requiredDurasteel;

    /**
     * Creates a new healing tool with the given cost.
     * @param cost - the cost of the healing tool.
     */
    public Healing(ObjectMap<String, Integer> cost, int ordering, String texture) {
        super(cost, ordering, texture);
    }


    /**
     * this class is meant to heal the walls by checking with enough resources
     * also trigger Sound effects while healing
     * @param player - the player interacting with the tool.
     * @param position - the position being interacted with.
     * @return a ToolResponse containing whether the position is valid.
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

    /**
     * Heals the structure at the given position.
     *
     * @param player - the player using the tool.
     * @param position - the position to use the tool.
     */
    @Override
    protected void performInteraction(Entity player, GridPoint2 position) {// Deduct the required resources
        deductResources(requiredDurasteel, requiredSolstite);

        Entity entity = new Entity();

        Entity clickedEntity = determineSelectedEntity(position);

        entity.getEvents().trigger("playSound","wallHeal");

        CombatStatsComponent combatStats = clickedEntity.getComponent(CombatStatsComponent.class);
        // For setting the health of the clicked entity to 100
        combatStats.setHealth(combatStats.getMaxHealth());
    }

    /**
     * Gets the clicked on structure.
     * @param position - the click position.
     * @return the structure clicked on.
     */
    private Entity determineSelectedEntity(GridPoint2 position) {
        return ServiceLocator.getStructurePlacementService().getStructureAt(position);
    }

    /**
     * Checks if the player has enough resources to use the tool.
     * @param requiredDurasteel - how much durasteel they need.
     * @param requiredSolstite - how much solstite they need.
     * @return whether the player has sufficient resources.
     */
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

    /**
     * Removes the given resources from the player.
     * @param requiredDurasteel - the amount of durasteel to remove.
     * @param requiredSolstite - the amount of solstite to remove.
     */
    private void deductResources(int requiredDurasteel, int requiredSolstite) {
        ServiceLocator.getGameStateObserverService().trigger("resourceAdd",
                Resource.Durasteel.toString(), -requiredDurasteel);
        ServiceLocator.getGameStateObserverService().trigger("resourceAdd",
                Resource.Solstite.toString(), -requiredSolstite);
    }
}

