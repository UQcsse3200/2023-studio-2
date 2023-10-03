package com.csse3200.game.entities.configs;

/**
 * This class is used to configure the Turret entity.
 *
 */
public class TurretConfig extends HealthEntityConfig {
    public int maxAmmo;
    public int damage;

    public TurretConfig() {
        health = 0;
        attackMultiplier = 1;
        isImmune = false;
        maxAmmo = 0;
        damage = 0;
    }
}

