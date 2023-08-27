package com.csse3200.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.areas.terrain.TerrainFactory.TerrainType;
import com.csse3200.game.components.resources.Resource;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.buildables.WallType;
import com.csse3200.game.entities.enemies.BossBehaviour;
import com.csse3200.game.entities.enemies.BossType;
import com.csse3200.game.entities.enemies.EnemyBehaviour;
import com.csse3200.game.entities.enemies.EnemyType;
import com.csse3200.game.entities.factories.EnemyFactory;
import com.csse3200.game.entities.factories.ObstacleFactory;
import com.csse3200.game.entities.factories.PlayerFactory;
import com.csse3200.game.entities.factories.StructureFactory;
import com.csse3200.game.files.UserSettings;
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
  private static final int NUM_MELEE_ENEMIES = 3;
  private static final int NUM_RANGE_ENEMIES = 3;
  private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(10, 10);
  private static final float WALL_WIDTH = 0.1f;
  private static final String[] forestTextures = {
    "images/elixir_collector.png", //TODO: Replace these images with copyright free images - these are just for testing purposes!!
    "images/broken_elixir_collector.png",
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
    "images/iso_grass_3.png",
      "images/base_enemy.png",
      "images/Troll.png"

  };
  private static final String[] forestTextureAtlases = {
    "images/terrain_iso_grass.atlas", "images/ghost.atlas", "images/ghostKing.atlas","images/base_enemy.atlas","images/troll_enemy.atlas"};
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

    displayUI();

    spawnTerrain();
    spawnTrees();
    spawnExtractors();
    spawnPlayer();
    spawnWalls();
    spawnEnemies();
    spawnBoss();

    playMusic();
  }

  private List<Entity> spawnWalls() {
    List<Entity> walls = new ArrayList<Entity>();

    Entity wall = ObstacleFactory.createCustomWall(WallType.basic);
    Entity intermediateWall = ObstacleFactory.createCustomWall(WallType.intermediate);
    spawnEntityAt(wall, new GridPoint2(10, 10), false, false);
    spawnEntityAt(intermediateWall, new GridPoint2(15, 15), false, false);

    walls.add(wall);
    walls.add(intermediateWall);

    return walls;
  }


  private void spawnExtractors() {
    GridPoint2 pos = new GridPoint2(terrain.getMapBounds(0).sub(2, 2).x/2, terrain.getMapBounds(0).sub(2, 2).y/2);
    Entity extractor = StructureFactory.createExtractor(30, Resource.Unobtanium, (long) 1.0, 1);
    spawnEntityAt(extractor, pos, true, false);
    targetables.add(extractor);
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

  private void spawnPlayer() {
    Entity newPlayer = PlayerFactory.createPlayer();
    spawnEntityAt(newPlayer, PLAYER_SPAWN, true, true);
    targetables.add(newPlayer);
  }

  private void spawnEnemies() {
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    for (int i = 0; i < NUM_MELEE_ENEMIES; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity melee = EnemyFactory.createEnemy(targetables, EnemyType.Melee, EnemyBehaviour.DTB);
      spawnEntityAt(melee, randomPos, true, true);
    }

    for (int i = 0; i < NUM_RANGE_ENEMIES; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity ranged = EnemyFactory.createEnemy(targetables, EnemyType.Ranged, EnemyBehaviour.PTB);
      spawnEntityAt(ranged, randomPos, true, true);
    }
  }

  private void spawnBoss() {
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
    Entity boss = EnemyFactory.createBoss(targetables, BossType.Melee, BossBehaviour.PTB);
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
