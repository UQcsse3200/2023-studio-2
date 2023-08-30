package com.csse3200.game.areas;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.gamearea.GameAreaDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class ExtractorMiniGameArea extends GameArea {
    private static final Logger logger = LoggerFactory.getLogger(ExtractorMiniGameArea.class);

    public ExtractorMiniGameArea() {
        super();
    }

    @Override
    public void create() {

        displayUI();
    }

    private void displayUI() {
        Entity ui = new Entity();
        ui.addComponent(new GameAreaDisplay("Extractor Repair minigame"));
        spawnEntity(ui);
    }



}