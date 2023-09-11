package com.csse3200.game.areas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.gamearea.GameAreaDisplay;
import com.csse3200.game.components.spacenavigation.NavigationBackground;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ObstacleFactory;
import com.csse3200.game.entities.factories.PlayerFactory;
import com.csse3200.game.entities.factories.ShipFactory;
import com.csse3200.game.files.UserSettings;
import com.csse3200.game.input.InputService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class ShopArea extends GameArea {
    private static final Logger logger = LoggerFactory.getLogger(ShopArea.class);
    private static final GridPoint2 SHIP_SPAWN = new GridPoint2(7, 10);
    private static final float STATIC_ASTEROID_SIZE = 1f;

    private static final String[] spaceMiniGameTextures = {
            "images/SpaceMiniGameBackground.png",
            "images/stone.png",
            "images/Ship.png"
    };
    private final TerrainFactory terrainFactory;
    private final ArrayList<Entity> targetables;

    /**
     * Constructor for initializing terrain area
     * @param terrainFactory Terrain factory being used in the area
     */
    public ShopArea(TerrainFactory terrainFactory) {
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
        spawnTerrain();
        spawnShip();
        createBoundary();
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
                ObstacleFactory.createBorder(STATIC_ASTEROID_SIZE, STATIC_ASTEROID_SIZE), pos, false, false);

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
                ObstacleFactory.createBorder(STATIC_ASTEROID_SIZE, STATIC_ASTEROID_SIZE), pos, false, false);

        // Increment the position for the next asteroid
        pos.y += 1;
        spawnStaticAsteroidsUp(n - 1, pos); // Recursive call
    }

    private void createBoundary(){
        spawnStaticAsteroidsRight(16,new GridPoint2(2,5));
        spawnStaticAsteroidsRight(16,new GridPoint2(2,13));
        spawnStaticAsteroidsUp(8,new GridPoint2(2,6));
        spawnStaticAsteroidsUp(8,new GridPoint2(17,6));
    }
    /**
     * Method for creating maze layout of the obstacle minigame
     */

    /**
     * Method for spawning terrain for background of obstacle minigame
     */
    private void spawnTerrain() {
        // Background terrain
        terrain = terrainFactory.createSpaceTerrain(TerrainFactory.TerrainType.SPACE_DEMO);
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
     * Method for loading the texture and the music of the map
     */
    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(spaceMiniGameTextures);
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

    }

    @Override
    public void dispose() {
        super.dispose();
        this.unloadAssets();
    }

}