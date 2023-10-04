package com.csse3200.game.components;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;

import java.util.List;

public class ProximityControllerComponent extends Component {
    public void checkAllEntitiesProximity() {
        List<Entity> entities = ServiceLocator.getEntityService().getEntitiesByComponent(DistanceCheckComponent.class);
        for (Entity target : entities) {
            target.getComponent(DistanceCheckComponent.class).checkDistance(entity);
        }
    }
}

