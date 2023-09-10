package com.csse3200.game.components.structures;

import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.services.GameStateObserver;
import com.csse3200.game.services.ServiceLocator;

import java.util.Map;

public class CostComponent extends Component implements Placeable {

    private final Map<String, Integer> cost;
    public CostComponent(Map<String, Integer> cost) {
        this.cost = cost;
    }

    @Override
    public void placed() {
        GameStateObserver stateObserver = ServiceLocator.getGameStateObserverService();
        for (var elementCost : cost.entrySet()) {
            stateObserver.trigger("resourceAdd", elementCost.getKey(), -elementCost.getValue());
        }
    }

    @Override
    public void removed() {

    }
}
