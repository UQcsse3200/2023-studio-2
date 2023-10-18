package com.csse3200.game.components.initialsequence;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.GdxGame;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.ResourceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link InitialScreenDisplay} class.
 */
public class InitialScreenDisplayTest {

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
     * Test asset loading in {@link InitialScreenDisplay}.
     */
    @Test
    void testInitialScreenDisplayAssetLoading() {
        Gdx.app.postRunnable(() -> {
            InitialScreenDisplay initialScreenDisplay = createInitialScreenDisplay();

            // Simulate creating and disposing the display
            initialScreenDisplay.create();
            initialScreenDisplay.dispose();

            // Verify that specific assets have been loaded and unloaded
            verifyAssetLoaded("InitialScreenBG.png");
            verifyAssetUnloaded("InitialScreenBG.png");
        });
    }

    /**
     * Creates an instance of the {@link InitialScreenDisplay} for testing.
     *
     * @return An instance of the {@link InitialScreenDisplay}.
     */
    private InitialScreenDisplay createInitialScreenDisplay() {
        ArrayList<String> assetPaths = new ArrayList<>();
        ArrayList<String> textList = new ArrayList<>();
        return new InitialScreenDisplay(game, assetPaths, textList);
    }

    /**
     * Verify that a specific asset has been loaded.
     *
     * @param assetPath The path to the asset to be verified.
     */
    private void verifyAssetLoaded(String assetPath) {
        ResourceService resourceService = ServiceLocator.getResourceService();
        boolean isLoaded = resourceService.containsAsset(assetPath, Texture.class);
        assertTrue(isLoaded, "Asset not loaded: " + assetPath);
    }

    /**
     * Verify that a specific asset has been unloaded.
     *
     * @param assetPath The path to the asset to be verified.
     */
    private void verifyAssetUnloaded(String assetPath) {
        ResourceService resourceService = ServiceLocator.getResourceService();
        boolean isUnloaded = !resourceService.containsAsset(assetPath, Texture.class);
        assertTrue(isUnloaded, "Asset not unloaded: " + assetPath);
    }
}
