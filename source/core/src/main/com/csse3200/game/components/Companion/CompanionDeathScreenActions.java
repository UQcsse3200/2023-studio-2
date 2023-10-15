package com.csse3200.game.components.Companion;

import com.csse3200.game.GdxGame;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.services.PlanetTravel;
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
    public void onExit() {
        logger.info("Launching main menu screen");
        game.setScreen(GdxGame.ScreenType.MAIN_MENU);
    }

    /**
     * Handles the respawn event by relaunching the main game screen.
     */
    public void onRespawn() {
        logger.info("Relaunching main game screen");
        new PlanetTravel(game).returnToCurrent();
        ServiceLocator.getEntityService().getCompanion().getComponent(CombatStatsComponent.class).setHealth(100);
    }
}

