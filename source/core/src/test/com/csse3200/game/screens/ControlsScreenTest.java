package com.csse3200.game.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.GdxGame;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.ResourceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link ControlsScreen} class.
 */
public class ControlsScreenTest {

    private Set<String> loadedAssets;
    private GdxGame game;

    @BeforeEach
    void setUp() {
        // Mock the LibGDX application
        Gdx.app = mock(Application.class);
        when(Gdx.app.getType()).thenReturn(Application.ApplicationType.HeadlessDesktop);

        // Initialize the LibGDX headless application
        game = new GdxGame();
        new HeadlessApplication(game);

        // Initialize the set of loaded assets
        loadedAssets = new HashSet<>();
    }

    /**
     * Test asset loading in {@link ControlsScreen}.
     */
    @Test
    void testControlsScreenAssetLoading() {
        Gdx.app.postRunnable(() -> {
            ControlsScreen controlsScreen = new ControlsScreen(game);

            // Simulate rendering and resizing
            controlsScreen.render(0.1f);
            controlsScreen.resize(800, 600);

            // Verify that a specific asset has been loaded
            verifyAssetLoaded("images/controls-images/Controls.png");

            // Dispose of the screen
            controlsScreen.dispose();
        });
    }

    private void verifyAssetLoaded(String assetPath) {
        // Replace with the actual logic to verify asset loading
        ResourceService resourceService = ServiceLocator.getResourceService();
        boolean isLoaded = resourceService.containsAsset(assetPath, Texture.class);
        assertTrue(isLoaded, "Asset not loaded: " + assetPath);
    }
}
