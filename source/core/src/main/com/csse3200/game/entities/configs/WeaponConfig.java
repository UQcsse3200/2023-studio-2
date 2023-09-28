package com.csse3200.game.entities.configs;

import com.csse3200.game.components.Weapons.WeaponType;

/**
 * Defines each entries properties stored in wall config file to be loaded by the Wall Factory.
 */
public class WeaponConfig extends BaseEntityConfig {
    public String name = "";
    public String description = "";
    //Direction Speed
    public float damage = 0;
    public float weaponSpeed = 0;
    //Ticks before despawn
    public int weaponDuration = 0;
    //Rotational Speed
    public int rotationSpeed = 0;
    //How far off is projectile fired from player click angle (used for melee swings)
    public int initialRotationOffset = 0;
    /* Image rotation of +x */
    public int attackCooldown = 300;

    public int animationType = 0;
    public int imageRotationOffset = 45;
    public float imageScale = 1;
    public String textureAtlas;
    public String imagePath;

    public WeaponType type;

    public String iconPath;
}
