package com.csse3200.game.areas;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.gamearea.GameAreaDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.BallFactory;
import com.csse3200.game.entities.factories.MinigameShipFactory;
import com.csse3200.game.entities.factories.ObstacleFactory;
import com.csse3200.game.entities.factories.SliderFactory;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;


/**
 * Brick game area for the minigame
 */
public class BricKBreakerGameArea extends GameArea{
    private Entity ball;
    private Entity slider;
    private static final GridPoint2 SLIDER_SPAWN = new GridPoint2(5, 8);
    private static final Logger logger = LoggerFactory.getLogger(BricKBreakerGameArea.class);
    private static final GridPoint2 BALL_SPAWN = new GridPoint2(5, 7);

    private static final String[] BrickBreakerTextures = {


            "images/minigame/Ball.png",
            "images/minigame/meteor.png",



            "images/brick-game/BrickGameBackground.png",
            "images/brick-game/Slider.png"


    };

    private final TerrainFactory terrainFactory;
    private final ArrayList<Entity> targetables;

    /**
     * Constructor for initializing minigame terrain
     * @param terrainFactory Terrain factory being used in the area
     */
    public BricKBreakerGameArea(TerrainFactory terrainFactory) {
        super();
        this.terrainFactory = terrainFactory;
        this.targetables = new ArrayList<>();
    }

    /**
     * Main method for calling all the methods in the Brick game minigame into the screen
     */
    @Override
    public void create() {
        loadAssets();
        displayUI();
        spawnTerrain();
        spawnBrickLayout();
        Entity newSlider = spawnSlider();
        //spawnBall();
    }
    /**
     * Method for spawning terrain for background of obstacle minigame
     */
    private void spawnTerrain() {
        // Background terrain
        terrain = terrainFactory.createSpaceTerrain(TerrainFactory.TerrainType.BRICK_DEMO);
        spawnEntity(new Entity().addComponent(terrain));

        // Terrain walls
        float tileSize = terrain.getTileSize();
        GridPoint2 tileBounds = terrain.getMapBounds(0);
        Vector2 worldBounds = new Vector2(tileBounds.x * tileSize, tileBounds.y * tileSize);

    }

    /**
     * method to display UI
     */
    private void displayUI() {
        Entity ui = new Entity();
        ui.addComponent(new GameAreaDisplay("Brick Breaker Game"));
        spawnEntity(ui);
    }

    /**
     * method to load assets
     */
    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(BrickBreakerTextures);
        while (!resourceService.loadForMillis(10)) {
            // This could be upgraded to a loading screen
            logger.info("Loading... {}%", resourceService.getProgress());
        }
    }

    /**
     * Method for creating bricks in left to right order
     * @param n Number of blocks that are added along the x-axis
     * @param pos Start position from where the number of blocks are added
     */
    private void spawnStaticBrickRight(int n, GridPoint2 pos){
        Entity asteroid_length = ObstacleFactory.createStaticBrick(1f,1f);
        if (n <= 0) {
            return;
        }
        spawnEntityAt(asteroid_length, pos, false, false);
        // Increment the position for the next asteroid
        pos.x += 1;
        pos.y += 0;
        spawnStaticBrickRight(n - 1, pos); // Recursive call
    }
    /**
    Method for spawing all bricks together
     */
    private void spawnBrickLayout(){
        spawnStaticBrickRight(20,new GridPoint2(5,10));
        spawnStaticBrickRight(20,new GridPoint2(5,11));
        spawnStaticBrickRight(20,new GridPoint2(5,12));
        spawnStaticBrickRight(20,new GridPoint2(5,13));
        spawnStaticBrickRight(20,new GridPoint2(5,14));
        spawnStaticBrickRight(20,new GridPoint2(5,15));

    }

    /**
     * Method for spawning balls
     * @return ball
     */
    private Entity spawnBall()
    {
        Entity newBall = BallFactory.createMinigameBall();
        spawnEntityAt(newBall, BALL_SPAWN, true, true);
        targetables.add(newBall);
        ball = newBall;
        return newBall;
    }

    /**
     * method for spawning the slider
     * @return ship
     */
    private Entity spawnSlider()
    {
        Entity newSlider = SliderFactory.createSlider();
        spawnEntityAt(newSlider, SLIDER_SPAWN, true, true);
        targetables.add(newSlider);
        slider = newSlider;
        return newSlider;
    }


    /**
     * Method for unloading the texture
     */
    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(BrickBreakerTextures);

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

