package com.csse3200.game.components.player;

import com.csse3200.game.GdxGame;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.screens.PlanetScreen;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * This class listens to events relevant to the Death Screen and does something when one of the
 * events is triggered.
 */
public class DeathScreenActions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(DeathScreenActions.class);

    private GdxGame game;
    private int lives;
    private static final PlayerConfig config =
            FileLoader.readClass(PlayerConfig.class, "configs/player.json");

    public DeathScreenActions(GdxGame game, int lives) { // Modify the constructor
        this.game = game;
        this.lives = lives;
    }

    /**
     * Overrides create() function to add Listeners.
     */
    @Override
    public void create() {
        entity.getEvents().addListener("exit", this::onExit);
        entity.getEvents().addListener("respawn", this::onRespawn);
    }
    /**
     * Exits to Main Menu game.
     */
    private void onExit() {
        logger.info("Launching main menu screen");
        game.setPlayerLives(config.lives); // Resets number of player's lives.
        game.setScreen(GdxGame.ScreenType.MAIN_MENU);
    }

    /**
     * Restarts level by relaunching main game.
     */
    private void onRespawn() {
        logger.info("Relaunching main game screen");
        game.setScreen((PlanetScreen) ServiceLocator.getGameStateObserverService().getStateData("currentPlanet"));
        game.setPlayerLives(lives); // stores number of lives remaining in GdxGame to be later accessed when respawning player.
    }
}