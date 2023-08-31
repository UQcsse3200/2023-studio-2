/**
 * This class represents the interaction controller component for a companion character in the game.
 * It provides methods for interacting with other entities in the game world.
 */
package com.csse3200.game.components.Companion;

import com.badlogic.gdx.utils.Array;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.InteractableComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;

public class CompanionInteractionControllerComponent extends Component {

    /**
     * Initiates an interaction with other entities in the game world.
     *
     * @param effectAll If true, the interaction will affect all nearby entities; otherwise, only the closest one is affected.
     */
    public void interact(boolean effectAll) {
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

        if (!effectAll && closest != null) {
            closest.getComponent(InteractableComponent.class).interact(entity);
        }
    }
}

