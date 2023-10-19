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
class SpawnAttackTaskTest {
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
  void shouldNotBeActive() {
    Entity target = new Entity();
    target.setPosition(0f, 6f);

    Entity entity = makePhysicsEntity();
    entity.create();
    entity.setPosition(0f, 0f);

    SpawnAttackTask spawnTask = new SpawnAttackTask(target, 1f, 10, 0);
    spawnTask.create(() -> entity);

    // Not currently active, does not need to spawn entities
    assertTrue(spawnTask.getPriority() < 0);
  }

  @Test
  void shouldBeActive() {
    Entity target = new Entity();
    target.setPosition(0f, 6f);

    Entity entity = makePhysicsEntity();
    entity.create();
    entity.setPosition(0f, 0f);

    SpawnAttackTask spawnTask = new SpawnAttackTask(target, 1f, 10, 10);
    spawnTask.create(() -> entity);

    // Currently active as entities need to be spawned
      assertEquals(10, spawnTask.getPriority());
  }



  private Entity makePhysicsEntity() {
    return new Entity()
        .addComponent(new PhysicsComponent())
        .addComponent(new PhysicsMovementComponent());
  }
}
