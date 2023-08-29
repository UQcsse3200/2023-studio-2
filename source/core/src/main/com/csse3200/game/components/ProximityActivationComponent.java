package com.csse3200.game.components;

import com.csse3200.game.entities.Entity;

import java.util.*;

/**
 * This component can be used to call an entry or exit method when on of the tracking
 * entities enters or exits a proximity.
 */
public class ProximityActivationComponent extends Component {
    private final float radius;
    private final ProximityFunc entered;
    private final ProximityFunc exited;
    private final List<Entity> entities;
    private final Map<Entity, Boolean> entityWithinRadiusMap = new HashMap<>();

    public ProximityActivationComponent(float radius, Entity entity,
                                        ProximityFunc entered, ProximityFunc exited) {
        this.radius = radius;
        this.entered = entered;
        this.exited = exited;
        this.entities = new ArrayList<>();
        this.entities.add(entity);
        entityWithinRadiusMap.put(entity, false);
    }

    public ProximityActivationComponent(float radius, List<Entity> entities,
                                        ProximityFunc entered, ProximityFunc exited) {
        this.radius = radius;
        this.entered = entered;
        this.exited = exited;
        this.entities = entities;
        entities.forEach(entity -> entityWithinRadiusMap.put(entity, false));
    }


    /**
     * Checks whether the entity has entered or exited the radius calls the relevant method if so.
     */
    @Override
    public void update() {
        for (Entity entity : entities) {
            boolean isInProximity = entityWithinRadiusMap.get(entity);

            // checks if the entity has entered the radius
            if (!isInProximity && entered != null
                    && entity.getCenterPosition().dst(entity.getCenterPosition()) <= radius) {
                isInProximity = true;
                entered.call(entity);
                // checks if the entity has exited the radius
            } else if (isInProximity && exited != null
                    && entity.getCenterPosition().dst(entity.getCenterPosition()) > radius) {
                isInProximity = false;
                exited.call(entity);
            }
        }
    }


    /**
     * Defines the entry and exit function type.
     */
    public interface ProximityFunc {
        void call(Entity entity);
    }
}
