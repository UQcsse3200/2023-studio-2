package com.csse3200.game.entities.factories;

import com.csse3200.game.components.FollowComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;

//TODO: REMOVE?
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


        return Box;
    }
    private BoxFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
