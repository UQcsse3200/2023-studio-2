package com.csse3200.game.components.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.Task.Status;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
class MovementTaskTest {
  @BeforeEach
  void beforeEach() {
    ServiceLocator.registerPhysicsService(new PhysicsService());
    GameTime gameTime = mock(GameTime.class);
    when(gameTime.getTime()).thenReturn(0L);
    ServiceLocator.registerTimeSource(gameTime);
  }

  @Test
  void shouldMoveOnStart() {
    Vector2 target = new Vector2(10f, 10f);
    MovementTask task = new MovementTask(target);
    Entity entity = new Entity().addComponent(new PhysicsComponent());
    PhysicsMovementComponent movementComponent = new PhysicsMovementComponent();
    entity.addComponent(movementComponent);
    entity.create();

    task.create(() -> entity);
    task.start();
    assertTrue(movementComponent.getMoving());
    assertEquals(target, movementComponent.getTarget());
    assertEquals(Status.ACTIVE, task.getStatus());
  }

  @Test
  void shouldStopWhenClose() {
    MovementTask task = new MovementTask(new Vector2(10f, 10f), 2f);
    Entity entity = new Entity().addComponent(new PhysicsComponent());
    PhysicsMovementComponent movementComponent = new PhysicsMovementComponent();
    entity.addComponent(movementComponent);
    entity.setPosition(5f, 5f);
    entity.create();

    task.create(() -> entity);
    task.start();
    task.update();
    assertTrue(movementComponent.getMoving());
    assertEquals(Status.ACTIVE, task.getStatus());

    entity.setPosition(10f, 9f);
    task.update();
    assertFalse(movementComponent.getMoving());
    assertEquals(Status.FINISHED, task.getStatus());
  }
}