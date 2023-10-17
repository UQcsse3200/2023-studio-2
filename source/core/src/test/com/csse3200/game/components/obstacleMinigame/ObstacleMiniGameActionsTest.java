package com.csse3200.game.components.obstacleMinigame;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.screens.SpaceMapScreen;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.mockito.Mockito.*;

public class ObstacleMiniGameActionsTest {

    private GdxGame mockGame;
    private Stage mockStage;
    private SpaceMapScreen mockScreen;
    private ObstacleMiniGameActions obstacleMiniGameActions;

    @BeforeEach
    public void setup() {
        // Initialize mock objects
        mockGame = mock(GdxGame.class);
        mockStage = mock(Stage.class);
        mockScreen = mock(SpaceMapScreen.class);

        // Create instance of the class to be tested
        obstacleMiniGameActions = new ObstacleMiniGameActions(mockGame, mockStage, mockScreen);
    }

    @Test
    public void testExit() {
        // Trigger the exit method
        obstacleMiniGameActions.exit();

        // Verify that the correct method is called on the game instance
        verify(mockGame).setScreen(GdxGame.ScreenType.MAIN_MENU);
    }

    @Test
    void testConstructor() {
        GdxGame game = null;
        Stage stage = null;
        SpaceMapScreen screen = null;
        ObstacleMiniGameActions actualObstacleMiniGameActions = new ObstacleMiniGameActions(game, stage, screen);
    }
}