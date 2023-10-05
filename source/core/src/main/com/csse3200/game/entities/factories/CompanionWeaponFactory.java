package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.CompanionWeapons.CompanionWeaponController;
import com.csse3200.game.components.CompanionWeapons.CompanionWeaponTargetComponent;
import com.csse3200.game.components.CompanionWeapons.CompanionWeaponType;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.CompanionWeaponConfig;
import com.csse3200.game.entities.configs.CompanionWeaponConfigs;
import com.csse3200.game.rendering.TextureRenderComponent;

/**
 * This is where the shield and the death potion are created
 */
public class CompanionWeaponFactory {
    private static final CompanionWeaponConfigs configs = new CompanionWeaponConfigs();

    static CompanionWeaponController weaponController;
    //by defaults creates a death potion, then creates a shield if that is the type. avoids null pointer exception
    public static Entity createCompanionWeapon(CompanionWeaponType weaponType, Entity companion) {
        CompanionWeaponConfig config = configs.GetWeaponConfig(weaponType);

        CompanionWeaponController weaponController = new CompanionWeaponController(
                CompanionWeaponType.Death_Potion,
                config.weaponDuration, config.currentRotation,
                config.weaponSpeed,
                config.rotationSpeed,
                config.animationType,
                config.initialRotationOffset

        );

        //if it is a death potion, return that
        if (weaponType == CompanionWeaponType.SHIELD) {
            //it is a shield
            weaponController = new CompanionWeaponController(
                    CompanionWeaponType.SHIELD,
                    config.weaponDuration, config.currentRotation,
                    config.weaponSpeed,
                    config.rotationSpeed,
                    config.animationType,
                    config.initialRotationOffset
            );
        }



        Entity attack = new Entity().addComponent(weaponController);
        attack.setEntityType("CompanionStaticWeapon");

        Texture texture = new Texture(config.imagePath);
        attack.addComponent(new TextureRenderComponent(texture));

        attack.scaleWidth(config.imageScale / 2);
        attack.addComponent(new CompanionWeaponTargetComponent(weaponType, companion));

        return attack;
    }

    private CompanionWeaponFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
