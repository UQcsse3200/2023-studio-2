package com.csse3200.game.entities.configs;

import com.csse3200.game.components.PowerupType;

public class PowerupConfigs {
    public PowerupConfig healthPowerup = new PowerupConfig();
    public PowerupConfig speedPowerup = new PowerupConfig();
    public PowerupConfig extraLifePowerup = new PowerupConfig();
    public PowerupConfig doubleCrossPowerup = new PowerupConfig();
    public PowerupConfig tempImmunityPowerup = new PowerupConfig();
    public PowerupConfig doubleDamagePowerup = new PowerupConfig();
    public PowerupConfig snapPowerup = new PowerupConfig();
    public PowerupConfig death_potion = new PowerupConfig();


    public PowerupConfig GetPowerupConfig(PowerupType type) {
        return switch (type) {
            case DEATH_POTION -> death_potion;
            case HEALTH_BOOST -> healthPowerup;
            case SPEED_BOOST -> speedPowerup;
            case TEMP_IMMUNITY -> tempImmunityPowerup;
            case EXTRA_LIFE -> extraLifePowerup;
            case DOUBLE_CROSS -> doubleCrossPowerup;
            case DOUBLE_DAMAGE -> doubleDamagePowerup;
            case SNAP -> snapPowerup;
        };
    }

}
