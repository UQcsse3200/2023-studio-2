package com.csse3200.game.entities.buildables;

import com.csse3200.game.entities.configs.WallConfig;
import com.csse3200.game.physics.components.PhysicsComponent;

public class Gate extends Wall {
    public Gate(WallConfig config) {
        super(config);
    }

    public void openGate() {
        getComponent(PhysicsComponent.class).setEnabled(false);
    }

    public void closeGate() {
        getComponent(PhysicsComponent.class).setEnabled(true);
    }
}
