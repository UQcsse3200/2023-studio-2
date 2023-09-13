package com.csse3200.game.components.story;

import com.csse3200.game.GdxGame;
import com.csse3200.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A component that defines actions for managing the game's story progression.
 */
public class StoryActions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(StoryActions.class);

    private GdxGame game;

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
        game.setScreen(GdxGame.ScreenType.MAIN_GAME);
    }

    /**
     * Action to skip the current part of the story and go to the main game.
     */
    private void onSkip() {
        logger.info("Skipping to game");
        game.setScreen(GdxGame.ScreenType.MAIN_GAME);
    }
}
