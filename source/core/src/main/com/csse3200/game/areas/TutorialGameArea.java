package com.csse3200.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.mapConfig.GameAreaConfig;
import com.csse3200.game.areas.mapConfig.InvalidConfigException;
import com.csse3200.game.areas.mapConfig.MapConfigLoader;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.TileEntity;
import com.csse3200.game.entities.factories.EnvironmentFactory;
import com.csse3200.game.entities.factories.ObstacleFactory;
import com.csse3200.game.entities.factories.PlayerFactory;
import com.csse3200.game.files.UserSettings;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TerrainService;
import com.csse3200.game.utils.math.GridPoint2Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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
        //ui.addComponent(new TutorialDialogue());
        spawnEntity(ui);
    }
}
