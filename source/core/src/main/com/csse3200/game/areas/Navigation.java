package com.csse3200.game.areas;

import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.navigation.PlanetComponent;
import com.csse3200.game.components.gamearea.GameAreaDisplay;
import com.csse3200.game.components.navigation.PlanetComponentt;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

public class Navigation extends GameArea {
    private final TerrainFactory terrainFactory;
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
            nextGameArea.create();// Similar navigation logic for planet2 if needed
        });
        spawnEntity(planet2);
    }

    private void displayUI() {
        Entity ui = new Entity();
        ui.addComponent(new GameAreaDisplay("Box Forest"));
        spawnEntity(ui);

    }

    @Override
    public void dispose() {
        super.dispose();
        ServiceLocator.getEntityService().dispose();
        ServiceLocator.getResourceService().unloadAssets(new String[]{image});
    }
}