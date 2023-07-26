package com.csse3200.game.components.tasks;

import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
class ChaseTaskTest {
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
  }

  @Test
  void shouldMoveTowardsTarget() {
    Entity target = new Entity();
    target.setPosition(2f, 2f);

    AITaskComponent ai = new AITaskComponent().addTask(new ChaseTask(target, 10, 5, 10));
    Entity entity = makePhysicsEntity().addComponent(ai);
    entity.create();
    entity.setPosition(0f, 0f);

    float initialDistance = entity.getPosition().dst(target.getPosition());
    // Run the game for a few cycles
    for (int i = 0; i < 3; i++) {
      entity.earlyUpdate();
      entity.update();
      ServiceLocator.getPhysicsService().getPhysics().update();
    }
    float newDistance = entity.getPosition().dst(target.getPosition());
    assertTrue(newDistance < initialDistance);
  }

  @Test
  void shouldChaseOnlyWhenInDistance() {
    Entity target = new Entity();
    target.setPosition(0f, 6f);

    Entity entity = makePhysicsEntity();
    entity.create();
    entity.setPosition(0f, 0f);

    ChaseTask chaseTask = new ChaseTask(target, 10, 5, 10);
    chaseTask.create(() -> entity);

    // Not currently active, target is too far, should have negative priority
    assertTrue(chaseTask.getPriority() < 0);

    // When in view distance, should give higher priority
    target.setPosition(0f, 4f);
    assertEquals(10, chaseTask.getPriority());

    // When active, should chase if within chase distance
    target.setPosition(0f, 8f);
    chaseTask.start();
    assertEquals(10, chaseTask.getPriority());

    // When active, should not chase outside chase distance
    target.setPosition(0f, 12f);
    assertTrue(chaseTask.getPriority() < 0);
  }

  private Entity makePhysicsEntity() {
    return new Entity()
        .addComponent(new PhysicsComponent())
        .addComponent(new PhysicsMovementComponent());
  }
}
