package com.csse3200.game.areas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.mapConfig.AssetsConfig;
import com.csse3200.game.areas.mapConfig.GameAreaConfig;
import com.csse3200.game.areas.mapConfig.InvalidConfigException;
import com.csse3200.game.areas.mapConfig.ConfigLoader;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.EnvironmentStatsComponent;
import com.csse3200.game.components.PowerupType;
import com.csse3200.game.components.explosives.ExplosiveConfig;
import com.csse3200.game.components.gamearea.PlanetHudDisplay;
import com.csse3200.game.components.player.InventoryDisplayComponent;
import com.csse3200.game.components.resources.Resource;
import com.csse3200.game.components.resources.ResourceDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.Fissure;
import com.csse3200.game.entities.PlaceableEntity;
import com.csse3200.game.entities.TileEntity;
import com.csse3200.game.entities.configs.*;
import com.csse3200.game.entities.factories.*;
import com.csse3200.game.files.UserSettings;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TerrainService;
import com.csse3200.game.utils.math.GridPoint2Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.csse3200.game.utils.LoadUtils.joinPath;

/**
 * A Base Game Area for any level.
 * Details of map can be defined in a config file to be passed to the constructor
 */
public class MapGameArea extends GameArea{

    public GameAreaConfig mapConfig = null;
    private static final Logger logger = LoggerFactory.getLogger(MapGameArea.class);
    private final TerrainFactory terrainFactory;
    private final GdxGame game;
    protected boolean validLoad = true;
    private static boolean freezing;

    public MapGameArea(String levelName, String gamearea, TerrainFactory terrainFactory, GdxGame game) {
        try {
            mapConfig = ConfigLoader.loadMapDirectory(levelName, gamearea);
            logger.info("Successfully loaded map {}", joinPath(List.of(levelName, gamearea)));
        } catch (InvalidConfigException exception) {
            logger.error("FAILED TO LOAD GAME IN CONSTRUCTOR - {}", exception.getMessage());
            validLoad = false;
        }
        this.game = game;
        this.terrainFactory = terrainFactory;
    }

    /**
     * Create the game area
     */
    @Override
    public void create() {
        if (!validLoad) {
            logger.error("FAILED TO LOAD GAME - RETURNING TO MAIN MENU");
            game.setScreen(GdxGame.ScreenType.MAIN_MENU);
            return;
        }
        loadAssets();

        registerEntityPlacementService();
        registerStructurePlacementService();

        spawnTerrain();
        spawnEnvironment();
        spawnExtractors();
        spawnShip();
        player = spawnPlayer();
        companion = spawnCompanion();
        spawnPowerups();
        spawnLaboratory();
        spawnPowerups();
        spawnPortal(player);
        spawnTurrets(player);
        spawnWalls(player);
        spawnGates(player);
        spawnExplosives(player);
        spawnTreeTop();
        spawnAstro();
        spawnTutnpc();
        spawnHellman();
        spawnSpawners();
        spawnJail();

        spawnAstronaut();

        //spawnEnvironmentDamage();
        spawnFreezingArea();
        spawnEnvironmentDamage();
        spawnSafeZone(player);

        displayUI();
        playMusic();
    }

    /**
     * Loads all assets listed in the config file
     */
    protected void loadAssets() {
        long start = System.currentTimeMillis();
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();

        if (mapConfig == null) return;
        resourceService.loadDynamicAssets(mapConfig.getEntityTextures());
        if (mapConfig.planetImage != null)
            resourceService.loadTextures(new String[] {mapConfig.planetImage});
        AssetsConfig assets = mapConfig.assets;

        if (assets != null) {
            assets.load(resourceService);
        }

        while (!resourceService.loadForMillis(10)) {
            // This could be upgraded to a loading screen
            logger.info("Loading... {}%", resourceService.getProgress());
        }
        logger.debug(String.format("Load took %d ms to load.", System.currentTimeMillis() - start));
    }

    /**
     * Creates the UI with a planet name as described in config file
     */
    private void displayUI() {
        Entity ui = new Entity();
        //Ensure non-null
        mapConfig.mapName = mapConfig.mapName == null ? "" : mapConfig.mapName;
        //ui.addComponent(new GameAreaDisplay(mapConfig.mapName));
        ui.addComponent(new PlanetHudDisplay(mapConfig.mapName, mapConfig.planetImage))
                .addComponent(new InventoryDisplayComponent());
        spawnEntity(ui);
    }

    /**
     * Spawns a portal that sends the player to a new location
     */
    private void spawnPortal(Entity playerEntity) {
        if (mapConfig.areaEntityConfig == null) return;

        for (PortalConfig portalConfig : mapConfig.areaEntityConfig.getEntities(PortalConfig.class)) {
            Entity portal = PortalFactory.createPortal(playerEntity, portalConfig);
            spawnEntityAt(portal, portalConfig.position, false, false);
        }
    }

    private void spawnSafeZone(Entity playerEntity) {
        if (mapConfig.areaEntityConfig == null) return;

        SafeZoneConfig safeZoneConfig = mapConfig.areaEntityConfig.getEntity(SafeZoneConfig.class);

        if (safeZoneConfig == null) return;

        Entity safeZone = SafeZoneFactory.createSafeZone(playerEntity);
        spawnEntityAt(safeZone, safeZoneConfig.position, false, false);
    }

    private void spawnEnvironmentDamage() {
        if (mapConfig.areaEntityConfig == null) return;

        Entity envDamage = EnvironmentalDamageFactory.createDamage();
        spawnEntityAt(envDamage, new GridPoint2(45, 45), false, false);
    }

    private void spawnFreezingArea() {
        if (mapConfig.areaEntityConfig == null) return;

        Entity freezeArea = FreezingAreaFactory.createFreezingArea();
        spawnEntityAt(freezeArea, new GridPoint2(40, 60), false, false);
    }

    public static float getSpeedMult() {
        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) terrain.getMap().getLayers().get("Base");
        Vector2 playerPos = getPlayer().getPosition();
        TiledMapTileLayer.Cell cell = collisionLayer.getCell((int) (playerPos.x * 2), (int) (playerPos.y * 2));
        Object speedMult = cell.getTile().getProperties().get("speedMult");

        return speedMult != null ? (float)speedMult : 1f;
    }

    public static boolean isOnIce() {
        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) terrain.getMap().getLayers().get("Base");
        Vector2 playerPos = getPlayer().getPosition();
        TiledMapTileLayer.Cell cell = collisionLayer.getCell((int) (playerPos.x * 2), (int) (playerPos.y * 2));
        Object onIce = cell.getTile().getProperties().get("slide");

        return onIce != null && (boolean) onIce;
    }

    public static boolean isFreezing() {
        return freezing;
    }

    public static void toggleFreezing(Entity player) {
        freezing = !freezing;
    }

    /**
     * Spawns the game terrain with wallSize determined by the config file
     */
    private void spawnTerrain() {
        // Background terrain
        terrain = terrainFactory.createTerrain(mapConfig.terrainPath);
        spawnEntity(new Entity().addComponent(terrain));

        // Terrain walls
        float tileSize = terrain.getTileSize();
        GridPoint2 tileBounds = terrain.getMapBounds(0);
        Vector2 worldBounds = new Vector2(tileBounds.x * tileSize, tileBounds.y * tileSize);

        // Left
        spawnEntityAt(
                ObstacleFactory.createWall(ObstacleFactory.WALL_SIZE, worldBounds.y), GridPoint2Utils.ZERO, false, false);
        // Right
        spawnEntityAt(
                ObstacleFactory.createWall(ObstacleFactory.WALL_SIZE, worldBounds.y),
                new GridPoint2(tileBounds.x, 0),
                false,
                false);
        // Top
        spawnEntityAt(
                ObstacleFactory.createWall(worldBounds.x, ObstacleFactory.WALL_SIZE),
                new GridPoint2(0, tileBounds.y),
                false,
                false);
        // Bottom
        spawnEntityAt(
                ObstacleFactory.createWall(worldBounds.x, ObstacleFactory.WALL_SIZE), GridPoint2Utils.ZERO, false, false);
        ServiceLocator.registerTerrainService(new TerrainService(terrain));
    }

    /**
     * Spawns the game environment
     */
    private void spawnEnvironment() {
        TiledMapTileLayer layer = (TiledMapTileLayer) terrain.getMap().getLayers().get("Tree Base");

        if (layer == null) {
            return;
        }

        List<TileEntity> environments = EnvironmentFactory.createEnvironment(layer);

        for (TileEntity tileEntity : environments) {
            spawnEntityAt(tileEntity.getEntity(), tileEntity.getTilePosition(), false, false);
        }
    }

    /**
     * Spawns powerups in the map at the positions as outlined by the config file
     */
    private void spawnPowerups() {
        for (PowerupConfig powerupConfig: mapConfig.areaEntityConfig.getEntities(PowerupConfig.class)) {
            Entity powerup = PowerupFactory.createPowerup(powerupConfig);
            spawnEntityAt(powerup, powerupConfig.position, true, false);
        }
    }

    /**
     * Spawns all the extractors for each resource type as defined in the config file
     * Also spawns the resource display for each resource
     */
    private void spawnExtractors() {
        //Spawn Display
        int scale = 5;
        int steps = 64;
        int maxResource = 1000;

        // Sets extractor count
        ServiceLocator.getGameStateObserverService().trigger("extractorsTotal", Resource.Nebulite.toString(),  0);
        ServiceLocator.getGameStateObserverService().trigger("extractorsTotal", Resource.Durasteel.toString(),  0);
        ServiceLocator.getGameStateObserverService().trigger("extractorsTotal", Resource.Solstite.toString(),  0);

        ResourceDisplay resourceDisplayComponent = new ResourceDisplay(scale, steps, maxResource)
                .withResource(Resource.Durasteel)
                .withResource(Resource.Solstite)
                .withResource(Resource.Nebulite);
        Entity resourceDisplay = new Entity().addComponent(resourceDisplayComponent);
        spawnEntity(resourceDisplay);

        //Spawn extractors if any in game area
        if (mapConfig.areaEntityConfig == null) return;

        for (ExtractorConfig extractorConfig : mapConfig.areaEntityConfig.getEntities(ExtractorConfig.class)) {
            PlaceableEntity extractor = StructureFactory.createExtractor(extractorConfig);
            ServiceLocator.getStructurePlacementService().placeStructureAt(extractor, extractorConfig.position);
        }

        for (FissureConfig fissureConfig : mapConfig.areaEntityConfig.getEntities(FissureConfig.class)) {
            PlaceableEntity fissure = new Fissure(fissureConfig);
            ServiceLocator.getStructurePlacementService().placeStructureAt(fissure, fissureConfig.position);
        }
    }

    /**
     * Spawns the saved gates into the map.
     * @param player - the player.
     */
    private void spawnGates(Entity player) {
        for (GateConfig gateConfig : mapConfig.areaEntityConfig.getEntities(GateConfig.class)) {
            var gate = BuildablesFactory.createGate(player, gateConfig);
            ServiceLocator.getStructurePlacementService().placeStructureAt(gate, gateConfig.position);

        }
    }

    /**
     * Spawns the saved turrets into the map.
     * @param player - the player.
     */
    private void spawnTurrets(Entity player) {
        for (TurretConfig config : mapConfig.areaEntityConfig.getEntities(TurretConfig.class)) {
            var turret = BuildablesFactory.createCustomTurret(config, player);
            ServiceLocator.getStructurePlacementService().placeStructureAt(turret, config.position);
        }
    }

    /**
     * Spawns the saved walls into the map.
     * @param player - the player.
     */
    private void spawnWalls(Entity player) {
        for (WallConfig config : mapConfig.areaEntityConfig.getEntities(WallConfig.class)) {
            var turret = BuildablesFactory.createWall(config, player);
            ServiceLocator.getStructurePlacementService().placeStructureAt(turret, config.position);
        }
    }

    /**
     * Spawns the saved explosives into the map.
     * @param player - the player.
     */
    private void spawnExplosives(Entity player) {
        for (LandmineConfig config : mapConfig.areaEntityConfig.getEntities(LandmineConfig.class)) {
            var landmine = ExplosivesFactory.createLandmine(config);
            ServiceLocator.getStructurePlacementService().placeStructureAt(landmine, config.position);
        }

        for (ExplosiveBarrelConfig config : mapConfig.areaEntityConfig.getEntities(ExplosiveBarrelConfig.class)) {
            var landmine = ExplosivesFactory.createExplosiveBarrel(config);
            ServiceLocator.getStructurePlacementService().placeStructureAt(landmine, config.position);
        }
    }


    /**
     * Spawns the ship at the position given by the config file
     */
    private void spawnShip() {
        if (mapConfig.areaEntityConfig == null) return;

        ShipConfig shipConfig = mapConfig.areaEntityConfig.getEntity(ShipConfig.class);
        if (shipConfig != null) {
            Entity ship = StructureFactory.createShip(game, mapConfig.winConditions, shipConfig);
            spawnEntityAt(ship, shipConfig.position, false, false);
        }
    }
    private void spawnTreeTop(){
        if (mapConfig.areaEntityConfig == null) return;

        TreeTopConfig treeTopConfig = mapConfig.areaEntityConfig.getEntity(TreeTopConfig.class);
        if (treeTopConfig != null) {
            Entity treeTop = ObstacleFactory.createTreeTop(treeTopConfig);
            spawnEntityAt(treeTop, treeTopConfig.position, false, false);
        }
    }

    /**
     * Spawns the player at the position given by the config file.
     * If that is null, then spawns at the centre of the map
     * @return The player entity created
     */
    protected Entity spawnPlayer() {
        Entity newPlayer;
        PlayerConfig playerConfig = null;
        if (mapConfig.areaEntityConfig != null) {
            playerConfig = mapConfig.areaEntityConfig.getEntity(PlayerConfig.class);
        }

        if (playerConfig != null) {
            newPlayer = PlayerFactory.createPlayer(playerConfig);
        } else {
            logger.info("Player not found in config file - creating generic player");
            newPlayer = PlayerFactory.createPlayer();
        }
        newPlayer.getComponent(CombatStatsComponent.class).setLives((int) ServiceLocator.getGameStateObserverService().getStateData("player/lives")); // Ensures previous number of lives is maintained.

        // environmental damage
        newPlayer.getComponent(EnvironmentStatsComponent.class).setSafeMap(mapConfig);
        newPlayer.getComponent(EnvironmentStatsComponent.class).damage(newPlayer.getComponent(CombatStatsComponent.class));

        newPlayer.getEvents().addListener("deathScreen", this::initiateDeathScreen);
        newPlayer.getEvents().addListener("death", () ->
                Gdx.app.postRunnable(() -> game.setScreen(GdxGame.ScreenType.PLAYER_DEATH))
        );

        if (playerConfig != null && playerConfig.position != null) {
            spawnEntityAt(newPlayer, playerConfig.position, true, true);
        } else {
            logger.info("Failed to load player position - created player at middle of map");
            //If no position specified spawn in middle of map.
            GridPoint2 pos = new GridPoint2(terrain.getMapBounds(0).x/2,terrain.getMapBounds(0).y/2);
            spawnEntityAt(newPlayer, pos, true, true);
        }
        return newPlayer;
    }
    private void spawnLaboratory(){
        LaboratoryConfig laboratoryConfig = mapConfig.areaEntityConfig.getEntity(LaboratoryConfig.class);
        if (laboratoryConfig !=null){
            Entity newLaboratory = LaboratoryFactory.createLaboratory();
            spawnEntityAt(newLaboratory, laboratoryConfig.position, true,false);
        }
    }

    public static Entity getPlayer() {
        return player;
    }

    /**
     * Spawns the companion at the position given by the config file
     */
    private Entity spawnCompanion() {
        Entity newCompanion;
        CompanionConfig companionConfig = null;
        if (mapConfig.areaEntityConfig != null) {
            companionConfig = mapConfig.areaEntityConfig.getEntity(CompanionConfig.class);
        }

        if (companionConfig != null) {
            newCompanion = CompanionFactory.createCompanion(companionConfig);
        } else {
            logger.info("Companion not found in config file - creating generic player");
            newCompanion = CompanionFactory.createCompanion();
        }
        if (companionConfig != null && companionConfig.position != null) {
            spawnEntityAt(newCompanion, companionConfig.position, true, true);
        } else {
            logger.info("Failed to load companion position - created companion at middle of map");
            //If no position specified spawn in middle of map.
            GridPoint2 pos = new GridPoint2(terrain.getMapBounds(0).x/2,terrain.getMapBounds(0).y/2);
            spawnEntityAt(newCompanion, pos, true, true);
        }

        newCompanion.getEvents().addListener("SpawnPowerup", (PowerupType type) -> {
            LaboratoryConfig labConfig = mapConfig.areaEntityConfig.getEntity(LaboratoryConfig.class);
            if (labConfig == null) return;
            Entity powerup = PowerupFactory.createPowerup(type);
            this.spawnEntityAt(powerup, labConfig.position, true, false);
        });

        return newCompanion;
    }

    /**
     * Spawns all the spawners detailed in the Game Area.
     */
    private void spawnSpawners() {
        if (mapConfig.areaEntityConfig == null) return;

        for (SpawnerConfig spawnerConfig : mapConfig.areaEntityConfig.getEntities(SpawnerConfig.class)) {
            Entity spawner = StructureFactory.createSpawner(spawnerConfig);
            spawnEntityAt(spawner, spawnerConfig.position, true, true);
        }
    }

    /**
     * Spawns the botanist NPC at the position given in the config file
     */
    private void spawnBotanist() {
        if (mapConfig.areaEntityConfig == null) return;

        BotanistConfig botanistConfig = mapConfig.areaEntityConfig.getEntity(BotanistConfig.class);
        if (botanistConfig != null) {
            Entity botanist = NPCFactory.createBotanist(botanistConfig);
            spawnEntityAt(botanist, botanistConfig.position, false, false);
        }
        //TODO: Implement this?
        //ship.addComponent(new DialogComponent(dialogueBox)); Adding dialogue component after entity creation is not supported
    }


    private void spawnAstro() {
        if (mapConfig.areaEntityConfig == null) return;

        AstroConfig astroConfig = mapConfig.areaEntityConfig.getEntity(AstroConfig.class);
        if (astroConfig != null) {
            Entity astro = NPCFactory.createAstro(astroConfig);
            spawnEntityAt(astro, astroConfig.position, false, false);
        }

    }
    private void spawnTutnpc() {
        if (mapConfig.areaEntityConfig == null) return;

        TutnpcConfig tutnpcConfig = mapConfig.areaEntityConfig.getEntity(TutnpcConfig.class);
        if (tutnpcConfig != null) {
            Entity tutnpc = NPCFactory.createTutnpc(tutnpcConfig);
            spawnEntityAt(tutnpc, tutnpcConfig.position, false, false);
        }

    }
    private void spawnHellman() {
        if (mapConfig.areaEntityConfig == null) return;

        HellmanConfig hellmanConfig = mapConfig.areaEntityConfig.getEntity(HellmanConfig.class);
        if (hellmanConfig != null) {
            Entity hellman = NPCFactory.createHellman();
            spawnEntityAt(hellman, hellmanConfig.position, false, false);
        }

    }

    private void spawnAstronaut() {
        if (mapConfig.areaEntityConfig == null) return;

        AstronautConfig astronautConfig = mapConfig.areaEntityConfig.getEntity(AstronautConfig.class);
        if (astronautConfig != null) {
            Entity astronaut = NPCFactory.createAstronaut(astronautConfig);
            spawnEntityAt(astronaut, astronautConfig.position, true, true);
        }
    }

    private void spawnJail() {
        if (mapConfig.areaEntityConfig == null) return;

        JailConfig jailConfig = mapConfig.areaEntityConfig.getEntity(JailConfig.class);
        if (jailConfig != null) {
            Entity jail = NPCFactory.createJail(jailConfig);
            spawnEntityAt(jail, jailConfig.position, true, true);
        }
    }

    /**
     * Plays the game music loaded from the config file
     */
    private void playMusic() {
        if (mapConfig.assets == null || mapConfig.assets.backgroundMusicPath == null) return;
        UserSettings.Settings settings = UserSettings.get();

        Music music = ServiceLocator.getResourceService().getAsset(mapConfig.assets.backgroundMusicPath, Music.class);
        music.setLooping(true);
        music.setVolume(settings.musicVolume);
        music.play();
    }

    @Override
    public void dispose() {
        super.dispose();
        if (mapConfig != null && mapConfig.assets != null && mapConfig.assets.backgroundMusicPath != null) {
            ServiceLocator.getResourceService().getAsset(mapConfig.assets.backgroundMusicPath, Music.class).stop();
        }
        this.unloadAssets();
    }

    /**
     * Unloads all assets from config file
     */
    protected void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();

        if (mapConfig == null) return;
        resourceService.loadDynamicAssets(mapConfig.getEntityTextures());
        AssetsConfig assets = mapConfig.assets;

        if (mapConfig.getEntityTextures() != null)
            resourceService.unloadAssets(mapConfig.getEntityTextures());

        if (assets != null) {
            assets.unload(resourceService);
        }
    }

    /**
     * Triggers the death screen.
     * @return death screen, specfic to the number of lives player has remaining.
     */
    private boolean initiateDeathScreen() {
        int lives = getPlayer().getComponent(CombatStatsComponent.class).getLives();
        switch (lives) {
            case 0:
                Gdx.app.postRunnable(() -> game.setScreen(GdxGame.ScreenType.PLAYER_DEATH_0));
                return true;
            case 1:
                Gdx.app.postRunnable(() -> game.setScreen(GdxGame.ScreenType.PLAYER_DEATH_1));
                return true;
            case 2:
                Gdx.app.postRunnable(() -> game.setScreen(GdxGame.ScreenType.PLAYER_DEATH_2));
                return true;
            case 3:
                Gdx.app.postRunnable(() -> game.setScreen(GdxGame.ScreenType.PLAYER_DEATH_3));
                return true;
            default:
                return false;
        }
    }
}
