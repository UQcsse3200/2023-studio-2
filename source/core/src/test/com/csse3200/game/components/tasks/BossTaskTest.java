package com.csse3200.game.components.tasks;

import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.ai.tasks.Task;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.enemies.EnemyType;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.StructurePlacementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class BossTaskTest {
  @BeforeEach
  void beforeEach() {
    ServiceLocator.registerPhysicsService(new PhysicsService());
    GameTime gameTime = mock(GameTime.class);
    ServiceLocator.registerTimeSource(gameTime);
    ServiceLocator.registerRenderService(new RenderService());
    ServiceLocator.registerStructurePlacementService(new StructurePlacementService(new EventHandler()));
  }

  /**
   * Tests that the Boss begins moving and the task runs
   */
  @Test
  void shouldTriggerEvent() {
    Entity target = new Entity();
    target.create();
    BossTask bossTask = new BossTask(EnemyType.BossMelee, target, 7, 100f, 1000f);

    PhysicsMovementComponent movement = new PhysicsMovementComponent();
    AITaskComponent aiTaskComponent = new AITaskComponent().addTask(bossTask);
    Entity entity = new Entity().addComponent(aiTaskComponent)
            .addComponent(movement)
            .addComponent(new PhysicsComponent());
    entity.create();

    bossTask.start();

    assertTrue(movement.getMoving());
    assertEquals(Task.Status.ACTIVE, bossTask.getStatus());
  }

  /**
   * Tests the special attack activates when 50% hp
   */
  @Test
  void shouldSpecialAttack() {
    Entity target = new Entity();
    target.create();
    BossTask bossTask = new BossTask(EnemyType.BossMelee, target, 7, 100f, 1000f);
    AITaskComponent aiTaskComponent = new AITaskComponent().addTask(bossTask);
    Entity entity = new Entity().addComponent(aiTaskComponent)
            .addComponent(new PhysicsMovementComponent())
            .addComponent(new PhysicsComponent())
            .addComponent(new CombatStatsComponent(100, 10, 1,false));
    entity.create();

    int halfHp = entity.getComponent(CombatStatsComponent.class).getMaxHealth() / 2;
    entity.getComponent(CombatStatsComponent.class).setHealth(halfHp);
    bossTask.start();
    bossTask.update();
    assertTrue(bossTask.isUnleashed());
  }
}