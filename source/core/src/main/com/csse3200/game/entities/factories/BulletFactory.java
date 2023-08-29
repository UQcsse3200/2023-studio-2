package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.ProjectileAttackComponent;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.npc.GhostAnimationController;
import com.csse3200.game.components.tasks.ChaseTask;
import com.csse3200.game.components.tasks.WanderTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.BulletConfig;
import com.csse3200.game.entities.configs.NPCConfigs;
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


public class BulletFactory {

    static final NPCConfigs configs =
            FileLoader.readClass(NPCConfigs.class, "configs/enemy.json");
    public static Entity createBullet(Entity target) {

        Entity enemy = createBaseBullet();

        AITaskComponent aiComponent = new AITaskComponent();


        aiComponent.addTask(new ChaseTask(target, 10, 100f, 100f));

        BulletConfig config = configs.GetBulletConfig();

        TextureAtlas atlas = new TextureAtlas(config.atlas);

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        atlas);
        animator.addAnimation("angry_float", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("float", 0.1f, Animation.PlayMode.LOOP);

        enemy
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack, 0 , false))
                .addComponent(animator)
                .addComponent(new GhostAnimationController())
                .addComponent(aiComponent);

        enemy.getComponent(AnimationRenderComponent.class).scaleEntity();

        return enemy;
    }


    public static Entity createBaseBullet() {

        Entity bullet =
                new Entity()
                        .addComponent(new PhysicsComponent())
                        .addComponent(new PhysicsMovementComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                        .addComponent(new ProjectileAttackComponent((short) (
                                PhysicsLayer.PLAYER |
                                        PhysicsLayer.WALL |
                                        PhysicsLayer.STRUCTURE),
                                1.5f));


        PhysicsUtils.setScaledCollider(bullet, 0.9f, 0.4f);
        return bullet;
    }
}
