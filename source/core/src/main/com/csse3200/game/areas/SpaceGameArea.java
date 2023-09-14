package com.csse3200.game.areas;
import com.badlogic.gdx.audio.Music;
import com.csse3200.game.GdxGame;
import com.csse3200.game.files.UserSettings;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.areas.terrain.TerrainFactory.TerrainType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.MinigameShipFactory;
import com.csse3200.game.entities.factories.ObstacleFactory;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.components.gamearea.GameAreaDisplay;
import com.csse3200.game.utils.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.csse3200.game.components.ships.DistanceDisplay;
import static com.csse3200.game.components.mainmenu.MainMenuActions.game;


/**
 * Forest area for the demo game with trees, a player, and some enemies.
 */
public class SpaceGameArea extends GameArea {
    private static final Logger logger = LoggerFactory.getLogger(ForestGameArea.class);
    private static final GridPoint2 SHIP_SPAWN = new GridPoint2(5, 10);
    private static final float ASTEROID_SIZE = 0.9f;
    private static final float STATIC_ASTEROID_SIZE =0.9f;
    private static final float WORMHOLE_SIZE = 0.9f;
    private static final int NUM_ENEMIES = 10;
    private float distance;
    private Entity ship;
    private Entity goal;
    private static final int NUM_ASTEROIDS = 100;
    private Label distanceLabel;
    private static final String[] spaceMiniGameTextures = {
            "images/SpaceMiniGameBackground.png",
            "images/meteor.png", // https://axassets.itch.io/spaceship-simple-assets
            "images/LeftShip.png",
            "images/Ship.png",
            "images/wormhole.png",
            "images/obstacle-enemy.png",
            "images/mainship.png"
    };
    private static final String backgroundMusic = "sounds/WereWasI.ogg"; //public domain https://opengameart.org/content/where-was-i
    private static final String[] spaceMusic = {backgroundMusic};
    private final TerrainFactory terrainFactory;
    private final ArrayList<Entity> targetables;

    private static final String[] spaceTextureAtlases = {"images/ship.atlas"};

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
     * Main method for calling all the methods in the obstacle
     * minigame into the SpaceMapScreen class
     */
    @Override
    public void create() {
        loadAssets();
        displayUI();
        playMusic();
        spawnTerrain();
        Entity newShip = spawnShip();
        spawnAsteroids();
        Entity goal = spawnGoal();
        createBoundary();
        spawnEnemy();

    }

    /**
     * Method for the background music of the game
     */
    private void playMusic() {
        UserSettings.Settings settings = UserSettings.get();
        Music music = ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class);
        music.setLooping(true);
        music.setVolume(settings.musicVolume);
        music.play();
    }


    /**
     * Method for creating boundaries along the x-axis
     * @param n Number of blocks that are added along the x-axis
     * @param pos Start position from where the number of blocks are added
     */
    private void spawnStaticAsteroidsRight(int n, GridPoint2 pos){
        Entity asteroid_length = ObstacleFactory.createStaticAsteroid(STATIC_ASTEROID_SIZE, STATIC_ASTEROID_SIZE);
        if (n <= 0) {
            return;
        }
        spawnEntityAt(asteroid_length, pos, false, false);
        // Increment the position for the next asteroid
        pos.x += 1;
        pos.y += 0;
        spawnStaticAsteroidsRight(n - 1, pos); // Recursive call
    }

    /**
     * Method of for creating the boundaries along the y-axis
     * @param n Number of blocks that are added along the y-axis
     * @param pos Start position from where the number of blocks are added on the map
     */
    private void spawnStaticAsteroidsUp(int n, GridPoint2 pos){
        Entity asteroid_width = ObstacleFactory.createStaticAsteroid(STATIC_ASTEROID_SIZE, STATIC_ASTEROID_SIZE);
        if (n <= 0) {
            return;
        }
        spawnEntityAt(asteroid_width, pos, false, false);
        // Increment the position for the next asteroid
        pos.y += 1;
        spawnStaticAsteroidsUp(n - 1, pos); // Recursive call
    }

    /**
     * Method for creating the boundaries of the map
     */
    private void createBoundary(){
        spawnStaticAsteroidsRight(60,new GridPoint2(0,-1));
        spawnStaticAsteroidsRight(60,new GridPoint2(0,29));
        spawnStaticAsteroidsUp(30,new GridPoint2(-1,0));
        spawnStaticAsteroidsUp(30,new GridPoint2(59,0));
    }


    /**
     * Method for spawning enemies randomly across the map in the minigame
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
     * Method for spawning asteroids randomly across the map in the minigame
     */
    private void spawnAsteroids() {
        GridPoint2 minPos = new GridPoint2(1, 1);
        GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);
        for (int i = 0; i < NUM_ASTEROIDS; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity asteroid = ObstacleFactory.createAsteroid(ASTEROID_SIZE,ASTEROID_SIZE);
            spawnEntityAt(asteroid, randomPos, false, false);
        }
    }

    /**
     * Method for placing the exit point from the obstacle minigame
     */
    private Entity spawnGoal() {
        GridPoint2 position = new GridPoint2(56, 10);
        Entity newGoal = ObstacleFactory.createObstacleGameGoal(WORMHOLE_SIZE, WORMHOLE_SIZE);
        //goal = ObstacleFactory.createObstacleGameGoal(WORMHOLE_SIZE, WORMHOLE_SIZE);
        spawnEntityAt(newGoal, position, false, false);
        goal = newGoal;
        return goal; // Return the instance variable
    }

    /**
     * DisplayUI method for displaying Space game in the main menu
     */
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
    private Entity spawnShip()
    {
        Entity newShip = MinigameShipFactory.createMinigameShip();
        spawnEntityAt(newShip, SHIP_SPAWN, true, true);
        targetables.add(newShip);
        ship = newShip;
        return newShip;
    }


    /**
     * Getter method for ship
     * @return Ship Entity
     */
    public Entity getShip(){
        return ship;
    }


    /**
     * Getter method for goal
     * @return Goal Entity
     */
    public Entity getGoal() {
        return goal;
    }

    /**
     * Method for calculating distance between two entities
     * @param entity1 Entity 1
     * @param entity2 Entity 2
     * @return Distance between Entity 1 and Entity 2
     */
    public static float calculateDistance(Entity entity1, Entity entity2) {
        // Get the center positions of both entities
        Vector2 position1 = entity1.getCenterPosition();
        Vector2 position2 = entity2.getCenterPosition();

        // Calculate the distance between the two positions
        float distance = position1.dst(position2);

        return distance;
    }




    /**
     * Method for loading the texture and the music of the map
     */
    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(spaceMiniGameTextures);
        resourceService.loadMusic(spaceMusic);
        resourceService.loadTextureAtlases(spaceTextureAtlases);
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
        resourceService.unloadAssets(spaceTextureAtlases);

    }

    /**
     * Override method for disposing and unloading assets
     */
    @Override
    public void dispose() {
        super.dispose();
        this.unloadAssets();
    }
}
