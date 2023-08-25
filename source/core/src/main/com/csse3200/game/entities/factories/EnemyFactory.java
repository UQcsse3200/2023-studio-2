package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.npc.GhostAnimationController;
import com.csse3200.game.components.tasks.ChaseTask;
import com.csse3200.game.components.tasks.WanderTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.BossConfig;
import com.csse3200.game.entities.configs.EnemyConfig;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.entities.enemies.BossType;
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


    Entity enemy = createBaseEnemy();

    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new WanderTask(new Vector2(2f, 2f), 2f));


    if (type == EnemyType.Ranged) {
      for (Entity i : targets) {
        if(i.getComponent(HitboxComponent.class).getLayer() == PhysicsLayer.PLAYER){
          aiComponent.addTask(new ChaseTask(i, 10, 3f, 4f));
        }
        else{
          aiComponent.addTask(new ChaseTask(i, 0, 3f, 4f));
        }
      }
    }

    else {
      for (Entity i : targets) {
        if (behaviour == EnemyBehaviour.PTB) {
          if (i.getComponent(HitboxComponent.class).getLayer() == PhysicsLayer.PLAYER) {
            aiComponent.addTask(new ChaseTask(i, 10, 3f, 4f));
          } else {
            aiComponent.addTask(new ChaseTask(i, 5, 3f, 4f));
          }
        } else {
          if ( i.getComponent(HitboxComponent.class).getLayer() == PhysicsLayer.STRUCTURE) {
            aiComponent.addTask(new ChaseTask(i, 10, 3f, 4f));
          } else {
            aiComponent.addTask(new ChaseTask(i, 5, 3f, 4f));
          }
        }
      }
    }


    EnemyConfig config = configs.GetEnemyConfig(type);

    AnimationRenderComponent animator =
        new AnimationRenderComponent(
            ServiceLocator.getResourceService().getAsset(config.atlas, TextureAtlas.class));
    animator.addAnimation("angry_float", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("float", 0.1f, Animation.PlayMode.LOOP);

    enemy
        .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
        .addComponent(animator)
        .addComponent(new GhostAnimationController())
        .addComponent(aiComponent);    // adds tasks depending on enemy type

    enemy.getComponent(AnimationRenderComponent.class).scaleEntity();

    return enemy;
  }

  /**
   * Creates a boss entity.
   *
   * @param targets entities to chase
   * @param type type of enemy - melee or ranged
   * @return entity
   */
  public static Entity createBoss(ArrayList<Entity> targets, BossType type) {
    Entity boss = createBaseEnemy();

    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new WanderTask(new Vector2(2f, 2f), 2f));

    if (type == BossType.Melee) {
      for (Entity i : targets) {
        aiComponent.addTask(new ChaseTask(i, 10, 3f, 4f));


      }
    } else {
      for (Entity i : targets) {
        aiComponent.addTask(new ChaseTask(i, 10, 3f, 4f));


      }
    }

    BossConfig config = configs.GetBossConfig(type);

    AnimationRenderComponent animator =
        new AnimationRenderComponent(
            ServiceLocator.getResourceService()
                .getAsset("images/ghostKing.atlas", TextureAtlas.class)); //  Currently a placeholder
    animator.addAnimation("float", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("angry_float", 0.1f, Animation.PlayMode.LOOP);

    boss
        .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
        .addComponent(animator)
        .addComponent(new GhostAnimationController())
        .addComponent(aiComponent);

    boss.getComponent(AnimationRenderComponent.class).scaleEntity();
    return boss;
  }

  /**
   * Creates a generic NPC to be used as a base entity by more specific NPC creation methods.
   *
   * @return entity
   */
  public static Entity createBaseEnemy() {

    Entity enemy =
        new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new PhysicsMovementComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
            .addComponent(new TouchAttackComponent((short) (
                    PhysicsLayer.PLAYER |
                    PhysicsLayer.WALL |
                    PhysicsLayer.STRUCTURE),
                    1.5f));


    PhysicsUtils.setScaledCollider(enemy, 0.9f, 0.4f);
    return enemy;
  }

  private EnemyFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
