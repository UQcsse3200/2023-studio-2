package com.csse3200.game.entities.factories;

import com.csse3200.game.components.player.InteractionControllerComponent;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.SliderConfig;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;

import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;


public class BallFactory {


    public static Entity createMinigameBall() {
        InputComponent inputComponent =
                ServiceLocator.getInputService().getInputFactory().createForBall();

        Entity ball =
                new Entity()
                        .addComponent(new TextureRenderComponent("images/minigame/Ball.png"))
                        .addComponent(inputComponent)
                        .addComponent(new PhysicsComponent());




        ball.setEntityType("ball");

        return ball;



    }
    private BallFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
