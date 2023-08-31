package com.csse3200.game.areas;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.gamearea.GameAreaDisplay;
import com.csse3200.game.components.navigation.NavigationPlanetComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.PlanetTravel;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

/**
 * A GameArea that displays all the possible planets the player can travel to and
 * allows for the selection of which planet to go to next.
 */
public class NavigationArea extends GameArea{

    // Textures to be loaded
    private static final String[] spaceMapTextures = {
            "images/heart.png"
    };

    // The game this area is in
    private final GdxGame game;

    // The Factory to generate the new areas terrain
    private final TerrainFactory terrainFactory;

    // Handler of the transition point between planets
    private final PlanetTravel planetTravel;

    /**
     * Constructor for the Navigation Game Area
     * @param game  Game its being played on
     * @param terrainFactory    The terrain factor used to generate the next planet area.
     */
    public NavigationArea(GdxGame game, TerrainFactory terrainFactory) {
        this.game = game;
        this.terrainFactory = terrainFactory;
        this.planetTravel = new PlanetTravel(game);
    }

    /**
     * Creates the NavigationArea buttons and assets
     */
    @Override
    public void create() {
        loadAssets();
        displayUI();
        createNavigationPlanets();
    }

    /**
     * Disposes of the NavigationArea
     */
    @Override
    public void dispose() {
        super.dispose();
        unloadAssets();
    }

    /**
     * Display the UI GameArea
     */
    private void displayUI() {
        Entity ui = new Entity();
        ui.addComponent(new GameAreaDisplay("Space Map"));
        spawnEntity(ui);
    }

    /**
     * Create all the planet button options
     */
    private void createNavigationPlanets() {
        createPlanetUI("Level 1", 100, 100);
        createPlanetUI("Level 2", 300, 100);
        createPlanetUI("Level 3", 500, 100);
        createPlanetUI("Level 4", 700, 100);

    }

    /**
     * Create a single planet button
     * @param planetName    The name of the planet
     * @param x     The x-coord of the button
     * @param y     The y-coord of the button
     */
    private void createPlanetUI(String planetName, int x, int y) {
        Entity planet = new Entity().addComponent(new NavigationPlanetComponent(
                "images/heart.png", x, y, planetName));
        planet.getEvents().addListener("Navigate" + planetName, () ->
                navigateToArea(new EarthGameArea(terrainFactory, game)));
        spawnEntity(planet);
    }

    /**
     * Transition from current NavigationArea to the new area
     * @param nextArea  Next area to transition to.
     */
    private void navigateToArea(GameArea nextArea) {
        this.dispose();
        nextArea.create();
    }

    /**
     * Load all required assets
     */
    private void loadAssets() {
        ServiceLocator.getResourceService().loadTextures(spaceMapTextures);
    }

    /**
     * Unload the assets used
     */
    private void unloadAssets() {
        ServiceLocator.getResourceService().unloadAssets(spaceMapTextures);
    }

}
