package com.csse3200.game.components;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;

public class ProximityTriggerComponent extends Component {
    private final Class<? extends Component>  flagComponent;
    private final float radius;
    private final TriggerFunc func;

    public ProximityTriggerComponent(Class<? extends Component> flagComponent, float radius, TriggerFunc func) {

        this.flagComponent = flagComponent;
        this.radius = radius;
        this.func = func;
    }

    @Override
    public void update() {
        for (var otherEntity : ServiceLocator.getEntityService().getEntitiesByComponent(flagComponent)) {
            if (entity.getPosition().dst(otherEntity.getPosition()) <= radius) {
                func.call();
                return;
            }
        }
    }

    public interface TriggerFunc {
        void call();
    }
}
