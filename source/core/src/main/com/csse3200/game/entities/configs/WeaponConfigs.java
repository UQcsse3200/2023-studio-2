package com.csse3200.game.entities.configs;

import com.csse3200.game.components.Weapons.WeaponType;

/**
 *
 */
public class WeaponConfigs {
    public WeaponConfig SLING_SHOT = new WeaponConfig();
    public WeaponConfig ELEC_WRENCH = new WeaponConfig();
    public WeaponConfig THROW_ELEC_WRENCH = new WeaponConfig();
    public WeaponConfig STICK = new WeaponConfig();
    public WeaponConfig KATANA = new WeaponConfig();
    public WeaponConfig WOODHAMMER = new WeaponConfig();
    public WeaponConfig STONEHAMMER = new WeaponConfig();
    public WeaponConfig STEELHAMMER = new WeaponConfig();

    public WeaponConfig GetWeaponConfig(WeaponType type) {
        return switch (type) {
            case SLING_SHOT -> SLING_SHOT;
            case ELEC_WRENCH -> ELEC_WRENCH;
            case THROW_ELEC_WRENCH -> THROW_ELEC_WRENCH;
            case STICK -> STICK;
            case KATANA -> KATANA;
            case WOODHAMMER -> WOODHAMMER;
            case STONEHAMMER -> STONEHAMMER;
            case STEELHAMMER -> STEELHAMMER;
            default -> ELEC_WRENCH;
        };
    }
}

