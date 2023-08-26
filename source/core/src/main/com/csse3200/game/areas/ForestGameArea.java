package com.csse3200.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.areas.terrain.TerrainFactory.TerrainType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.buildables.WallType;
import com.csse3200.game.entities.factories.*;
import com.csse3200.game.files.UserSettings;
import com.csse3200.game.services.StructurePlacementService;
import com.csse3200.game.services.TerrainService;
import com.csse3200.game.utils.math.GridPoint2Utils;
import com.csse3200.game.utils.math.RandomUtils;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.components.gamearea.GameAreaDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/** Forest area for the demo game with trees, a player, and some enemies. */
public class ForestGameArea extends GameArea {
  private static final Logger logger = LoggerFactory.getLogger(ForestGameArea.class);
  private static final int NUM_TREES = 7;
  private static final int NUM_ENEMIES = 2;
  private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(10, 10);
  private static final float WALL_WIDTH = 0.1f;
  private static final float ASTEROID_SIZE = 0.9f;
  private static final float BOUNCE = 5.0f;
  private static final String[] forestTextures = {

    "images/elixir_collector.png", //TODO: Replace these images with copyright free images - these are just for testing purposes!!
    "images/broken_elixir_collector.png",
          "images/meteor.png",
    "images/box_boy_leaf.png",
    "images/tree.png",
    "images/wall.png",
    "images/wall2.png",
    "images/ghost_king.png",
    "images/ghost_1.png",
    "images/grass_1.png",
    "images/grass_2.png",
    "images/grass_3.png",
    "images/hex_grass_1.png",
    "images/hex_grass_2.png",
    "images/hex_grass_3.png",
    "images/iso_grass_1.png",
    "images/iso_grass_2.png",
    "images/iso_grass_3.png"
  };
  private static final String[] forestTextureAtlases = {
    "images/terrain_iso_grass.atlas", "images/ghost.atlas", "images/ghostKing.atlas"
  };
  private static final String[] forestSounds = {"sounds/Impact4.ogg"};
  private static final String backgroundMusic = "sounds/BGM_03_mp3.mp3";
  private static final String[] forestMusic = {backgroundMusic};

  private final TerrainFactory terrainFactory;
  private final ArrayList<Entity> targetables;

  /**
   * Initialise this ForestGameArea to use the provided TerrainFactory.
   * @param terrainFactory TerrainFactory used to create the terrain for the GameArea.
   * @requires terrainFactory != null
   */
  public ForestGameArea(TerrainFactory terrainFactory) {
    super();
    this.terrainFactory = terrainFactory;
    this.targetables = new ArrayList<>();
  }

  /** Create the game area, including terrain, static entities (trees), dynamic entities (player) */
  @Override
  public void create() {
    loadAssets();

    registerStructurePlacementService();

    displayUI();

    spawnTerrain();
    spawnTrees();
    spawnExtractors();
    var player = spawnPlayer();
    spawnEnemies();
    spawnBoss();
<<<<<<< HEAD
    spawnWalls();
    spawnAsteroids();

=======
    spawnWalls(player);
>>>>>>> feature/buildables

    playMusic();
  }

<<<<<<< HEAD
  private void spawnAsteroids() {
    //Extra Spicy Asteroids
    GridPoint2 posAs = new GridPoint2(8, 8);
    spawnEntityAt(
            ObstacleFactory.createAsteroid(ASTEROID_SIZE, ASTEROID_SIZE, BOUNCE), posAs, false, false);

  }
  private List<Entity> spawnWalls() {
=======
  private List<Entity> spawnWalls(Entity player) {
>>>>>>> feature/buildables
    List<Entity> walls = new ArrayList<Entity>();
    StructurePlacementService structurePlacementService = ServiceLocator.getStructurePlacementService();

    Entity wall = BuildablesFactory.createCustomWall(WallType.basic);
    Entity intermediateWall = BuildablesFactory.createCustomWall(WallType.intermediate);
    structurePlacementService.PlaceStructureAt(wall, new GridPoint2(10, 10), false, false);
    structurePlacementService.PlaceStructureAt(intermediateWall, new GridPoint2(20, 15), false, false);

    walls.add(wall);
    walls.add(intermediateWall);

    Entity gate = BuildablesFactory.createGate(WallType.basic, player);
    spawnEntityAt(gate, new GridPoint2(10, 15), false, false);

    return walls;
  }


  private void spawnExtractors() {
    GridPoint2 pos = new GridPoint2(terrain.getMapBounds(0).sub(2, 2).x/2, terrain.getMapBounds(0).sub(2, 2).y/2);
    Entity extractor = StructureFactory.createExtractor();
    extractor.setPosition(terrain.tileToWorldPosition(pos));
    spawnExtractor(extractor);
  }


  private void displayUI() {
    Entity ui = new Entity();
    ui.addComponent(new GameAreaDisplay("Box Forest"));
    spawnEntity(ui);
  }

  private void spawnTerrain() {
    // Background terrain
    terrain = terrainFactory.createTerrain(TerrainType.FOREST_DEMO);
    spawnEntity(new Entity().addComponent(terrain));

    // Terrain walls
    float tileSize = terrain.getTileSize();
    GridPoint2 tileBounds = terrain.getMapBounds(0);
    Vector2 worldBounds = new Vector2(tileBounds.x * tileSize, tileBounds.y * tileSize);

    // Left
    spawnEntityAt(
        ObstacleFactory.createWall(WALL_WIDTH, worldBounds.y), GridPoint2Utils.ZERO, false, false);
    // Right
    spawnEntityAt(
        ObstacleFactory.createWall(WALL_WIDTH, worldBounds.y),
        new GridPoint2(tileBounds.x, 0),
        false,
        false);
    // Top
    spawnEntityAt(
        ObstacleFactory.createWall(worldBounds.x, WALL_WIDTH),
        new GridPoint2(0, tileBounds.y),
        false,
        false);
    // Bottom
    spawnEntityAt(
        ObstacleFactory.createWall(worldBounds.x, WALL_WIDTH), GridPoint2Utils.ZERO, false, false);

<<<<<<< HEAD
=======
    ServiceLocator.registerTerrainService(new TerrainService(terrain));
>>>>>>> feature/buildables
  }

  private void spawnTrees() {
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    for (int i = 0; i < NUM_TREES; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity tree = ObstacleFactory.createTree();
      spawnEntityAt(tree, randomPos, true, false);
    }
  }

  private Entity spawnPlayer() {
    Entity newPlayer = PlayerFactory.createPlayer();
    spawnEntityAt(newPlayer, PLAYER_SPAWN, true, true);
    targetables.add(newPlayer);
    return newPlayer;
  }

  private void spawnEnemies() {
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    for (int i = 0; i < NUM_ENEMIES; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity ghost = EnemyFactory.createMeleeEnemy(targetables);
      spawnEntityAt(ghost, randomPos, true, true);
    }
  }

  private void spawnBoss() {
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
    Entity boss = EnemyFactory.createBoss(targetables);
    spawnEntityAt(boss, randomPos, true, true);
  }

  private void playMusic() {
    UserSettings.Settings settings = UserSettings.get();

    Music music = ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class);
    music.setLooping(true);
    music.setVolume(settings.musicVolume);
    music.play();
  }

  private void loadAssets() {
    logger.debug("Loading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(forestTextures);
    resourceService.loadTextureAtlases(forestTextureAtlases);
    resourceService.loadSounds(forestSounds);
    resourceService.loadMusic(forestMusic);

    while (!resourceService.loadForMillis(10)) {
      // This could be upgraded to a loading screen
      logger.info("Loading... {}%", resourceService.getProgress());
    }
  }

  private void unloadAssets() {
    logger.debug("Unloading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.unloadAssets(forestTextures);
    resourceService.unloadAssets(forestTextureAtlases);
    resourceService.unloadAssets(forestSounds);
    resourceService.unloadAssets(forestMusic);
  }

  @Override
  public void dispose() {
    super.dispose();
    ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class).stop();
    this.unloadAssets();
  }
}
