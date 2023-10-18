package com.csse3200.game.components.maingame;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
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
 * Unit tests for the {@link ReturnToPlanetDisplay} class.
 */
public class ReturnToPlanetDisplayTest {

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
     * Test the assets related to the skin used for buttons in {@link ReturnToPlanetDisplay}.
     */
    @Test
    void testReturnToPlanetDisplaySkinAssets() {
        Gdx.app.postRunnable(() -> {
            ReturnToPlanetDisplay returnToPlanetDisplay = createReturnToPlanetDisplay();

            // Simulate creating and disposing the display
            returnToPlanetDisplay.create();
            returnToPlanetDisplay.dispose();

            // Verify that specific assets related to the skin have been loaded and unloaded
            verifyAssetLoaded("your_skin_atlas_path_here"); // Modify with the actual atlas path used in the skin
            verifyAssetUnloaded("your_skin_atlas_path_here"); // Modify with the actual atlas path used in the skin
        });
    }

    private ReturnToPlanetDisplay createReturnToPlanetDisplay() {
        // To access the skin, you might need to modify the ReturnToPlanetDisplay class
        // to provide a method to retrieve the skin or use reflection to access it.
        return new ReturnToPlanetDisplay();
    }

    private void verifyAssetLoaded(String assetPath) {
        // Verify asset loading
        ResourceService resourceService = ServiceLocator.getResourceService();
        boolean isLoaded = resourceService.containsAsset(assetPath, Skin.class);
        assertTrue(isLoaded, "Skin asset not loaded: " + assetPath);
    }

    private void verifyAssetUnloaded(String assetPath) {
        // Verify asset unloading
        ResourceService resourceService = ServiceLocator.getResourceService();
        boolean isUnloaded = !resourceService.containsAsset(assetPath, Skin.class);
        assertTrue(isUnloaded, "Skin asset not unloaded: " + assetPath);
    }
}
