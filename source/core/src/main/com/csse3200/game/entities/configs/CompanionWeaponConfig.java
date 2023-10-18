package com.csse3200.game.entities.configs;

import com.csse3200.game.components.companionweapons.CompanionWeaponType;

public class CompanionWeaponConfig extends BaseEntityConfig {
    public String name = "";
    public String description = "";
    public float damage = 0;
    public float weaponSpeed = 0;
    public int weaponDuration = 0;
    public int rotationSpeed = 1;
    public int initialRotationOffset = 0;
    public int attackCooldown = 5;
    public float currentRotation = 45;
    public int ammoUse = 1;
    public int animationType = 0;
    public int imageRotationOffset = 45;
    public float imageScale = 1;
    public String textureAtlas;
    public String imagePath;
    public CompanionWeaponType weaponType;
    public int health = 30;
    public int projectiles = 1;

    public String slotType = "ranged" ;

    public float range = 10.0f; // Set a fixed maximum range of 200 units for the projectile
    public int attackNum =0;
    public ParticleEffectsConfig effects = new ParticleEffectsConfig();




    public CompanionWeaponConfig(
            String name,
            String description,
            float damage,
            float weaponSpeed,
            int weaponDuration,
            int rotationSpeed,
            int initialRotationOffset,
            int attackCooldown,
            int ammoUse,
            int animationType,
            int imageRotationOffset,
            float imageScale,
            String textureAtlas,
            String imagePath,
            CompanionWeaponType weaponType,
            String slotType, int projectiles
    ) {
        this.name = name;
        this.description = description;
        this.damage = damage;
        this.weaponSpeed = weaponSpeed;
        this.weaponDuration = weaponDuration;
        this.rotationSpeed = rotationSpeed;
        this.initialRotationOffset = initialRotationOffset;
        this.attackCooldown = attackCooldown;
        this.ammoUse = ammoUse;
        this.animationType = animationType;
        this.imageRotationOffset = imageRotationOffset;
        this.imageScale = imageScale;
        this.textureAtlas = textureAtlas;
        this.imagePath = imagePath;
        this.weaponType = weaponType;
        this.slotType = slotType;
        this.projectiles=projectiles;
    }

    public String getImagePath() {
        return imagePath;
    }

}