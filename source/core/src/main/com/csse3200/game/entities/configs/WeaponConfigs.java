package com.csse3200.game.entities.configs;

import com.csse3200.game.components.Weapons.WeaponType;

/**
 * Class to return weapon configs
 */
public class WeaponConfigs {
    public WeaponConfig MELEE_WRENCH = new WeaponConfig();
    public WeaponConfig MELEE_KATANA = new WeaponConfig();
    public WeaponConfig MELEE_BEE_STING = new WeaponConfig();
    public WeaponConfig RANGED_SLINGSHOT = new WeaponConfig();
    public WeaponConfig RANGED_SNIPER = new WeaponConfig();
    public WeaponConfig RANGED_GRENADE = new WeaponConfig();
    public WeaponConfig RANGED_BOOMERANG = new WeaponConfig();
    public WeaponConfig RANGED_BLUEMERANG = new WeaponConfig();
    public WeaponConfig RANGED_HOMING = new WeaponConfig();
    public WeaponConfig RANGED_MISSILES = new WeaponConfig();
    public WeaponConfig STICK = new WeaponConfig();
    public WeaponConfig WOODHAMMER = new WeaponConfig();
    public WeaponConfig STONEHAMMER = new WeaponConfig();
    public WeaponConfig STEELHAMMER = new WeaponConfig();
    public WeaponConfig RANGED_NUKE = new WeaponConfig();
    public WeaponConfig MELEE_LASER_SWORD = new WeaponConfig();

    public WeaponConfig GetWeaponConfig(WeaponType type) {
        return switch (type) {
            case MELEE_WRENCH -> MELEE_WRENCH;
            case MELEE_KATANA -> MELEE_KATANA;
            case MELEE_BEE_STING -> MELEE_BEE_STING;
            case MELEE_LASER_SWORD -> MELEE_LASER_SWORD;
            case RANGED_SLINGSHOT -> RANGED_SLINGSHOT;
            case RANGED_SNIPER -> RANGED_SNIPER;
            case RANGED_BOOMERANG -> RANGED_BOOMERANG;
            case RANGED_GRENADE -> RANGED_GRENADE;
            case RANGED_BLUEMERANG -> RANGED_BLUEMERANG;
            case RANGED_HOMING -> RANGED_HOMING;
            case RANGED_MISSILES -> RANGED_MISSILES;
            case RANGED_NUKE -> RANGED_NUKE;
            case STICK -> STICK;
            case WOODHAMMER -> WOODHAMMER;
            case STONEHAMMER -> STONEHAMMER;
            case STEELHAMMER -> STEELHAMMER;
            default -> MELEE_WRENCH;
        };
    }
}