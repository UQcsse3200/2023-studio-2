package com.csse3200.game.entities.factories;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.PlayerFactory;
import com.csse3200.game.entities.factories.CompanionFactory;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.components.Companion.CompanionActions;
import com.csse3200.game.entities.configs.CompanionConfig;
import com.csse3200.game.components.Companion.CompanionStatsDisplay;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.components.FollowComponent;

public class BoxFactory {


    /**
     * Create a Box entity.
     * @return entity
     */
    //added a player reference for basic player tracking
    public static Entity createBox(Entity companionEntity) {
        InputComponent inputComponent =
                ServiceLocator.getInputService().getInputFactory().createForCompanion();


        Entity Box =
                new Entity()
                        .addComponent(new TextureRenderComponent("images/alert.png"))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new FollowComponent(companionEntity, 2.f));

        Box.getComponent(TextureRenderComponent.class).scaleEntity();



      /*  TextureRenderComponent textureComponent = Box.getComponent(TextureRenderComponent.class);
        float desiredScale = 0.5f;  // Adjust as needed

// Get the current dimensions of the texture
        float originalWidth = textureComponent.getWidth();
        float originalHeight = textureComponent.getHeight();

// Calculate the new dimensions based on the desired scale
        float newWidth = originalWidth * desiredScale;
        float newHeight = originalHeight * desiredScale;

// Set the new dimensions for the texture
        textureComponent.setWidth(newWidth);
        textureComponent.setHeight(newHeight);*/



        return Box;
    }
    private BoxFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
