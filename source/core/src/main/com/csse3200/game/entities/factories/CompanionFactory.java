package com.csse3200.game.entities.factories;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.components.Companion.CompanionAnimationController;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Companion.*;
import com.csse3200.game.components.player.InteractionControllerComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.entities.configs.CompanionConfig;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.components.FollowComponent;


/**
 * Factory to create a companion entity.
 *
 * <p> Predefined companion properties are loaded from a config stored as a JSON file and should have
 * the properties stored in 'CompanionConfig'.
 */
public class CompanionFactory {
    private static final CompanionConfig companionConfig =
            FileLoader.readClass(CompanionConfig.class, "configs/companion.json");

    //TODO: REMOVE - LEGACY
    /**
     * Create a Companion entity.
     *
     * @param playerEntity The player entity to which the companion is associated.
     * @return The created companion entity.
     */
    // Added a player reference for basic player tracking
    public static Entity createCompanion(Entity playerEntity) {
        return createCompanion(playerEntity, companionConfig);
    }

    /**
     * Create a Companion entity matching the config file
     * @param playerEntity The player entity to which the companion is associated.
     * @param config Configuration file to match companion to
     * @return The created companion entity.
     */
    // Added a player reference for basic player tracking
    public static Entity createCompanion(Entity playerEntity, CompanionConfig config) {
        InputComponent inputComponent =
                ServiceLocator.getInputService().getInputFactory().createForCompanion();

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset(config.spritePath, TextureAtlas.class));
        animator.addAnimation("UP", 1f);
        animator.addAnimation("DOWN", 1f);
        animator.addAnimation("LEFT", 1f);
        animator.addAnimation("RIGHT", 1f);
        animator.addAnimation("UP_RIGHT", 1f);
        animator.addAnimation("UP_LEFT", 1f);
        animator.addAnimation("DOWN_RIGHT", 1f);
        animator.addAnimation("DOWN_LEFT", 1f);


        Entity companion =
                new Entity()
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.COMPANION))
                        .addComponent(new ColliderComponent())
                        .addComponent(new CompanionActions())
                        .addComponent(new CombatStatsComponent(config.health, config.baseAttack, config.attackMultiplier, config.isImmune))
                        .addComponent(new CompanionInventoryComponent())
                        .addComponent(inputComponent)
                        .addComponent(new FollowComponent(playerEntity, 2f))
                        .addComponent(new CompanionInteractionControllerComponent())
                        .addComponent(new CompanionStatsDisplay(playerEntity))
                        .addComponent(animator)
                        .addComponent(new CompanionAnimationController())
                        .addComponent(new InteractionControllerComponent(false));

        //int health = companion.getComponent(CombatStatsComponent.class).getHealth();
        //CompanionStatsDisplay companionStatsDisplay = new CompanionStatsDisplay(true, 0, health);
        //CompanionInventoryComponent companionInventoryComponent = new CompanionInventoryComponent(10);
        animator.startAnimation("DOWN");
        //companion.addComponent(companionStatsDisplay);
        PhysicsUtils.setScaledCollider(companion, 0.4f, 0.2f);
        companion.getComponent(ColliderComponent.class).setDensity(1.0f);
        companion.scaleHeight(0.9f);

        companion.getComponent(CompanionActions.class).setBulletTexturePath(config.bulletTexturePath);
        companion.getComponent(TextureRenderComponent.class).scaleEntity();
        return companion;
    }

    private CompanionFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
