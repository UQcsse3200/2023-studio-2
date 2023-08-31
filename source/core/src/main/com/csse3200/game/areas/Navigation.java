package com.csse3200.game.areas;

import com.badlogic.gdx.Gdx;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.navigation.PlanetComponent;
import com.csse3200.game.components.gamearea.GameAreaDisplay;
import com.csse3200.game.components.navigation.PlanetComponentt;
import com.csse3200.game.components.navigation.level3;
import com.csse3200.game.components.navigation.level4;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The `Navigation` class represents a navigational game area in which players can explore
 * and interact with various navigational elements such as planets. This class encapsulates
 * the creation, display, and navigation logic within the game.
 */
public class Navigation extends GameArea {

    private final TerrainFactory terrainFactory;
    private static final Logger logger = LoggerFactory.getLogger(Navigation.class);
    private static final String[] NavigationTextures = {"images/box_boy_title.png"};
    private final String image;
    private final GdxGame game;

    /**
     * Constructs a new `Navigation` instance with the given terrain factory and image.
     *
     * @param terrainFactory The factory responsible for generating terrain elements.
     * @param image The image resource associated with the navigation area.
     */
    public Navigation(TerrainFactory terrainFactory, String image, GdxGame game) {
        this.terrainFactory = terrainFactory;
        this.image = image;
        this.game = game;
    }

    /**
     * Initializes and sets up the navigation game area. This includes loading necessary
     * resources, displaying UI elements, creating navigational entities, and managing
     * the transition to other game areas.
     */
    @Override
    public void create() {
        // Load the required resources, such as textures
        ResourceService resourceService = ServiceLocator.getResourceService();
        ServiceLocator.getResourceService().loadTextures(new String[]{image});
        while (!resourceService.loadForMillis(10)) {
            // Implement loading screen logic here if desired
        }

        // Display UI components specific to the navigation area
        displayUI();

        // Create and configure navigational entities
        configureNavigationEntities();

        // Unload temporary assets after setting up the navigation area
        unloadTemporaryAssets();
    }

    /**
     * Displays user interface (UI) components relevant to the navigation area.
     * This can include game area title, navigation instructions, etc.
     */
    private void displayUI() {
        Entity ui = new Entity();
        ui.addComponent(new GameAreaDisplay("Box Forest")); // Title of the navigation area
        spawnEntity(ui);
    }

    /**
     * Configures the navigational elements within the navigation area. This includes
     * creating planets and defining their behavior when navigated.
     */
    private void configureNavigationEntities() {
        // Create planet entities with associated navigation behavior
        Entity planet1 = new Entity().addComponent(new PlanetComponent(image, 730, 750));
        planet1.getEvents().addListener("Navigate", () -> {
            navigateToGameArea(new EarthGameArea(terrainFactory, game));
        });
        spawnEntity(planet1);

        // Similar configuration for other navigational entities (e.g., planet2, planet3, planet4)
        // ...
    }

    /**
     * Navigates to the specified game area, disposing of the current navigation area.
     *
     * @param nextGameArea The game area to navigate to.
     */
    private void navigateToGameArea(GameArea nextGameArea) {
        this.dispose(); // Dispose of the current navigation area
        nextGameArea.create(); // Create and display the next game area
    }

    /**
     * Unloads temporary assets and resources used during the navigation area setup.
     * These assets were loaded solely for setup purposes and are no longer needed.
     */
    private void unloadTemporaryAssets() {
        logger.debug("Unloading temporary assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(NavigationTextures);
    }

    /**
     * Releases resources and performs cleanup when disposing of the navigation area.
     * This includes disposing of entities and unloading associated assets.
     */
    @Override
    public void dispose() {
        super.dispose(); // Perform parent class disposal
        ServiceLocator.getEntityService().dispose(); // Dispose of entities
        ServiceLocator.getResourceService().unloadAssets(new String[]{image}); // Unload image asset
    }
}