package com.csse3200.game.areas;

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

public class Navigation extends GameArea {
    private final TerrainFactory terrainFactory;
    private static final Logger logger = LoggerFactory.getLogger(Navigation.class);
    private static final String[] NavigationTextures = {"images/box_boy_title.png"};
    private final String image;
    public Navigation(TerrainFactory terrainFactory,String image ){
        this.terrainFactory=terrainFactory;
        this.image=image;
    }
    @Override
    public void create()
    {
        ResourceService resourceService=ServiceLocator.getResourceService();
        ServiceLocator.getResourceService().loadTextures(new String[]{image});
        while (!resourceService.loadForMillis(10)) {
            // This could be upgraded to a loading screen
//            logger.info("Loading... {}%", resourceService.getProgress());
        }
        displayUI();
        Entity planet1=new Entity().addComponent(new PlanetComponent(image,730,750));
        planet1.getEvents().addListener("Navigate",()->{
            ForestGameArea nextGameArea=new ForestGameArea(terrainFactory);
            this.dispose();
            nextGameArea.create();


        });
        spawnEntity(planet1);
        Entity planet2 = new Entity().addComponent(new PlanetComponentt(image, 900, 600)); // You need to provide the appropriate image and coordinates
        planet2.getEvents().addListener("Navigatee", () -> {
            ForestGameArea nextGameArea=new ForestGameArea(terrainFactory);
            this.dispose();
            nextGameArea.create();// Similar navigation logic for other planets if needed
        });
        spawnEntity(planet2);

        Entity planet3 = new Entity().addComponent(new level3(image, 500, 500)); // You need to provide the appropriate image and coordinates
        planet3.getEvents().addListener("Navigateee", () -> {
            ForestGameArea nextGameArea=new ForestGameArea(terrainFactory);
            this.dispose();
            nextGameArea.create();// Similar navigation logic for other planets if needed
        });
        spawnEntity(planet3);

        Entity planet4 = new Entity().addComponent(new level4(image, 600, 580)); // You need to provide the appropriate image and coordinates
        planet4.getEvents().addListener("Navigateeee", () -> {
            ForestGameArea nextGameArea=new ForestGameArea(terrainFactory);
            this.dispose();
            nextGameArea.create();// Similar navigation logic for other planets if needed
        });
        spawnEntity(planet4);
    }

    private void displayUI() {
        Entity ui = new Entity();
        ui.addComponent(new GameAreaDisplay("Box Forest"));
        spawnEntity(ui);

    }
    private void loadAssists() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(NavigationTextures);
        ServiceLocator.getResourceService().loadAll();
    }

    /**
     *
     */
    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(NavigationTextures);
    }

    @Override
    public void dispose() {
        super.dispose();
        ServiceLocator.getEntityService().dispose();
        ServiceLocator.getResourceService().unloadAssets(new String[]{image});
    }
}