package com.csse3200.game.entities.factories;

import com.badlogic.gdx.utils.Array;
import com.csse3200.game.ui.DialogComponent;
import com.csse3200.game.ui.DialogueBox;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.DeathComponent;
import com.csse3200.game.components.HealthBarComponent;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.npc.EnemyAnimationController;
import com.csse3200.game.components.tasks.*;
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
   */
  public static Entity createEnemy(EnemyConfig config) {
    AnimationRenderComponent animator;
    AITaskComponent aiComponent = new AITaskComponent();
    aiComponent.addTask(new WanderTask(new Vector2(2f, 2f), 2f));

    // Cycles through all targets
    //TODO: This should probably be contained in its own AITask -
    // this doesn't allow for new entities after enemy creation
    Array<Entity> targets = ServiceLocator.getEntityService().getEntitiesByComponent(HitboxComponent.class);
    for (Entity target : targets) {
      // Adds the specific behaviour to entity
      EnemyBehaviourSelector(target, config.type, config.behaviour, aiComponent);
    }

    TextureAtlas atlas = new TextureAtlas(config.spritePath);
    animator = new AnimationRenderComponent(atlas);

    // Create enemy with basic functionalities seen in components
    Entity enemy =
        new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new PhysicsMovementComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent())
            .addComponent(new DeathComponent())
            .addComponent(animator)
            .addComponent(new HealthBarComponent(false))
            .addComponent(new TouchAttackComponent((short) (
                    PhysicsLayer.PLAYER |
                    PhysicsLayer.COMPANION |
                    PhysicsLayer.WALL |
                    PhysicsLayer.STRUCTURE |
                    PhysicsLayer.WEAPON),
                    1.5f))
            .addComponent(new DialogComponent(dialogueBox));

    if (config.type == EnemyType.Ranged) {
      enemy.getComponent(HitboxComponent.class).setLayer(PhysicsLayer.ENEMY_RANGE);
    } else {
      enemy.getComponent(HitboxComponent.class).setLayer(PhysicsLayer.NPC);
    }

    // Animations for each enemy
    animator.addAnimation("float", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("angry_float", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("left",0.2f,Animation.PlayMode.LOOP);
    animator.addAnimation("stand",0.3f,Animation.PlayMode.LOOP);
    animator.addAnimation("attack",0.05f,Animation.PlayMode.LOOP);
    animator.addAnimation("death", 0.2f, Animation.PlayMode.LOOP);

    // Adding in animation controllers into the new enemy
    enemy
            .addComponent(new EnemyAnimationController())
            // adds tasks depending on enemy type
            .addComponent(aiComponent)
            .addComponent(new CombatStatsComponent(config.health, config.baseAttack, config.attackMultiplier, config.isImmune));

    // Scaling the enemy's visual size
    enemy.getComponent(AnimationRenderComponent.class).scaleEntity();
    PhysicsUtils.setScaledCollider(enemy, 0.45f, 0.2f);
    enemy.scaleHeight(2f);
    return enemy;
  }

  /**
   * Function to set the behaviour of the desired entity
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
    float viewDistance = 3f;
    float maxChaseDistance = 4f;

    if (type == EnemyType.Melee && !isPlayer && !matchingBehaviour) priority = 5; //Special case for player targeting melee

    //Special case for shooting player
    if (type == EnemyType.Ranged || type == EnemyType.BossRanged) {
      float aimDelay = 2f;
      float range = 3f;
      float shootDistance = 3f;
      viewDistance = 6f;
      maxChaseDistance = 6f;

      aiTaskComponent.addTask(new AimTask(aimDelay, target, range));
      aiTaskComponent.addTask(new ChaseTask(target, priority, viewDistance, maxChaseDistance, shootDistance));
    } else {
      aiTaskComponent.addTask(new ChaseTask(target, priority, viewDistance, maxChaseDistance));
    }
  }

  /**
   * Throws error when attempting the instantiating of static class
   */
  private EnemyFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
