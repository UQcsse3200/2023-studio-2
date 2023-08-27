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


    /**
     * Checks whether the entity has entered or exited the radius calls the relevant method if so.
     */
    @Override
    public void update() {
        // checks if the entity has entered the radius
        if (!isInProximity && entered != null
                && entity.getCenterPosition().dst(trackingEntity.getCenterPosition()) <= radius) {
            isInProximity = true;
            entered.call(trackingEntity);
        // checks if the entity has exited the radius
        } else if (isInProximity && exited != null
                && entity.getCenterPosition().dst(trackingEntity.getCenterPosition()) > radius) {
            isInProximity = false;
            exited.call(trackingEntity);
        }
    }


    /**
     * This interface is used to allow a function to be passed as a parameter in the constructor.
     */
    public interface ProximityFunc {
        void call(Entity entity);
    }
}
