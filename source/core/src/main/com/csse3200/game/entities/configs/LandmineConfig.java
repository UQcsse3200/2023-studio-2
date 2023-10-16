package com.csse3200.game.entities.configs;

import com.csse3200.game.components.explosives.ExplosiveConfig;

/**
 * Used to save landmines. It needs to be a separate class to be
 * able to distinguish between teh different explosives.
 */
public class LandmineConfig extends ExplosiveConfig {
    public LandmineConfig() {
        super();
    }

    public LandmineConfig(LandmineConfig config) {
        super(config);
    }
}
