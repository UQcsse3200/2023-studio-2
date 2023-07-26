package com.csse3200.game.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class RendererTest {
  @Spy OrthographicCamera camera;
  @Mock SpriteBatch spriteBatch;
  @Mock Stage stage;
  @Mock Graphics graphics;
  @Mock RenderService renderService;
  @Mock DebugRenderer debugRenderer;
  PhysicsService physicsService;

  @BeforeEach
  void beforeEach() {
    Gdx.graphics = graphics;
    physicsService = new PhysicsService(mock(PhysicsEngine.class));
  }

  @Test
  void shouldResizeCamera() {
    CameraComponent cameraComponent = makeCameraEntity(camera);

    when(stage.getViewport()).thenReturn(mock(Viewport.class));
    when(graphics.getWidth()).thenReturn(100);
    when(graphics.getHeight()).thenReturn(200);
    Renderer renderer =
        new Renderer(cameraComponent, 10, spriteBatch, stage, renderService, debugRenderer);

    assertEquals(Vector3.Zero, camera.position);
    assertEquals(10, camera.viewportWidth);
    assertEquals(20, camera.viewportHeight);

    renderer.resize(200, 100);
    assertEquals(10, camera.viewportWidth);
    assertEquals(5, camera.viewportHeight);
  }

  @Test
  void shouldResizeViewPort() {
    CameraComponent cameraComponent = makeCameraEntity(camera);
    ScreenViewport screenViewport = spy(ScreenViewport.class);
    Stage stage = new Stage(screenViewport, spriteBatch);

    Renderer renderer =
        new Renderer(cameraComponent, 10, spriteBatch, stage, renderService, debugRenderer);

    assertEquals(0, stage.getViewport().getScreenWidth());
    assertEquals(0, stage.getViewport().getScreenHeight());

    renderer.resize(200, 100);
    verify(screenViewport).update(200, 100, true);
    assertEquals(200, stage.getViewport().getScreenWidth());
    assertEquals(100, stage.getViewport().getScreenHeight());
  }

  @Test
  void shouldRender() {
    CameraComponent cameraComponent = makeCameraEntity(camera);
    Renderer renderer =
        new Renderer(cameraComponent, 10, spriteBatch, stage, renderService, debugRenderer);
    renderer.render();
    verify(renderService).render(spriteBatch);
  }

  private static CameraComponent makeCameraEntity(Camera camera) {
    Entity camEntity = new Entity().addComponent(new CameraComponent(camera));
    return camEntity.getComponent(CameraComponent.class);
  }
}
