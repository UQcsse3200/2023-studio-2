package com.csse3200.game.entities.factories;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.tasks.ChaseTask;
import com.csse3200.game.components.tasks.WanderTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.csse3200.game.ui.DialogComponent;
import com.csse3200.game.ui.DialogueBox;

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

  /** The shared dialogue box instance used by NPCs. */
  public static DialogueBox dialogueBox;

  /** Configuration class for NPC properties. */
  private static final NPCConfigs configs = FileLoader.readClass(NPCConfigs.class, "configs/NPCs.json");

  /** Asset manager to load and manage assets. */
  public AssetManager assetManager;

  /**
   * Creates an instance of NPCFactory.
   *
   * @param assetManager The asset manager to use for asset loading.
   */
  public NPCFactory(AssetManager assetManager) {
    this.assetManager = assetManager;
  }

//  public static Entity createGhost(Entity target) {
//
//    Entity ghost = createBaseNPC(target);
//    BaseEntityConfig config = configs.ghost;
//
//    AnimationRenderComponent animator =
//        new AnimationRenderComponent(
//            ServiceLocator.getResourceService().getAsset("images/ghost.atlas", TextureAtlas.class));
//    animator.addAnimation("angry_float", 0.1f, Animation.PlayMode.LOOP);
//    animator.addAnimation("float", 0.1f, Animation.PlayMode.LOOP);
//
//    ghost
//        .addComponent(new CombatStatsComponent(config.health, config.baseAttack, config.attackMultiplier, config.isImmune))
//        .addComponent(animator)
//        .addComponent(new GhostAnimationController())
//        .addComponent(new DialogComponent(dialogueBox));
//    ghost.getComponent(AnimationRenderComponent.class).scaleEntity();
//
//    return ghost;
//  }


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
  /**
   * Creates a generic Botanist NPC entity.
   *
   * @return The created Botanist NPC entity.
   */
  public static Entity createBotanist() {
    Entity botanist =
            new Entity()
                    .addComponent(new TextureRenderComponent("images/oldman_down_1.png"))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new DialogComponent(dialogueBox))
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.NPC_OBSTACLE));
       FileLoader.readClass(NPCConfigs.class, "configs/NPCs.json");

    botanist.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    botanist.getComponent(TextureRenderComponent.class).scaleEntity();
    botanist.scaleHeight(1.1f);
    PhysicsUtils.setScaledCollider(botanist, 0.9f, 0.7f);
    return botanist;
  }
  /**
   * Creates a generic NPC to be used as a base entity by more specific NPC creation methods.
   *
   * @param target The target entity to be chased by the NPC.
   * @return The created base NPC entity.
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
                    .addComponent(new TouchAttackComponent((short) (PhysicsLayer.PLAYER | PhysicsLayer.OBSTACLE), 1.5f))
                    .addComponent(aiComponent);

    PhysicsUtils.setScaledCollider(npc, 0.9f, 0.4f);
    return npc;
  }
  /**
   * Private constructor to prevent instantiation of the NPCFactory.
   *
   * <p>This class should be used as a static util class.
   */
  public NPCFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }


}
