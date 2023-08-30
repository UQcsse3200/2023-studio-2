package com.csse3200.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.gamearea.GameAreaDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.StructureFactory;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csse3200.game.areas.terrain.TerrainFactory.TerrainType; //Temporary, for extractor minigame

import java.util.ArrayList;

public class ExtractorMiniGameArea extends GameArea {
    private static final Logger logger = LoggerFactory.getLogger(ExtractorMiniGameArea.class);

    private static final String[] extractorMiniGameTextures = {
            "images/ExtractorMiniGameBackground.png", //TODO: Replace these images with suitable images - these are just for testing purposes!!
    };

    private final TerrainFactory terrainFactory;

    public ExtractorMiniGameArea(TerrainFactory terrainFactory) {
        super();
        this.terrainFactory = terrainFactory;
    }

    @Override
    public void create() {
        loadAssets();
        registerStructurePlacementService();

        displayUI();
        spawnTerrain(); //
        spawnExtractorsRepairPart();
    }

    private void spawnExtractorsRepairPart() {

        Entity extractorRepairPart = StructureFactory.createExtractorRepairPart();
        int[][] positions = {
                {5,4}, {5,7}, {5,10}, {9,4}, {9,7}, {9,10}, {12,4}, {12,7}, {12,10}
        };
        int randomIndex = MathUtils.random(0, positions.length - 1);
        int[] randomPosition = positions[randomIndex];
        extractorRepairPart.setPosition(randomPosition[0], randomPosition[1]);
        spawnEntity(extractorRepairPart);
    }

    private void spawnTerrain() {
        // Background terrain

        terrain = terrainFactory.createSpaceTerrain(TerrainType.REPAIR_DEMO); //PLEASE EDIT
        spawnEntity(new Entity().addComponent(terrain));
    }

    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(extractorMiniGameTextures);

        while (!resourceService.loadForMillis(10)) {
            // This could be upgraded to a loading screen
            logger.info("Loading... {}%", resourceService.getProgress());
        }
    }

    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(extractorMiniGameTextures);
    }

    @Override
    public void dispose() {
        super.dispose();
        this.unloadAssets();
    }

    private void displayUI() {
        Entity ui = new Entity();
        ui.addComponent(new GameAreaDisplay("Extractor Repair minigame"));
        spawnEntity(ui);
    }


}