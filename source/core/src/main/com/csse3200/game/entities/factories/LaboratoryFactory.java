package com.csse3200.game.entities.factories;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.csse3200.game.LabWindow;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.InteractableComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.LaboratoryConfig;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;

import static com.csse3200.game.screens.MainMenuScreen.logger;

/**
 * The LaboratoryFactory class is responsible for creating laboratory entities in the game.
 */
public class LaboratoryFactory {

    private static final LaboratoryConfig config =
            FileLoader.readClass(LaboratoryConfig.class, "configs/laboratory.json");
    /**
     * Creates a new laboratory entity with default properties.
     *
     * @return The created laboratory entity.
     */
    public static Entity createLaboratory() {
        // Create a new entity for the laboratory
        Entity laboratory = new Entity()
                .addComponent(new TextureRenderComponent(config.spritePath))
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.LABORATORY))
                .addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.StaticBody))
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.LABORATORY))
                .addComponent(new CombatStatsComponent(4, 0, 0, false));

        logger.debug("creating laboratory");
        // Set the laboratory's body type and scale
        laboratory.getComponent(PhysicsComponent.class).setBodyType(BodyDef.BodyType.StaticBody);
        laboratory.getComponent(TextureRenderComponent.class).scaleEntity();
        laboratory.setScale(3f, 3.0f);
        PhysicsUtils.setScaledCollider(laboratory, 1f, 0.8f);

        // Set the laboratory's initial health
        laboratory.getComponent(CombatStatsComponent.class).setHealth(0);

        // Add an interactable component to the laboratory
        laboratory.addComponent(new InteractableComponent(entity -> {
            CombatStatsComponent healthStats = laboratory.getComponent(CombatStatsComponent.class);

            // Check if the laboratory is dead
            if (healthStats.isDead()) {
                LabWindow labWindow = LabWindow.MakeNewLaboratory();
                ServiceLocator.getRenderService().getStage().addActor(labWindow);
            }
        }, 5f));

        return laboratory;
    }

    /**
     * Private constructor to prevent instantiation.
     *
     * @throws IllegalStateException If an attempt is made to instantiate this utility class.
     */
    private LaboratoryFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
