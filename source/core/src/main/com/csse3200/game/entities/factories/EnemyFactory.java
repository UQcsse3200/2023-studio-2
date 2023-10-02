package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.*;
import com.csse3200.game.components.npc.EnemyAnimationController;
import com.csse3200.game.components.structures.TurretTargetableComponent;
import com.csse3200.game.components.tasks.AimTask;
import com.csse3200.game.components.tasks.ChaseTask;
import com.csse3200.game.components.tasks.RunTask;
import com.csse3200.game.components.tasks.WanderTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.EnemyConfig;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.entities.enemies.EnemyBehaviour;
import com.csse3200.game.entities.enemies.EnemyType;
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

import java.util.ArrayList;
import java.util.List;
/**
 * Factory to create non-playable enemies entities with predefined components.
 *
 * <p>Each NPC entity type should have a creation method that returns a corresponding entity.
 * Predefined entity properties can be loaded from configs stored as json files which are defined in
 * "NPCConfigs".
 *
 * <p>If needed, this factory can be separated into more specific factories for entities with
 * similar characteristics.
 */

public class EnemyFactory {
  private static final NPCConfigs configs =
      FileLoader.readClass(NPCConfigs.class, "configs/enemy.json");
  public static DialogueBox dialogueBox;

  public static List<Entity> enemiesList = new ArrayList<Entity>();
  /**
   * Creates an enemy - using the default config as defined by the type and behaviour
   * @param type - enemy type
   * @param behaviour - Targeting behaviour of enemy
   * @return new enemy entity
   */
  public static Entity createEnemy(EnemyType type, EnemyBehaviour behaviour) {
    return createEnemy(configs.GetEnemyConfig(type, behaviour));
  }

  /**
   * Creates a melee enemy entity.
   *
   * @param config - config file to replicate entity from
   * @return entity
   * also helps in triggering sound
   */
  public static Entity createEnemy(EnemyConfig config) {
    System.out.println(config.type);
    AnimationRenderComponent animator;
    AITaskComponent aiComponent = new AITaskComponent();
    aiComponent.addTask(new WanderTask(new Vector2(2f, 2f), 2f));

    int health = config.health;
    int baseAttack = config.baseAttack;
    int speed = config.speed;
    int specialAttack = config.specialAttack;

    // Cycles through all targets
    //TODO: This should probably be contained in its own AITask -
    // this doesn't allow for new entities after enemy creation
    List<Entity> targets = ServiceLocator.getEntityService().getEntitiesByComponent(HitboxComponent.class);
    for (Entity target : targets) {
      // Adds the specific behaviour to entity
      EnemyBehaviourSelector(target, config.type, config.behaviour, aiComponent);
    }

    TextureAtlas atlas = new TextureAtlas(config.spritePath);
    animator = new AnimationRenderComponent(atlas);

    // SETUP
    Entity enemy =
        new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new PhysicsMovementComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new DeathComponent())
            .addComponent(new HitboxComponent())
            .addComponent(new HealthBarComponent(false))
            .addComponent(new TouchAttackComponent((short) (
                    PhysicsLayer.PLAYER |
                    PhysicsLayer.COMPANION |
                    PhysicsLayer.WALL |
                    PhysicsLayer.STRUCTURE |
                    PhysicsLayer.WEAPON),
                    1.5f))
            .addComponent(new CombatStatsComponent(
                    health,
                    baseAttack,
                    1,
                    false))
            .addComponent(new DialogComponent(dialogueBox))
            .addComponent(new TurretTargetableComponent())
                .addComponent(new SoundComponent(config.sound));

    if (config.type == EnemyType.Ranged) {
      enemy.getComponent(HitboxComponent.class).setLayer(PhysicsLayer.ENEMY_RANGE);
    } else {
      enemy.getComponent(HitboxComponent.class).setLayer(PhysicsLayer.ENEMY_MELEE);
    }
    enemy.addComponent(aiComponent);

    // TYPE
    EnemyTypeSelector(enemy, config.type);

    // ANIMATIONS
    animator.addAnimation("float", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("angry_float", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("left",0.2f,Animation.PlayMode.LOOP);
    animator.addAnimation("stand",0.3f,Animation.PlayMode.LOOP);
    animator.addAnimation("attack",0.05f,Animation.PlayMode.LOOP);
    animator.addAnimation("death", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("chaseLeft",0.3f,Animation.PlayMode.LOOP_REVERSED);
    animator.addAnimation("attackLeft",0.05f,Animation.PlayMode.LOOP_REVERSED);
    enemy.addComponent(animator);
    enemy.addComponent(new EnemyAnimationController());

    // Adding in animation controllers into the new enemy
    enemy
            .addComponent(new EnemyAnimationController())
            // adds tasks depending on enemy type
            .addComponent(aiComponent)
            .addComponent(new CombatStatsComponent(config.health, config.baseAttack, config.attackMultiplier, config.isImmune));

    // Scaling the enemy's visual size
    // UI adjustments
    enemy.getComponent(AnimationRenderComponent.class).scaleEntity();
    PhysicsUtils.setScaledCollider(enemy, 0.45f, 0.2f);
    enemy.scaleHeight(getEnemyscale(config));

    if(!(config.isBoss)){
      enemiesList.add(enemy);
    }

    return enemy;
  }

  /**
   * Determines the appropriate scale for an enemy based on its type.
   * @param config The type of the enemy for which the scale is to be determined.
   * @return The scaling factor for the provided enemy type.
   */
  static float getEnemyscale(EnemyConfig config){
    float scale = 2.0f;

    if (config.type == EnemyType.Ranged) {
      scale = 2.0f;
      if (config.isBoss){
        scale = 4.4f;
      }
    }
    else if (config.type == EnemyType.Melee) {
      scale = 1.8f;
      if (config.isBoss){
        scale = 4.4f;
      }
    }
    return scale;
  }

  /**
   * Function to set the physicsLayer of the desired enemy
   *
   * @param type
   */
  private static void EnemyTypeSelector(Entity enemy, EnemyType type) {
    enemy.addComponent(new HitboxComponent());
    if (type == EnemyType.Ranged) {
      enemy.getComponent(HitboxComponent.class).setLayer(PhysicsLayer.ENEMY_RANGE);
    }
    if (type == EnemyType.Melee) {
      enemy.getComponent(HitboxComponent.class).setLayer(PhysicsLayer.ENEMY_MELEE);
    }
    if (type == EnemyType.BossRanged) {
      enemy.getComponent(HitboxComponent.class).setLayer(PhysicsLayer.ENEMY_RANGE);
    }
    if (type == EnemyType.BossMelee) {
      enemy.getComponent(HitboxComponent.class).setLayer(PhysicsLayer.ENEMY_MELEE);
    }
  }

  /**
   * Function to set the behaviour of the desired enemy
   *
   * @param target The player entity
   * @param type Melee, Ranged, Boss or Mixture of the referred
   * @param behaviour Player or Destructible Targeting
   */
  private static void EnemyBehaviourSelector(Entity target, EnemyType type, EnemyBehaviour behaviour, AITaskComponent aiTaskComponent) {
    short layer = target.getComponent(HitboxComponent.class).getLayer();
    boolean isPlayer = PhysicsLayer.contains(layer, (short) (PhysicsLayer.PLAYER | PhysicsLayer.COMPANION));
    boolean isStructure = PhysicsLayer.contains(layer, PhysicsLayer.STRUCTURE);
    boolean matchingBehaviour = isPlayer && behaviour == EnemyBehaviour.PTE || isStructure && behaviour == EnemyBehaviour.DTE;

    int priority = matchingBehaviour ? 10 : 0; //Matching behaviour and target gives priority 10
    float viewDistance = 100f;
    float maxChaseDistance = 100f;

    if (type == EnemyType.Melee && !isPlayer && !matchingBehaviour) priority = 5; //Special case for player targeting melee

    //Special case for shooting player
    if (type == EnemyType.Ranged || type == EnemyType.BossRanged) {
      float aimDelay = 2f;
      float range = 3f;
      float shootDistance = 3f;
      viewDistance = 100f;
      maxChaseDistance = 100f;

      aiTaskComponent.addTask(new AimTask(aimDelay, target, range));
      aiTaskComponent.addTask(new RunTask(target, 11, 2f));
      aiTaskComponent.addTask(new ChaseTask(target, priority, viewDistance, maxChaseDistance, shootDistance));
    } else {
      aiTaskComponent.addTask(new ChaseTask(target, priority, viewDistance, maxChaseDistance));
    }
  }

  public static List<Entity> getEnemyList(){
    return enemiesList;
  }


  /**
   * Throws error when attempting the instantiating of static class
   */
  private EnemyFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
