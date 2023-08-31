package com.csse3200.game.story;

import com.csse3200.game.GdxGame;
import com.csse3200.game.components.story.StoryActions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.lang.reflect.Method;

import static org.mockito.Mockito.*;

public class StoryActionsTest {

    private StoryActions storyActions;
    private GdxGame game;

    @BeforeEach
    void setUp() {
        // Create a mock GdxGame
        game = Mockito.mock(GdxGame.class);

        // Create an instance of StoryActions with the mock GdxGame
        storyActions = new StoryActions(game);
    }

    @Test
    void testOnNext() throws Exception {
        // Use reflection to access the private onNext method
        Method onNextMethod = StoryActions.class.getDeclaredMethod("onNext");
        onNextMethod.setAccessible(true);

        // Call the private onNext method
        onNextMethod.invoke(storyActions);

        // Verify that the appropriate action was taken
        verify(game).setScreen(GdxGame.ScreenType.MAIN_GAME);
    }

    @Test
    void testOnSkip() throws Exception {
        // Use reflection to access the private onSkip method
        Method onSkipMethod = StoryActions.class.getDeclaredMethod("onSkip");
        onSkipMethod.setAccessible(true);


        onSkipMethod.invoke(storyActions);

        verify(game).setScreen(GdxGame.ScreenType.MAIN_GAME);
    }

}
