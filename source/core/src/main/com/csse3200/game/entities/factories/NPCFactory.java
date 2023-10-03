package com.csse3200.game.entities.factories;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.InteractableComponent;
import com.csse3200.game.components.SoundComponent;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.npc.AstroAnimationController;
import com.csse3200.game.components.npc.BotanistAnimationController;
import com.csse3200.game.components.npc.FireAnimationController;
import com.csse3200.game.components.npc.JailAnimationController;
import com.csse3200.game.components.player.InteractionControllerComponent;
import com.csse3200.game.components.tasks.ChaseTask;
import com.csse3200.game.components.tasks.WanderTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.BotanistConfig;
import com.csse3200.game.entities.configs.AstroConfig;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;
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

  /**
   * Creates a generic Botanist NPC entity.
   * @return The created Botanist NPC entity.
   */
  public static Entity createBotanist() {
    return createBotanist(configs.botanist);
  }


  /**
   *
   * @param config Creates a Botanist NPC to match the config file
   * @return The created Botanist NPC entity.
   * and helps in triggering sound
   */
  public static Entity createBotanist(BotanistConfig config) {

    AITaskComponent aiComponent = new AITaskComponent();
    aiComponent.addTask(new WanderTask(new Vector2(1.5f, 1.5f), 1f));

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/botanist.atlas", TextureAtlas.class));
    animator.addAnimation("row-1-column-2", 0.01f, Animation.PlayMode.LOOP);
    animator.addAnimation("row-1-column-3", 0.01f, Animation.PlayMode.LOOP);
    animator.addAnimation("row-1-column-4", 0.01f, Animation.PlayMode.LOOP);
    animator.addAnimation("row-1-column-5", 0.01f, Animation.PlayMode.LOOP);
    animator.addAnimation("row-1-column-1", 0.01f, Animation.PlayMode.NORMAL);
    animator.addAnimation("row-1-column-6", 0.01f, Animation.PlayMode.LOOP);
    animator.addAnimation("row-1-column-7", 0.01f, Animation.PlayMode.LOOP);
    animator.addAnimation("row-1-column-8", 0.01f, Animation.PlayMode.LOOP);

    Entity botanist =
            new Entity()
                    .addComponent(animator)
                    .addComponent(new BotanistAnimationController())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.NPC_OBSTACLE))
                    .addComponent(new DialogComponent(dialogueBox))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new PhysicsMovementComponent())
                    .addComponent(new InteractionControllerComponent(true))
                    .addComponent(aiComponent);

    botanist.addComponent(new InteractableComponent(entity -> {
      botanist.getComponent(DialogComponent.class).showdialogue("NPC: (Desperate) Hey, you there!\n Please, help me! I've been stuck in\nhere for days!", "");
    },10f));

    botanist.scaleHeight(6.1f);
    return botanist;
  }

  /**
   * Creates a Astro NPC to match the config file
   * @return The created Astro NPC entity.
   */
  public static Entity createAstro() {

//    AITaskComponent aiComponent = new AITaskComponent();
//    aiComponent.addTask(new WanderTask(new Vector2(1.5f, 1.5f), 1f));

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/npc/Astro_NPC.atlas", TextureAtlas.class));
    animator.addAnimation("Astro_Up", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("Astro_UpLeft", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("Astro_Left", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("Astro_DownLeft", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("Astro_Down", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("Astro_DownRight", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("Astro_Right", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("Astro_UpRight", 0.2f, Animation.PlayMode.LOOP);

    Entity Astro =
            new Entity()
                    .addComponent(animator)
                    .addComponent(new AstroAnimationController(new AssetManager()))
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.NPC_OBSTACLE))
                    .addComponent(new DialogComponent(dialogueBox))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new InteractionControllerComponent(true))
                    .addComponent(new PhysicsMovementComponent());
//                    .addComponent(aiComponent);

    Astro.getComponent(ColliderComponent.class).setDensity(1.5f);
    Astro.addComponent(new InteractableComponent(entity -> {
      Astro.getComponent(DialogComponent.class).showdialogue("NPC: (Desperate) Hey, you there!\n Please, help me! I've been stuck in\nhere for days!", "");
    },3f));
    animator.startAnimation("Astro_Down");
    return Astro;
  }
  public static Entity createJail() {

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/Jail/jail.atlas", TextureAtlas.class));
    animator.addAnimation("jail_close", 0.2f, Animation.PlayMode.LOOP);

    Entity Jail =
            new Entity()
                    .addComponent(animator)
                    .addComponent(new JailAnimationController(new AssetManager()))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new InteractionControllerComponent(true))
                    .addComponent(new PhysicsMovementComponent());

    Jail.scaleHeight(1.7f);
    animator.startAnimation("jail_close");
    return Jail;
  }
  public static Entity createFire() {
    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/minigame/fire.atlas", TextureAtlas.class));
    animator.addAnimation("image_part1", 0.01f, Animation.PlayMode.LOOP);
    animator.addAnimation("image_part2", 0.01f, Animation.PlayMode.LOOP);
    animator.addAnimation("image_part3", 0.01f, Animation.PlayMode.LOOP);
    animator.addAnimation("image_part4", 0.01f, Animation.PlayMode.LOOP);
    animator.addAnimation("image_part5", 0.01f, Animation.PlayMode.LOOP);


    Entity fire =
            new Entity()
                    .addComponent(animator)
                    .addComponent(new FireAnimationController(new AssetManager()));

    return fire;
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