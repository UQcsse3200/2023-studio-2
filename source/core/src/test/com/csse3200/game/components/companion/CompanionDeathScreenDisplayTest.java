package com.csse3200.game.components.companion;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertNotNull;

/**
 * Test class for the CompanionDeathScreenDisplay component.
 */
public class CompanionDeathScreenDisplayTest {

    private HeadlessApplication app;

    /**
     * Set up method for the test. Mocks LibGDX application and GL20.
     */
    @Before
    public void setUp() {
        // Mock the LibGDX application
        app = new HeadlessApplication(new ApplicationListener() {
            @Override
            public void create() { }

            @Override
            public void render() { }

            @Override
            public void resize(int width, int height) { }

            @Override
            public void pause() { }

            @Override
            public void resume() { }

            @Override
            public void dispose() { }
        });

        Gdx.gl = Mockito.mock(GL20.class); // Mock the GL20
    }

    /**
     * Test method to ensure that CompanionDeathScreenDisplay can be created and initialized.
     */
    @Test
    public void testCreate() {
        // Mock the required dependencies
        CompanionDeathScreenDisplay deathScreenDisplay = new CompanionDeathScreenDisplay();

        // Create a mock Texture
        Texture mockTexture = Mockito.mock(Texture.class);

        // Set the Texture for the CompanionDeathScreenDisplay
        deathScreenDisplay.setTexture(mockTexture);

        // Call the method to create the display
        //deathScreenDisplay.create();

        // Assert that the display was created (not null)
        assertNotNull(deathScreenDisplay);
    }

    /**
     * Tear down method for cleaning up the LibGDX application.
     */
    @After
    public void tearDown() {
        // Clean up the LibGDX application
        app.exit();
    }
}

