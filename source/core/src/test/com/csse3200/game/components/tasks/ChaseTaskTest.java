package com.csse3200.game.components.tasks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.listeners.EventListener0;
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

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class ChaseTaskTest {
  @BeforeEach
  void beforeEach() {
    // Mock rendering, physics, game time, game area
    RenderService renderService = new RenderService();
    TerrainComponent testTerrain = makeComponent();
    GameArea gameArea = new GameArea() {
      @Override
      public void create() {
        terrain = testTerrain;
      }
    };
    gameArea.create();
    ServiceLocator.registerGameArea(gameArea);
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
  void shouldChaseOnlyWhenInDistanceOne() {
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

  @Test
  void shouldChaseOnlyWhenInDistanceTwo() {
    Entity target = new Entity();
    target.setPosition(0f, 6f);

    Entity entity = makePhysicsEntity();
    entity.create();
    entity.setPosition(0f, 0f);

    ChaseTask chaseTask = new ChaseTask(target, 10, 5, 10, 2f);
    chaseTask.create(() -> entity);

    // Not currently active, target is too far, should have negative priority
    assertTrue(chaseTask.getPriority() < 0);

    // When in view distance, should give higher priority
    target.setPosition(0f, 4f);
    assertEquals(10, chaseTask.getPriority());

    // When active, should chase if within chase distance
    target.setPosition(0f, 7f);
    chaseTask.start();
    assertEquals(10, chaseTask.getPriority());

    // When active, should not chase outside chase distance
    target.setPosition(0f, 15f);
    assertTrue(chaseTask.getPriority() < 0);

    //When active, should not chase within the shoot distance
    target.setPosition(0f, 1f);
    assertTrue(chaseTask.getPriority() < 0);
  }
  @Test
  void shouldTriggerCorrectAnimation(){
    Entity target = new Entity();
    target.setPosition(3f, 0f);
    Entity entity = makePhysicsEntity();
    entity.create();
    entity.setPosition(0f, 0f);
    ChaseTask chaseTask = new ChaseTask(target, 10, 5, 10, 2f);
    chaseTask.create(() -> entity);

    EventListener0 callback = mock(EventListener0.class);
    entity.getEvents().addListener("chaseStart", callback);
    chaseTask.start();
    verify(callback).handle();

  }
  @Test
  void shouldTriggerCorrectAnimation2(){
    Entity target = new Entity();
    target.setPosition(0f, 0f);
    Entity entity = makePhysicsEntity();
    entity.create();
    entity.setPosition(3f, 0f);
    ChaseTask chaseTask = new ChaseTask(target, 10, 5, 10, 2f);
    chaseTask.create(() -> entity);

    EventListener0 callback = mock(EventListener0.class);
    entity.getEvents().addListener("chaseLeft", callback);
    chaseTask.start();
    verify(callback).handle();}

  private Entity makePhysicsEntity() {
    return new Entity()
        .addComponent(new PhysicsComponent())
        .addComponent(new PhysicsMovementComponent());
  }

  private static TerrainComponent makeComponent() {
    OrthographicCamera camera = mock(OrthographicCamera.class);
    String mapPath = "map/base.tmx";
    TmxMapLoader mapLoader = new TmxMapLoader();

    TiledMap tiledMap = null;
    for (String origin :
            new String[]{"source/core/assets/", "core/assets/", "./"}) {
      File file = Gdx.files.internal(origin + mapPath).file();
      if (file.exists()) {
        tiledMap = mapLoader.load(file.getAbsolutePath());
        break;
      }
    }

    if (tiledMap == null) {
      throw new RuntimeException("Error loading TileMap" + mapPath);
    }

    TiledMapRenderer renderer = mock(TiledMapRenderer.class);

    return new TerrainComponent(camera, tiledMap, renderer, TerrainComponent.TerrainOrientation.ORTHOGONAL, 0.5f);
  }
}
