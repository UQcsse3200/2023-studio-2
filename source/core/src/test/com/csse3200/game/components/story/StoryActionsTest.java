//package com.csse3200.game.components.story;
//
//import com.badlogic.gdx.Gdx;
//import com.csse3200.game.GdxGame;
//import com.csse3200.game.screens.PlanetScreen;
//import com.csse3200.game.services.GameStateObserver;
//import com.csse3200.game.services.ServiceLocator;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mockito;
//
//import java.util.Map;
//
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//public class StoryActionsTest {
//
//    private StoryActions storyActions;
//    private GdxGame game;
//    private GameStateObserver gameStateObserverMock;
//
//    @Before
//    public void setUp() {
//        game = Mockito.mock(GdxGame.class);
//
//        // Mock the GameStateObserver
//        gameStateObserverMock = Mockito.mock(GameStateObserver.class);
//
//        storyActions = new StoryActions(game);
//
//        // Mock the Gdx behavior
//        Gdx.app = Mockito.mock(com.badlogic.gdx.Application.class);
//        when(Gdx.app.getType()).thenReturn(com.badlogic.gdx.Application.ApplicationType.HeadlessDesktop);
//
//        // Register the GameStateObserver with ServiceLocator
//        ServiceLocator.registerGameStateObserverService(gameStateObserverMock);
//    }
//
//    @Test
//    public void testOnSkip() {
//        storyActions.onSkip();
//
//        // Verify that the expected methods are called on the mock objects
//        verify(game).setScreen(Mockito.any(PlanetScreen.class));
//    }
//
//    @Test
//    public void testOnNext() {
//        storyActions.onNext();
//
//        // Verify that the expected methods are called on the mock objects with any argument
//        verify((Game) game).setScreen(Mockito.any()); // Assuming Game is the intended class
//    }
//
//
//}
