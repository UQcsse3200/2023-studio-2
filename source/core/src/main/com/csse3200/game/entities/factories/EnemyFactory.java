package com.csse3200.game.entities.factories;

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

import java.util.ArrayList;

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
   * Creates a melee enemy entity.
   *
   * @param targets entity to chase
   * @param type type of enemy - melee or ranged
   * @return entity
   */
  public static Entity createEnemy(ArrayList<Entity> targets, EnemyType type, EnemyBehaviour behaviour) {

    EnemyConfig config = configs.GetEnemyConfig(type, behaviour);
    AnimationRenderComponent animator;
    AITaskComponent aiComponent = new AITaskComponent();
    aiComponent.addTask(new WanderTask(new Vector2(2f, 2f), 2f));

    // Cycles through all targets
    for (Entity target : targets) {
      // Adds the specific behaviour to entity
      EnemyBehaviourSelector(target, type, behaviour, aiComponent);
    }

    TextureAtlas atlas = new TextureAtlas(config.atlas);
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
                    PhysicsLayer.WALL |
                    PhysicsLayer.STRUCTURE),
                    1.5f))
            .addComponent(new DialogComponent(dialogueBox));

    if (type == EnemyType.Ranged) {
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
            .addComponent(new CombatStatsComponent(config.health, config.baseAttack, 1, false));

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
    // Ranged Enemies
    if (type == EnemyType.Ranged) {
      // Player Targeting
      if (behaviour == EnemyBehaviour.PTE) {
        if (target.getComponent(HitboxComponent.class).getLayer() == PhysicsLayer.PLAYER) {
          aiTaskComponent.addTask(new AimTask( 2f, target, 3f));
          aiTaskComponent.addTask(new ChaseTask(target, 10, 6f, 6f, 3f));
        } else {
          aiTaskComponent.addTask(new ChaseTask(target, 0, 3f, 4f));
        }
      }
      // Destructible Targeting
      if (behaviour == EnemyBehaviour.DTE) {
        if (target.getComponent(HitboxComponent.class).getLayer() == PhysicsLayer.STRUCTURE) {
          aiTaskComponent.addTask(new ChaseTask(target, 10, 3f, 4f));
        } else {
          aiTaskComponent.addTask(new ChaseTask(target, 0, 3f, 4f));
        }
      }
    }
    // Melee Enemy
    if (type == EnemyType.Melee) {
      // Player Targeting
      if (behaviour == EnemyBehaviour.PTE) {
        if (target.getComponent(HitboxComponent.class).getLayer() == PhysicsLayer.PLAYER) {
          aiTaskComponent.addTask(new ChaseTask(target, 10, 3f, 4f));
        } else {
          aiTaskComponent.addTask(new ChaseTask(target, 5, 3f, 4f));
        }
      }
      // Destructible Targeting
      if (behaviour == EnemyBehaviour.DTE) {
        if (target.getComponent(HitboxComponent.class).getLayer() == PhysicsLayer.STRUCTURE) {
          aiTaskComponent.addTask(new ChaseTask(target, 10, 3f, 4f));
        } else {
          aiTaskComponent.addTask(new ChaseTask(target, 0, 3f, 4f));
        }
      }
    }
    // Boss Melee
    if (type == EnemyType.BossMelee) {
      // Player Targetting
      if (behaviour == EnemyBehaviour.PTE) {
        if (target.getComponent(HitboxComponent.class).getLayer() == PhysicsLayer.PLAYER) {
          aiTaskComponent.addTask(new ChaseTask(target, 10, 3f, 4f));
        } else {
          aiTaskComponent.addTask(new ChaseTask(target, 0, 3f, 4f));
        }
      }
      // Destructible Targetting
      if (behaviour == EnemyBehaviour.DTE) {
        if (target.getComponent(HitboxComponent.class).getLayer() == PhysicsLayer.STRUCTURE) {
          aiTaskComponent.addTask(new ChaseTask(target, 10, 3f, 4f));
        } else {
          aiTaskComponent.addTask(new ChaseTask(target, 0, 3f, 4f));
        }
      }
    }
    // Boss Ranged (For future sprints)
    if (type == EnemyType.BossRanged) {
      // Player Targeting
      if (behaviour == EnemyBehaviour.PTE) {
        if (target.getComponent(HitboxComponent.class).getLayer() == PhysicsLayer.PLAYER) {
          aiTaskComponent.addTask(new ChaseTask(target, 10, 3f, 4f));
        } else {
          aiTaskComponent.addTask(new ChaseTask(target, 0, 3f, 4f));
        }
      }
      // Destructible Targeting
      if (behaviour == EnemyBehaviour.DTE) {
        if (target.getComponent(HitboxComponent.class).getLayer() == PhysicsLayer.STRUCTURE) {
          aiTaskComponent.addTask(new ChaseTask(target, 10, 3f, 4f));
        } else {
          aiTaskComponent.addTask(new ChaseTask(target, 0, 3f, 4f));
        }
      }
    }
  }

  /**
   * Throws error when attempting the instantiating of static class
   */
  private EnemyFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
