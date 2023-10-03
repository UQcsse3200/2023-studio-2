package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.Weapons.SpecWeapon.StaticController;
import com.csse3200.game.components.Weapons.WeaponControllerComponent;
import com.csse3200.game.components.Weapons.WeaponType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.WeaponConfig;
import com.csse3200.game.entities.configs.WeaponConfigs;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.rendering.TextureRenderComponent;

/**
 * Class to create weapons for the player to hold
 */
public class PlayerWeaponFactory {
    private static final WeaponConfigs configs =
            FileLoader.readClass(WeaponConfigs.class, "configs/weapons.json");

    /**
     * Static function to create a new weapon entity
     *
     * @param weaponType - the type of weapon entity to be made
     * @param player     - player entity to track
     * @return A reference to the created weapon entity
     */
    public static Entity createPlayerWeapon(WeaponType weaponType, Entity player) {
        WeaponConfig config = configs.GetWeaponConfig(weaponType);
        WeaponControllerComponent wepCon = new StaticController(config, player);

        Texture texture = new Texture(config.imagePath);
        Entity attack = new Entity()
                .addComponent(new TextureRenderComponent(texture))
                .addComponent(wepCon);

        attack.setEntityType("playerStaticWeapon");
        attack.scaleWidth(config.imageScale / 2);
        return attack;
    }

    private PlayerWeaponFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}