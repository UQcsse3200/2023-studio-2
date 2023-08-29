package com.csse3200.game.areas.terrain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.areas.terrain.TerrainComponent.TerrainOrientation;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.utils.math.RandomUtils;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

/** Factory for creating game terrains. */
public class TerrainFactory {
  private static final GridPoint2 MAP_SIZE = new GridPoint2(30, 30);
  private static final int TUFT_TILE_COUNT = 30;
  private static final int ROCK_TILE_COUNT = 30;

  private final OrthographicCamera camera;
  private final TerrainOrientation orientation;

  /**
   * Create a terrain factory with Orthogonal orientation
   *
   * @param cameraComponent Camera to render terrains to. Must be ortographic.
   */
  public TerrainFactory(CameraComponent cameraComponent) {
    this(cameraComponent, TerrainOrientation.ORTHOGONAL);
  }

  /**
   * Create a terrain factory
   *
   * @param cameraComponent Camera to render terrains to. Must be orthographic.
   * @param orientation orientation to render terrain at
   */
  public TerrainFactory(CameraComponent cameraComponent, TerrainOrientation orientation) {
    this.camera = (OrthographicCamera) cameraComponent.getCamera();
    this.orientation = orientation;
  }

  /**
   * Create a terrain of the given type, using the orientation of the factory. This can be extended
   * to add additional game terrains.
   *
   * @return Terrain component which renders the terrain
   */
  public TerrainComponent createTerrain() {
    ResourceService resourceService = ServiceLocator.getResourceService();
    TmxMapLoader mapLoader = new TmxMapLoader();
    TiledMap tiledMap = mapLoader.load(Gdx.files.internal("source/core/assets/map/base.tmx").file().getAbsolutePath());
    TiledMapRenderer renderer = createRenderer(tiledMap, 0.5f / 16);

    return new TerrainComponent(camera, tiledMap, renderer, orientation, 0.5f);
  }

  public TerrainComponent createSpaceTerrain(TerrainType terrainType) {
    ResourceService resourceService = ServiceLocator.getResourceService();
    switch (terrainType) {
      case SPACE_DEMO:
        TextureRegion spaceVoid =
                new TextureRegion(resourceService.getAsset("images/SpaceMiniGameBackground.png", Texture.class));
        return createSpaceDemoTerrain(1f,spaceVoid);
      case REPAIR_DEMO:
        TextureRegion extractorRepair =
                new TextureRegion(resourceService.getAsset("images/ExtractorMiniGameBackground.png", Texture.class));
        return createExtractorDemoTerrain(1f, extractorRepair);
      default:
        return null;
    }
  }

  private TerrainComponent createForestDemoTerrain(
      float tileWorldSize, TextureRegion grass, TextureRegion grassTuft, TextureRegion rocks) {
    GridPoint2 tilePixelSize = new GridPoint2(grass.getRegionWidth(), grass.getRegionHeight());
    TiledMap tiledMap = createForestDemoTiles(tilePixelSize, grass, grassTuft, rocks);
    TiledMapRenderer renderer = createRenderer(tiledMap, tileWorldSize / tilePixelSize.x);
    return new TerrainComponent(camera, tiledMap, renderer, orientation, tileWorldSize);
  }

  private TerrainComponent createSpaceDemoTerrain(
          float tileWorldSize, TextureRegion spaceVoid) {
    GridPoint2 tilePixelSize = new GridPoint2(spaceVoid.getRegionWidth(),spaceVoid.getRegionHeight());
    TiledMap tiledMap = createSpaceDemoTiles(tilePixelSize,spaceVoid);
    TiledMapRenderer renderer= createRenderer(tiledMap,tileWorldSize/tilePixelSize.x);
    return new TerrainComponent(camera, tiledMap, renderer, orientation, tileWorldSize);
  }

  private TerrainComponent createExtractorDemoTerrain(
          float tileWorldSize, TextureRegion extractorRepair) {
    GridPoint2 tilePixelSize = new GridPoint2(extractorRepair.getRegionWidth(),extractorRepair.getRegionHeight());
    TiledMap tiledMap = createExtractorDemoTiles(tilePixelSize,extractorRepair);
    TiledMapRenderer renderer= createRenderer(tiledMap,tileWorldSize/tilePixelSize.x * 20);
    return new TerrainComponent(camera, tiledMap, renderer, orientation, tileWorldSize);
  }


  private TiledMapRenderer createRenderer(TiledMap tiledMap, float tileScale) {
    switch (orientation) {
      case ORTHOGONAL:
        return new OrthogonalTiledMapRenderer(tiledMap, tileScale);
      case ISOMETRIC:
        return new IsometricTiledMapRenderer(tiledMap, tileScale);
      case HEXAGONAL:
        return new HexagonalTiledMapRenderer(tiledMap, tileScale);
      default:
        return null;
    }
  }

  private TiledMap createForestDemoTiles(
      GridPoint2 tileSize, TextureRegion grass, TextureRegion grassTuft, TextureRegion rocks) {
    TiledMap tiledMap = new TiledMap();
    TerrainTile grassTile = new TerrainTile(grass);
    TerrainTile grassTuftTile = new TerrainTile(grassTuft);
    TerrainTile rockTile = new TerrainTile(rocks);
    TiledMapTileLayer layer = new TiledMapTileLayer(MAP_SIZE.x, MAP_SIZE.y, tileSize.x, tileSize.y);

    // Create base grass
    fillTiles(layer, MAP_SIZE, grassTile);

    // Add some grass and rocks
    fillTilesAtRandom(layer, MAP_SIZE, grassTuftTile, TUFT_TILE_COUNT);
    fillTilesAtRandom(layer, MAP_SIZE, rockTile, ROCK_TILE_COUNT);

    tiledMap.getLayers().add(layer);
    return tiledMap;
  }

  private TiledMap createSpaceDemoTiles(
          GridPoint2 tileSize, TextureRegion spaceVoid) {
    TiledMap tiledMap = new TiledMap();
    TerrainTile spaceTile = new TerrainTile(spaceVoid);
    TiledMapTileLayer layer = new TiledMapTileLayer(MAP_SIZE.x, MAP_SIZE.y, tileSize.x, tileSize.y);

    fillTiles(layer,MAP_SIZE,spaceTile);
    tiledMap.getLayers().add(layer);
    return tiledMap;

  }

  private TiledMap createExtractorDemoTiles(
          GridPoint2 tileSize, TextureRegion extractorRepair) {
    TiledMap tiledMap = new TiledMap();
    TerrainTile spaceTile = new TerrainTile(extractorRepair);
    TiledMapTileLayer layer = new TiledMapTileLayer(MAP_SIZE.x, MAP_SIZE.y, tileSize.x, tileSize.y);

    fillTiles(layer,MAP_SIZE,spaceTile);
    tiledMap.getLayers().add(layer);
    return tiledMap;

  }


  private static void fillTilesAtRandom(
      TiledMapTileLayer layer, GridPoint2 mapSize, TerrainTile tile, int amount) {
    GridPoint2 min = new GridPoint2(0, 0);
    GridPoint2 max = new GridPoint2(mapSize.x - 1, mapSize.y - 1);

    for (int i = 0; i < amount; i++) {
      GridPoint2 tilePos = RandomUtils.random(min, max);
      Cell cell = layer.getCell(tilePos.x, tilePos.y);
      cell.setTile(tile);
    }
  }

  private static void fillTiles(TiledMapTileLayer layer, GridPoint2 mapSize, TerrainTile tile) {
    for (int x = 0; x < mapSize.x; x++) {
      for (int y = 0; y < mapSize.y; y++) {
        Cell cell = new Cell();
        cell.setTile(tile);
        layer.setCell(x, y, cell);
      }
    }
  }

  /**
   * This enum should contain the different terrains in your game, e.g. forest, cave, home, all with
   * the same oerientation. But for demonstration purposes, the base code has the same level in 3
   * different orientations.
   */
  public enum TerrainType {
    FOREST_DEMO,
    FOREST_DEMO_ISO,
    FOREST_DEMO_HEX,
    SPACE_DEMO,
    REPAIR_DEMO
  }
}
