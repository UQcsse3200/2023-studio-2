package com.csse3200.game.entities.buildables;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.WallConfig;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;

public class Wall extends Entity {
    public Wall(WallConfig config) {
        super();
        addComponent(new TextureRenderComponent(config.texture));
        addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.StaticBody));
        addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));
        addComponent(new CombatStatsComponent(config.health, 0));
        addComponent(new WallStatsDisplay());

        setScale(1f, 1f);
        getComponent(TextureRenderComponent.class).scaleEntity();
    }
}
