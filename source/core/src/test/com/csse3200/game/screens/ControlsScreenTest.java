package com.csse3200.game.screens;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.csse3200.game.components.controls.ControlsScreenActions;
import com.csse3200.game.components.controls.ControlsScreenDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.RenderFactory;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.input.InputService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.rendering.Renderer;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.csse3200.game.GdxGame;
import com.csse3200.game.screens.ControlsScreen;
import com.csse3200.game.services.ServiceLocator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;



public class ControlsScreenTest {

    private GdxGame game;
    private ControlsScreen controlsScreen;

    @Before
    public void setUp() {
        // Initialize LibGDX in headless mode for testing
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        new HeadlessApplication(new ApplicationListener() {
            @Override
            public void create() {
                game = new GdxGame();
                controlsScreen = new ControlsScreen(game);
                controlsScreen.show(); // Simulate screen showing
            }

            @Override
            public void resize(int width, int height) {
                // Manually simulate resize
                controlsScreen.resize(width, height);
            }

            @Override
            public void render() {
                // Manually simulate render (not graphics-related)
                // You can leave this empty for headless testing
            }

            @Override
            public void pause() {
            }

            @Override
            public void resume() {
            }

            @Override
            public void dispose() {
                // Manually dispose resources
                controlsScreen.hide(); // Simulate screen hiding
                controlsScreen.dispose();
                game.dispose();
            }
        }, config);
    }

    @Test
    public void testControlsScreenAssets() {
        // Create an AssetManager and load assets
        AssetManager assetManager = new AssetManager();
        assetManager.load("images/Controls.png", Texture.class);
//        assertTrue("The asset 'images/Controls.png' should be loaded.", assetManager.isLoaded("images/Controls.png", Texture.class));
    }


    @After
    public void tearDown() {
        // Dispose resources if needed
    }

}
