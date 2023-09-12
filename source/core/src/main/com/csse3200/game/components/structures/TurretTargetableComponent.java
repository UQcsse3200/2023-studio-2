package com.csse3200.game.components.structures;

import com.csse3200.game.components.Component;

public class TurretTargetableComponent extends Component {
    private boolean isInFov;

    public TurretTargetableComponent() {
        this.isInFov = false;
    }

    public boolean isInFov() {
        return isInFov;
    }

    public void setInFov(boolean inFov) {
        isInFov = inFov;
    }
}
