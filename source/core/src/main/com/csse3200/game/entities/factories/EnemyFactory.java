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
   * @return entity
   */
  public static Entity createMeleeEnemy(ArrayList<Entity> targets) {
    Entity enemy = createBaseEnemy(targets);
    EnemyConfig config = configs.enemy;

    AnimationRenderComponent animator =
        new AnimationRenderComponent(
            ServiceLocator.getResourceService().getAsset("images/ghost.atlas", TextureAtlas.class));
    animator.addAnimation("angry_float", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("float", 0.1f, Animation.PlayMode.LOOP);

    enemy
        .addComponent(new CombatStatsComponent(config.health, config.baseAttack, 1, false))
        .addComponent(animator)
        .addComponent(new GhostAnimationController());

    enemy.getComponent(AnimationRenderComponent.class).scaleEntity();

    return enemy;
  }

  /**
   * Creates a boss entity.
   *
   * @param targets entities to chase
   * @return entity
   */
  public static Entity createBoss(ArrayList<Entity> targets) {
    Entity boss = createBaseEnemy(targets);
    BossConfig config = configs.boss;

    AnimationRenderComponent animator =
        new AnimationRenderComponent(
            ServiceLocator.getResourceService()
                .getAsset("images/ghostKing.atlas", TextureAtlas.class)); //  Currently a placeholder
    animator.addAnimation("float", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("angry_float", 0.1f, Animation.PlayMode.LOOP);

    boss
        .addComponent(new CombatStatsComponent(config.health, config.baseAttack, 2, false))
        .addComponent(animator)
        .addComponent(new GhostAnimationController());

    boss.getComponent(AnimationRenderComponent.class).scaleEntity();
    return boss;
  }

  /**
   * Creates a generic NPC to be used as a base entity by more specific NPC creation methods.
   *
   * @return entity
   */
  private static Entity createBaseEnemy(ArrayList<Entity> targets) {
    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new WanderTask(new Vector2(2f, 2f), 2f));

    // Add all the targets
    for (Entity i : targets) {
      aiComponent.addTask(new ChaseTask(i, 10, 3f, 4f));
    }

    Entity enemy =
        new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new PhysicsMovementComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
            .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 1.5f))
            .addComponent(aiComponent);

    PhysicsUtils.setScaledCollider(enemy, 0.9f, 0.4f);
    return enemy;
  }

  private EnemyFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
