package com.csse3200.game.components.controls;

import com.csse3200.game.GdxGame;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for the {@link ControlsScreenActions} class.
 */
class ControlsScreenActionsTest {

    private ControlsScreenActions controlsScreenActions;
    private GdxGame game;

    /**
     * Set up the test environment before each test case.
     */
    @BeforeEach
    void setUp() {
        game = mock(GdxGame.class);
        controlsScreenActions = new ControlsScreenActions(game, 3);
    }

    /**
     * Test the {@link ControlsScreenActions#onExit()} method.
     */
    @Test
    void onExit() {
        // Invoke the onExit method
        // controlsScreenActions.onExit();

        // Verify that the game's screen type is set to SETTINGS
        // verify(game).setScreen(GdxGame.ScreenType.SETTINGS);
    }
}
