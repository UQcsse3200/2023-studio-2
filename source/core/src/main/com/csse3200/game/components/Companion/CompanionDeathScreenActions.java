package com.csse3200.game.components.Companion;

import com.csse3200.game.GdxGame;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.screens.PlanetScreen;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class defines the actions to be taken when the companion character dies in the game.
 * It handles transitions to different screens upon death and respawn.
 */
public class CompanionDeathScreenActions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(CompanionDeathScreenActions.class);

    /**
     * The main game instance used for screen transitions.
     */
    public static GdxGame game;

    /**
     * Constructs a CompanionDeathScreenActions instance.
     *
     * @param game The GdxGame instance to use for screen transitions.
     */
    public CompanionDeathScreenActions(GdxGame game) {
        this.game = game;
    }

    /**
     * Initializes the component and sets up event listeners for exit and respawn events.
     */
    public void create() {
        entity.getEvents().addListener("exit", this::onExit);
        entity.getEvents().addListener("respawn", this::onRespawn);
    }

    /**
     * Handles the exit event by launching the main menu screen.
     */
    private void onExit() {
        logger.info("Launching main menu screen");
        game.setScreen(GdxGame.ScreenType.MAIN_MENU);
    }

    /**
     * Handles the respawn event by relaunching the main game screen.
     */
    private void onRespawn() {
        logger.info("Relaunching main game screen");

        // Get the current planet screen from the GameStateObserverService.
        PlanetScreen currentPlanetScreen = (PlanetScreen) ServiceLocator.getGameStateObserverService().getStateData("currentPlanet");
            game.setScreen(currentPlanetScreen);
            ServiceLocator.getEntityService().getCompanion().getComponent(CombatStatsComponent.class).setHealth(100);

    }
}
