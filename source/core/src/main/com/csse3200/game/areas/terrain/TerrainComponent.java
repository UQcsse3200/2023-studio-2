package com.csse3200.game.areas.terrain;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.csse3200.game.rendering.RenderComponent;

/**
 * Render a tiled terrain for a given tiled map and orientation. A terrain is a map of tiles that
 * shows the 'ground' in the game. Enabling/disabling this component will show/hide the terrain.
 */
public class TerrainComponent extends RenderComponent {
  private static final int TERRAIN_LAYER = 0;

  private final TiledMap tiledMap;
  private final TiledMapRenderer tiledMapRenderer;
  private final OrthographicCamera camera;
  private final TerrainOrientation orientation;
  private final float tileSize;

  public TerrainComponent(
          OrthographicCamera camera,
          TiledMap map,
          TiledMapRenderer renderer,
          TerrainOrientation orientation,
          float tileSize) {
    this.camera = camera;
    this.tiledMap = map;
    this.orientation = orientation;
    this.tileSize = tileSize;
    this.tiledMapRenderer = renderer;
  }

  public Vector2 tileToWorldPosition(GridPoint2 tilePos) {
    return tileToWorldPosition(tilePos.x, tilePos.y);
  }

  public Vector2 tileToWorldPosition(int x, int y) {
    switch (orientation) {
      case HEXAGONAL:
        float hexLength = tileSize / 2;
        float yOffset = (x % 2 == 0) ? 0.5f * tileSize : 0f;
        return new Vector2(x * (tileSize + hexLength) / 2, y + yOffset);
      case ISOMETRIC:
        return new Vector2((x + y) * tileSize / 2, (y - x) * tileSize / 2);
      case ORTHOGONAL:
        return new Vector2(x * tileSize, y * tileSize);
      default:
        return null;
    }
  }

  public GridPoint2 worldPositionToTile(Vector2 vector) {
    return worldPositionToTile(vector.x, vector.y);
  }

  public GridPoint2 worldPositionToTile(float x, float y) {
    int gridX;
    int gridY;
    switch (orientation) {
      case HEXAGONAL:
        float hexLength = tileSize / 2;
        gridX = (int) (x * 2 / (tileSize + hexLength));
        float yOffset = (gridX % 2 == 0) ? 0.5f * tileSize : 0f;
        gridY = (int) (y - yOffset);
        return new GridPoint2(gridX, gridY);
      case ISOMETRIC:
        gridX = (int) (((x * 2 / tileSize) - (y * 2 / tileSize)) / 2);
        gridY = (int) ((y * 2 / tileSize) + gridX);
        return new GridPoint2(gridX, gridY);
      case ORTHOGONAL:
        return new GridPoint2((int) (x / tileSize), (int) (y / tileSize));
      default:
        return null;
    }
  }

  public float getTileSize() {
    return tileSize;
  }

  public GridPoint2 getMapBounds(int layer) {
    TiledMapTileLayer terrainLayer = (TiledMapTileLayer)tiledMap.getLayers().get(layer);
    return new GridPoint2(terrainLayer.getWidth(), terrainLayer.getHeight());
  }

  public boolean gridWithinBounds(int x, int y) {
    GridPoint2 block = new GridPoint2(x, y);
    return getMapBounds(TERRAIN_LAYER).x > block.x && getMapBounds(TERRAIN_LAYER).y > block.y && block.x >= 0 && block.y >= 0;
  }

  public TiledMap getMap() {
    return tiledMap;
  }

  @Override
  public void draw(SpriteBatch batch) {
    tiledMapRenderer.setView(camera);
    tiledMapRenderer.render();
  }

  @Override
  public void dispose() {
    tiledMap.dispose();
    super.dispose();
  }

  @Override
  public float getZIndex() {
    return 0f;
  }

  @Override
  public int getLayer() {
    return TERRAIN_LAYER;
  }

  public enum TerrainOrientation {
    ORTHOGONAL,
    ISOMETRIC,
    HEXAGONAL
  }

  public Vector3 unproject(Vector3 screenCoords) {
    return camera.unproject(screenCoords).scl(new Vector3(1/tileSize, 1/tileSize, 1/tileSize));
  }
}

