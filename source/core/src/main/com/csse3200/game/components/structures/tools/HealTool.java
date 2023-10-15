package com.csse3200.game.components.structures.tools;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.ObjectMap;
import com.csse3200.game.components.structures.CostComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.components.resources.Resource;

import java.util.HashMap;
import java.util.Map;

/**
 * This tool allows the user to heal structures.
 */
public class HealTool extends Tool {
    private HashMap<Resource, Integer> requiredResources;

    /**
     * Creates a new healing tool with the given cost.
     * @param cost - the cost of the healing tool.
     */
    public HealTool(ObjectMap<String, Integer> cost) {
        super(cost);
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

        int healAmount = combatStats.getMaxHealth() - combatStats.getHealth();

        float healPercent = (float) healAmount / combatStats.getMaxHealth();

        if (healPercent == 0) {
            return new ToolResponse(PlacementValidity.INVALID, "Already full health");
        }

        // Calculate the healing cost based on the current health deficit'
        requiredResources = new HashMap<>();

        // retrieve cost of component
        var costComponent = clickedEntity.getComponent(CostComponent.class);

        // if placed with tool, this should never evaluate to true, however better safe than sorry ;)
        if (costComponent == null) {
            return new ToolResponse(PlacementValidity.INVALID, "Cannot be healed");
        }

        for (var costEntry : costComponent.getCost()) {
            requiredResources.put(Resource.valueOf(costEntry.key), (int)Math.ceil(costEntry.value * healPercent));
        }

        // Check if the player has enough resources for healing
        if (!playerHasEnoughResources(requiredResources)) {
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
        deductResources(requiredResources);

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
     * @param requiredResources - a map containing the required resources and their costs.
     * @return whether the player has sufficient resources.
     */
    private boolean playerHasEnoughResources(Map<Resource, Integer> requiredResources) {
        try {
            for (var costEntry : requiredResources.entrySet()) {
                int resources = (int)ServiceLocator.getGameStateObserverService()
                        .getStateData("resource/" + costEntry.getKey());

                if (resources < costEntry.getValue()) {
                    return false;
                }
            }
        } catch (NullPointerException e) {
            return false;
        }

        return true;
    }

    /**
     * Removes the given resources from the player.
     * @param requiredResources - the amount of each resource required.
     */
    private void deductResources(Map<Resource, Integer> requiredResources) {
        for (var costEntry : requiredResources.entrySet()) {
            ServiceLocator.getGameStateObserverService().trigger("resourceAdd",
                    costEntry.getKey().toString(), -costEntry.getValue());
        }
    }
}

