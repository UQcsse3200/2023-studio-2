package com.csse3200.game.entities.configs;

import com.csse3200.game.components.CompanionWeapons.CompanionWeaponType;

public class CompanionWeaponConfigs {

    public CompanionWeaponConfig  SHIELD_2;



    public CompanionWeaponConfig Death_Potion;
    public CompanionWeaponConfig SHIELD;

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
                1,                          // Ammo Use
                4,                          // Animation Type
                0,                          // Image Rotation Offset
                1f,                       // Image Scale
                "images/weapons/sword.atlas",   // Texture Atlas
                "images/powerups/death_potion.png",// Image Path
                CompanionWeaponType.Death_Potion, // Companion Weapon Type
                "ranged"    ,1         // Slot Type
        );

        //initialise the shield here
        SHIELD = new CompanionWeaponConfig(
                "SHIELD",            // Name
                "Shield for the companion",        // Description
                50.0f,                      // Damage
                5.0f,                       // Weapon Speed
                10,                        // Weapon Duration
                18,                        // Rotation Speed
                0,                          // Initial Rotation Offset
                2,                          // Attack Cooldown
                1,                          // Ammo Use
                0,                          // Animation Type
                0,                          // Image Rotation Offset
                3f,                       // Image Scale
                "images/companion/Companionshield.atlas",   // Texture Atlas
                "images/companion/CompanionShield.png",// Image Path
                CompanionWeaponType.SHIELD, // Companion Weapon Type
                "new slot type"   ,
                0
        );

        SHIELD_2= new CompanionWeaponConfig(
                " SHIELD_2",            // Name
                "CIRCULAR DAMAGE",        // Description
                50.0f,                      // Damage
                5.0f,                       // Weapon Speed
                10,                        // Weapon Duration
                18,                        // Rotation Speed
                0,                          // Initial Rotation Offset
                2,                          // Attack Cooldown
                1,                          // Ammo Use
                1,                          // Animation Type
                0,                          // Image Rotation Offset
                1f,                       // Image Scale
                "images/companion/Companionshield.atlas",
                "images/companion/CompanionShield.png",
                CompanionWeaponType. SHIELD_2, // Companion Weapon Type
                "Fire"     ,4        // Slot Type
        );




    }
    public CompanionWeaponConfig GetWeaponConfig(CompanionWeaponType type) {
        return switch (type) {
            case Death_Potion -> Death_Potion;
            case SHIELD -> SHIELD;
            case  SHIELD_2 ->  SHIELD_2;
            default -> SHIELD_2;
        };
    }
}
