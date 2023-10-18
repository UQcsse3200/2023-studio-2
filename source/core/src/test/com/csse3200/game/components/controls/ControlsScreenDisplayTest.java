package com.csse3200.game.components.controls;

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
 * Unit tests for the {@link ControlsScreenDisplay} class.
 */
public class ControlsScreenDisplayTest {

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
     * Test asset loading in {@link ControlsScreenDisplay}.
     */
    @Test
    void testControlsScreenDisplayAssetLoading() {
        Gdx.app.postRunnable(() -> {
            ControlsScreenDisplay controlsScreenDisplay = createControlsScreenDisplay();

            // Simulate creating and disposing the display
            controlsScreenDisplay.create();
            controlsScreenDisplay.dispose();

            // Verify that specific assets have been loaded and unloaded
            verifyAssetLoaded("images/controls-images/on_exit.png");
            verifyAssetUnloaded("images/controls-images/on_exit_hover.PNG");
        });
    }

    private ControlsScreenDisplay createControlsScreenDisplay() {
        return new ControlsScreenDisplay(game, true);
    }

    private void verifyAssetLoaded(String assetPath) {
        // Replace with the actual logic to verify asset loading
        ResourceService resourceService = ServiceLocator.getResourceService();
        boolean isLoaded = resourceService.containsAsset(assetPath, Texture.class);
        assertTrue(isLoaded, "Asset not loaded: " + assetPath);
    }

    private void verifyAssetUnloaded(String assetPath) {
        // Replace with the actual logic to verify asset unloading
        ResourceService resourceService = ServiceLocator.getResourceService();
        boolean isUnloaded = !resourceService.containsAsset(assetPath, Texture.class);
        assertTrue(isUnloaded, "Asset not unloaded: " + assetPath);
    }
}
