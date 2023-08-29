package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.ai.tasks.Task;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.DeathComponent;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.npc.GhostAnimationController;
import com.csse3200.game.components.tasks.ChaseTask;
import com.csse3200.game.components.tasks.WanderAndShootTask;
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
    PriorityTask chaseTask;
    AITaskComponent aiComponent = new AITaskComponent();
    if (type != EnemyType.Ranged) {
      aiComponent.addTask(new WanderTask(new Vector2(2f, 2f), 2f));
    }

    for (Entity target : targets) {
      chaseTask = EnemyBehaviourSelector(target, type, behaviour);
      aiComponent.addTask(chaseTask);
    }

    TextureAtlas atlas = new TextureAtlas(config.atlas);
    animator = new AnimationRenderComponent(atlas);


    Entity enemy =
        new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new PhysicsMovementComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
            .addComponent(new DeathComponent())
            .addComponent(animator)
            .addComponent(new TouchAttackComponent((short) (
                    PhysicsLayer.PLAYER |
                    PhysicsLayer.WALL |
                    PhysicsLayer.STRUCTURE),
                    1.5f));

    animator.addAnimation("float", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("angry_float", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("left",0.2f,Animation.PlayMode.LOOP);
    animator.addAnimation("stand",0.3f,Animation.PlayMode.LOOP);
    animator.addAnimation("death", 0.2f, Animation.PlayMode.LOOP);


    enemy
            .addComponent(new GhostAnimationController())
            .addComponent(aiComponent)    // adds tasks depending on enemy type
            .addComponent(new CombatStatsComponent(config.health, config.baseAttack, 0, false));

    enemy.getComponent(AnimationRenderComponent.class).scaleEntity();

    PhysicsUtils.setScaledCollider(enemy, 0.9f, 0.4f);
    return enemy;
  }

  private static PriorityTask EnemyBehaviourSelector(Entity target, EnemyType type, EnemyBehaviour behaviour) {
    PriorityTask chaseTask = null;
    if (type == EnemyType.Ranged) {
      if (behaviour == EnemyBehaviour.PTE) {
        if (target.getComponent(HitboxComponent.class).getLayer() == PhysicsLayer.PLAYER) {
          chaseTask = (new WanderAndShootTask(new Vector2(2f, 2f), 2f, target));
        } else {
          chaseTask = (new ChaseTask(target, 0, 3f, 4f));
        }
      }
      if (behaviour == EnemyBehaviour.DTE) {
        if (target.getComponent(HitboxComponent.class).getLayer() == PhysicsLayer.STRUCTURE) {
          chaseTask = (new ChaseTask(target, 10, 100f, 100f));
        } else {
          chaseTask = (new ChaseTask(target, 0, 3f, 4f));
        }
      }
    }
    if (type == EnemyType.Melee) {
      if (behaviour == EnemyBehaviour.PTE) {
        if (target.getComponent(HitboxComponent.class).getLayer() == PhysicsLayer.PLAYER) {
          chaseTask = (new ChaseTask(target, 10, 100f, 100f));
        } else {
          chaseTask = (new ChaseTask(target, 0, 3f, 4f));
        }
      }
      if (behaviour == EnemyBehaviour.DTE) {
        if (target.getComponent(HitboxComponent.class).getLayer() == PhysicsLayer.STRUCTURE) {
          chaseTask = (new ChaseTask(target, 10, 100f, 100f));
        } else {
          chaseTask = (new ChaseTask(target, 0, 3f, 4f));
        }
      }
    }

    if (type == EnemyType.BossMelee) {
      if (behaviour == EnemyBehaviour.PTE) {
        if (target.getComponent(HitboxComponent.class).getLayer() == PhysicsLayer.PLAYER) {
          chaseTask = (new ChaseTask(target, 10, 100f, 100f));
        } else {
          chaseTask = (new ChaseTask(target, 0, 3f, 4f));
        }
      }
      if (behaviour == EnemyBehaviour.DTE) {
        if (target.getComponent(HitboxComponent.class).getLayer() == PhysicsLayer.STRUCTURE) {
          chaseTask = (new ChaseTask(target, 10, 100f, 4f));
        } else {
          chaseTask = (new ChaseTask(target, 0, 3f, 4f));
        }
      }
    }

    if (type == EnemyType.BossRanged) {
      if (behaviour == EnemyBehaviour.PTE) {
        if (target.getComponent(HitboxComponent.class).getLayer() == PhysicsLayer.PLAYER) {
          chaseTask = (new ChaseTask(target, 10, 100f, 100f));
        } else {
          chaseTask = (new ChaseTask(target, 0, 3f, 4f));
        }
      }
      if (behaviour == EnemyBehaviour.DTE) {
        if (target.getComponent(HitboxComponent.class).getLayer() == PhysicsLayer.STRUCTURE) {
          chaseTask = (new ChaseTask(target, 10, 100f, 4f));
        } else {
          chaseTask = (new ChaseTask(target, 0, 3f, 4f));
        }
      }
    }
    return chaseTask;
  }

  private EnemyFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
