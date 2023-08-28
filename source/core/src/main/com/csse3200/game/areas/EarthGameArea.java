package com.csse3200.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.buildables.WallType;
import com.csse3200.game.entities.factories.*;
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
public class EarthGameArea extends GameArea {
    private static final Logger logger = LoggerFactory.getLogger(EarthGameArea.class);
    private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(15, 15);

    private static final int NUM_ENEMIES = 2;
    private static final float WALL_WIDTH = 0.1f;
    private static final float ASTEROID_SIZE = 0.9f;
    private static final float BOUNCE = 5.0f;

    private static final String[] earthSounds = {"sounds/Impact4.ogg"};

    private static final String[] earthTextures = {

            "images/elixir_collector.png", //TODO: Replace these images with copyright free images - these are just for testing purposes!!
            "images/broken_elixir_collector.png",
            "images/meteor.png",
            "images/box_boy_leaf.png",
            "images/wall.png",
            "images/wall2.png",
            "images/ghost_king.png",
    };
    private static final String[] earthTextureAtlases = {
            "images/ghost.atlas", "images/ghostKing.atlas"
    };
    private static final String backgroundMusic = "sounds/BGM_03_mp3.mp3";
    private static final String[] earthMusic = {backgroundMusic};

    private final TerrainFactory terrainFactory;
    private final ArrayList<Entity> targetables;

    /**
     * Initialise this ForestGameArea to use the provided TerrainFactory.
     * @param terrainFactory TerrainFactory used to create the terrain for the GameArea.
     * @requires terrainFactory != null
     */
    public EarthGameArea(TerrainFactory terrainFactory) {
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
        spawnEnvironment();
        spawnExtractors();
        spawnPlayer();
        spawnEnemies();
        spawnBoss();
        spawnWalls();
        spawnAsteroids();


        playMusic();
    }

    private void spawnAsteroids() {
        //Extra Spicy Asteroids
        GridPoint2 posAs = new GridPoint2(8, 8);
        spawnEntityAt(
                ObstacleFactory.createAsteroid(ASTEROID_SIZE, ASTEROID_SIZE, BOUNCE), posAs, false, false);

    }
    private void spawnEnvironment() {
        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) terrain.getMap().getLayers().get("Tree Base");

        for (int y = 0; y < collisionLayer.getHeight(); y++) {
            for (int x = 0; x < collisionLayer.getWidth(); x++) {
                TiledMapTileLayer.Cell cell = collisionLayer.getCell(x, collisionLayer.getHeight() - 1 - y);
                if (cell != null) {
                    GridPoint2 tilePosition = new GridPoint2(x, collisionLayer.getHeight() - 1 - y);
                    Entity environment = ObstacleFactory.createEnvironment();
                    spawnEntityAt(environment, tilePosition, false, false);
                }
            }
        }
    }
    private List<Entity> spawnWalls() {
        List<Entity> walls = new ArrayList<Entity>();

        Entity wall = BuildablesFactory.createCustomWall(WallType.basic);
        Entity intermediateWall = BuildablesFactory.createCustomWall(WallType.intermediate);
        spawnEntityAt(wall, new GridPoint2(10, 10), false, false);
        spawnEntityAt(intermediateWall, new GridPoint2(15, 15), false, false);

        walls.add(wall);
        walls.add(intermediateWall);

        return walls;
    }


    private void spawnExtractors() {
        GridPoint2 pos = new GridPoint2(terrain.getMapBounds(0).sub(2, 2).x/2, terrain.getMapBounds(0).sub(2, 2).y/2);
        Entity extractor = StructureFactory.createExtractor();
        spawnEntityAt(extractor, pos, true, false);
    }


    private void displayUI() {
        Entity ui = new Entity();
        ui.addComponent(new GameAreaDisplay("Box Forest"));
        spawnEntity(ui);
    }

    private void spawnTerrain() {
        // Background terrain
        terrain = terrainFactory.createTerrain();
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


    private void spawnPlayer() {
        Entity newPlayer = PlayerFactory.createPlayer();
        spawnEntityAt(newPlayer, PLAYER_SPAWN, true, true);
        targetables.add(newPlayer);
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
        resourceService.loadSounds(earthSounds);
        resourceService.loadTextures(earthTextures);
        resourceService.loadTextureAtlases(earthTextureAtlases);

        resourceService.loadMusic(earthMusic);

        while (!resourceService.loadForMillis(10)) {
            // This could be upgraded to a loading screen
            logger.info("Loading... {}%", resourceService.getProgress());
        }
    }

    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(earthTextures);
        resourceService.unloadAssets(earthTextureAtlases);
        resourceService.unloadAssets(earthSounds);
        resourceService.unloadAssets(earthMusic);
    }

    @Override
    public void dispose() {
        super.dispose();
        ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class).stop();
        this.unloadAssets();
    }
}