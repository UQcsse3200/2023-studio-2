package com.csse3200.game.components.story;

import com.csse3200.game.GdxGame;
import com.csse3200.game.components.Component;
import com.csse3200.game.screens.PlanetScreen;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A component that defines actions for managing the game's story progression.
 */
public class StoryActions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(StoryActions.class);

    private final GdxGame game;

    /**
     * Creates a new instance of StoryActions.
     *
     * @param game The main game instance.
     */
    public StoryActions(GdxGame game) {
        this.game = game;
    }

    /**
     * Called when this component is created. Registers event listeners.
     */
    @Override
    public void create() {
        entity.getEvents().addListener("skip", this::onSkip);
    }

    /**
     * Action to proceed to the next part of the story.
     */
    private void onNext() {
        logger.info("Load next");
        game.setScreen((PlanetScreen) ServiceLocator.getGameStateObserverService().getStateData("currentPlanet"));
    }

    /**
     * Action to skip the current part of the story and go to the main game.
     */
    private void onSkip() {
        logger.info("Skipping to game");
        String startPlanetName = "Earth";
        logger.info(String.format("Start game, go to %s", startPlanetName));
        PlanetScreen planetScreen = new PlanetScreen(game, startPlanetName);
        ServiceLocator.getGameStateObserverService().trigger("updatePlanet", "currentPlanet", planetScreen);
        game.setScreen(planetScreen);
    }
}
