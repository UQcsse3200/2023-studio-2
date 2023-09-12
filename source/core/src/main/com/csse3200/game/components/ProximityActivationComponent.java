package com.csse3200.game.components;

import com.csse3200.game.entities.Entity;

import java.util.*;

/**
 * This component can be used to call an entry or exit method when on of the tracking
 * entities enters or exits a proximity.
 */
public class ProximityActivationComponent extends Component {
    protected final float radius;
    protected final ProximityFunc entered;
    protected final ProximityFunc exited;
    private List<Entity> entities = new ArrayList<>();
    private final Map<Entity, Boolean> entityWithinRadiusMap = new HashMap<>();

    public ProximityActivationComponent(float radius, Entity entity,
                                        ProximityFunc entered, ProximityFunc exited) {
        this.radius = radius;
        this.entered = entered;
        this.exited = exited;
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

    public ProximityActivationComponent(float radius, ProximityFunc entered, ProximityFunc exited) {
        this.radius = radius;
        this.entered = entered;
        this.exited = exited;
    }

    /**
     * Checks whether the entity has entered or exited the radius calls the relevant method if so.
     */
    @Override
    public void update() {
        for (Entity entity : entities) {
            boolean isInProximity = entityWithinRadiusMap.get(entity);

            if (!isInProximity && entityIsInProximity(entity)) {
                entityWithinRadiusMap.put(entity, true);
                entered.call(entity);
            } else if (isInProximity && !entityIsInProximity(entity)) {
                entityWithinRadiusMap.put(entity, false);
                exited.call(entity);
            }
            else if (isInProximity) {
                entered.call(entity);
            }
        }
    }

    public boolean entityIsInProximity(Entity entity) {
        // Calculate the distance between the entity and the center position
        float distance = this.entity.getCenterPosition().dst(entity.getCenterPosition());
        return distance <= radius;
    }

    public interface ProximityFunc {
        void call(Entity entity);
    }
}