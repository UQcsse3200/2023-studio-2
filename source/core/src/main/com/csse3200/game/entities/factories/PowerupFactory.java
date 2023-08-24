package com.csse3200.game.entities.factories;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.csse3200.game.components.PowerupComponent;
import com.csse3200.game.components.PowerupType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;

public class PowerupFactory {

    /**
     * Creates a powerup entity based on the specified type. The entity will have a texture render component
     * representing the image of the powerup, a physics component, and a hitbox component set to PLAYER layer.
     *
     * @param type The type of powerup to create.
     * @return Entity representing the powerup.
     * 
     * 
     */
    public static Entity createPowerup(PowerupType type, double modifier) {
        Entity powerup = new Entity()
                .addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.StaticBody))
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
                .addComponent(new PowerupComponent(type, PhysicsLayer.PLAYER));

        // todo: Add switch cases for texture render component for various textures between powerups

        return powerup;
    }

    public static Entity createHealthPowerup() {
        Entity healthPowerup = createPowerup(PowerupType.HEALTH_BOOST, 100)
                .addComponent(new TextureRenderComponent("images/healthpowerup.png"));
        return healthPowerup;
    }
}
