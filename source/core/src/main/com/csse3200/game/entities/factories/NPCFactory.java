package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.*;
import com.csse3200.game.components.FollowComponent;
import com.csse3200.game.components.InteractableComponent;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.maingame.MainGameActions;
import com.csse3200.game.components.npc.*;
import com.csse3200.game.components.player.InteractionControllerComponent;
import com.csse3200.game.components.tasks.ChaseTask;
import com.csse3200.game.components.tasks.WanderTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.*;
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
import com.csse3200.game.ui.DialogComponent;
import com.csse3200.game.ui.DialogueBox;
import com.csse3200.game.ui.TitleBox;

import static com.csse3200.game.components.mainmenu.MainMenuActions.game;
import static com.csse3200.game.ui.DialogComponent.stage;
import static com.csse3200.game.ui.UIComponent.skin;

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

  /**
   * Creates a generic Botanist NPC entity.
   * @return The created Botanist NPC entity.
   */
  public static Entity createBotanist() {
    return createBotanist(configs.botanist);
  }

  /**
   * Creates a Botanist NPC to match the config file
   * @return The created Botanist NPC entity.
   */
  public static Entity createBotanist(BotanistConfig config) {

    AITaskComponent aiComponent = new AITaskComponent();
    aiComponent.addTask(new WanderTask(new Vector2(1.5f, 1.5f), 1f));

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset(config.spritePath, TextureAtlas.class));
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
                    .addComponent(new PhysicsComponent())
                    .addComponent(new PhysicsMovementComponent())
                    .addComponent(new InteractionControllerComponent(true))
                    .addComponent(aiComponent);
    botanist.addComponent(new InteractableComponent(entity -> {
      String[] storytext= {"{SLOW}Mysterious Entity: Thank you for saving me .",
              "{SLOW}I will give you all the Knowledge I have, \nto find a suitable planet for your Journey "};
      String[] titletext= {"",""};
      String[] window = {"dialogue_7", "dialogue_7"};

      TitleBox titleBox = new TitleBox(game, titletext, storytext, skin, window);
      titleBox.showDialog(ServiceLocator.getRenderService().getStage());
    },10f));

    botanist.scaleHeight(1f);
    return botanist;
  }

  /**
   * Creates an Astro NPC to match the config file
   * @return The created Astro NPC entity.
   */
  public static Entity createAstro(AstroConfig astroConfig) {

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/npc/caged_NPC.atlas", TextureAtlas.class));
    animator.addAnimation("Astro_Up", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("Astro_UpLeft", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("Astro_Left", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("Astro_DownLeft", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("Astro_Down", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("Astro_DownRight", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("Astro_Right", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("Astro_UpRight", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("Astro_StandDown", 0.2f);

    Entity astro =
            new Entity()
                    .addComponent(animator)
                    .addComponent(new AstroAnimationController())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.NPC_OBSTACLE))
                    .addComponent(new DialogComponent(dialogueBox))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new InteractionControllerComponent(true))
                    .addComponent(new PhysicsMovementComponent())
                    .addComponent(new FollowComponent(ServiceLocator.getEntityService().getPlayer(),0f));
    astro.addComponent(new SaveableComponent<>(p -> {
      astroConfig.position = p.getGridPosition();
      return astroConfig;
    }, AstroConfig.class));

    astro.getComponent(ColliderComponent.class).setDensity(1.5f);
    astro.addComponent(new InteractableComponent(entity -> {
      astro.getComponent(FollowComponent.class).setEntity(astro);
      astro.getComponent(FollowComponent.class).setFollowSpeed(2f);
    },3f));
    animator.startAnimation("Astro_StandDown");
    return astro;
  }

  public static Entity createTutnpc(TutnpcConfig tutnpcConfig) {

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset(tutnpcConfig.spritePath, TextureAtlas.class));
    animator.addAnimation("Tut_Up", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("Tut_UpLeft", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("Tut_Left", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("Tut_DownLeft", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("Tut_Down", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("Tut_DownRight", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("Tut_Right", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("Tut_UpRight", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("Tut_StandDown", 0.2f);


    Entity tutnpc =
            new Entity()
                    .addComponent(animator)
                    .addComponent(new TutnpcAnimationController())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.NPC_OBSTACLE))
                    .addComponent(new DialogComponent(dialogueBox))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new InteractionControllerComponent(true))
                    .addComponent(new PhysicsMovementComponent());

    tutnpc.getComponent(ColliderComponent.class).setDensity(1.5f);
    tutnpc.scaleHeight(0.7f);
    String[] storytext = {"I am your Tutorial Guide", "I am here to teach you how to play!"};
    String[] titletext = {"", ""};
    tutnpc.addComponent(new InteractableComponent(entity -> {
      tutnpc.getComponent(DialogComponent.class).showdialogue(storytext, titletext);
    }, 3f));
    animator.startAnimation("Tut_StandDown");
    return tutnpc;
  }

  public static Entity createHellman(HellmanConfig hellmanConfig) {

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/npc/Hellman.atlas", TextureAtlas.class));
    animator.addAnimation("row-1-column-1", 0.01f, Animation.PlayMode.LOOP);
    animator.addAnimation("row-1-column-2", 0.01f, Animation.PlayMode.LOOP);
    animator.addAnimation("row-1-column-3", 0.01f, Animation.PlayMode.LOOP);
    animator.addAnimation("row-1-column-4", 0.01f, Animation.PlayMode.LOOP);
    animator.addAnimation("row-1-column-5", 0.01f, Animation.PlayMode.LOOP);
    animator.addAnimation("row-1-column-6", 0.01f, Animation.PlayMode.LOOP);
    animator.addAnimation("row-1-column-7", 0.01f, Animation.PlayMode.LOOP);
    animator.addAnimation("row-1-column-8", 0.01f, Animation.PlayMode.LOOP);
    animator.addAnimation("row-1-column-9", 0.01f, Animation.PlayMode.LOOP);
    animator.addAnimation("row-1-column-10", 0.01f, Animation.PlayMode.LOOP);
    animator.addAnimation("row-1-column-11", 0.01f, Animation.PlayMode.LOOP);
    animator.addAnimation("row-1-column-12", 0.01f, Animation.PlayMode.LOOP);
    animator.addAnimation("row-1-column-13", 0.01f, Animation.PlayMode.LOOP);
    animator.addAnimation("row-1-column-14", 0.01f, Animation.PlayMode.LOOP);
    animator.addAnimation("idle", 0.01f, Animation.PlayMode.NORMAL);

    Entity Hellman =
            new Entity()
                    .addComponent(animator)
                    .addComponent(new HellmanAnimationController())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.NPC_OBSTACLE))
                    .addComponent(new DialogComponent(dialogueBox))
                    .addComponent(new PhysicsComponent().setBodyType(BodyType.StaticBody))
                    .addComponent(new InteractionControllerComponent(true))
                    .addComponent(new PhysicsMovementComponent());
    Hellman.addComponent(new SaveableComponent<>(p -> {
      hellmanConfig.position = p.getGridPosition();
      return hellmanConfig;
    }, HellmanConfig.class));

    Hellman.getComponent(ColliderComponent.class).setDensity(1.5f);
    Hellman.scaleHeight(2.0f);
    String[] storytext = {"If you defeat this boss,\n I will help you find a suitable planet", "You will find the boss on the \nother side of the portal."};
    String[] titletext = {"", ""};
    Hellman.addComponent(new InteractableComponent(entity -> {
      Hellman.getComponent(DialogComponent.class).showdialogue(storytext, titletext);
    }, 3f));
    return Hellman;
  }

  public static Entity createCircle(CircleConfig circleConfig) {
    Entity Circle =
            new Entity()
                    .addComponent(new TextureRenderComponent(circleConfig.spritePath))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new PhysicsMovementComponent())
                    .addComponent(new InteractionControllerComponent(true));

    Circle.scaleHeight(1.0f);
    Circle.addComponent(new InteractableComponent(entity -> {

          String[] nextMessages = {"SpaceBar is used to Sprint","You can swap around weapons by 1,2 and 3 \n 1 is Sword \n 2 is Fire Boomerang\n 3 is hammer which builds/repair things",
                  "After using 3 click on T to open the action picker and click on the action you want to do \n you can also directly switch by using the mouse and clicking on the action you want to do on the right "};
          String[] nextTitles = {"","",""};
          String[] window = {"dialogue_5","dialogue_5","dialogue_5"};
          TitleBox titleBox = new TitleBox(game, nextTitles, nextMessages, skin, window);
          titleBox.showDialog(ServiceLocator.getRenderService().getStage());
      }, 3f));
    return Circle;
  }

  public static Entity createAstronaut(AstronautConfig astronautConfig) {

    AITaskComponent aiComponent = new AITaskComponent();
    aiComponent.addTask(new WanderTask(new Vector2(1.5f, 1.5f), 1f));

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset(astronautConfig.spritePath, TextureAtlas.class));
    animator.addAnimation("row-1-column-1", 0.01f, Animation.PlayMode.LOOP);
    animator.addAnimation("row-1-column-2", 0.01f, Animation.PlayMode.LOOP);
    animator.addAnimation("row-1-column-3", 0.01f, Animation.PlayMode.LOOP);
    animator.addAnimation("row-1-column-4", 0.01f, Animation.PlayMode.LOOP);
    animator.addAnimation("row-2-column-1", 0.01f, Animation.PlayMode.LOOP);
    animator.addAnimation("row-2-column-2", 0.01f, Animation.PlayMode.LOOP);
    animator.addAnimation("row-2-column-3", 0.01f, Animation.PlayMode.LOOP);
    animator.addAnimation("row-2-column-4", 0.01f, Animation.PlayMode.LOOP);
    animator.addAnimation("row-3-column-1", 0.01f, Animation.PlayMode.LOOP);
    animator.addAnimation("row-3-column-2", 0.01f, Animation.PlayMode.LOOP);
    animator.addAnimation("row-3-column-3", 0.01f, Animation.PlayMode.LOOP);
    animator.addAnimation("row-3-column-4", 0.01f, Animation.PlayMode.LOOP);
    animator.addAnimation("row-4-column-1", 0.01f, Animation.PlayMode.LOOP);
    animator.addAnimation("row-4-column-2", 0.01f, Animation.PlayMode.LOOP);
    animator.addAnimation("row-4-column-3", 0.01f, Animation.PlayMode.LOOP);
    animator.addAnimation("row-4-column-4", 0.01f, Animation.PlayMode.LOOP);


    Entity astronaut =
            new Entity()
                    .addComponent(animator)
                    .addComponent(new AstronautAnimationController())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.NPC_OBSTACLE))
                    .addComponent(new DialogComponent(dialogueBox))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new PhysicsMovementComponent())
                    .addComponent(aiComponent);
    astronaut.addComponent(new SaveableComponent<>(p -> {
      astronautConfig.position = p.getGridPosition();
      return astronautConfig;
    }, AstronautConfig.class));

    astronaut.addComponent(new InteractableComponent(entity -> {
      String[] storytext= {"Hello, I've been stuck here for weeks","Can I please come with you?"};
      String[] titletext= {"",""};
      astronaut.getComponent(DialogComponent.class).showdialogue(storytext,titletext);
      // Since Dialogue Box does not show up on screen, cannot 'exit' dialogue box therefore
      // the 'resumeGame' function is never called. This code is to ensure gameplay can continue
      for (Entity mainGame : ServiceLocator.getEntityService().getEntitiesByComponent(MainGameActions.class)) {
        mainGame.getEvents().trigger("resumeGame");
      }
    },1f));

    astronaut.scaleHeight(1f);
    return astronaut;
  }
  public static Entity createJail(JailConfig jailConfig) {

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset(jailConfig.spritePath, TextureAtlas.class));
    animator.addAnimation("jail_close", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("jail_open", 0.2f, Animation.PlayMode.LOOP);

    Entity jail =
            new Entity()
                    .addComponent(animator)
                    .addComponent(new PhysicsComponent())
                    .addComponent(new PhysicsMovementComponent());
    jail.addComponent(new SaveableComponent<>(p -> {
      jailConfig.position = p.getGridPosition();
      return jailConfig;
    }, JailConfig.class));

    jail.addComponent(new InteractableComponent(entity -> {
      animator.startAnimation("jail_open");
      String[] storytext= {"{SLOW}Astro: Thank you for saving me .\n I have a ship that can help you ESCAPE EARTH!! "
              ,"{SLOW}Emily:Is that your ship there ?\n It is guarded by the deadly."
              ,"{SLOW}Player: We can handle it. \n Let me Lead the way!"};
      String[] titletext= {"","",""};
      String[] window = {"dialogue_1", "dialogue_1","dialogue_3"};

      TitleBox titleBox = new TitleBox(game, titletext, storytext, skin, window);
      titleBox.showDialog(ServiceLocator.getRenderService().getStage());

    },1f));

    jail.scaleHeight(1.7f);
    animator.startAnimation("jail_close");

    return jail;
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