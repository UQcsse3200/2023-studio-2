package com.csse3200.game.entities.configs;

import com.csse3200.game.components.explosives.ExplosiveConfig;

/**
 * Used to save explosive barrels. It needs to be a separate
 * class to be able to distinguish between teh different explosives.
 */
public class ExplosiveBarrelConfig extends ExplosiveConfig {
    public ExplosiveBarrelConfig() {
        super();
    }

    public ExplosiveBarrelConfig(ExplosiveBarrelConfig config) {
        super(config);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }
}
