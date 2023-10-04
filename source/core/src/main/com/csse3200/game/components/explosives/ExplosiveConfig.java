package com.csse3200.game.components.explosives;

/**
 * A config used to define the properties of an explosive.
 */
public class ExplosiveConfig {
    /**
     * Where to find the explosive effect to use.
     */
    public String effectPath;
    /**
     * Where to find the sound effect to use.
     */
    public String soundPath;
    /**
     * How much damage should be inflicted at ground 0.
     */
    public int damage;
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
}
