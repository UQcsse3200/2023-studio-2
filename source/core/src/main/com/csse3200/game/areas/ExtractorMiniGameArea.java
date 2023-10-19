package com.csse3200.game.areas;

import com.badlogic.gdx.math.MathUtils;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.areas.terrain.TerrainFactory.TerrainType;
import com.csse3200.game.components.gamearea.GameAreaDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.StructureFactory;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Represents the mini game area specifically for the Extractor minigame.
 */
public class ExtractorMiniGameArea extends GameArea {
    private static final Logger logger = LoggerFactory.getLogger(ExtractorMiniGameArea.class);

    private static final String[] extractorMiniGameTextures = {
            "images/minigame/ExtractorMiniGameBackground.png", //TODO: Replace these images with suitable images - these are just for testing purposes!!
    };

    private final TerrainFactory terrainFactory;

    public final ArrayList<int[]> firePositions;

    public final ArrayList<int[]> holePositions;
    public MouseState mouseState;

    /**
     * Constructs a new ExtractorMiniGameArea with the specified terrain factory.
     *
     * @param terrainFactory The factory used to create terrain entities.
     */
    public ExtractorMiniGameArea(TerrainFactory terrainFactory) {
        super();
        this.terrainFactory = terrainFactory;
        this.firePositions = new ArrayList<>();
        this.firePositions.add(new int[]{4, 3});
        this.firePositions.add(new int[]{4, 9});
        this.firePositions.add(new int[]{10, 3});
        this.firePositions.add(new int[]{10, 9});
        this.holePositions = new ArrayList<>();
        this.holePositions.add(new int[]{4, 6});
        this.holePositions.add(new int[]{10, 6});
        this.holePositions.add(new int[]{7, 3});
        this.holePositions.add(new int[]{7, 6});
        this.holePositions.add(new int[]{7, 9});
        this.mouseState = MouseState.DEFAULT;
    }

    /**
     * Initiates the creation of the game area and its entities.
     */
    @Override
    public void create() {
        loadAssets();
        registerStructurePlacementService();

        displayUI();
        spawnTerrain(); //
        spawnExtractorsRepairs();
        spawnExtinguisher();
        spawnSpanner();
        spawnExtractorsFirePart();
        spawnExtractorsFirePart();
        spawnExtractorsHolePart();
        spawnExtractorsHolePart();
    }

    /**
     * Spawns extractor repair entities in the game area.
     */
    public void spawnExtractorsRepairs() {
        for (int i = 3; i < 10; i += 3) {
            for (int j = 3; j < 10; j += 3) {
                Entity extractorsRepair = StructureFactory.createExtractorRepair();
                extractorsRepair.setPosition(i, j);
                spawnEntity(extractorsRepair);
            }
        }
    }

    /**
     * Spawns an extinguisher entity in the game area.
     */

    public void spawnExtinguisher() {
        Entity extinguisher = StructureFactory.createExtinguisher(terrain, this);
        extinguisher.setPosition(0, 4);
        spawnEntity(extinguisher);
    }

    /**
     * Spawns a spanner entity in the game area.
     */
    public void spawnSpanner() {
        Entity spanner = StructureFactory.createSpanner(terrain, this);
        spanner.setPosition(12, 4);
        spawnEntity(spanner);
    }

    /**
     * Spawns a fire part for the extractor in the game area.
     */
    public void spawnExtractorsFirePart() {
        Entity extractorFirePart = StructureFactory.createExtractorFirePart(terrain, this);
        int randomIndex = MathUtils.random(0, firePositions.size() - 1);
        int[] randomPosition = this.firePositions.get(randomIndex);
        extractorFirePart.setPosition(randomPosition[0], randomPosition[1]);
        spawnEntity(extractorFirePart);
        this.firePositions.remove(randomIndex);
    }

    /**
     * Spawns a hole part for the extractor in the game area.
     */
    public void spawnExtractorsHolePart() {

        Entity extractorHolePart = StructureFactory.createExtractorHolePart(terrain, this);
        int randomIndex = MathUtils.random(0, holePositions.size() - 1);
        int[] randomPosition = this.holePositions.get(randomIndex);
        extractorHolePart.setPosition(randomPosition[0], randomPosition[1]);
        spawnEntity(extractorHolePart);
        this.holePositions.remove(randomIndex);
    }

    /**
     * Spawns a bang effect for the extractor at the specified position.
     *
     * @param x The x-coordinate of the position.
     * @param y The y-coordinate of the position.
     */
    public void spawnExtractorBang(int x, int y) {
        Entity extractorBang = StructureFactory.createExtractorBang();
        extractorBang.setPosition(x, y);
        spawnEntity(extractorBang);
    }

    /**
     * Spawns the terrain for the game area.
     */
    private void spawnTerrain() {
        // Background terrain

        terrain = terrainFactory.createSpaceTerrain(TerrainType.REPAIR_DEMO); //PLEASE EDIT
        spawnEntity(new Entity().addComponent(terrain));
    }


    /**
     * Loads necessary assets for the mini game area.
     */
    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(extractorMiniGameTextures);

        while (!resourceService.loadForMillis(10)) {
            // This could be upgraded to a loading screen
            logger.info("Loading... {}%", resourceService.getProgress());
        }
    }

    /**
     * Unloads assets used by the mini game area.
     */
    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(extractorMiniGameTextures);
    }


    /**
     * Performs cleanup and asset unloading when disposing the mini game area.
     */
    @Override
    public void dispose() {
        super.dispose();
        this.unloadAssets();
    }

    /**
     * Displays the user interface for the mini game area.
     */
    private void displayUI() {
        Entity ui = new Entity();
        ui.addComponent(new GameAreaDisplay("Extractor Repair minigame"));
        spawnEntity(ui);
    }

    /**
     * Enumeration representing the possible mouse states in the ExtractorMiniGameArea.
     */
    public enum MouseState {
        DEFAULT,
        EXTINGUISHER,
        SPANNER
    }
}