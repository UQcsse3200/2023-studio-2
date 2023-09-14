package com.csse3200.game.areas;
import com.badlogic.gdx.audio.Music;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.resources.Resource;
import com.csse3200.game.components.resources.ResourceDisplay;
import com.csse3200.game.concurrency.JobSystem;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.*;
import com.csse3200.game.entities.enemies.*;
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
import java.util.Optional;
import java.util.List;

import static com.csse3200.game.entities.factories.EnvironmentFactory.createEnvironment;


/** Planet Earth area for the demo game with trees, a player, and some enemies. */
public class LushGameArea extends GameArea {
    private static final Logger logger = LoggerFactory.getLogger(LushGameArea.class);
    //private DialogueBox dialogueBox;
    private static final int NUM_TREES = 7;
    private static final int NUM_MELEE_ENEMIES_PTE = 1;
    private static final int NUM_MELEE_ENEMIES_DTE = 1;
    private static final int NUM_RANGE_ENEMIES_PTE = 1;
    private static final int NUM_RANGE_ENEMIES_DTE = 1;
    private static final int NUM_POWERUPS = 3;
    private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(10, 40);
    private static final GridPoint2 SHIP_SPAWN = new GridPoint2(10, 10);
    private static final GridPoint2 PORTAL_ONE = new GridPoint2(30, 40);
    private static final GridPoint2 PORTAL_TWO = new GridPoint2(78, 10);
    private static final float WALL_WIDTH = 0.1f;
    private static final float ASTEROID_SIZE = 0.9f;
    private static final String mapPath = "map/map1.tmx";
    private static final String[] earthTextures = {
            "images/SpaceMiniGameBackground.png", // Used as a basic texture for repair minigame
            "images/extractor.png",
            "images/broken_extractor.png",
            "images/meteor.png", // https://axassets.itch.io/spaceship-simple-assets
            "images/box_boy_leaf.png",
            "images/RightShip.png",
            "images/ghost_king.png",
            "images/ghost_1.png",
            "images/base_enemy.png",
            "images/Troll.png",
            "images/rangeEnemy.png",
            "images/stone_wall.png",
            "images/healthpowerup.png", // Free to use - https://merchant-shade.itch.io/16x16-mixed-rpg-icons
            "images/speedpowerup.png", // Free to use - https://merchant-shade.itch.io/16x16-mixed-rpg-icons
            "images/Ship.png",
            "images/stone_wall.png",
            "images/oldman_down_1.png",
            "images/player_blank.png",
            "images/wrench.png",
            "images/durastell.png",
            "images/nebulite.png",
            "images/uparrow.png",
            "images/solsite.png",
            "images/resourcebar_background.png",
            "images/resourcebar_durasteel.png",
            "images/resourcebar_foreground.png",
            "images/resourcebar_nebulite.png",
            "images/resourcebar_solstite.png",
            "images/resourcebar_lights.png",
            "map/portal.png",
            "images/playerSS_6.png"
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
            "images/botanist.atlas",
            "images/playerSS.atlas",
            "images/wrench.atlas",
            "images/open_gate.atlas",
            "images/closed_gate.atlas",
            "images/botanist.atlas"

    };
    private static final String[] earthSounds = {"sounds/Impact4.ogg"};
    private static final String backgroundMusic = "sounds/theEND.mp3";
    private static final String[] earthMusic = {backgroundMusic};

    private final TerrainFactory terrainFactory;
    private final ArrayList<Entity> targetables;
    private final GdxGame game;

    /**
     * Initialise this EarthGameArea to use the provided TerrainFactory.
     * @param terrainFactory TerrainFactory used to create the terrain for the GameArea.
     * @requires terrainFactory != null
     */
    public LushGameArea(TerrainFactory terrainFactory, GdxGame game) {
        super();
        this.game = game;
        this.terrainFactory = terrainFactory;
        this.targetables = new ArrayList<>();
    }

    /** Create the game area, including terrain, static entities (trees), dynamic entities (player) */
    @Override
    public void create() {
        loadAssets();

        registerEntityPlacementService();
        registerStructurePlacementService();

        displayUI();

        spawnTerrain();
        spawnEnvironment();
        spawnPowerups();
        spawnExtractors();

        spawnShip();
        spawnPlayer(PLAYER_SPAWN);

        spawnEnemies();
        spawnSecretEnemies();
        spawnBoss();
        spawnSecretBoss();
        spawnAsteroids();
        spawnBotanist();

        playMusic();

        spawnPortal(PORTAL_ONE, 40, 5);
        spawnPortal(PORTAL_TWO, 16, 20);

    }

    /**
     * Spawns a portal that sends the player to a new location
     */
    private void spawnPortal(GridPoint2 position, float x, float y) {
        StructurePlacementService placementService = ServiceLocator.getStructurePlacementService();
        Entity portal = PortalFactory.createPortal(x, y, player);
        placementService.PlaceStructureAt(portal, position, false, false);
    }

    /**
     * Spawns objects defined on the collision layer with a custom sized collision box determined
     * based on the dimensions defined in the .tsx file
     */
    private void spawnEnvironment() {
        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) terrain.getMap().getLayers().get("Tree Base");
        createEnvironment(collisionLayer);
    }

    /**
     * Spawns a Botanist NPC entity at a predefined spawn position on the terrain.
     * The Botanist entity is created using the NPCFactory.createBotanist() method.
     * The entity is then added to the world and positioned at the specified spawn position.
     *
     * @see NPCFactory#createBotanist() Method used to create the Botanist NPC entity.
     */
    private void spawnBotanist() {
        // Calculate the spawn position based on terrain bounds
        GridPoint2 spawnPosition = new GridPoint2(terrain.getMapBounds(0).sub(15, 2).x / 2,
                terrain.getMapBounds(0).sub(2, 2).y / 3);

        // Create the Botanist NPC entity
        Entity botanist = NPCFactory.createBotanist();

        // Spawn the entity at the calculated position
        // Arguments: entity, position, isCentered, isLocal
        spawnEntityAt(botanist, spawnPosition, true, false);
    }

    /**
     * Spawns a movable asteroid entity which the player can push
     */
    private void spawnAsteroids() {
        //Extra Spicy Asteroids
        GridPoint2 posAs = new GridPoint2(8, 8);
        spawnEntityAt(
                ObstacleFactory.createAsteroid(ASTEROID_SIZE, ASTEROID_SIZE), posAs, false, false);

    }

    /**
     * Spawns three extractors next to each other in the world and adds them to the list of
     * targetable entities
     */
    private void spawnExtractors() {
        GridPoint2 pos = new GridPoint2(terrain.getMapBounds(0).sub(0, 2).x/2, terrain.getMapBounds(0).sub(2, 2).y/2);
        Entity extractor = StructureFactory.createExtractor(30, Resource.Nebulite, (long) 100.0, 1);
        spawnEntityAt(extractor, pos, true, false);
        targetables.add(extractor);

        pos = new GridPoint2(terrain.getMapBounds(0).sub(8, 2).x/2, terrain.getMapBounds(0).sub(2, 2).y/2);
        extractor = StructureFactory.createExtractor(30, Resource.Solstite, (long) 100.0, 1);
        targetables.add(extractor);
        spawnEntityAt(extractor, pos, true, false);

        pos = new GridPoint2(terrain.getMapBounds(0).sub(16, 2).x/2, terrain.getMapBounds(0).sub(2, 2).y/2);
        extractor = StructureFactory.createExtractor(30, Resource.Durasteel, (long) 100.0, 1);
        targetables.add(extractor);
        spawnEntityAt(extractor, pos, true, false);

        ResourceDisplay resourceDisplayComponent = new ResourceDisplay()
                .withResource(Resource.Durasteel)
                .withResource(Resource.Solstite)
                .withResource(Resource.Nebulite);
        Entity resourceDisplay = new Entity().addComponent(resourceDisplayComponent);
        spawnEntity(resourceDisplay);
    }

    /**
     * Spawns the player's ship, which will act as the exit point of the map
     */
    private void spawnShip() {
        GridPoint2 spawnPosition = new GridPoint2(7*terrain.getMapBounds(0).sub(1, 1).x/12,
                2*terrain.getMapBounds(0).sub(1, 1).y/3);
        Entity ship = StructureFactory.createShip(game);
        spawnEntityAt(ship, spawnPosition, false, false);
    }

    /**
     * Display the UI elements for the current level
     */
    private void displayUI() {
        Entity ui = new Entity();
        ui.addComponent(new GameAreaDisplay("An Alien Jungle"));
        spawnEntity(ui);
    }

    /**
     * Creates the background graphics and invisible walls for the edges of the map
     */
    private void spawnTerrain() {
        // Background terrain
        terrain = terrainFactory.createTerrain(mapPath);
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

    /**
     * Spawns a set number of health and speed powerups at random locations
     */
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

    /**
     * Spawns enemy entities randomly throughout the map
     */
    private void spawnEnemies() {
        GridPoint2 minPos = new GridPoint2(0, 0);
        GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);


        for (int i = 0; i < NUM_MELEE_ENEMIES_PTE; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity melee = EnemyFactory.createEnemy(targetables, EnemyType.Melee, EnemyBehaviour.DTE);
            spawnEntityAt(melee, randomPos, true, true);
            //melee.addComponent(new DialogComponent(dialogueBox));
        }

        for (int i = 0; i < NUM_MELEE_ENEMIES_DTE; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity melee = EnemyFactory.createEnemy(targetables, EnemyType.Melee, EnemyBehaviour.DTE);
            spawnEntityAt(melee, randomPos, true, true);
            //melee.addComponent(new DialogComponent(dialogueBox));
        }

        for (int i = 0; i < NUM_RANGE_ENEMIES_PTE; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity melee = EnemyFactory.createEnemy(targetables, EnemyType.Ranged, EnemyBehaviour.DTE);
            spawnEntityAt(melee, randomPos, true, true);
            //melee.addComponent(new DialogComponent(dialogueBox));
        }

        for (int i = 0; i < NUM_RANGE_ENEMIES_DTE; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity ranged = EnemyFactory.createEnemy(targetables, EnemyType.Ranged, EnemyBehaviour.PTE);
            spawnEntityAt(ranged, randomPos, true, true);
            //ranged.addComponent(new DialogComponent(dialogueBox));
        }
    }

    private void spawnSecretEnemies() {
        GridPoint2 minPos = new GridPoint2(71, 1);
        GridPoint2 maxPos = new GridPoint2(89, 19);


        for (int i = 0; i < NUM_MELEE_ENEMIES_PTE; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity melee = EnemyFactory.createEnemy(targetables, EnemyType.Melee, EnemyBehaviour.DTE);
            spawnEntityAt(melee, randomPos, true, true);
            //melee.addComponent(new DialogComponent(dialogueBox));
        }

        for (int i = 0; i < NUM_MELEE_ENEMIES_DTE; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity melee = EnemyFactory.createEnemy(targetables, EnemyType.Melee, EnemyBehaviour.DTE);
            spawnEntityAt(melee, randomPos, true, true);
            //melee.addComponent(new DialogComponent(dialogueBox));
        }

        for (int i = 0; i < NUM_RANGE_ENEMIES_PTE; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity melee = EnemyFactory.createEnemy(targetables, EnemyType.Ranged, EnemyBehaviour.DTE);
            spawnEntityAt(melee, randomPos, true, true);
            //melee.addComponent(new DialogComponent(dialogueBox));
        }

        for (int i = 0; i < NUM_RANGE_ENEMIES_DTE; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity ranged = EnemyFactory.createEnemy(targetables, EnemyType.Ranged, EnemyBehaviour.PTE);
            spawnEntityAt(ranged, randomPos, true, true);
            //ranged.addComponent(new DialogComponent(dialogueBox));
        }
    }

    private void spawnSecretBoss() {
        GridPoint2 minPos = new GridPoint2(71, 1);
        GridPoint2 maxPos = new GridPoint2(89, 19);

        GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
        Entity boss = EnemyFactory.createEnemy(targetables, EnemyType.BossMelee, EnemyBehaviour.PTE);
        spawnEntityAt(boss, randomPos, true, true);
    }

    /**
     * Spawns a single boss enemy at a random location
     */
    private void spawnBoss() {
        GridPoint2 minPos = new GridPoint2(0, 0);
        GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

        GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
        Entity boss = EnemyFactory.createEnemy(targetables, EnemyType.BossMelee, EnemyBehaviour.PTE);
        spawnEntityAt(boss, randomPos, true, true);
        //boss.addComponent(new DialogComponent(dialogueBox));

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
        JobSystem.launchBlocking(() -> this.readMusic(earthMusic));
        resourceService.loadTextures(earthTextures);
        resourceService.loadTextureAtlases(earthTextureAtlases);
        resourceService.loadSounds(earthSounds);

        while (!resourceService.loadForMillis(10)) {
            // This could be upgraded to a loading screen
            logger.info("Loading... {}%", resourceService.getProgress());
        }
    }

    /**
     * Loads music files which are typically very large in a parallel thread
     * @param files an array of music files
     * @return array of loaded music
     */
    private Music[] readMusic(String[] files) {
        ResourceService resourceService = ServiceLocator.getResourceService();
        Music[] music = new Music[files.length];
        for (int i = 0; i < files.length; i++) {
            resourceService.loadMusic(new String[]{files[i]});
            music[i] = resourceService.getAsset(files[i], Music.class);
        }

        resourceService.loadAll();
        return music;
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

    public static Entity getPlayer() {
        return player;
    }
}