package com.csse3200.game.areas;

import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TutorialGameArea extends MapGameArea {
    private static final Logger logger = LoggerFactory.getLogger(TutorialGameArea.class);

    public TutorialGameArea(String configPath,TerrainFactory terrainFactory, GdxGame game, int playerLives) {
        super(configPath, terrainFactory, game, playerLives);
    }

    /**
     * Create the game area
     */
    @Override
    public void create() {
        super.create();
        createDialogueUI();
    }

    private void createDialogueUI() {
        Entity ui = new Entity();
        ui.addComponent(new TutorialDialogue());
        spawnEntity(ui);
    }
}
