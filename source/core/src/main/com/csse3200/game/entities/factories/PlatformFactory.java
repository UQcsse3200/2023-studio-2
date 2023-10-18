package com.csse3200.game.entities.factories;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;

public class PlatformFactory {
    public static Entity createPlatform(){
        Entity platform = new Entity()
                .addComponent(new TextureRenderComponent("images/platform.png"))
                .addComponent(new PhysicsComponent());
        Body platformBody = platform.getComponent(PhysicsComponent.class).getBody();
        platformBody.setType(BodyDef.BodyType.StaticBody); // Make the platform static
        platformBody.setFixedRotation(true); // Prevent platform from rotating
        platformBody.setUserData(platform);
        platform.getComponent(TextureRenderComponent.class).scaleEntity();
        platform.setScale(2f,0.6f);
        return platform;
    }
}