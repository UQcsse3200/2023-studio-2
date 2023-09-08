package com.csse3200.game.entities.factories;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.entities.Portal;

public class PortalFactory {

    public static Entity createPortal(float width, float height, Entity player) {

        Entity portal = new Portal(player);
        portal.setScale(width, height);
        return portal;
    }

    private PortalFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
