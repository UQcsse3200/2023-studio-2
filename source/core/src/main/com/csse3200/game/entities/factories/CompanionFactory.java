/**
 * Factory to create a companion entity.
 *
 * <p> Predefined companion properties are loaded from a config stored as a JSON file and should have
 * the properties stored in 'CompanionConfig'.
 */
package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Companion.*;
import com.csse3200.game.components.FollowComponent;
import com.csse3200.game.components.player.InteractionControllerComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.CompanionConfig;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * Factory to create a companion entity.
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
        AnimationRenderComponent infanimator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset("images/companionSS.atlas", TextureAtlas.class));

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset("images/comp_spritesheet.atlas", TextureAtlas.class));
        animator.addAnimation("UP", 1f);
        animator.addAnimation("DOWN", 1f);
        animator.addAnimation("LEFT", 1f);
        animator.addAnimation("RIGHT", 1f);
        animator.addAnimation("UP_RIGHT", 1f);
        animator.addAnimation("UP_LEFT", 1f);
        animator.addAnimation("DOWN_RIGHT", 1f);
        animator.addAnimation("DOWN_LEFT", 1f);
        infanimator.addAnimation("UP_1", 1f);
        infanimator.addAnimation("DOWN_1", 1f);
        infanimator.addAnimation("RIGHT_1", 1f);
        infanimator.addAnimation("LEFT_1", 1f);

        Entity companion =
                new Entity()
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.COMPANION))
                        .addComponent(new ColliderComponent())
                        .addComponent(new CompanionActions())
                        .addComponent(new CombatStatsComponent(config.health, config.baseAttack, config.attackMultiplier, config.isImmune))
                        .addComponent(new CompanionInventoryComponent())
                        .addComponent(inputComponent)
                        .addComponent(new FollowComponent(playerEntity, 1.5f))
                        .addComponent(animator)
                        .addComponent(infanimator)
                        .addComponent(new CompanionStatsDisplay(playerEntity))
                        .addComponent(new KeyboardCompanionInputComponent())
                        .addComponent(new CompanionAnimationController())
                        .addComponent(new InteractionControllerComponent(false));

        animator.startAnimation("DOWN");
        infanimator.startAnimation("RIGHT_1");
        PhysicsUtils.setScaledCollider(companion, 0.4f, 0.2f);
        companion.getComponent(ColliderComponent.class).setDensity(1.0f);
        companion.scaleHeight(0.9f);
        companion.setEntityType("companion");
        return companion;
    }

    private CompanionFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
