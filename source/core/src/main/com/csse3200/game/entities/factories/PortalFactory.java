package com.csse3200.game.entities.factories;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.entities.Portal;

public class PortalFactory {

    public static Entity createPortal(float width, float height, float x, float y, Entity player) {

        Entity portal = new Portal(player).
                addComponent(new TextureRenderComponent("map/portal.png"))
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));
        portal.getComponent(PhysicsComponent.class).setBodyType(BodyDef.BodyType.StaticBody);
        portal.setScale(width, height);
        portal.setPosition(x, y);
        return portal;
    }

    private PortalFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
