package com.csse3200.game.components.structures;

import com.badlogic.gdx.utils.ObjectMap;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.services.GameStateObserver;
import com.csse3200.game.services.ServiceLocator;


/**
 * Used to charge the player resources when a structure is placed and refund
 * the player resources when a structure is picked up.
 */
public class CostComponent extends Component implements Placeable {

    private final ObjectMap<String, Integer> cost;

    /**
     * Creates a new CostComponent with the given cost.
     * @param cost - the cost to charge the player when placed.
     */
    public CostComponent(ObjectMap<String, Integer> cost) {
        this.cost = cost;
    }

    /**
     * Charges the player the specified cost of the structure.
     */
    @Override
    public void placed() {
        for (var elementCost : cost.entries()) {
            addResource(elementCost.key, -elementCost.value);
        }
    }

    /**
     * Refunds the player the specified cost of the structure proportional to the
     * percentage of health remaining.
     */
    @Override
    public void removed() {
        var combatStatsComponent = entity.getComponent(CombatStatsComponent.class);

        if (combatStatsComponent == null) {
            for (var elementCost : cost.entries()) {
                addResource(elementCost.key, elementCost.value);
            }
        } else {
            var healthPercentage = combatStatsComponent.getHealth() / combatStatsComponent.getMaxHealth();
            // if there is a health component, returns the cost in proportion to the health
            for (var elementCost : cost.entries()) {
                addResource(elementCost.key, elementCost.value * healthPercentage);
            }
        }
    }

    /**
     * Updates the specified resource by the specified amount.
     * @param resource - the resource to update
     * @param amount - the amount to add to the specified resource.
     */
    private void addResource(String resource, int amount) {
        GameStateObserver stateObserver = ServiceLocator.getGameStateObserverService();
        stateObserver.trigger("resourceAdd", resource, amount);
    }

    /**
     * Returns a duplicate of the cost map.
     *
     * @return a duplicate of the cost map.
     */
    public ObjectMap<String, Integer> getCost() {
        return new ObjectMap<>(cost);
    }
}
