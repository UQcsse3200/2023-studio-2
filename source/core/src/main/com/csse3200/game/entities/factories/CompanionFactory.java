/**
 * Factory to create a companion entity.
 *
 * <p> Predefined companion properties are loaded from a config stored as a JSON file and should have
 * the properties stored in 'CompanionConfig'.
 */
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
 */
public class CompanionFactory {
    private static final CompanionConfig stats =
            FileLoader.readClass(CompanionConfig.class, "configs/companion.json");

    /**
     * Create a Companion entity.
     *
     * @param playerEntity The player entity to which the companion is associated.
     * @return The created companion entity.
     */
    public static Entity createCompanion(Entity playerEntity) {
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
                        .addComponent(new CombatStatsComponent(stats.health, stats.baseAttack, stats.attackMultiplier, stats.isImmune))
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

        companion.getComponent(CompanionActions.class).setBulletTexturePath(stats.bulletTexturePath);
        companion.setEntityType("companion");
        return companion;
    }

    private CompanionFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
