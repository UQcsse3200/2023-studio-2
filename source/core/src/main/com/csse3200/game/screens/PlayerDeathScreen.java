package com.csse3200.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.SoundComponent;
import com.csse3200.game.components.player.DeathScreenActions;
import com.csse3200.game.components.player.DeathScreenDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.SoundsConfig;
import com.csse3200.game.entities.factories.RenderFactory;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.rendering.Renderer;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The game screen indicating a player's death and options to either restart or exit game.
 */
public class PlayerDeathScreen extends ScreenAdapter {
    SoundsConfig soundsConfig = FileLoader.readClass(SoundsConfig.class, "configs/deathScreen.json", FileLoader.Location.INTERNAL);
    public static final Logger logger = LoggerFactory.getLogger(PlayerDeathScreen.class);
    private final GdxGame game;
    private final Renderer renderer;
    private static final String[] deathScreenTextures = {"images/deathscreens/deathscreen_0.jpg", "images/deathscreens/deathscreen_1.jpg", "images/deathscreens/deathscreen_2.jpg", "images/deathscreens/deathscreen_3.jpg"};
    private static final String[] deathScreenSounds = {
            "sounds/playerDeathRespawn.mp3",
            "sounds/playerDead.mp3"
    };
    private int lives;

    private final DeathScreenDisplay deathScreenDisplay;

    public PlayerDeathScreen(GdxGame game, int lives) {
        this.game = game;
        game.setPlayerLives(lives);
        this.lives = lives;
        deathScreenDisplay = new DeathScreenDisplay(lives);
        renderer = RenderFactory.createRenderer();
        loadAssets();
        createUI();
    }

    @Override
    public void render(float delta) {
        ServiceLocator.getEntityService().update();
        renderer.render();
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
        deathScreenDisplay.resize();
        logger.trace("Resized renderer: ({} x {})", width, height);
    }


    @Override
    public void pause() {
        logger.info("Game paused");
    }

    @Override
    public void resume() {
        logger.info("Game resumed");
    }

    @Override
    public void dispose() {
        logger.debug("Disposing death screen");

        renderer.dispose();
        unloadAssets();
    }

    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(deathScreenTextures);
        resourceService.loadSounds(deathScreenSounds);
        ServiceLocator.getResourceService().loadAll();
    }

    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(deathScreenTextures);
    }

    /**
     * Creates the death screen's ui including components for rendering ui elements to the screen and
     * capturing and handling ui input.
     */
    private void createUI() {
        logger.debug("Creating ui");
        Stage stage = ServiceLocator.getRenderService().getStage();
        Entity ui = new Entity();
        ui.addComponent(deathScreenDisplay)
                .addComponent(new InputDecorator(stage, 10))
                .addComponent(new DeathScreenActions(game, lives))
                .addComponent(new SoundComponent(soundsConfig));
        ServiceLocator.getEntityService().register(ui);
    }
}
