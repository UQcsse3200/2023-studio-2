//package com.csse3200.game.components.mainmenu;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.scenes.scene2d.Stage;
//import com.badlogic.gdx.scenes.scene2d.ui.Skin;
//import com.csse3200.game.GdxGame;
//import com.csse3200.game.screens.PlanetScreen;
//import com.csse3200.game.services.GameStateObserver;
//import com.csse3200.game.services.ServiceLocator;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mockito;
//
//public class MainMenuActionsTest {
//
//    private MainMenuActions mainMenuActions;
//    private GdxGame gameMock;
//    private Stage stageMock;
//    private Skin skinMock;
//
//    @Before
//    public void setUp() {
//        gameMock = Mockito.mock(GdxGame.class);
//        stageMock = Mockito.mock(Stage.class);
//        skinMock = Mockito.mock(Skin.class);
//
//        mainMenuActions = new MainMenuActions(gameMock, stageMock, skinMock);
//    }
//
//    @Test
//    public void testOnStart() {
//        // Mock the behavior of ServiceLocator and GdxGame as needed
//        GameStateObserver gameStateObserverMock = Mockito.mock(GameStateObserver.class);
//        Mockito.when(ServiceLocator.getGameStateObserverService()).thenReturn(gameStateObserverMock);
//
//        // Call the method to be tested
//        mainMenuActions.onStart();
//
//        // Verify that the expected methods were called
//        Mockito.verify(gameMock).setScreen(Mockito.any(PlanetScreen.class));
//        Mockito.verify(gameStateObserverMock).trigger(Mockito.eq("updatePlanet"), Mockito.eq("currentPlanet"), Mockito.any(PlanetScreen.class));
//    }
//
//    @Test
//    public void testOnLoad() {
//        // Call the method to be tested
//        mainMenuActions.onLoad();
//
//        // Verify that the expected method was called
//        Mockito.verify(gameMock).setScreen(GdxGame.ScreenType.GAME_STORY);
//    }
//
//    @Test
//    public void testOnExit() {
//        // Call the method to be tested
//        mainMenuActions.onExit();
//
//        // Verify that the expected method was called
//        Mockito.verify(gameMock).exit();
//    }
//
//    @Test
//    public void testOnSettings() {
//        // Call the method to be tested
//        mainMenuActions.onSettings();
//
//        // Verify that the expected method was called
//        Mockito.verify(gameMock).setScreen(GdxGame.ScreenType.SETTINGS);
//    }
//
//    @Test
//    public void testOnMini() {
//        // Call the method to be tested
//        mainMenuActions.onMini();
//
//        // Verify that the expected method was called
//        Mockito.verify(stageMock).addActor(Mockito.any(MainAlert.class));
//        // You can add further verifications as needed based on the behavior of MainAlert
//    }
//
//    @Test
//    public void testOnExtractor() {
//        // Call the method to be tested
//        mainMenuActions.onExtractor();
//
//        // Verify that the expected method was called
//        Mockito.verify(gameMock).setScreen(GdxGame.ScreenType.EXTRACTOR_GAME);
//    }
//
//    @Test
//    public void testOnSpaceMap() {
//        // Call the method to be tested
//        mainMenuActions.onSpaceMap();
//
//        // Verify that the expected methods were called
//        Mockito.verify(ServiceLocator.getGameStateObserverService()).trigger(
//                Mockito.eq("updatePlanet"), Mockito.eq("currentPlanet"), Mockito.any(PlanetScreen.class));
//        Mockito.verify(gameMock).setScreen(GdxGame.ScreenType.NAVIGATION_SCREEN);
//    }
//
//    @Test
//    public void testOnShop() {
//        // Call the method to be tested
//        mainMenuActions.onShop();
//
//        // Verify that the expected method was called
//        Mockito.verify(gameMock).setScreen(GdxGame.ScreenType.UPGRADE_SHOP);
//    }
//}
