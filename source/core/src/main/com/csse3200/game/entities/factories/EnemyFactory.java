package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.*;
import com.csse3200.game.components.flags.EnemyFlag;
import com.csse3200.game.components.npc.EnemyAnimationController;
import com.csse3200.game.components.structures.TurretTargetableComponent;
import com.csse3200.game.components.tasks.*;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.EnemyConfig;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.entities.enemies.EnemyBehaviour;
import com.csse3200.game.entities.enemies.EnemyName;
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
   * @param name - the name of the enemy
   * @return new enemy entity
   */
  public static Entity createEnemy(EnemyName name) {
    return createEnemy(configs.GetEnemyConfig(name));
  }

  /**
   * Creates a melee enemy entity.
   *
   * @param config - config file to replicate entity from
   * @return entity
   * also helps in triggering sound
   */
  public static Entity createEnemy(EnemyConfig config) {
    AnimationRenderComponent animator;
    AITaskComponent aiComponent = new AITaskComponent();
    aiComponent.addTask(new WanderTask(new Vector2(2f, 2f), 2f));

    // Cycles through all targets
    //TODO: This should probably be contained in its own AITask -
    // this doesn't allow for new entities after enemy creation
    List<Entity> targets = ServiceLocator.getEntityService().getEntitiesByComponent(HitboxComponent.class);
    for (Entity target : targets) {
      // Adds the specific behaviour to entity
      enemyBehaviourSelector(target, config.type, config.behaviour, aiComponent, config.isBoss);
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
            .addComponent(new HealthBarComponent(false))
            .addComponent(new TouchAttackComponent((short) (
                    PhysicsLayer.PLAYER |
                    PhysicsLayer.COMPANION |
                    PhysicsLayer.WALL |
                    PhysicsLayer.STRUCTURE |
                    PhysicsLayer.WEAPON),
                    1.5f))
            .addComponent(new CombatStatsComponent(
                    config.health,
                    config.baseAttack,
                    config.attackMultiplier,
                    config.isImmune))
            .addComponent(new DialogComponent(dialogueBox))
            .addComponent(new TurretTargetableComponent())
            .addComponent(new SoundComponent(config.sound))
            .addComponent(new EnemyFlag());

    // TYPE
    enemyTypeSelector(enemy, config.type);

    enemy.addComponent(aiComponent);

    if(!(config.isBoss)){
        enemiesList.add(enemy);
    }

    // ANIMATIONS
    animator.addAnimation("float", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("angry_float", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("left",0.2f,Animation.PlayMode.LOOP);
    animator.addAnimation("stand",0.3f,Animation.PlayMode.LOOP);
    animator.addAnimation("attack",0.05f,Animation.PlayMode.LOOP);
    animator.addAnimation("death", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("chaseLeft",0.3f,Animation.PlayMode.LOOP_REVERSED);
    animator.addAnimation("attackLeft",0.05f,Animation.PlayMode.LOOP_REVERSED);
    animator.addAnimation("invisible",0.5f, Animation.PlayMode.LOOP);
    enemy.addComponent(animator);
    enemy.addComponent(new EnemyAnimationController());

    // Scaling the enemy's visual size
    // UI adjustments
    enemy.getComponent(AnimationRenderComponent.class).scaleEntity();
    enemy.scaleWidth(getEnemyscale(config));
    PhysicsUtils.setScaledCollider(enemy, 0.5f, 0.5f);

    return enemy;
  }

  /**
   * Determines the appropriate scale for an enemy based on its type.
   * @param config The type of the enemy for which the scale is to be determined.
   * @return The scaling factor for the provided enemy type.
   */
  static float getEnemyscale(EnemyConfig config){
    float scale = 1f;

    if (config.type == EnemyType.Ranged) {
      if (config.isBoss){
        scale = 3.0f;
      }
    }
    else if (config.type == EnemyType.Melee) {
      if (config.behaviour == EnemyBehaviour.DTE) {
        scale = 1.2f;
      }

      if (config.isBoss){
        scale = 3.0f;
      }
    }
    return scale;
  }

  /**
   * Sets the enemy's type depending on type given
   * @param enemy The enemy to work with
   * @param type The type the enemy needs to be
   */
  private static void enemyTypeSelector(Entity enemy, EnemyType type) {
    enemy.addComponent(new HitboxComponent());
    if (type == EnemyType.Ranged) {
      enemy.getComponent(HitboxComponent.class).setLayer(PhysicsLayer.ENEMY_RANGE);
    }
    if (type == EnemyType.Melee) {
      enemy.getComponent(HitboxComponent.class).setLayer(PhysicsLayer.ENEMY_MELEE);
    }
  }

  /**
   * Function to set the behaviour of the desired enemy
   * @param target The player entity
   * @param type Melee, Ranged, Boss or Mixture of the referred
   * @param behaviour Player or Destructible Targeting
   * @param aiTaskComponent The enemy's aiComponent
   * @param isBoss Boolean indicating if enemy is a boss type
   */
  private static void enemyBehaviourSelector(Entity target, EnemyType type, EnemyBehaviour behaviour, AITaskComponent aiTaskComponent, boolean isBoss) {
    short layer = target.getComponent(HitboxComponent.class).getLayer();
    boolean isPlayer = PhysicsLayer.contains(layer, PhysicsLayer.PLAYER);
    boolean isStructure = PhysicsLayer.contains(layer, PhysicsLayer.STRUCTURE);
    boolean matchingBehaviour = isPlayer && behaviour == EnemyBehaviour.PTE || isStructure && behaviour == EnemyBehaviour.DTE;

    int priority = matchingBehaviour ? 10 : 0; //Matching behaviour and target gives priority 10
    float viewDistance = 100f;
    float maxChaseDistance = 100f;

    if (isPlayer && behaviour == EnemyBehaviour.DTE) {
      //Special case for player targeting meleeDTE will add chaseTask of prio 9
      priority = 9;
    }
    // Select and add the necessary behaviour
    addBehaviour(type, aiTaskComponent, target, priority, viewDistance, maxChaseDistance, isBoss);
  }

  /**
   * Helper Method that edits the aicomponent of enemies. Boss enemies and Ranged enemies requires different tasks.
   * @param type Type of enemy
   * @param aiComponent aiComponent of the enemy
   * @param target the Player's entity
   * @param priority given priority to enemy entity
   * @param viewDistance view distance for ranged enemies
   * @param maxChaseDistance max chase distance for enemies
   * @param isBoss boolean for boss check
   */
  static void addBehaviour(EnemyType type, AITaskComponent aiComponent, Entity target, int priority, float viewDistance, float maxChaseDistance, boolean isBoss) {
    // Select behaviour
    if (isBoss) {
      if (type == EnemyType.Melee) {
        // Melee Boss
        aiComponent.addTask(new BossTask(EnemyType.Melee, target, priority, viewDistance, maxChaseDistance));
      } else {
        // Ranged Boss
        aiComponent.addTask(new BossTask(EnemyType.Ranged, target, priority, viewDistance, maxChaseDistance));
      }
    } else if (type == EnemyType.Melee) {
      // Melee Enemy
      aiComponent.addTask(new ChaseTask(target, priority, viewDistance, maxChaseDistance));
    } else if (type == EnemyType.Ranged) {
      // Ranged Enemy
      float aimDelay = 2f;
      float range = 3f;
      float shootDistance = 3f;
      viewDistance = 100f;
      maxChaseDistance = 100f;

      aiComponent.addTask(new AimTask(aimDelay, target, range));
      aiComponent.addTask(new RunTask(target, 11, 2f));
      aiComponent.addTask(new ChaseTask(target, priority, viewDistance, maxChaseDistance, shootDistance));
    }
  }

  /**
   * Sets a target for an enemy
   * @param target The player's entity
   * @param aiTaskComponent the enemy's aiComponent
   */
  public static void targetSet(Entity target, AITaskComponent aiTaskComponent) {
    short layer = target.getComponent(HitboxComponent.class).getLayer();
    boolean isEnemy = PhysicsLayer.contains(layer, (short) (PhysicsLayer.ENEMY_RANGE | PhysicsLayer.ENEMY_MELEE));

    int priority = isEnemy ? 10 : 0;
    float viewDistance = 100f;
    float maxChaseDistance = 100f;

    if (PhysicsLayer.contains(layer, PhysicsLayer.ENEMY_MELEE)){
      priority = 10;
    }

    if (PhysicsLayer.contains(layer, PhysicsLayer.ENEMY_RANGE)) {
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

  /**
   * Helper to return the list of all current enemies on field
   * @return List of all Entities classifed as entities
   */
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
