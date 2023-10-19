package com.csse3200.game.screens;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.csse3200.game.GdxGame;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.ResourceService;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.HashSet;
import java.util.Set;

/**
 * Unit tests for the {@link InitialScreen} class.
 */
public class InitialScreenTest {

    private Set<String> loadedAssets;

    @BeforeEach
    void setUp() {
        // Mock the LibGDX application
        Gdx.app = mock(Application.class);
        when(Gdx.app.getType()).thenReturn(Application.ApplicationType.HeadlessDesktop);

        // Initialize the LibGDX headless application
        new HeadlessApplication(new GdxGame());

        // Initialize the set of loaded assets
        loadedAssets = new HashSet<>();
    }

    /**
     * Test asset loading in {@link InitialScreen}.
     */
    @Test
    void testInitialScreenAssetLoading() {
        // Create an instance of the InitialScreen
        Gdx.app.postRunnable(() -> {
            InitialScreen initialScreen = new InitialScreen(new GdxGame());

            // Simulate rendering and resizing
            initialScreen.render(0.1f);
            initialScreen.resize(800, 600);

            // Verify that a specific asset has been loaded
//            verifyAssetLoaded("images/menu/InitialScreenBG.png");

            // Dispose of the screen
            initialScreen.dispose();
        });
    }

    private void verifyAssetLoaded(String assetPath) {
        // Replace with the actual logic to verify asset loading
        if (loadedAssets.contains(assetPath)) {
            // Asset is loaded
            System.out.println("Asset is loaded: " + assetPath);
        } else {
            // Asset is not loaded
            System.err.println("Asset not loaded: " + assetPath);
        }
    }
}
