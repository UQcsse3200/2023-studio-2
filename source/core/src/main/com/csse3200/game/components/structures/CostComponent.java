package com.csse3200.game.components.structures;

import com.badlogic.gdx.utils.ObjectMap;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.services.GameStateObserver;
import com.csse3200.game.services.ServiceLocator;

public class CostComponent extends Component implements Placeable {

    private final ObjectMap<String, Integer> cost;
    public CostComponent(ObjectMap<String, Integer> cost) {
        this.cost = cost;
    }

    @Override
    public void placed() {
        GameStateObserver stateObserver = ServiceLocator.getGameStateObserverService();
        for (var elementCost : cost.entries()) {
            stateObserver.trigger("resourceAdd", elementCost.key, -elementCost.value);
        }
    }

    @Override
    public void removed() {
        var combatStatsComponent = entity.getComponent(CombatStatsComponent.class);
        GameStateObserver stateObserver = ServiceLocator.getGameStateObserverService();

        if (combatStatsComponent == null) {
            for (var elementCost : cost.entries()) {
                stateObserver.trigger("resourceAdd", elementCost.key, elementCost.value);
            }
        } else {
            var healthPercentage = combatStatsComponent.getHealth() / combatStatsComponent.getMaxHealth();
            // if there is a health component, returns the cost in proportion to the health
            for (var elementCost : cost.entries()) {
                stateObserver.trigger("resourceAdd", elementCost.key, elementCost.value * healthPercentage);
            }
        }
    }
}
