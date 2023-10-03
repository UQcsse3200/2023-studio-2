// core/src/com/mygame/MiniScreenTest.java
package com.csse3200.game.components.miniscreen;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import org.junit.Before;
import org.junit.Test;


public class MiniScreenTest {

    @Before
    public void setUp() {
        // Create a headless application for testing
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        new HeadlessApplication(new TestApplicationListener(), config);
    }

    @Test
    public void testMiniScreenCreation() {
        // Your test code here
    }

    // Mock application listener for testing
    private static class TestApplicationListener extends ApplicationAdapter {
        @Override
        public void create() {
            Gdx.app.exit(); // Exit immediately to avoid starting the game
        }
    }
}
