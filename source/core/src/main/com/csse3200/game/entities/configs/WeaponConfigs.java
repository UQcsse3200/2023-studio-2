package com.csse3200.game.entities.configs;

import com.csse3200.game.components.Weapons.WeaponType;

/**
 *
 */
public class WeaponConfigs {
    public WeaponConfig SLING_SHOT = new WeaponConfig();
    public WeaponConfig ELEC_WRENCH = new WeaponConfig();
    public WeaponConfig THROW_ELEC_WRENCH = new WeaponConfig();


    public WeaponConfig GetWeaponConfig(WeaponType type) {
        return switch (type) {
            case SLING_SHOT -> SLING_SHOT;
            case ELEC_WRENCH -> ELEC_WRENCH;
            case THROW_ELEC_WRENCH -> THROW_ELEC_WRENCH;
            default -> ELEC_WRENCH;
        };
    }
}
