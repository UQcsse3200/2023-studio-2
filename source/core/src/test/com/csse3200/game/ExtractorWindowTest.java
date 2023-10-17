package com.csse3200.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.csse3200.game.windows.ExtractorMinigameWindow;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.ResourceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

public class ExtractorWindowTest {

    private Entity extractor;

    @BeforeEach
    void setUp() {
        // Mock the LibGDX application
        Gdx.app = mock(Application.class);
        when(Gdx.app.getType()).thenReturn(Application.ApplicationType.HeadlessDesktop);

        // Create a mock Entity for the extractor
        extractor = new Entity();
    }

    @Test
    void testExtractorMinigameWindowCreation() {
        Gdx.app.postRunnable(() -> {
            ExtractorMinigameWindow minigameWindow = ExtractorMinigameWindow.makeNewMinigame(extractor);

            // Verify that the window is not null
            assertNotNull(minigameWindow);

            // Verify that the window's size is appropriate
            float expectedWidth = (float) (Gdx.graphics.getWidth() * 0.8);
            float expectedHeight = (float) (Gdx.graphics.getHeight() * 0.65);
            assertEquals(expectedWidth, minigameWindow.getWidth(), 0.001);
            assertEquals(expectedHeight, minigameWindow.getHeight(), 0.001);

            // Dispose of the window
            minigameWindow.remove();
        });
    }
}
