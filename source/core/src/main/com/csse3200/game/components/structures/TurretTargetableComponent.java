package com.csse3200.game.components.structures;

import com.csse3200.game.components.Component;

/**
 * This component allows a turret to target the entity it is placed on.
 */
public class TurretTargetableComponent extends Component {
    private boolean isInFov;

    public TurretTargetableComponent() {
        this.isInFov = false;
    }

    /**
     * Returns whether the entity is within a turrets fov.
     * @return whether the entity is within a turrets fov.
     */
    public boolean isInFov() {
        return isInFov;
    }

    /**
     * Sets whether the entity is within a turrets fov
     * @param inFov - whether the entity is within a turrets fov
     */
    public void setInFov(boolean inFov) {
        isInFov = inFov;
    }
}
