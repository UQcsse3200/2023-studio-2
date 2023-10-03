package com.csse3200.game.entities.factories;


import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.Weapons.WeaponControllerComponent;
import com.csse3200.game.components.Weapons.WeaponTargetComponent;
import com.csse3200.game.components.Weapons.WeaponType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.WeaponConfig;
import com.csse3200.game.entities.configs.WeaponConfigs;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.rendering.TextureRenderComponent;

public class CompanionShieldFactory {
    /**
     * Load in all the configs, and pick a shield
     */
    private static final WeaponConfigs configs = FileLoader.readClass(WeaponConfigs.class, "configs/weapons.json");

    /**
     * Method to actually create a new shield entity
     * @param weaponType - should always be Shield
     * @param companion - the companion entity to follow
     * @return  A reference to the created weapon entity
     */
    public static Entity createCompanionShield(WeaponType weaponType, Entity companion) {
        // get the config of the weapon
        WeaponConfig config = configs.GetWeaponConfig(weaponType);
        //create a weapon controller component
        WeaponControllerComponent weaponController = new WeaponControllerComponent(WeaponType.STATIC_WEAPON,
                Integer.MAX_VALUE,0,0,0,0,0);
        //attack is an entity which can be called
        Entity attack = new Entity().addComponent(weaponController);
        attack.setEntityType("companionShield");
        //pull the texture of the attack and give it to the attack component
        Texture texture = new Texture(config.imagePath);
        attack.addComponent(new TextureRenderComponent(texture));

        attack.scaleWidth(config.imageScale);
        attack.addComponent(new WeaponTargetComponent(WeaponType.SHIELD, companion));
        return attack;

    }

    /**
     * backup
     */
    public CompanionShieldFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }

}
