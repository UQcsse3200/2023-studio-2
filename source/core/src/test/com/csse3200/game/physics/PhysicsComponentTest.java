package com.csse3200.game.physics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class PhysicsComponentTest {
  @Mock PhysicsEngine engine;
  @Mock Body body;

  @BeforeEach
  void beforeEach() {
    when(engine.createBody(any())).thenReturn(body);
    PhysicsService service = new PhysicsService(engine);
    ServiceLocator.registerPhysicsService(service);
  }

  @Test
  void shouldBecomeActiveOnCreate() {
    Entity entity = new Entity();
    PhysicsComponent component = new PhysicsComponent();
    entity.addComponent(component);

    verify(engine).createBody(any());
    assertEquals(body, component.getBody());

    entity.create();
    verify(body).setActive(true);
  }

  @Test
  void shouldMoveEntityToBody() {
    Entity entity = new Entity();
    PhysicsComponent component = new PhysicsComponent();
    entity.addComponent(component);
    entity.create();

    // Move entity to body
    Vector2 newPos = new Vector2(2f, 3f);
    when(body.getPosition()).thenReturn(newPos);
    entity.earlyUpdate();
    assertEquals(newPos, entity.getPosition());

    // Move body to entity
    newPos = new Vector2(-3f, 5f);
    entity.setPosition(newPos);
    verify(body).setTransform(eq(newPos), anyFloat());
  }
}
