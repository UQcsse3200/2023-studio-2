package com.csse3200.game.entities.factories;

import com.csse3200.game.components.PowerupComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;

public class PowerupFactory {

    /**
     * Creates a base powerup entity with a texture render component (image of health powerup),
     * a physics component, and a hitbox component set to PLAYER layer.
     *
     * @return Entity representing the base powerup.
     *
     * REF: https://cdn.apexminecrafthosting.com/img/uploads/2020/11/27214702/splash-potion-large.png
     */
    public static Entity createBasePowerup() {
        Entity potion = new Entity()
                .addComponent(new TextureRenderComponent("images/healthpowerup.png"))
                .addComponent(new PhysicsComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER));
        return potion;
    }

    /**
     * Creates a speed boost powerup entity. Currently, it returns the same entity
     * as the base powerup. This may be changed in future to include additional
     * components or properties specific to the speed boost.
     *
     * @return Entity representing the speed boost powerup.
     */
    public static Entity createSpeedBoostPowerup() {
        return createBasePowerup();
    }
}
