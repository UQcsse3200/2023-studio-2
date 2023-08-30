package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.ProjectileAttackComponent;
import com.csse3200.game.components.npc.EnemyAnimationController;
import com.csse3200.game.components.tasks.ProjectileMovementTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.EnemyBulletConfig;
import com.csse3200.game.entities.configs.ProjectileConfigs;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * Factory to create projectile entities with predefined components.
 */
public class ProjectileFactory {

    static final ProjectileConfigs configs =
            FileLoader.readClass(ProjectileConfigs.class, "configs/projectile.json");

    /**
     * Creates A projectile, using the stats of the shooter which will fire towards a certain location.
     *
     * @param targetLocation The location where the projectile is aimed
     * @param shooter The entity which fired the projectile
     * @return The projectile entity
     */
    public static Entity createEnemyBullet(Vector2 targetLocation, Entity shooter) {
        int damage = shooter.getComponent(CombatStatsComponent.class).getAttack();

        Entity enemy = createBaseBullet();

        AITaskComponent aiComponent = new AITaskComponent();

        aiComponent.addTask(new ProjectileMovementTask(targetLocation, 10, 100f, 100f));

        EnemyBulletConfig config = configs.GetProjectileConfig();

        TextureAtlas atlas = new TextureAtlas(config.atlas);

        // Animations for the bullet
        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        atlas);
        animator.addAnimation("angry_float", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("float", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("explode",0.3f, Animation.PlayMode.LOOP);


        enemy
                .addComponent(new CombatStatsComponent(0, damage, 1, false))
                .addComponent(animator)
                .addComponent(new EnemyAnimationController())
                .addComponent(aiComponent);

        enemy.getComponent(AnimationRenderComponent.class).scaleEntity();

        return enemy;
    }

    /**
     * Creates the base projectile entity with the default components.
     *
     * @return The base projectile entity
     */
    public static Entity createBaseBullet() {
        // Makes bullet entity with physical interaction components
        Entity bullet =
                new Entity()
                        .addComponent(new PhysicsComponent())
                        .addComponent(new PhysicsMovementComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                        .addComponent(new ProjectileAttackComponent((short) (
                                PhysicsLayer.PLAYER |
                                PhysicsLayer.WALL |
                                PhysicsLayer.STRUCTURE |
                                PhysicsLayer.NPC),
                                1.5f));

        // Image scaling
        PhysicsUtils.setScaledCollider(bullet, 0.05f, 0.05f);
        return bullet;
    }
}
