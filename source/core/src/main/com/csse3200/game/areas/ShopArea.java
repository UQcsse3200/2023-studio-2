package com.csse3200.game.areas;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.MinigameShipFactory;
import com.csse3200.game.entities.factories.ObstacleFactory;
import com.csse3200.game.entities.factories.ShipUpgradesFactory;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Shop Area for spawning items and stats about the ship
 */
public class ShopArea extends GameArea {
    private static final Logger logger = LoggerFactory.getLogger(ShopArea.class);
    private static final GridPoint2 SHIP_SPAWN = new GridPoint2(7, 10);
    private static final float STATIC_ASTEROID_SIZE = 1.17f;
    private int health;
    private int fuel;

    private static final String[] spaceMiniGameTextures = {
            "images/minigame/SpaceMiniGameBackground.png",
            "images/structure-icons/stone_wall.png",
            "images/powerups/health_potion.png",
            "images/powerups/speed_potion.png"
    };
    private static final String[] spaceTextureAtlases = {"images/minigame/ship.atlas"};
    private final TerrainFactory terrainFactory;

    private final ArrayList<Entity> targeTables;


    /**
     * Constructor for initializing terrain area
     * @param terrainFactory Terrain factory being used in the area
     */
    public ShopArea(TerrainFactory terrainFactory) {
        super();
        this.terrainFactory = terrainFactory;
        this.targeTables = new ArrayList<>();
        //By default user has one for each bottle
        this.health = 1;
        this.fuel = 1;
    }

    /**
     * Main method for calling all the methods in the ShopArea into the UpgradeShopScreen class
     */
    @Override
    public void create() {
        loadAssets();
        spawnTerrain();
        spawnShip();
        spawnShipUpgrades();
        createBoundary();
    }

    /**
     * Spawns ShipUpgrades in the map at the position specified
     */
    private void spawnShipUpgrades() {
        for (int i = 0; i < this.health; i++) {
            Entity shipUpgrade = ShipUpgradesFactory.createHealthUpgrade();
            spawnEntityAt(shipUpgrade, new GridPoint2(8, 12), true, true);
        }
        for (int i = 0; i < this.health; i++) {
            Entity speedUpgrade = ShipUpgradesFactory.createFuelUpgrade();
            spawnEntityAt(speedUpgrade, new GridPoint2(7, 9), true, true);
        }
    }
    /**
     * Recursively calls n number of stones starting from position to the right
     * @param n Number of stones
     * @param pos Starting position for stone spawning
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

    /**
     * Recursively calls n number of stones starting from position to up
     * @param n Number of stones
     * @param pos Starting position for stone spawning
     */
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

    /**
     * Create the boundary for the shop made from stone
     */

    private void createBoundary(){
        spawnStaticAsteroidsRight(16,new GridPoint2(2,5));
        spawnStaticAsteroidsRight(16,new GridPoint2(2,13));
        spawnStaticAsteroidsUp(8,new GridPoint2(2,6));
        spawnStaticAsteroidsUp(8,new GridPoint2(17,6));
    }
    /**
     * Method for spawning terrain for background of the shop
     */
    private void spawnTerrain() {
        // Background terrain
        terrain = terrainFactory.createSpaceTerrain(TerrainFactory.TerrainType.SPACE_DEMO);
        spawnEntity(new Entity().addComponent(terrain));
    }
    /**
     * Method for spawning ship on the shop
     */
    private void spawnShip()
    {
        Entity newShip = MinigameShipFactory.createMinigameShip();
        spawnEntityAt(newShip, SHIP_SPAWN, true, true);
        targeTables.add(newShip);
    }

    /**
     * Method for loading the texture
     */
    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(spaceMiniGameTextures);
        resourceService.loadTextureAtlases(spaceTextureAtlases);
        while (!resourceService.loadForMillis(10)) {
            // This could be upgraded to a loading screen
            logger.info("Loading... {}%", resourceService.getProgress());
        }
    }

    /**
     * Method for unloading the texture
     */
    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(spaceMiniGameTextures);
        resourceService.unloadAssets(spaceTextureAtlases);

    }

    @Override
    public void dispose() {
        super.dispose();
        this.unloadAssets();
    }

}