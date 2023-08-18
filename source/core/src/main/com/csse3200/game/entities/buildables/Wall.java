package com.csse3200.game.entities.buildables;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;

public class Wall extends Entity {
    public Wall() {
        super();
        addComponent(new TextureRenderComponent("images/wall.png"));
        addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.StaticBody));
        addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

        setScale(1f, 1f);
        getComponent(TextureRenderComponent.class).scaleEntity();
    }

    public Wall(float x, float y) {
        this();
        setPosition(x, y);
    }

    public Wall(Vector2 position) {
        this();
        setPosition(position);
    }
}
