package com.csse3200.game.entities.factories;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.npc.BotanistAnimationController;
import com.csse3200.game.components.npc.GhostAnimationController;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.tasks.ChaseTask;
import com.csse3200.game.components.tasks.WanderTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.BaseEntityConfig;
import com.csse3200.game.entities.configs.BotanistConfig;
import com.csse3200.game.entities.configs.GhostKingConfig;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.csse3200.game.ui.DialogComponent;
import com.csse3200.game.ui.DialogueBox;
import com.csse3200.game.ui.TitleBox;


/**
 * Factory to create non-playable character (NPC) entities with predefined components.
 *
 * <p>Each NPC entity type should have a creation method that returns a corresponding entity.
 * Predefined entity properties can be loaded from configs stored as json files which are defined in
 * "NPCConfigs".
 *
 * <p>If needed, this factory can be separated into more specific factories for entities with
 * similar characteristics.
 */
public class NPCFactory {
  public static DialogueBox dialogueBox;
  private static final NPCConfigs configs =
      FileLoader.readClass(NPCConfigs.class, "configs/NPCs.json");

  public AssetManager assetManager;


  public NPCFactory(AssetManager assetManager) {
    this.assetManager = assetManager;
  }
  /**
   * Creates a ghost entity.
   *
   * @param target entity to chase
   * @return entity
   */
  public static Entity createGhost(Entity target) {

    Entity ghost = createBaseNPC(target);
    BaseEntityConfig config = configs.ghost;

    AnimationRenderComponent animator =
        new AnimationRenderComponent(
            ServiceLocator.getResourceService().getAsset("images/ghost.atlas", TextureAtlas.class));
    animator.addAnimation("angry_float", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("float", 0.1f, Animation.PlayMode.LOOP);

    ghost
        .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
        .addComponent(animator)
        .addComponent(new GhostAnimationController())
        .addComponent(new DialogComponent(dialogueBox));
    ghost.getComponent(AnimationRenderComponent.class).scaleEntity();

    return ghost;
  }

  /**
   * Creates a ghost king entity.
   *
   * @param target entity to chase
   * @return entity
   */
  public static Entity createGhostKing(Entity target) {
    Entity ghostKing = createBaseNPC(target);
    GhostKingConfig config = configs.ghostKing;

    AnimationRenderComponent animator =
        new AnimationRenderComponent(
            ServiceLocator.getResourceService()
                .getAsset("images/ghostKing.atlas", TextureAtlas.class));
    animator.addAnimation("float", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("angry_float", 0.1f, Animation.PlayMode.LOOP);

    ghostKing
        .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
        .addComponent(animator)
        .addComponent(new GhostAnimationController())
        .addComponent(new DialogComponent(dialogueBox));
    ghostKing.getComponent(AnimationRenderComponent.class).scaleEntity();
    return ghostKing;
  }
  //TODO
//  public static Entity createBotanist() {
//    AnimationRenderComponent animator = new AnimationRenderComponent(
//            ServiceLocator.getResourceService().getAsset("images/botanist.atlas", TextureAtlas.class));
//    animator.addAnimation("idle_left", Float.MAX_VALUE, Animation.PlayMode.LOOP);
//    animator.addAnimation("idle_right", Float.MAX_VALUE, Animation.PlayMode.LOOP);
//    animator.addAnimation("wanderStart_left", 0.4f, Animation.PlayMode.LOOP_REVERSED);
//    animator.addAnimation("wanderStart_right", 0.4f, Animation.PlayMode.LOOP);
////    animator.addAnimation("runLeft", 0.2f, Animation.PlayMode.LOOP_REVERSED);
////    animator.addAnimation("runRight", 0.2f, Animation.PlayMode.LOOP);
//
//    AITaskComponent aiTaskComponent = new AITaskComponent()
//            .addTask(new WanderTask(new Vector2(2f, 2f), 2f));
//
//    Entity botanist = new Entity()
//            .addComponent(new PhysicsComponent())
//            .addComponent(new PhysicsMovementComponent())
//            .addComponent(new ColliderComponent())
//            .addComponent(aiTaskComponent)
//            .addComponent(animator)
//            .addComponent(new BotanistAnimationController());
//
//    PhysicsUtils.setScaledCollider(botanist, 0.9f, 0.4f);
//    return botanist;
//  }
  public static Entity createBotanist() {
    Entity botanist =
            new Entity()
                    .addComponent(new TextureRenderComponent("images/oldman_down_1.png"))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.NPC_OBSTACLE));

    botanist.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    botanist.getComponent(TextureRenderComponent.class).scaleEntity();
    botanist.scaleHeight(1.1f);
    PhysicsUtils.setScaledCollider(botanist, 0.9f, 0.7f);
    return botanist;
  }
  /**
   * Creates a generic NPC to be used as a base entity by more specific NPC creation methods.
   *
   * @return entity
   */
  private static Entity createBaseNPC(Entity target) {
    AITaskComponent aiComponent =
        new AITaskComponent()
            .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
            .addTask(new ChaseTask(target, 10, 3f, 4f));
    Entity npc =
        new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new PhysicsMovementComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
            .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 1.5f))
            .addComponent(aiComponent);

    PhysicsUtils.setScaledCollider(npc, 0.9f, 0.4f);
    return npc;
  }

  public NPCFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }

}
