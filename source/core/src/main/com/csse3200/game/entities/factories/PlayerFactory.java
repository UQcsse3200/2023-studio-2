package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.InteractionControllerComponent;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.components.player.PlayerAnimationController;
import com.csse3200.game.components.player.PlayerStatsDisplay;
import com.csse3200.game.components.player.WeaponComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.origiPlayerConfig;
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
import com.csse3200.game.ui.DialogComponent;
import com.csse3200.game.ui.DialogueBox;

/**
 * Factory to create a player entity.
 *
 * <p>Predefined player properties are loaded from a config stored as a json file and should have
 * the properties stores in 'PlayerConfig'.
 */

public class PlayerFactory {

  private static DialogueBox dialogueBox;
  private static final origiPlayerConfig stats =
      FileLoader.readClass(origiPlayerConfig.class, "configs/player.json");


  /**
   * Create a player entity.
   * @return entity
   */
  public static Entity createPlayer() {
    InputComponent inputComponent =
        ServiceLocator.getInputService().getInputFactory().createForPlayer();

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/playerSS.atlas", TextureAtlas.class));
    animator.addAnimation("UP", 1f);
    animator.addAnimation("DOWN", 1f);
    animator.addAnimation("LEFT", 1f);
    animator.addAnimation("RIGHT", 1f);
    animator.addAnimation("UP_RIGHT", 1f);
    animator.addAnimation("UP_LEFT", 1f);
    animator.addAnimation("DOWN_RIGHT", 1f);
    animator.addAnimation("DOWN_LEFT", 1f);

    Entity player =
        new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
            .addComponent(new PlayerActions())
            .addComponent(new CombatStatsComponent(stats.health, stats.baseAttack, stats.attackMultiplier, stats.isImmune))
            .addComponent(new InventoryComponent(stats.gold))
            .addComponent(inputComponent)
            .addComponent(new PlayerStatsDisplay())
            .addComponent(animator)
            .addComponent(new PlayerAnimationController())
            .addComponent(new WeaponComponent())
            .addComponent(new DialogComponent(dialogueBox))
            .addComponent(new InteractionControllerComponent(false));

    PhysicsUtils.setScaledCollider(player, 0.6f, 0.3f);
    player.getComponent(ColliderComponent.class).setDensity(1.5f);
    animator.startAnimation("DOWN");
    return player;
  }

  private PlayerFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
