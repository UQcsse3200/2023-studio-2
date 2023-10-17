package com.csse3200.game.components.spacenavigation;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import com.csse3200.game.GdxGame;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import java.lang.reflect.Field;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class NavigationBackgroundTest {

    @BeforeEach
    void setUp() {
        // Mock the LibGDX application
        Gdx.app = mock(Application.class);
        when(Gdx.app.getType()).thenReturn(Application.ApplicationType.HeadlessDesktop);

    }

    @Test
    void testAssetLoading() {
        NavigationBackground navigationBackground = new NavigationBackground();

        // Verify that the spaceBackground texture is not null
        Texture spaceBackground = navigationBackground.spaceBackground;
        if (spaceBackground != null) {
            // Texture is not null, the asset was loaded successfully
        } else {
            // Texture is null, the asset was not loaded
            throw new AssertionError("spaceBackground is null");
        }
    }
}