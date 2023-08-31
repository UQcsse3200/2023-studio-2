package com.csse3200.game.areas;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.gamearea.GameAreaDisplay;
import com.csse3200.game.components.navigation.NavigationPlanetComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

public class NavigationArea extends GameArea{

    private static final String[] spaceMapTextures = {
            "images/heart.png"
    };

    private final GdxGame game;
    private final TerrainFactory terrainFactory;

    public NavigationArea(GdxGame game, TerrainFactory terrainFactory) {
        this.game = game;
        this.terrainFactory = terrainFactory;
    }

    @Override
    public void create() {
        loadAssets();
        displayUI();
        createNavigationPlanets();
    }

    @Override
    public void dispose() {
        super.dispose();
        unloadAssets();
    }

    private void displayUI() {
        Entity ui = new Entity();
        ui.addComponent(new GameAreaDisplay("Space Map"));
        spawnEntity(ui);
    }

    private void createNavigationPlanets() {
        createPlanetUI("Level 1", 100, 100);
        createPlanetUI("Level 2", 300, 100);
        createPlanetUI("Level 3", 500, 100);
        createPlanetUI("Level 4", 700, 100);

    }

    private void createPlanetUI(String planetName, int x, int y) {
        Entity planet = new Entity().addComponent(new NavigationPlanetComponent(
                "images/heart.png", x, y, planetName));
        planet.getEvents().addListener("Navigate" + planetName, () -> {
            navigateToArea(new EarthGameArea(terrainFactory, game));
        });
        spawnEntity(planet);


    }

    private void navigateToArea(GameArea nextArea) {
        this.dispose();
        nextArea.create();
    }

    private void loadAssets() {
        ServiceLocator.getResourceService().loadTextures(spaceMapTextures);
    }

    private void unloadAssets() {
        ServiceLocator.getResourceService().unloadAssets(spaceMapTextures);
    }

}
