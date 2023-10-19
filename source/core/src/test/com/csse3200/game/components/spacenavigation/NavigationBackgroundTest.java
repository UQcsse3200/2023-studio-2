package com.csse3200.game.components.spacenavigation;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;

import static org.mockito.Mockito.when;


public class NavigationBackgroundTest {

    @BeforeEach
    void setUp() {
        // Mock the LibGDX application
        Gdx.app = mock(Application.class);
        when(Gdx.app.getType()).thenReturn(Application.ApplicationType.HeadlessDesktop);
    }
    /**
     * Test whether the spaceBackground texture asset is successfully loaded in the NavigationBackground class.
     */
    @Test
    void testAssetLoading() {
        NavigationBackground navigationBackground = new NavigationBackground();

        // Verify that the spaceBackground texture is not null
        Texture spaceBackground = navigationBackground.spaceBackground;
        assert spaceBackground != null : "Asset loading failed: spaceBackground is null.";
    }
}