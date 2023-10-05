package com.csse3200.game.components;

import com.csse3200.game.services.ServiceLocator;

/**
 * Used to trigger a single function if there is an entity with the given flag
 * component within the defined radius.
 */
public class ProximityTriggerComponent extends Component {
    private final Class<? extends Component>  flagComponent;
    private final float radius;
    private final TriggerFunc func;

    /**
     * Creates a new ProximityTriggerComponent
     * @param flagComponent - the component used to identify entities which can trigger the function.
     * @param radius - the proximity from the entity the other entity must be to trigger the function.
     * @param func - the function to be triggered.
     */
    public ProximityTriggerComponent(Class<? extends Component> flagComponent, float radius, TriggerFunc func) {
        this.flagComponent = flagComponent;
        this.radius = radius;
        this.func = func;
    }

    /**
     * Checks if any of the entities with the flagComponent are within the given radius. If there is one,
     * calls the function and stops.
     */
    @Override
    public void update() {
        for (var otherEntity : ServiceLocator.getEntityService().getEntitiesByComponent(flagComponent)) {
            if (entity.getPosition().dst(otherEntity.getPosition()) <= radius) {
                func.call();
                return;
            }
        }
    }

    /**
     * An interface allowing a function to be passed to the component.
     */
    public interface TriggerFunc {
        void call();
    }
}
