package com.csse3200.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.resources.Resource;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ObstacleFactory;
import com.csse3200.game.entities.factories.PlayerFactory;
import com.csse3200.game.entities.factories.PowerupFactory;
import com.csse3200.game.entities.factories.*;
import com.csse3200.game.entities.enemies.*;
import com.csse3200.game.files.UserSettings;
import com.csse3200.game.services.TerrainService;
import com.csse3200.game.utils.math.GridPoint2Utils;
import com.csse3200.game.utils.math.RandomUtils;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.components.gamearea.GameAreaDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/** Planet Earth area for the demo game with trees, a player, and some enemies. */
public class EarthGameArea extends GameArea {
    private static final Logger logger = LoggerFactory.getLogger(EarthGameArea.class);
    private static final int NUM_TREES = 7;
    private static final int NUM_MELEE_ENEMIES_PTE = 1;
    private static final int NUM_MELEE_ENEMIES_DTE = 1;
    private static final int NUM_RANGE_ENEMIES_PTE = 1;
    private static final int NUM_RANGE_ENEMIES_DTE = 1;
    private static final int NUM_POWERUPS = 3;
    private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(10, 10);
    private static final GridPoint2 SHIP_SPAWN = new GridPoint2(10, 10);
    private static final float WALL_WIDTH = 0.1f;
    private static final float ASTEROID_SIZE = 0.9f;
    private static final String[] earthTextures = {

            "images/elixir_collector.png", //TODO: Replace these images with copyright free images - these are just for testing purposes!!
            "images/broken_elixir_collector.png",
            "images/meteor.png", // https://axassets.itch.io/spaceship-simple-assets
            "images/box_boy_leaf.png",
            "images/RightShip.png",
            "images/wall.png",
            "images/wall2.png",
            "images/gate_close.png",
            "images/gate_open.png",
            "images/ghost_king.png",
            "images/ghost_1.png",
            "images/base_enemy.png",
            "images/Troll.png",
            "images/rangeEnemy.png",
            "images/stone_wall.png",
            "images/healthpowerup.png", // Free to use - https://merchant-shade.itch.io/16x16-mixed-rpg-icons
            "images/speedpowerup.png", // Free to use - https://merchant-shade.itch.io/16x16-mixed-rpg-icons
            "images/Ship.png",
            "images/stone_wall.png"
    };
    private static final String[] earthTextureAtlases = {
            "images/terrain_iso_grass.atlas",
            "images/ghost.atlas",
            "images/ghostKing.atlas",
            "images/base_enemy.atlas",
            "images/troll_enemy.atlas",
            "images/rangeEnemy.atlas",
            "images/stone_wall.atlas",
            "images/dirt_wall.atlas",
            "images/gate.atlas"
    };
    private static final String[] earthSounds = {"sounds/Impact4.ogg"};
    private static final String backgroundMusic = "sounds/theEND.mp3";
    private static final String[] earthMusic = {backgroundMusic};

    private final TerrainFactory terrainFactory;
    private final ArrayList<Entity> targetables;
    private Entity player;

    /**
     * Initialise this EarthGameArea to use the provided TerrainFactory.
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

        registerStructurePlacementService();

        displayUI();

        spawnTerrain();
        spawnEnvironment();
        spawnPowerups();
        spawnExtractors();

        spawnShip();
        spawnPlayer();

        spawnEnemies();
        spawnBoss();
        spawnAsteroids();

        playMusic();
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
    private void spawnAsteroids() {
        //Extra Spicy Asteroids
        GridPoint2 posAs = new GridPoint2(8, 8);
        spawnEntityAt(
                ObstacleFactory.createAsteroid(ASTEROID_SIZE, ASTEROID_SIZE), posAs, false, false);

    }

    private void spawnExtractors() {
        GridPoint2 pos = new GridPoint2(terrain.getMapBounds(0).sub(2, 2).x/2, terrain.getMapBounds(0).sub(2, 2).y/2);
        Entity extractor = StructureFactory.createExtractor(30, Resource.Unobtanium, (long) 1.0, 1);
        spawnEntityAt(extractor, pos, true, false);
    }

    private void spawnShip() {
        GridPoint2 spawnPosition = new GridPoint2(terrain.getMapBounds(0).sub(1, 1).x/2,
                terrain.getMapBounds(0).sub(1, 1).y/3);
        Entity ship = StructureFactory.createShip();
        spawnEntityAt(ship, spawnPosition, false, false);
    }

    private void displayUI() {
        Entity ui = new Entity();
        ui.addComponent(new GameAreaDisplay("Planet Earth"));
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
        ServiceLocator.registerTerrainService(new TerrainService(terrain));
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
        player = newPlayer;
    }

    private void spawnPowerups() {
        GridPoint2 minPos = new GridPoint2(0, 0);
        GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

        for (int i = 0; i < NUM_POWERUPS; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            GridPoint2 randomPos2 = RandomUtils.random(minPos, maxPos);

            Entity healthPowerup = PowerupFactory.createHealthPowerup();
            Entity speedPowerup = PowerupFactory.createSpeedPowerup();

            spawnEntityAt(healthPowerup, randomPos, true, false);
            spawnEntityAt(speedPowerup, randomPos2, true, false);

            // Test
            // System.out.println(ServiceLocator.getEntityService().getEntitiesByComponent(PowerupComponent.class).toString());
        }
    }

    private void spawnEnemies() {
        GridPoint2 minPos = new GridPoint2(0, 0);
        GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

        for (int i = 0; i < NUM_MELEE_ENEMIES_PTE; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity melee = EnemyFactory.createEnemy(targetables, EnemyType.Melee, EnemyBehaviour.DTE);
            spawnEntityAt(melee, randomPos, true, true);
        }

        for (int i = 0; i < NUM_MELEE_ENEMIES_DTE; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity melee = EnemyFactory.createEnemy(targetables, EnemyType.Melee, EnemyBehaviour.DTE);
            spawnEntityAt(melee, randomPos, true, true);
        }

        for (int i = 0; i < NUM_RANGE_ENEMIES_PTE; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity melee = EnemyFactory.createEnemy(targetables, EnemyType.Ranged, EnemyBehaviour.DTE);
            spawnEntityAt(melee, randomPos, true, true);
        }

        for (int i = 0; i < NUM_RANGE_ENEMIES_DTE; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity ranged = EnemyFactory.createEnemy(targetables, EnemyType.Ranged, EnemyBehaviour.PTE);
            spawnEntityAt(ranged, randomPos, true, true);
        }
    }

    private void spawnBoss() {
        GridPoint2 minPos = new GridPoint2(0, 0);
        GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

        GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
        Entity boss = EnemyFactory.createBoss(targetables, EnemyType.BossMelee, EnemyBehaviour.PTE);
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
        resourceService.loadTextures(earthTextures);
        resourceService.loadTextureAtlases(earthTextureAtlases);
        resourceService.loadSounds(earthSounds);
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

    public Entity getPlayer() {
        return player;
  }
}