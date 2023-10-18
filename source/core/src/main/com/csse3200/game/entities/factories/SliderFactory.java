package com.csse3200.game.entities.factories;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.SliderConfig;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;


public class SliderFactory {

    private static final SliderConfig stats = FileLoader.readClass(SliderConfig.class, "configs/slider.json");
    /**
     * Creates a new minigame slider to match the config file
     * @return Created minigame slider
     */
    public static Entity createSlider() {
        InputComponent inputComponent =
                ServiceLocator.getInputService().getInputFactory().createForSlider();



        Entity slider =
                new Entity()
                        .addComponent(new TextureRenderComponent("images/ship/Ship.png"))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.SHIP))
                        .addComponent(new ColliderComponent())
                        .addComponent(inputComponent);


        slider.getComponent(ColliderComponent.class).setDensity(1.5f);
        slider.setEntityType("slider");

        return slider;
    }

    private SliderFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}




