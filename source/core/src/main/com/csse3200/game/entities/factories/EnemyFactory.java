package com.csse3200.game.entities.factories;

import com.csse3200.game.components.*;
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
import com.csse3200.game.entities.configs.EnemyConfigs;
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
  private static final EnemyConfigs configs =
      FileLoader.readClass(EnemyConfigs.class, "configs/enemy.json");
  public static DialogueBox dialogueBox;

  /**
   * Creates a melee enemy entity.
   *
   * @param targets entity to chase
   * @param type type of enemy - melee or ranged
   * @return entity
   */
  public static Entity createEnemy(ArrayList<Entity> targets, EnemyType type, EnemyBehaviour behaviour) {
    // CONFIGS
    EnemyConfig config = configs.GetEnemyConfig(type, behaviour);
    int health = config.health;
    int baseAttack = config.baseAttack;
    int speed = config.speed;
    int specialAttack = config.specialAttack;
    TextureAtlas atlas = new TextureAtlas(config.atlas);

    // SETUP
    Entity enemy =
        new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new PhysicsMovementComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new DeathComponent())
            .addComponent(new HealthBarComponent(false))
            .addComponent(new TouchAttackComponent(
                    (short) (PhysicsLayer.PLAYER |
                    PhysicsLayer.WALL |
                    PhysicsLayer.STRUCTURE),
                    1.5f))
            .addComponent(new CombatStatsComponent(
                    health,
                    baseAttack,
                    1,
                    false))
            .addComponent(new DialogComponent(dialogueBox));

    // TASKS
    AITaskComponent aiComponent = new AITaskComponent();
    aiComponent.addTask(new WanderTask(new Vector2(2f, 2f), 2f));
    // Cycles through all targets
    for (Entity target : targets) {
      // Adds the specific behaviour to entity
      EnemyBehaviourSelector(target, type, behaviour, aiComponent);
    }
    enemy.addComponent(aiComponent);

    // TYPE
    EnemyTypeSelector(enemy, type);

    // ANIMATIONS
    AnimationRenderComponent animator = new AnimationRenderComponent(atlas);
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

    // UI adjustments
    enemy.getComponent(AnimationRenderComponent.class).scaleEntity();
    PhysicsUtils.setScaledCollider(enemy, 0.45f, 0.2f);
    float scale = 2.0f;
    if (type == EnemyType.BossRanged){
      scale = 1.5f;
    } else if (type == EnemyType.Ranged) {
      scale = 1.0f;
    }
    enemy.scaleHeight(scale);
    return enemy;
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
    // Ranged Enemies
    if (type == EnemyType.Ranged) {
      // Wizard
      if (behaviour == EnemyBehaviour.PTE) {
        if (target.getComponent(HitboxComponent.class).getLayer() == PhysicsLayer.PLAYER) {
          aiTaskComponent.addTask(new AimTask( 2f, target, 3f));
          aiTaskComponent.addTask(new ChaseTask(target, 10, 100f, 100f, 3f));
        } else {
          aiTaskComponent.addTask(new ChaseTask(target, 0, 3f, 4f));
        }
      }
      // TODO: TBA
      if (behaviour == EnemyBehaviour.DTE) {
        if (target.getComponent(HitboxComponent.class).getLayer() == PhysicsLayer.STRUCTURE) {
          aiTaskComponent.addTask(new ChaseTask(target, 10, 6f, 6f, 3f));
        } else {
          aiTaskComponent.addTask(new ChaseTask(target, 0, 3f, 4f));
        }
      }
    }
    // Red Ghost
    if (type == EnemyType.Melee) {
      // Player Targeting
      if (behaviour == EnemyBehaviour.PTE) {
        // delete this line when finished with it
        aiTaskComponent.addTask(new BossTask("Test"));
        // heheehhehehehehehheehehehheheheh
        if (target.getComponent(HitboxComponent.class).getLayer() == PhysicsLayer.PLAYER) {
          aiTaskComponent.addTask(new ChaseTask(target, 10, 100f, 100f));
        } else {
          aiTaskComponent.addTask(new ChaseTask(target, 5, 3f, 4f));
        }
      }
      // Troll
      if (behaviour == EnemyBehaviour.DTE) {
        if (target.getComponent(HitboxComponent.class).getLayer() == PhysicsLayer.STRUCTURE) {
          aiTaskComponent.addTask(new ChaseTask(target, 10, 100f, 100f));
        } else {
          aiTaskComponent.addTask(new ChaseTask(target, 0, 3f, 4f));
        }
      }
    }
    // M.E.C.H
    if (type == EnemyType.BossMelee) {
      // Player Targeting
      if (behaviour == EnemyBehaviour.PTE) {
        if (target.getComponent(HitboxComponent.class).getLayer() == PhysicsLayer.PLAYER) {
          aiTaskComponent.addTask(new ChaseTask(target, 10, 100f, 100f));
        } else {
          aiTaskComponent.addTask(new ChaseTask(target, 0, 3f, 4f));
        }
      }
      // B.U.L.L
      if (behaviour == EnemyBehaviour.DTE) {
        if (target.getComponent(HitboxComponent.class).getLayer() == PhysicsLayer.STRUCTURE) {
          aiTaskComponent.addTask(new ChaseTask(target, 10, 100f, 100f));
        } else {
          aiTaskComponent.addTask(new ChaseTask(target, 0, 3f, 4f));
        }
      }
    }
    // TODO: TBA
    if (type == EnemyType.BossRanged) {
      // Player Targeting
      if (behaviour == EnemyBehaviour.PTE) {
        if (target.getComponent(HitboxComponent.class).getLayer() == PhysicsLayer.PLAYER) {
          aiTaskComponent.addTask(new ChaseTask(target, 10, 6f, 6f, 3f));
        } else {
          aiTaskComponent.addTask(new ChaseTask(target, 0, 3f, 4f));
        }
      }
      // TODO: TBA
      if (behaviour == EnemyBehaviour.DTE) {
        if (target.getComponent(HitboxComponent.class).getLayer() == PhysicsLayer.STRUCTURE) {
          aiTaskComponent.addTask(new ChaseTask(target, 10, 6f, 6f, 3f));
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
