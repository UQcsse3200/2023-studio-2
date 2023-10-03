package com.csse3200.game.areas;

import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.gamearea.GameAreaDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class BricKBreakerGameArea extends GameArea{
    private static final Logger logger = LoggerFactory.getLogger(BricKBreakerGameArea.class);

    private static final String[] BrickBreakerTextures = {
            "images/minigame/SpaceMiniGameBackground.png",

    };

    private final TerrainFactory terrainFactory;
    private final ArrayList<Entity> targetables;

    public BricKBreakerGameArea(TerrainFactory terrainFactory) {
        super();
        this.terrainFactory = terrainFactory;
        this.targetables = new ArrayList<>();
    }

    @Override
    public void create() {
        loadAssets();
        displayUI();
    }
    private void displayUI() {
        Entity ui = new Entity();
        ui.addComponent(new GameAreaDisplay("Space Game"));
        spawnEntity(ui);
    }

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
     * Method for unloading the texture and the music of the map
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

