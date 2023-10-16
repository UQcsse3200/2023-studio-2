package com.csse3200.game.entities.factories;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.csse3200.game.components.*;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.PowerupConfig;
import com.csse3200.game.entities.configs.PowerupConfigs;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;

public class PowerupFactory {
    public static final PowerupConfigs configs = FileLoader.readClass(PowerupConfigs.class, "configs/powerups.json");

    private PowerupFactory() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Creates a powerup entity based on the specified type. The entity will have a
     * texture render component
     * representing the image of the powerup, a physics component, and a hitbox
     * component set to PLAYER layer,
     * and a PowerupComponment.
     *
     */
    public static Entity createPowerup(PowerupConfig config) {

        // Initialise and resize a new Powerup
        Entity powerup = new Entity()
                .addComponent(new TextureRenderComponent(config.spritePath))
                .addComponent(new PowerupComponent(config.type))
                .addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.StaticBody))
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.ITEMS_ABOVE_PLATFORM))
                .addComponent(new PowerUpDisplayHUD(config.type));

        //NEW INVENTORY WHICH IS JUST POWERUPS
        powerup.addComponent(new InteractableComponent(entity -> powerup.getComponent(PowerupComponent.class).updatePowerupInventory(), 1f));

        // OLD INVENTORY WHICH INVOLVES WEAPONS
        //powerup.addComponent(new InteractableComponent(entity -> powerup.getComponent(PowerupComponent.class).updateInventory(), 1f));



        powerup.setScale(0.6f, 0.6f);

        return powerup;
    }

    /**
     * Creates a power-up entity with the respective type
     *
     * @param powerupType type of powerup to create.
     * @return Entity representing a health boost power-up.
     */
    public static Entity createPowerup(PowerupType powerupType) {
        return createPowerup(configs.GetPowerupConfig(powerupType));
    }
}
