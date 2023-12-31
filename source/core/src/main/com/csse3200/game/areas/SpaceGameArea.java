package com.csse3200.game.areas;
import com.badlogic.gdx.audio.Music;
import com.csse3200.game.files.UserSettings;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.areas.terrain.TerrainFactory.TerrainType;
import com.csse3200.game.components.gamearea.GameAreaDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.MinigameShipFactory;
import com.csse3200.game.entities.factories.MinigameObjectFactory;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Random;


/**
 * Forest area for the demo game with trees, a player, and some enemies.
 */
public class SpaceGameArea extends GameArea {
    private static final Logger logger = LoggerFactory.getLogger(SpaceGameArea.class);
    private static final GridPoint2 SHIP_SPAWN = new GridPoint2(5, 10);
    private static final float ASTEROID_SIZE = 0.9f;
    private static final float STATIC_ASTEROID_SIZE =0.9f;
    private static final float WORMHOLE_SIZE = 0.9f;
    private static final int NUM_ENEMIES = 10;
    private Entity ship;
    private Entity goal;
    private static final int NUM_ASTEROIDS = 100;
    private TerrainFactory terrainFactory;
    private ArrayList<Entity> targetTables;
    private Random random;
    int randomX, randomY;
    private Vector2 goalPosition;

    /**
     * Constructor for initializing terrain area
     * @param terrainFactory Terrain factory being used in the area
     */
    public SpaceGameArea(TerrainFactory terrainFactory) {
        super();
        this.terrainFactory = terrainFactory;
        this.targetTables = new ArrayList<>();
    }

    /**
     * Main method for calling all the methods in the obstacle
     * minigame into the SpaceMapScreen class
     */
    @Override
    public void create() {
        displayUI();
        spawnTerrain();
        spawnGoal();
        spawnShip();
        spawnAsteroids();
        createBoundary();
        spawnEnemy();
        playMusic();
    }
    /**
     * Method for the background music of the game
     */
    private void playMusic() {
        UserSettings.Settings settings = UserSettings.get();
        Music music = ServiceLocator.getResourceService().getAsset("sounds/BGM_03_mp3.wav", Music.class);
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
        Entity asteroidlength = MinigameObjectFactory.createStaticAsteroid(STATIC_ASTEROID_SIZE, STATIC_ASTEROID_SIZE);
        if (n <= 0) {
            return;
        }
        spawnEntityAt(asteroidlength, pos, false, false);
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
        Entity asteroidWidth = MinigameObjectFactory.createStaticAsteroid(STATIC_ASTEROID_SIZE, STATIC_ASTEROID_SIZE);
        if (n <= 0) {
            return;
        }
        spawnEntityAt(asteroidWidth, pos, false, false);
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
            Entity randomenemy = MinigameObjectFactory.createObstacleEnemy(WORMHOLE_SIZE,WORMHOLE_SIZE);
            spawnEntityAt(randomenemy, randomPos,true,false);
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
            Entity asteroid = MinigameObjectFactory.createAsteroid(ASTEROID_SIZE,ASTEROID_SIZE);
            spawnEntityAt(asteroid, randomPos, false, false);
        }
    }

    /**
     * Method for placing the exit point from the obstacle minigame
     */
    public Entity spawnGoal() {
        random = new Random();
        // Continue generating random positions until an unoccupied position is found.
        do {
            randomX = random.nextInt(terrain.getMapBounds(0).x);
            randomY = random.nextInt(terrain.getMapBounds(0).y);
            goalPosition = new Vector2((float) randomX, (float) randomY);
        } while (isPositionOccupied(goalPosition));
        Entity newGoal = MinigameObjectFactory.createObstacleGameGoal(WORMHOLE_SIZE, WORMHOLE_SIZE);
        spawnEntityAtVector(newGoal, goalPosition);
        goal = newGoal;
        return goal;
    }

    /**
     * Check if a position is occupied by an entity.
     */
    public boolean isPositionOccupied(Vector2 position) {
        // Loop through the list of existing entities and check if any entity occupies the given position.
        for (Entity entity : targetTables) {
            //GridPoint2 and Vector2 is unrelated
            if (entity != null && entity.getPosition().equals(position)) {
                return true; // Position is occupied.
            }
        }
        return false; // Position is not occupied.
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
        targetTables.add(newShip);
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
     * Override method for disposing and unloading assets
     */
    @Override
    public void dispose() {
        super.dispose();
    }
}
