package com.csse3200.game.entities.configs;

import com.csse3200.game.components.Weapons.WeaponType;

/**
 * Defines each entries properties stored in wall config file to be loaded by the Wall Factory.
 */
public class WeaponConfig extends BaseEntityConfig {
    public String name = "";
    public String description = "";

    public int health = 30;

    //Weapon stats
    public float damage = 0;
    public float weaponSpeed = 0;
    public float rotationSpeed = 0;
    public float weaponDuration = 5000;
    public int attackCooldown = 5;
    public int ammoUse = 1;
    public int projectiles = 1;

    public float imageScale = 1;
    public float staticImageScale = 1;
    public String textureAtlas;
    public String imagePath;
    public SoundsConfig sound;

    public WeaponType type;
    public String slotType = "new slot type";

    public String iconPath;
}