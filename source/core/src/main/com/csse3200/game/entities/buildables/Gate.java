package com.csse3200.game.entities.buildables;

import com.csse3200.game.components.ProximityActivationComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.WallConfig;
import com.csse3200.game.physics.components.PhysicsComponent;

public class Gate extends Wall {
    public Gate(WallConfig config, Entity player) {
        super(config);
        addComponent(new ProximityActivationComponent(2f, player, this::openGate, this::closeGate));
    }

    public void openGate(Entity player) {
        getComponent(PhysicsComponent.class).setEnabled(false);
    }

    public void closeGate(Entity player) {
        getComponent(PhysicsComponent.class).setEnabled(true);
    }
}
