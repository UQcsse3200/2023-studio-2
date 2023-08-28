package com.csse3200.game.entities.factories;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.PlayerFactory;
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

/**
 * Factory to create a companion entity.
 *
 * <p> Predefined companion properties are loaded from a config stored as a json file and should have
 * the properties stores in 'CompanionConfig'.
 */
public class CompanionFactory {
    private static final CompanionConfig stats =
            FileLoader.readClass(CompanionConfig.class, "configs/companion.json");

    /**
     * Create a Companion entity.
     * @return entity
     */
    //added a player reference for basic player tracking
    public static Entity createCompanion(Entity playerEntity) {
        InputComponent inputComponent =
                ServiceLocator.getInputService().getInputFactory().createForCompanion();

        Entity Companion =
                new Entity()
                        .addComponent(new TextureRenderComponent("images/static.png"))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new CompanionActions())
                        .addComponent(inputComponent)
                        .addComponent(new FollowComponent(playerEntity, 20.f));


        PhysicsUtils.setScaledCollider(Companion, 0.4f, 0.2f);
        Companion.getComponent(ColliderComponent.class).setDensity(1.0f);
        Companion.getComponent(TextureRenderComponent.class).scaleEntity();
        return Companion;
    }


    private CompanionFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
