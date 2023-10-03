/**
 * Factory to create a companion entity.
 *
 * <p> Predefined companion properties are loaded from a config stored as a JSON file and should have
 * the properties stored in 'CompanionConfig'.
 */
package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Companion.CompanionWeaponComponent;
import com.csse3200.game.components.Companion.*;
import com.csse3200.game.components.FollowComponent;
import com.csse3200.game.components.HealthBarComponent;
import com.csse3200.game.components.SaveableComponent;
import com.csse3200.game.components.player.InteractionControllerComponent;
import com.csse3200.game.components.player.PlayerAnimationController;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.CompanionConfig;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;


import java.util.Objects;

/**
 * Factory to create a companion entity.
 */
public class CompanionFactory {
    private static final CompanionConfig config =
            FileLoader.readClass(CompanionConfig.class, "configs/companion.json");

    public static Entity createCompanion(){
       return createCompanion(config);
    }
    /**
     * Create a Companion entity matching the config file
     * @param config Configuration file to match companion to
     * @return The created companion entity.
     */
    // Added a player reference for basic player tracking
    public static Entity createCompanion(CompanionConfig config) {
        InputComponent inputComponent = ServiceLocator.getInputService().getInputFactory().createForCompanion();
        Entity player = ServiceLocator.getEntityService().getPlayer();

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset(config.spritePath, TextureAtlas.class));
        animator.addAnimation("Companion_DownLeft", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("Companion_UpRight", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("Companion_Up", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("Companion_Left", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("Companion_DownRight", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("Companion_Down", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("Companion_UpLeft", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("Companion_Right", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("Companion_StandDown", 0.2f);
        animator.addAnimation("Companion_StandUp", 0.2f);
        animator.addAnimation("Companion_StandLeft", 0.2f);
        animator.addAnimation("Companion_StandRight", 0.2f);

        Entity companion =
                new Entity()
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.COMPANION))
                        .addComponent(new ColliderComponent())
                        .addComponent(new CompanionActions())
                        .addComponent(new CombatStatsComponent(config.health, config.baseAttack, config.attackMultiplier, config.isImmune))
                        .addComponent(new CompanionInventoryComponent())
                        .addComponent(inputComponent)
                        .addComponent(animator)
                        .addComponent(new HealthBarComponent(true))
                        .addComponent(new CompanionWeaponComponent())
                        /*.addComponent(infanimator)*/
                        .addComponent(new CompanionStatsDisplay(config))
                        .addComponent(new CompanionInGameAlerts())
                        .addComponent(new CompanionAnimationController())
                        .addComponent(new FollowComponent(player,1f))
                        .addComponent(new CompanionShieldComponent())
                        .addComponent(new InteractionControllerComponent(false));
                         companion.addComponent(new SaveableComponent<>(p -> {
                               CompanionConfig companionConfig = config;
                               companionConfig.position = new GridPoint2((int) companion.getPosition().x, (int)companion.getPosition().y);
                               companionConfig.health = companion.getComponent(CombatStatsComponent.class).getHealth();
                               return companionConfig;
                                         }, CompanionConfig.class));

                        animator.startAnimation("Companion_StandDown");
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
