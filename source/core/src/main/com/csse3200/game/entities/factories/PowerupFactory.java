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
     * Creates a powerup entity. The entity will have a texture render component
     * @param type The type of powerup to create.
     * @return Entity representing the powerup.
     *
     *
     */
    public static Entity createPowerup(PowerupType type) {

        // Initialise a new Powerup
        Entity powerup = new Entity()
                .addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.StaticBody))
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.COMPANION))
                .addComponent(new PowerupComponent(type, PhysicsLayer.COMPANION));
        powerup.setScale(0.6f, 0.6f);

        // Assigns texture based on the specific PowerupType
        switch (type) {
            case SPEED_BOOST:
                powerup.addComponent(new TextureRenderComponent("images/static.png"));
                break;
                //can add more powerups
            default:
                throw new IllegalArgumentException("You must assign a valid PowerupType");
        }
        return powerup;
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