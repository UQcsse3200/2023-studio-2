package com.csse3200.game.areas;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.mapConfig.*;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Spy;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class MapGameAreaTest {
    GameAreaConfig gameAreaConfig;
    @Spy
    AssetManager assetManager;
    MockedStatic<FileLoader> mockFileLoader;
    MockedStatic<ConfigLoader> loaderMockedStatic;

    @BeforeEach
    void setup() {
        gameAreaConfig = new GameAreaConfig();
        gameAreaConfig.assets = new AssetsConfig();
        assetManager = spy(AssetManager.class);
        ServiceLocator.registerResourceService(new ResourceService(assetManager));
        ServiceLocator.registerEntityService(mock(EntityService.class));
        ServiceLocator.registerRenderService(mock(RenderService.class));

        mockFileLoader = mockStatic(FileLoader.class);
        loaderMockedStatic = mockStatic(ConfigLoader.class);
    }

    @AfterEach
    void teardown() {
        mockFileLoader.close();
        loaderMockedStatic.close();
    }

    @Test
    void constructorValidConfigPath() {
        //Load the gameAreaConfig regardless of method.
        loaderMockedStatic.when(() -> ConfigLoader.loadMapDirectory(any(), any())).thenReturn(gameAreaConfig);
        TerrainFactory terrainFactory = mock(TerrainFactory.class);
        GdxGame game = mock(GdxGame.class);

        MapGameArea mapGameArea = new MapGameArea("level", "area", terrainFactory, game);

        assertTrue(mapGameArea.validLoad);
    }

    @Test
    void constructorInvalidConfigPath() {
        //Fail the gameAreaConfig load regardless of method.
        loaderMockedStatic.when(() -> ConfigLoader.loadMapDirectory(any(), any())).thenThrow(InvalidConfigException.class);
        TerrainFactory terrainFactory = mock(TerrainFactory.class);
        GdxGame game = mock(GdxGame.class);

        MapGameArea mapGameArea = new MapGameArea("level", "area", terrainFactory, game);

        assertFalse(mapGameArea.validLoad);
    }

    @Test
    void loadAndUnloadEntityTextures() {
        gameAreaConfig = spy(GameAreaConfig.class);
        when(gameAreaConfig.getEntityTextures()).thenReturn(new String[] {"Texture1.png", "Texture2.atlas"});
        //Load the gameAreaConfig regardless of method.
        loaderMockedStatic.when(() -> ConfigLoader.loadMapDirectory(any(), any())).thenReturn(gameAreaConfig);

        TerrainFactory terrainFactory = mock(TerrainFactory.class);
        GdxGame game = mock(GdxGame.class);
        MapGameArea mapGameArea = new MapGameArea("level", "area", terrainFactory, game);
        mapGameArea.loadAssets();

        verify(assetManager).load("Texture1.png", Texture.class);
        verify(assetManager).load("Texture2.atlas", TextureAtlas.class);

        mapGameArea.unloadAssets();
        verify(assetManager).unload("Texture1.png");
        verify(assetManager).unload("Texture2.atlas");
    }

    //TODO: Add tests for entities?
}