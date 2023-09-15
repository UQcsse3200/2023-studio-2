package com.csse3200.game.entities.factories;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.csse3200.game.components.InteractableComponent;
import com.csse3200.game.components.PowerUpDisplayHUD;
import com.csse3200.game.components.PowerupComponent;
import com.csse3200.game.components.PowerupType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.PowerupConfig;
import com.csse3200.game.entities.configs.PowerupConfigs;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;

public class PowerupFactory {
    public static final PowerupConfigs configs
            = FileLoader.readClass(PowerupConfigs.class, "configs/powerups.json");

    private PowerupFactory() {
        throw new IllegalStateException("Utility class");
    }

    public static Entity createPowerup(PowerupConfig config) {
        // Initialise and resize a new Powerup
        Entity powerup = new Entity()
                .addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.StaticBody))
                .addComponent(new PowerupComponent(config.type))
                .addComponent(new TextureRenderComponent(config.spritePath));

        powerup.addComponent(new InteractableComponent(powerup.getComponent(PowerupComponent.class)::applyEffect, 1f));
        powerup.setScale(0.6f, 0.6f);

        return powerup;
    }

    //TODO: REMOVE - LEGACY
    /**
     * Creates a powerup entity based on the specified type. The entity will have a texture render component
     * representing the image of the powerup, a physics component, and a hitbox component set to PLAYER layer,
     * and a PowerupComponment.
     *
     * @param type The type of powerup to create.
     * @return Entity representing the powerup.
     *
     *
     */
    public static Entity createPowerup(PowerupType type) {

        // Initialise and resize a new Powerup
        Entity powerup = new Entity()
                .addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.StaticBody))
                .addComponent(new PowerupComponent(type));
        powerup.addComponent(new PowerUpDisplayHUD(type));

        powerup.addComponent(new InteractableComponent(powerup.getComponent(PowerupComponent.class)::applyEffect, 1f));
        powerup.setScale(0.6f, 0.6f);

        // Assigns texture based on the specific PowerupType
        switch (type) {
            case HEALTH_BOOST -> powerup.addComponent(new TextureRenderComponent("images/healthpowerup.png"));
            case SPEED_BOOST -> powerup.addComponent(new TextureRenderComponent("images/speedpowerup.png"));
            default -> throw new IllegalArgumentException("You must assign a valid PowerupType");
        }
        return powerup;
    }

    /**
     * Creates a health boost power-up entity.
     *
     * @return Entity representing a health boost power-up.
     */
    public static Entity createHealthPowerup() {
        return createPowerup(configs.healthPowerup);
    }

    /**
     * Creates a speed boost power-up entity.
     *
     * @return Entity representing a speed boost power-up.
     */
    public static Entity createSpeedPowerup() {
        return createPowerup(configs.speedPowerup);
    }
}

