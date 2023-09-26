package com.csse3200.game.entities.factories;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.csse3200.game.components.*;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.PlayerConfig;
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

    // TODO: REMOVE - LEGACY
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
                .addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.StaticBody))
                .addComponent(new PowerupComponent(config.type))
                .addComponent(new TextureRenderComponent(config.spritePath))
                .addComponent(new PowerupComponent(config.type))
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.ITEMS_ABOVE_PLATFORM))
                .addComponent(new ItemPickupComponent(PhysicsLayer.ITEMS_ABOVE_PLATFORM))
                .addComponent(new PowerUpDisplayHUD(config.type));
/*
        powerup.addComponent(new InteractableComponent(powerup.getComponent(PowerupComponent.class)::applyEffect, 1f));*/
        powerup.setScale(0.6f, 0.6f);

        // Assigns texture based on the specific PowerupType
        switch (config.type) {
            case HEALTH_BOOST -> {powerup.addComponent(new TextureRenderComponent("images/Potion2re.png"))
                                         .addComponent(new PowerupComponent(PowerupType.HEALTH_BOOST));}
            case SPEED_BOOST -> {powerup.addComponent(new TextureRenderComponent("images/Potion4re.png"))
                                         .addComponent(new PowerupComponent(PowerupType.SPEED_BOOST));}
            case EXTRA_LIFE -> {powerup.addComponent(new TextureRenderComponent("images/powerups/extra_life.png"))
                                         .addComponent(new PowerupComponent(PowerupType.EXTRA_LIFE));}
            case DOUBLE_CROSS -> {powerup.addComponent(new TextureRenderComponent("images/powerups/double_cross.png"))
                                         .addComponent(new PowerupComponent(PowerupType.DOUBLE_CROSS));}
            case TEMP_IMMUNITY -> {powerup.addComponent(new TextureRenderComponent("images/Potion1re.png"))
                                          .addComponent(new PowerupComponent(PowerupType.TEMP_IMMUNITY));}
            case DOUBLE_DAMAGE -> {powerup.addComponent(new TextureRenderComponent("images/powerups/double_damage.png"))
                                          .addComponent(new PowerupComponent(PowerupType.DOUBLE_DAMAGE));}
            case SNAP -> {powerup.addComponent(new TextureRenderComponent("images/powerups/snap.png"))
                                 .addComponent(new PowerupComponent(PowerupType.SNAP));}
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

    /**
     * Creates a extra life power-up entity.
     *
     * @return Entity representing a extra life power-up.
     */
    public static Entity createExtraLifePowerup() {
        return createPowerup(configs.extraLifePowerup);
    }

    /**
     * Creates a double cross power-up entity.
     *
     * @return Entity representing a double cross power-up.
     */
    public static Entity createDoubleCrossPowerup() {
        return createPowerup(configs.doubleCrossPowerup);
    }

    /**
     * Creates a temp immunity power-up entity.
     *
     * @return Entity representing a temp immunity power-up.
     */
    public static Entity createtempImmunityPowerup() {
        return createPowerup(configs.tempImmunityPowerup);
    }

    /**
     * Creates a double damage power-up entity.
     *
     * @return Entity representing a double damage power-up.
     */
    public static Entity createDoubleDamagePowerup() {
        return createPowerup(configs.doubleDamagePowerup);
    }

    /**
     * Creates a snap power-up entity.
     *
     * @return Entity representing a snap power-up.
     */
    public static Entity createSnapPowerup() { return createPowerup(configs.snapPowerup);}
}
