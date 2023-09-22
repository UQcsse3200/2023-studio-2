package com.csse3200.game.entities.factories;

import com.badlogic.gdx.physics.box2d.*;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;

public class PlatformFactory {
    public static Entity createPlatform(){
        Entity Platform = new Entity()
                .addComponent(new TextureRenderComponent("images/platform.png"))
                .addComponent(new PhysicsComponent());
        Body platformBody = Platform.getComponent(PhysicsComponent.class).getBody();
        platformBody.setType(BodyDef.BodyType.StaticBody); // Make the platform static
        platformBody.setFixedRotation(true); // Prevent platform from rotating
        platformBody.setUserData(Platform);
        Platform.getComponent(TextureRenderComponent.class).scaleEntity();
        Platform.setScale(2f,0.6f);
        return Platform;
    }
}