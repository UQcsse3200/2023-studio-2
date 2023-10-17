package com.csse3200.game.components.explosives;

import com.csse3200.game.entities.configs.HealthEntityConfig;

/**
 * A config used to define the properties of an explosive.
 */
public class ExplosiveConfig extends HealthEntityConfig {
    /**
     * Where to find the explosive effect to use.
     */
    public String effectPath;
    /**
     * Where to find the sound effect to use.
     */
    public String soundPath;
    /**
     * How far away from ground 0 damage should be inflicted.
     */
    public float damageRadius;
    /**
     * The distance for which other chainable explosives will be set off.
     */
    public float chainRadius;
    /**
     * Whether surrounding explosions can set the explosive off.
     */
    public boolean chainable;

    public ExplosiveConfig() {
        super();
    }

    public ExplosiveConfig(ExplosiveConfig config) {
        super(config);

        effectPath = config.effectPath;
        soundPath = config.soundPath;
        damageRadius = config.damageRadius;
        chainRadius = config.chainRadius;
        chainable = config.chainable;
    }
}
