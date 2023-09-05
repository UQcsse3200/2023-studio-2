package com.csse3200.game.entities;

import com.csse3200.game.services.GameStateInteraction;
import com.csse3200.game.services.GameStateObserver;
import com.csse3200.game.services.ServiceLocator;

import javax.xml.crypto.dsig.keyinfo.KeyValue;
import java.util.Map;

public class PlaceableEntity extends Entity {
    private final Map<String, Integer> cost;

    public PlaceableEntity(Map<String, Integer> cost) {
        super();

        this.cost = cost;
    }

    /**
     * This function is called when a PlaceableEntity is placed via the structure
     * placement service. Override this method to implement specific functionality
     * when placed.
     */
    public void placed() {
        GameStateObserver stateObserver = ServiceLocator.getGameStateObserverService();
        for (var elementCost : cost.entrySet()) {
            stateObserver.trigger("resourceAdd", elementCost.getKey(), -elementCost.getValue());
        }
    }

    /**
     * This function is called when a PlaceableEntity is removed via the structure
     * placement service. Override this method to implement specific functionality
     * when removed.
     */
    public void removed() {

    }

    /**
     * @return the cost of the entity.
     */
    public Map<String, Integer> getCost() {
        return cost;
    }
}
