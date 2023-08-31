package com.csse3200.game.entities.factories;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.csse3200.game.components.InteractableComponent;
import com.csse3200.game.components.PowerupComponent;
import com.csse3200.game.components.PowerupType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;

public class PowerupFactory {

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
                .addComponent(new PowerupComponent(type, PhysicsLayer.PLAYER));

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
        return createPowerup(PowerupType.HEALTH_BOOST);
    }

    /**
     * Creates a speed boost power-up entity.
     *
     * @return Entity representing a speed boost power-up.
     */
    public static Entity createSpeedPowerup() {
        return createPowerup(PowerupType.SPEED_BOOST);
    }
}

