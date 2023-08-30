package com.csse3200.game.areas;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.gamearea.GameAreaDisplay;
import com.csse3200.game.components.navigation.PlanetComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Navigationnn extends GameArea {
    private static final Logger logger = LoggerFactory.getLogger(Navigationnn.class);

    private GdxGame game;
    public Navigationnn(GdxGame game, String title, Skin skin) {
        this.game = game;





        // Create a "Start" button to close the dialog
        TextButton spaceButton = new TextButton("OK", skin);
        Entity entity = null;
        entity.getEvents().addListener("ok", this::onOK);
        spaceButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("OK button clicked");
                entity.getEvents().trigger("ok");
            }
        });



        // Lay out the dialog's components

    }

    private Label getTitleLabel() {

        return null;
    }

    public void showDialog(Stage stage) {
    }
    private void onOK() {
        logger.info("Start game");
        game.setScreen(GdxGame.ScreenType.MAIN_GAME);
    }

    @Override
    public void create() {
        ResourceService resourceService= ServiceLocator.getResourceService();
        ServiceLocator.getResourceService().loadTextures(new String[]{});
        while (!resourceService.loadForMillis(10)) {
            // This could be upgraded to a loading screen
//            logger.info("Loading... {}%", resourceService.getProgress());
        }
        displayUI();
        Entity planet1=new Entity().addComponent(new PlanetComponent("images/heart.png",730,750));
        planet1.getEvents().addListener("Navigate",()->{
            TerrainFactory terrainFactory = null;
            ForestGameArea nextGameArea=new ForestGameArea(terrainFactory);
            this.dispose();
            nextGameArea.create();


        });
        spawnEntity(planet1);
    }
    private void displayUI() {
        Entity ui = new Entity();
        ui.addComponent(new GameAreaDisplay("Box Forest"));
        spawnEntity(ui);
    }}
