package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.ai.tasks.Task;
import com.csse3200.game.areas.EarthGameArea;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.ProjectileAttackComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
public class ProjectileMovementTaskTest {
    private Entity newPlayer;
    private Entity projectile;
    private ProjectileMovementTask task;
    @BeforeEach
    void beforeEach() {
        // Mock rendering, physics, game time
        RenderService renderService = new RenderService();
        renderService.setDebug(mock(DebugRenderer.class));
        ServiceLocator.registerRenderService(renderService);
        GameTime gameTime = mock(GameTime.class);
        when(gameTime.getDeltaTime()).thenReturn(20f / 1000);
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(new PhysicsService());

        // Creating Player
        newPlayer = new Entity().addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER));
        // Projectile Setup
        projectile = new Entity()
                        .addComponent(new PhysicsComponent())
                        .addComponent(new PhysicsMovementComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                        .addComponent(new ProjectileAttackComponent((short) (
                                PhysicsLayer.PLAYER |
                                        PhysicsLayer.WALL |
                                        PhysicsLayer.STRUCTURE),
                                1.5f));
        AITaskComponent aiComponent = new AITaskComponent();
        task = new ProjectileMovementTask(newPlayer.getPosition(), 10, 100f, 100f);
        aiComponent.addTask(task);
        projectile.addComponent(aiComponent);
    }

    /**
     * Creates a projectile targetting player. Will pass test if the chase task (toward player) is active
     */
    @Test
    void createProjectileTest() {
        task.create(() -> projectile);
        task.start();
        // Checks the Projectile has been
        Assertions.assertEquals(Task.Status.ACTIVE, task.getStatus());
    }

    /**
     *
     */
//    @Test
//    void sendProjectileTest() {
//        // Arbitrary Projectile
//        float initialDistance = projectile.getPosition().dst(newPlayer.getPosition());
//        task.create(() -> projectile);
//        task.start();
//        // Run the game for a few cycles
//        for (int i = 0; i < 3; i++) {
//            projectile.earlyUpdate();
//            projectile.update();
//            ServiceLocator.getPhysicsService().getPhysics().update();
//        }
//        float newDistance = projectile.getPosition().dst(newPlayer.getPosition());
//        System.out.println(initialDistance + "->" + newDistance);
//        Assertions.assertTrue(newDistance < initialDistance);
//    }
}
