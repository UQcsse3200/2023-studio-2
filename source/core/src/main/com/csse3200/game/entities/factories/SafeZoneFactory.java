package com.csse3200.game.entities.factories;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.csse3200.game.components.SaveableComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.SafeZone;
import com.csse3200.game.entities.configs.SafeZoneConfig;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;

/**
 * Factory to create Safezone entities.
 */
public class SafeZoneFactory {

    public static Entity createSafeZone(Entity player, SafeZoneConfig config) {
        Entity safeZone = new SafeZone(player)
                .addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.StaticBody))
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.NONE))
                .addComponent(new TextureRenderComponent(config.spritePath))
                .addComponent(new SaveableComponent<>(s -> {
                    config.position = s.getGridPosition();
                    return config;
                }, SafeZoneConfig.class));
        safeZone.setScale(5.0f, 5.0f);
        return safeZone;
    }

    private SafeZoneFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
