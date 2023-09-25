package com.csse3200.game.physics;

import com.badlogic.gdx.physics.box2d.*;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.GameTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
class PhysicsEngineTest {
  @Mock GameTime gameTime;
  @Mock World world;

  @Test
  void shouldNotStepWithoutEnoughTime() {
    when(gameTime.getDeltaTime()).thenReturn(0f);
    PhysicsEngine engine = new PhysicsEngine(world, gameTime);
    engine.update();
    verify(world, times(0)).step(anyFloat(), anyInt(), anyInt());
  }

  @Test
  void shouldStepOnceAfterTime() {
    when(gameTime.getDeltaTime()).thenReturn(0.02f);
    PhysicsEngine engine = new PhysicsEngine(world, gameTime);

    engine.update();
    verify(world, times(1)).step(anyFloat(), anyInt(), anyInt());
  }

  @Test
  void shouldStepMultipleAfterLongTime() {
    PhysicsEngine engine = new PhysicsEngine(world, gameTime);
    when(gameTime.getDeltaTime()).thenReturn(0.06f);

    engine.update();
    verify(world, times(3)).step(anyFloat(), anyInt(), anyInt());
  }

  @Test
  void shouldCreateBody() {
    PhysicsEngine engine = new PhysicsEngine(world, gameTime);
    BodyDef bodyDef = new BodyDef();
    Body body = engine.createBody(bodyDef);
    verify(world).createBody(bodyDef);

    engine.destroyBody(body);
    verify(world).destroyBody(body);
  }

  @Test
  void shouldCreateJoint() {
    PhysicsEngine engine = new PhysicsEngine(world, gameTime);
    JointDef jointDef = new JointDef();
    Joint joint = engine.createJoint(jointDef);
    verify(world).createJoint(jointDef);

    engine.destroyJoint(joint);
    verify(world).destroyJoint(joint);
  }

  @Test
  void shouldDisposeWorld() {
    PhysicsEngine engine = new PhysicsEngine(world, gameTime);
    engine.dispose();
    verify(world).dispose();
  }
}
