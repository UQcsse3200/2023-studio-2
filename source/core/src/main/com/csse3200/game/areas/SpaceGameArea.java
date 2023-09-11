package com.csse3200.game.areas;
import com.badlogic.gdx.audio.Music;
import com.csse3200.game.files.UserSettings;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.areas.terrain.TerrainFactory.TerrainType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ShipFactory;
import com.csse3200.game.entities.factories.ObstacleFactory;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.components.gamearea.GameAreaDisplay;
import com.csse3200.game.utils.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;


/** Forest area for the demo game with trees, a player, and some enemies. */
public class SpaceGameArea extends GameArea {
    private static final Logger logger = LoggerFactory.getLogger(ForestGameArea.class);
    private static final GridPoint2 SHIP_SPAWN = new GridPoint2(5, 10);
    private static final float ASTEROID_SIZE = 0.9f;
    private static final float STATIC_ASTEROID_SIZE =0.9f;
    private static final float WORMHOLE_SIZE = 0.9f;
    private static final int NUM_ENEMIES = 10;
    private Entity newShip;
    GridPoint2 startingPosition = new GridPoint2(0, 0);
    int numberOfAsteroids = 10; // Change this to the desired number of asteroids

    private static final String[] spaceMiniGameTextures = {
            "images/SpaceMiniGameBackground.png",
            "images/meteor.png", // https://axassets.itch.io/spaceship-simple-assets
            "images/LeftShip.png",
            "images/Ship.png",
            "images/wormhole.jpg",
            "images/obstacle-enemy.png"
    };
    private static final String backgroundMusic = "sounds/WereWasI.ogg"; //public domain https://opengameart.org/content/where-was-i
    private static final String[] spaceMusic = {backgroundMusic};
    private final TerrainFactory terrainFactory;
    private final ArrayList<Entity> targetables;

    /**
     * Constructor for initializing terrain area
     * @param terrainFactory Terrain factory being used in the area
     */
    public SpaceGameArea(TerrainFactory terrainFactory) {
        super();
        this.terrainFactory = terrainFactory;
        this.targetables = new ArrayList<>();
    }

    /**
     * Main method for calling all the methods in the obstacle minigame into the SpaceMapScreen class
     */
    @Override
    public void create() {
        loadAssets();
        displayUI();
        playMusic();
        spawnTerrain();
        spawnShip();
        spawnAsteroids();
        spawnGoal();
        createBoundary();
        spawnEnemy();
    }

    private void playMusic() {
        UserSettings.Settings settings = UserSettings.get();
        Music music = ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class);
        music.setLooping(true);
        music.setVolume(settings.musicVolume);
        music.play();
    }

    /**
     * Spawn Asteroids of a given size and position
     * @requires ASTEROID_SIZE != null
     */
    private void spawnAsteroids() {
        //Extra Spicy Asteroids
        GridPoint2 posAs = new GridPoint2(22, 10);
        spawnEntityAt(
                ObstacleFactory.createAsteroid(ASTEROID_SIZE,ASTEROID_SIZE), posAs, false, false);

    }

    /**
     * Recursively calls n number of asteroids starting from position to the right
     * @param n Number of asteroids
     * @param pos Starting position for asteroid spawning
     */
    private void spawnStaticAsteroidsRight(int n, GridPoint2 pos){
        if (n <= 0) {
            return;
        }

        spawnEntityAt(
                ObstacleFactory.createStaticAsteroid(STATIC_ASTEROID_SIZE, STATIC_ASTEROID_SIZE), pos, false, false);

        // Increment the position for the next asteroid
        pos.x += 1;
        pos.y += 0;
        spawnStaticAsteroidsRight(n - 1, pos); // Recursive call
    }
    private void spawnStaticAsteroidsUp(int n, GridPoint2 pos){
        if (n <= 0) {
            return;
        }

        spawnEntityAt(
                ObstacleFactory.createStaticAsteroid(STATIC_ASTEROID_SIZE, STATIC_ASTEROID_SIZE), pos, false, false);

        // Increment the position for the next asteroid
        pos.y += 1;
        spawnStaticAsteroidsUp(n - 1, pos); // Recursive call
    }

    private void createBoundary(){
        spawnStaticAsteroidsRight(30,new GridPoint2(0,0));
        spawnStaticAsteroidsRight(30,new GridPoint2(0,29));
        spawnStaticAsteroidsUp(28,new GridPoint2(0,1));
        spawnStaticAsteroidsUp(28,new GridPoint2(29,1));
    }

    /**
     * Method for spawning enemies randomly
     */
    private void spawnEnemy(){
        GridPoint2 minPos = new GridPoint2(1, 1);
        GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);
        for (int i = 0; i < NUM_ENEMIES; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity rand_enemy = ObstacleFactory.createObstacleEnemy(WORMHOLE_SIZE,WORMHOLE_SIZE);
            spawnEntityAt(rand_enemy, randomPos,true,false);
        }
    }

    /**
     * Method for placing the exit point from the obstacle minigame
     */
    private void spawnGoal(){
        GridPoint2 position = new GridPoint2(24,10);
        spawnEntityAt(
                ObstacleFactory.createObstacleGameGoal(WORMHOLE_SIZE,WORMHOLE_SIZE), position,false,false);
    }


    private void displayUI() {
        Entity ui = new Entity();
        ui.addComponent(new GameAreaDisplay("Space Game"));
        spawnEntity(ui);
    }

    /**
     * Method for spawning terrain for background of obstacle minigame
     */
    private void spawnTerrain() {
        // Background terrain
        terrain = terrainFactory.createSpaceTerrain(TerrainType.SPACE_DEMO);
        spawnEntity(new Entity().addComponent(terrain));

        // Terrain walls
        float tileSize = terrain.getTileSize();
        GridPoint2 tileBounds = terrain.getMapBounds(0);
        Vector2 worldBounds = new Vector2(tileBounds.x * tileSize, tileBounds.y * tileSize);

    }

    /**
     * Method for spawning ship on the obstacle minigame map
     */
    private void spawnShip()
    {
        Entity newShip = ShipFactory.createShip();
        spawnEntityAt(newShip, SHIP_SPAWN, true, true);
        targetables.add(newShip);
    }

    /**
     * Getter method for ship
     * @return Ship Entity
     */
    public Entity getShip(){
        return newShip;
    }

    /**
     * Method for loading the texture and the music of the map
     */
    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(spaceMiniGameTextures);
        resourceService.loadMusic(spaceMusic);
        while (!resourceService.loadForMillis(10)) {
            // This could be upgraded to a loading screen
            logger.info("Loading... {}%", resourceService.getProgress());
        }
    }

    /**
     * Method for unloading the texture and the music of the map
     */
    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(spaceMiniGameTextures);
        resourceService.unloadAssets(spaceMusic);

    }

    @Override
    public void dispose() {
        super.dispose();
        this.unloadAssets();
    }

}
