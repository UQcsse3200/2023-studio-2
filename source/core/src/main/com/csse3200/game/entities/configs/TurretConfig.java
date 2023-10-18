package com.csse3200.game.entities.configs;

/**
 * This class is used to configure the Turret entity.
 *
 */
public class TurretConfig extends HealthEntityConfig {
    public int maxAmmo = 0;
    public int damage = 0;


    public TurretConfig() {

    }

    public TurretConfig(TurretConfig config) {
        super(config);

        maxAmmo = config.maxAmmo;
        damage = config.damage;
    }

    public void setMaxAmmo(int maxAmmo) {
        this.maxAmmo = maxAmmo;
    }
}

