package com.csse3200.game.entities.configs;

import com.csse3200.game.components.companionweapons.CompanionWeaponType;

public class CompanionWeaponConfigs {

    public CompanionWeaponConfig  SHIELD_2;



    public CompanionWeaponConfig Death_Potion;
    public CompanionWeaponConfig SHIELD;
    public CompanionWeaponConfig SWORD;

    public CompanionWeaponConfigs() {
        // Initialize the Death_Potion configuration here
        Death_Potion = new CompanionWeaponConfig(
                "Death_Potion",            // Name
                "A deadly potion.",        // Description
                50.0f,                      // Damage
                5.0f,                       // Weapon Speed
                10,                        // Weapon Duration
                18,                        // Rotation Speed
                0,                          // Initial Rotation Offset
                2,                          // Attack Cooldown
                0,                          // Ammo Use
                4,                          // Animation Type
                0,                          // Image Rotation Offset
                1f,                       // Image Scale
                "images/weapons/sword.atlas",   // Texture Atlas
                "images/powerups/death_potion.png",// Image Path
                CompanionWeaponType.Death_Potion, // Companion Weapon Type
                "ranged"    ,1         // Slot Type
        );


        ParticleEffectsConfig explosiveConfig = new ParticleEffectsConfig();
        explosiveConfig.effectsMap.put("deathPotion", "particle-effects/deathpotion/Death.effect");

        Death_Potion.effects = explosiveConfig;
        //initialise the shield here
        SHIELD = new CompanionWeaponConfig(
                "SHIELD",            // Name
                "Shield for the companion",        // Description
                50.0f,                      // Damage
                25.0f,                       // Weapon Speed
                10,                        // Weapon Duration
                28,                        // Rotation Speed
                0,                          // Initial Rotation Offset
                2,                          // Attack Cooldown
                0,                          // Ammo Use
                0,                          // Animation Type
                0,                          // Image Rotation Offset
                3f,                       // Image Scale
                "images/companion/Companionshield.atlas",   // Texture Atlas
                "images/companion/CompanionShield.png",// Image Path
                CompanionWeaponType.SHIELD, // Companion Weapon Type
                "new slot type"   ,
                0
        );
        explosiveConfig.effectsMap.put("shield", "particle-effects/shield/shield.effect");
        SHIELD.effects =explosiveConfig;

        SHIELD_2= new CompanionWeaponConfig(
                " SHIELD_2",            // Name
                "CIRCULAR DAMAGE",        // Description
                50.0f,                      // Damage
                5.0f,                       // Weapon Speed
                25,                        // Weapon Duration
                28,                        // Rotation Speed
                0,                          // Initial Rotation Offset
                2,                          // Attack Cooldown
                0,                          // Ammo Use
                4,                          // Animation Type
                0,                          // Image Rotation Offset
                3f,                       // Image Scale
                "images/companion/Companionshield.atlas",
                "images/companion/CompanionShield.png",
                CompanionWeaponType. SHIELD_2, // Companion Weapon Type
                "Fire"     ,4        // Slot Type
        );

        SWORD= new CompanionWeaponConfig(
                " SWORD",            // Name
                "SHORT DISTANCE",        // Description
                25.0f,                      // Damage
                2.5f,                       // Weapon Speed
                150,                        // Weapon Duration
                1,                        // Rotation Speed
                0,                          // Initial Rotation Offset
                2,                          // Attack Cooldown
                0,                          // Ammo Use
                4,                          // Animation Type
                0,                          // Image Rotation Offset
                1f,                       // Image Scale
                "images/weapons/sword.atlas",
                "images/upgradetree/sword.png",
                CompanionWeaponType.SWORD,
                "melee"     ,
                4
        );
        explosiveConfig.effectsMap.put("deathPotion", "particle-effects/deathpotion/Death.effect");
        SWORD.effects =explosiveConfig;






    }



    public CompanionWeaponConfig GetWeaponConfig(CompanionWeaponType type) {
        return switch (type) {
            case Death_Potion -> Death_Potion;
            case SHIELD -> SHIELD;
            case  SHIELD_2 ->  SHIELD_2;
            case SWORD -> SWORD;
            default -> SHIELD_2;
        };
    }
}
