package com.csse3200.game.rendering;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.utils.math.Vector2Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class DebugRendererTest {
  @Mock ShapeRenderer shapeRenderer;
  @Mock Box2DDebugRenderer physicsRenderer;
  @Mock Matrix4 projMatrix;

  DebugRenderer debugRenderer;

  @BeforeEach
  void beforeEach() {
    debugRenderer = new DebugRenderer(physicsRenderer, shapeRenderer);
  }

  @Test
  void shouldRenderPhysics() {
    World physicsWorld = mock(World.class);

    debugRenderer.renderPhysicsWorld(physicsWorld);
    debugRenderer.render(projMatrix);

    verify(physicsRenderer).render(eq(physicsWorld), any());
  }

  @Test
  void shouldRenderLine() {
    Vector2 from = new Vector2(1f, 2f);
    Vector2 to = new Vector2(3f, 4f);
    debugRenderer.drawLine(from, to);
    debugRenderer.render(projMatrix);

    verify(shapeRenderer).line(from, to);

    // Should not render next frame
    debugRenderer.render(projMatrix);
    verify(shapeRenderer, times(1)).line(any(Vector2.class), any(Vector2.class));
  }

  @Test
  void shouldRenderRect() {
    Vector2 from = new Vector2(1f, 2f);
    Vector2 to = new Vector2(3f, 4f);

    debugRenderer.drawRectangle(from, to);
    debugRenderer.render(projMatrix);

    verify(shapeRenderer).rect(from.x, from.y, to.x, to.y);

    // Should not render next frame
    debugRenderer.render(projMatrix);
    verify(shapeRenderer, times(1)).rect(anyFloat(), anyFloat(), anyFloat(), anyFloat());
  }

  @Test
  void shouldNotRenderWhenDisabled() {
    debugRenderer.setActive(false);
    debugRenderer.drawRectangle(Vector2.Zero, Vector2Utils.ONE);
    debugRenderer.render(projMatrix);

    verify(shapeRenderer, times(0)).line(any(Vector2.class), any(Vector2.class));
  }
}
