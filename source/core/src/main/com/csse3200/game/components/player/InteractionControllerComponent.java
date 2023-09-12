package com.csse3200.game.components.player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.InteractableComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;

/**
 * InteractionControllerComponent
 * It allows entities to interact with each other through the service locator and the interact() method
 */
public class InteractionControllerComponent extends Component {
    private boolean effectAll;
    public InteractionControllerComponent(boolean effectAll) {
        this.effectAll = effectAll;
    }

    /**
     * Interact will use the service locator to interact to enumerate through all entities that
     * have interact component, and will call interact() with between the two entities.
     */
    public void interact() {
        Array<Entity> entities = ServiceLocator.getEntityService().getEntitiesByComponent(InteractableComponent.class);
        float minDist = Float.MAX_VALUE;
        Entity closest = null;
        for (Entity target : entities) {
            if (effectAll) {
                target.getComponent(InteractableComponent.class).interact(entity);
                continue;
            }

            float distance = entity.getCenterPosition().dst(target.getCenterPosition());
            if (distance < minDist) {
                minDist = distance;
                closest = target;
            }
        }
        //If you are not interacting with ALL entities, and you have a closest entity, make sure you interact with
        // whatever entity is closest to this entity
        if (!effectAll && closest != null) {
            closest.getComponent(InteractableComponent.class).interact(entity);
        }
    }
}
