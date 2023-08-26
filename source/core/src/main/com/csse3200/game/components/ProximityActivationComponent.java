package com.csse3200.game.components;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.listeners.EventListener1;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.services.ServiceLocator;

import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * This component can be used to call a given method when the given entity enters
 * the given proximity. An exit method can also be configured.
 */
public class ProximityActivationComponent extends Component {
    private final float radius;
    private final ProximityFunc entered;
    private final ProximityFunc exited;
    private final Entity trackingEntity;
    private boolean isInProximity = false;

    public ProximityActivationComponent(float radius, Entity entity,
                                        ProximityFunc entered, ProximityFunc exited) {
        this.radius = radius;
        this.entered = entered;
        this.exited = exited;
        this.trackingEntity = entity;
    }

    @Override
    public void update() {
        if (!isInProximity && entered != null
                && entity.getPosition().dst(trackingEntity.getPosition()) <= radius) {
            isInProximity = true;
            entered.call(trackingEntity);
        } else if (isInProximity && exited != null
                && entity.getPosition().dst(trackingEntity.getPosition()) > radius) {
            isInProximity = false;
            exited.call(trackingEntity);
        }
    }

    public interface ProximityFunc {
        void call(Entity entity);
    }
}
