package com.csse3200.game.areas.terrain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.TerrainComponent.TerrainOrientation;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
class TerrainComponentTest {
  @Test
  void shouldConvertPositionOrthogonal() {
    TerrainComponent component = makeComponent(TerrainOrientation.ORTHOGONAL, 3f);
    assertEquals(new Vector2(0f, 0f), component.tileToWorldPosition(0, 0));
    assertEquals(new Vector2(6f, 12f), component.tileToWorldPosition(2, 4));
    assertEquals(new Vector2(-15f, -9f), component.tileToWorldPosition(-5, -3));
  }

  @Test
  void shouldConvertPositionIsometric() {
    TerrainComponent component = makeComponent(TerrainOrientation.ISOMETRIC, 3f);
    assertEquals(new Vector2(0f, 0f), component.tileToWorldPosition(0, 0));
    assertEquals(new Vector2(9f, 3f), component.tileToWorldPosition(2, 4));
    assertEquals(new Vector2(-12f, 3f), component.tileToWorldPosition(-5, -3));
  }

  @Test
  void shouldConvertPositionHexagonal() {
    TerrainComponent component = makeComponent(TerrainOrientation.HEXAGONAL, 3f);
  }

  private static TerrainComponent makeComponent(TerrainOrientation orientation, float tileSize) {
    OrthographicCamera camera = mock(OrthographicCamera.class);
    TiledMap map = mock(TiledMap.class);
    TiledMapRenderer mapRenderer = mock(TiledMapRenderer.class);
    return new TerrainComponent(camera, map, mapRenderer, orientation, tileSize);
  }
}
