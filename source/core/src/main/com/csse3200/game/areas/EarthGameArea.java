package com.csse3200.game.areas;

import com.badlogic.gdx.Gdx;
import java.util.List;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.resources.Resource;
import com.csse3200.game.entities.buildables.TurretType;
import com.csse3200.game.entities.factories.CompanionFactory;
import com.csse3200.game.components.resources.ResourceDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ObstacleFactory;
import com.csse3200.game.entities.factories.PlayerFactory;
import com.csse3200.game.entities.factories.PowerupFactory;
import com.csse3200.game.entities.factories.*;
import com.csse3200.game.entities.enemies.*;
import com.csse3200.game.files.UserSettings;
import com.csse3200.game.physics.components.PhysicsComponent;
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
    //private DialogueBox dialogueBox;
    private static List<Entity> itemsOnMap = new ArrayList<>();
    private static final int NUM_TREES = 7;
    private static final int NUM_MELEE_PTE = 2;
    private static final int NUM_MELEE_DTE = 2;
    private static final int NUM_RANGE_PTE = 2;
    private static final int NUM_POWERUPS = 3;
    private static final int NUM_Laboratory = 4;
    private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(10, 10);
    private static final GridPoint2 COMPANION_SPAWN = new GridPoint2(8, 8);
    /*private static final GridPoint2 BOX_SPAWN = new GridPoint2(10, 10);*/
    private static final GridPoint2 SHIP_SPAWN = new GridPoint2(10, 10);
    private static final float WALL_WIDTH = 0.1f;
    private static final float ASTEROID_SIZE = 0.9f;
    private static final String[] earthTextures = {
            "images/SpaceMiniGameBackground.png", // Used as a basic texture for repair minigame
            "images/refinedExtractor2.png",
            "images/refinedBrokenExtractor.png",
            "images/meteor.png", // https://axassets.itch.io/spaceship-simple-assets
            "images/box_boy_leaf.png",
            "images/RightShip.png",
            "images/wall.png",
//            "images/companionSS_0.png",
//            "images/companionSS_1.png",
//            "images/companionSS_2.png",
//            "images/companionSS.png",
            "images/wall2.png",
            "images/gate_close.png",
            "images/gate_open.png",
            "images/ghost_king.png",
            "images/companion_DOWN.png",
            "images/ghost_1.png",
            "images/base_enemy.png",
            "images/Troll.png",
            "images/rangeEnemy.png",
            "images/stone_wall.png",
            "images/healthpowerup.png", // Free to use - https://merchant-shade.itch.io/16x16-mixed-rpg-icons
            "images/speedpowerup.png", // Free to use - https://merchant-shade.itch.io/16x16-mixed-rpg-icons
            "images/refinedShip.png",
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
            "images/TurretOne.png",
            "images/TurretTwo.png",
            "images/playerSS_6.png",
            "images/laboratory.png",
            "images/Potion.png",
            "images/upgradetree/exit.png",
            "images/upgradetree/background.png",
            "images/upgradetree/upgradebench.png",
            "images/upgradetree/hammer1.png",
            "images/upgradetree/hammer2.png",
            "images/upgradetree/stick.png",
            "images/upgradetree/exit.png",
            "images/player.png",
            "images/nebulite.png",
            "images/solstite.png",
            "images/durasteel.png",
            "images/f_button.png",
            "images/ExtractorAnimation.png"
    };
    private static final String[] earthTextureAtlases = {
            "images/terrain_iso_grass.atlas",
            "images/ghost.atlas",
            "images/ghostKing.atlas",
            "images/base_enemy.atlas",
            "images/troll_enemy.atlas",
            "images/rangeEnemy.atlas",
            "images/botanist.atlas",
            "images/boss_enemy.atlas",
            "images/botanist.atlas",
            "images/playerSS.atlas",
            "images/wrench.atlas",
            "images/baseballbat.atlas",
            "images/structures/closed_gate.atlas",
            "images/structures/open_gate.atlas",
            "images/structures/dirt_wall.atlas",
            "images/structures/stone_wall.atlas",
            "images/botanist.atlas",
            "images/comp_spritesheet.atlas",
            "images/sling_shot.atlas",
            "images/player.atlas",
            "images/ExtractorAnimation.atlas"

    };
    private static final String[] earthSounds = {"sounds/Impact4.wav, sounds/Impact.ogg, sounds/Impact4.ogg"};
    private static final String backgroundMusic = "sounds/BGM_03_mp3.wav";
    private static final String[] earthMusic = {backgroundMusic};

    private final TerrainFactory terrainFactory;
    private final ArrayList<Entity> targetables;
    private Entity player;
    private Entity companion;
    private Entity laboratory;
    private GdxGame game;

    /**
     * Initialise this EarthGameArea to use the provided TerrainFactory.
     * @param terrainFactory TerrainFactory used to create the terrain for the GameArea.
     * @requires terrainFactory != null
     */
    public EarthGameArea(TerrainFactory terrainFactory, GdxGame game) {
        super();
        this.game = game;
        this.terrainFactory = terrainFactory;
        this.targetables = new ArrayList<>();
        ServiceLocator.registerGameArea(this);
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
        laboratory = spawnLaboratory();
        spawnUpgradeBench();
        spawnShip();

        player = spawnPlayer();
        companion = spawnCompanion(player);
        spawnPotion(companion,laboratory);

        spawnEnemies();
        spawnBoss();
        spawnAsteroids();
        spawnBotanist();

        playMusic();
    }
    public static void removeItemOnMap(Entity entityToRemove) {
        entityToRemove.setEnabled(false);
        itemsOnMap.remove(entityToRemove);
        Gdx.app.postRunnable(entityToRemove::dispose);
    }
    private void spawnEnvironment() {
        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) terrain.getMap().getLayers().get("Tree Base");
        Entity environment;
        for (int y = 0; y < collisionLayer.getHeight(); y++) {
            for (int x = 0; x < collisionLayer.getWidth(); x++) {
                TiledMapTileLayer.Cell cell = collisionLayer.getCell(x, collisionLayer.getHeight() - 1 - y);
                if (cell != null) {
                    MapObjects objects = cell.getTile().getObjects();
                    GridPoint2 tilePosition = new GridPoint2(x, collisionLayer.getHeight() - 1 - y);
                    if (objects.getCount() >= 1) {
                        RectangleMapObject object = (RectangleMapObject) objects.get(0);
                        Rectangle collisionBox = object.getRectangle();
                        float collisionX = 0.5f-collisionBox.x / 16;
                        float collisionY = 0.5f-collisionBox.y / 16;
                        float collisionWidth = collisionBox.width / 32;
                        float collisionHeight = collisionBox.height / 32;
                        environment = ObstacleFactory.createEnvironment(collisionWidth, collisionHeight, collisionX, collisionY);
                    }
                    else {
                        environment = ObstacleFactory.createEnvironment();
                    }
                    spawnEntityAt(environment, tilePosition, false, false);
                }
            }
        }
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

    private void spawnAsteroids() {
        //Extra Spicy Asteroids
        GridPoint2 posAs = new GridPoint2(8, 8);
        spawnEntityAt(
                ObstacleFactory.createAsteroid(ASTEROID_SIZE, ASTEROID_SIZE), posAs, false, false);

    }

    private void spawnUpgradeBench() {
        Entity upgradeBench = StructureFactory.createUpgradeBench();
        spawnEntityAt(upgradeBench, new GridPoint2(20, 40), true, true);
    }

    private void spawnExtractors() {
        ServiceLocator.getGameStateObserverService().trigger("resourceMax", Resource.Nebulite.toString(),  (int) 100);
        ServiceLocator.getGameStateObserverService().trigger("resourceMax", Resource.Durasteel.toString(),  (int) 500);
        ServiceLocator.getGameStateObserverService().trigger("resourceMax", Resource.Solstite.toString(),  (int) 1000);
        ServiceLocator.getGameStateObserverService().trigger("extractorsCount", Resource.Nebulite.toString(),  (int) 0);
        ServiceLocator.getGameStateObserverService().trigger("extractorsCount", Resource.Durasteel.toString(),  (int) 0);
        ServiceLocator.getGameStateObserverService().trigger("extractorsCount", Resource.Solstite.toString(),  (int) 0);
        ServiceLocator.getGameStateObserverService().trigger("extractorsTotal", Resource.Nebulite.toString(),  (int) 0);
        ServiceLocator.getGameStateObserverService().trigger("extractorsTotal", Resource.Durasteel.toString(),  (int) 0);
        ServiceLocator.getGameStateObserverService().trigger("extractorsTotal", Resource.Solstite.toString(),  (int) 0);
        ServiceLocator.getGameStateObserverService().trigger("extractorsMax", Resource.Nebulite.toString(),  (int) 4);
        ServiceLocator.getGameStateObserverService().trigger("extractorsMax", Resource.Durasteel.toString(),  (int) 4);
        ServiceLocator.getGameStateObserverService().trigger("extractorsMax", Resource.Solstite.toString(),  (int) 4);

        GridPoint2 pos = new GridPoint2(terrain.getMapBounds(0).sub(0, 2).x/2, terrain.getMapBounds(0).sub(2, 2).y/2);
        Entity extractor = StructureFactory.createExtractor(30, Resource.Nebulite, (long) 1000.0, 10);
        spawnEntityAt(extractor, pos, true, false);
        targetables.add(extractor);

        pos = new GridPoint2(terrain.getMapBounds(0).sub(8, 2).x/2, terrain.getMapBounds(0).sub(2, 2).y/2);
        extractor = StructureFactory.createExtractor(30, Resource.Solstite, (long) 1000.0, 10);
        targetables.add(extractor);
        spawnEntityAt(extractor, pos, true, false);

        pos = new GridPoint2(terrain.getMapBounds(0).sub(16, 2).x/2, terrain.getMapBounds(0).sub(2, 2).y/2);
        extractor = StructureFactory.createExtractor(30, Resource.Durasteel, (long) 1000.0, 10);
        targetables.add(extractor);
        spawnEntityAt(extractor, pos, true, false);

        ResourceDisplay resourceDisplayComponent = new ResourceDisplay(5, 32, 1000)
                .withResource(Resource.Durasteel)
                .withResource(Resource.Solstite)
                .withResource(Resource.Nebulite);
        Entity resourceDisplay = new Entity().addComponent(resourceDisplayComponent);
        spawnEntity(resourceDisplay);
    }
    public Entity getExtractorIcon(){
        GridPoint2 pos = new GridPoint2(terrain.getMapBounds(0).sub(24, 2).x/2, terrain.getMapBounds(0).sub(2, 2).y/2);
        Entity extractor = StructureFactory.createExtractor(30, Resource.Durasteel, (long) 100.0, 1);
        targetables.add(extractor);
        extractor.setScale(new Vector2(0.5f,0.5f));
        spawnEntityAt(extractor, pos, true, false);
        return extractor;
    }
    public Entity getExtractor() {
        GridPoint2 pos = new GridPoint2(terrain.getMapBounds(0).sub(24, 2).x / 2, terrain.getMapBounds(0).sub(2, 2).y / 2);
        Entity extractor = StructureFactory.createExtractor(30, Resource.Durasteel, (long) 100.0, 1);
        targetables.add(extractor);
        spawnEntityAt(extractor, pos, true, false);
        return extractor;
    }

    private void spawnShip() {
        GridPoint2 spawnPosition = new GridPoint2(7*terrain.getMapBounds(0).sub(1, 1).x/12,
                2*terrain.getMapBounds(0).sub(1, 1).y/3);
        Entity ship = StructureFactory.createShip(game);
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
    private Entity spawnLaboratory(){
        GridPoint2 randomPos = new GridPoint2(34,19);
        Entity newLaboratory = LaboratoryFactory.createLaboratory();
        spawnEntityAt(newLaboratory, randomPos, true,false);
        return newLaboratory;
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
        //TODO: Think of solution for sharing player between screens (Currently it keeps getting disposed!!)
        this.player = PlayerFactory.createPlayer();
        this.player.getEvents().addListener("death", () -> game.setScreen(GdxGame.ScreenType.PLAYER_DEATH));
        ServiceLocator.getGameStateObserverService().trigger("updatePlayer", "player", this.player);
        spawnEntityAt(this.player, PLAYER_SPAWN, true, true);
        targetables.add(this.player);
        return this.player;
    }
    private Entity spawnCompanion(Entity playerEntity) {
        Entity newCompanion = CompanionFactory.createCompanion(playerEntity);
        PhysicsComponent playerPhysics = playerEntity.getComponent(PhysicsComponent.class);
        //calculate the player position
        Vector2 playerPosition = playerPhysics.getBody().getPosition();
        spawnEntityAt(newCompanion, COMPANION_SPAWN, true, true);
        targetables.add(newCompanion);
        return newCompanion;
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
        }
    }
    private void spawnPotion(Entity companionEntity ,Entity laboratoryEntity){
        Entity newPotion = PotionFactory.createDeathPotion(companionEntity, laboratoryEntity);
        itemsOnMap.add(newPotion);
        GridPoint2 pos = new GridPoint2(34, 18);
        spawnEntityAt(newPotion, pos, true, false);
    }

    /**
     * Spawns all the enemies detailed in the Game Area.
     */
    private void spawnEnemies() {
        GridPoint2 minPos = new GridPoint2(0, 0);
        GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

        // Spawning enemies based on set number of each type
        for (int i = 0; i < NUM_MELEE_PTE; i++) {
            GridPoint2 randomPos1 = RandomUtils.random(minPos, maxPos);
            Entity meleePTE = EnemyFactory.createEnemy(targetables, EnemyType.Melee, EnemyBehaviour.PTE);
            spawnEntityAt(meleePTE, randomPos1, true, true);
            EnemyFactory.enemies.add(meleePTE);
        }

        for (int i = 0; i < NUM_MELEE_DTE; i++) {
            GridPoint2 randomPos2 = RandomUtils.random(minPos, maxPos);
            Entity meleeDTE = EnemyFactory.createEnemy(targetables, EnemyType.Melee, EnemyBehaviour.DTE);
            spawnEntityAt(meleeDTE, randomPos2, true, true);
            EnemyFactory.enemies.add(meleeDTE);
        }

        for (int i = 0; i < NUM_RANGE_PTE; i++) {
            GridPoint2 randomPos3 = RandomUtils.random(minPos, maxPos);
            Entity rangePTE = EnemyFactory.createEnemy(targetables, EnemyType.Ranged, EnemyBehaviour.PTE);
            spawnEntityAt(rangePTE, randomPos3, true, true);
            EnemyFactory.enemies.add(rangePTE);
        }
    }

    /**
     * Spawns the boss for the Game Area's map.
     */
    private void spawnBoss() {
        GridPoint2 minPos = new GridPoint2(0, 0);
        GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

        GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
        Entity boss = EnemyFactory.createEnemy(targetables, EnemyType.BossMelee, EnemyBehaviour.PTE);
        spawnEntityAt(boss, randomPos, true, true);
        EnemyFactory.enemies.add(boss);
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
  public void setCompanion(Entity Companion){companion=Companion;}
    public Entity getCompanion() {
        return companion;
    }
}