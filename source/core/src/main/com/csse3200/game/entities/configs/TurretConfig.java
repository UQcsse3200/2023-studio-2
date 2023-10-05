package com.csse3200.game.entities.configs;

/**
 * This class is used to configure the Turret entity.
 *
 */
public class TurretConfig extends HealthEntityConfig {
    public int maxAmmo = 0;
    public int damage = 0;

    public TurretConfig() {
        health = 0;
        attackMultiplier = 1;
        isImmune = false;
    }

    public void setMaxAmmo(int maxAmmo) {
        this.maxAmmo = maxAmmo;
    }
}

