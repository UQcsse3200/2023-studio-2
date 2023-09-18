package com.csse3200.game.areas;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.mapConfig.AreaEntityConfig;
import com.csse3200.game.areas.mapConfig.GameAreaConfig;
import com.csse3200.game.areas.mapConfig.InvalidConfigException;
import com.csse3200.game.areas.mapConfig.MapConfigLoader;
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
import org.mockito.Mock;
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
    MockedStatic<MapConfigLoader> loaderMockedStatic;

    @BeforeEach
    void setup() {
        gameAreaConfig = new GameAreaConfig();
        assetManager = spy(AssetManager.class);
        ServiceLocator.registerResourceService(new ResourceService(assetManager));
        ServiceLocator.registerEntityService(mock(EntityService.class));
        ServiceLocator.registerRenderService(mock(RenderService.class));

        mockFileLoader = mockStatic(FileLoader.class);
        loaderMockedStatic = mockStatic(MapConfigLoader.class);
    }

    @AfterEach
    void teardown() {
        mockFileLoader.close();
        loaderMockedStatic.close();
    }

    @Test
    void constructorValidConfigPath() {
        //Load the gameAreaConfig regardless of method.
        loaderMockedStatic.when(() -> MapConfigLoader.loadMapDirectory(any())).thenReturn(gameAreaConfig);
        loaderMockedStatic.when(() -> MapConfigLoader.loadMapFile(any())).thenReturn(gameAreaConfig);
        TerrainFactory terrainFactory = mock(TerrainFactory.class);
        GdxGame game = mock(GdxGame.class);

        assertDoesNotThrow(() -> new MapGameArea("configPath", terrainFactory, game));
    }

    @Test
    void constructorInvalidConfigPath() {
        //Fail the gameAreaConfig load regardless of method.
        loaderMockedStatic.when(() -> MapConfigLoader.loadMapDirectory(any())).thenThrow(InvalidConfigException.class);
        loaderMockedStatic.when(() -> MapConfigLoader.loadMapFile(any())).thenThrow(InvalidConfigException.class);
        TerrainFactory terrainFactory = mock(TerrainFactory.class);
        GdxGame game = mock(GdxGame.class);

        MapGameArea mapGameArea = new MapGameArea("configPath", terrainFactory, game);
        mapGameArea.create();

        //Verify invalid load and that screen changes to main menu
        verify(game).setScreen(GdxGame.ScreenType.MAIN_MENU);
    }

    @Test
    void loadEntityTextures() {
        gameAreaConfig = spy(GameAreaConfig.class);
        when(gameAreaConfig.getEntityTextures()).thenReturn(new String[] {"Texture1.png", "Texture2.atlas"});
        //Load the gameAreaConfig regardless of method.
        loaderMockedStatic.when(() -> MapConfigLoader.loadMapDirectory(any())).thenReturn(gameAreaConfig);
        loaderMockedStatic.when(() -> MapConfigLoader.loadMapFile(any())).thenReturn(gameAreaConfig);

        TerrainFactory terrainFactory = mock(TerrainFactory.class);
        GdxGame game = mock(GdxGame.class);
        MapGameArea mapGameArea = new MapGameArea("configPath", terrainFactory, game);
        mapGameArea.loadAssets();

        ResourceService resourceService = ServiceLocator.getResourceService();
        verify(assetManager).load("Texture1.png", Texture.class);
    }

    @Test
    void loadTexturePaths() {
        gameAreaConfig.texturePaths = new String[] {"Texture3.png", "Texture4.png"};
        //Load the gameAreaConfig regardless of method.
        loaderMockedStatic.when(() -> MapConfigLoader.loadMapDirectory(any())).thenReturn(gameAreaConfig);
        loaderMockedStatic.when(() -> MapConfigLoader.loadMapFile(any())).thenReturn(gameAreaConfig);

        TerrainFactory terrainFactory = mock(TerrainFactory.class);
        GdxGame game = mock(GdxGame.class);
        MapGameArea mapGameArea = new MapGameArea("configPath", terrainFactory, game);
        mapGameArea.loadAssets();

        verify(assetManager).load("Texture3.png", Texture.class);
        verify(assetManager).load("Texture4.png", Texture.class);
    }

    @Test
    void loadTextureAtlasPaths() {
        gameAreaConfig.textureAtlasPaths = new String[] {"Texture5.atlas", "Texture6.atlas"};
        //Load the gameAreaConfig regardless of method.
        loaderMockedStatic.when(() -> MapConfigLoader.loadMapDirectory(any())).thenReturn(gameAreaConfig);
        loaderMockedStatic.when(() -> MapConfigLoader.loadMapFile(any())).thenReturn(gameAreaConfig);

        TerrainFactory terrainFactory = mock(TerrainFactory.class);
        GdxGame game = mock(GdxGame.class);
        MapGameArea mapGameArea = new MapGameArea("configPath", terrainFactory, game);
        mapGameArea.loadAssets();

        ResourceService resourceService = ServiceLocator.getResourceService();
        verify(assetManager).load("Texture5.atlas", TextureAtlas.class);
        verify(assetManager).load("Texture6.atlas", TextureAtlas.class);

    }

    @Test
    void loadSoundEffects() {
        gameAreaConfig.soundPaths = new String[] {"soundeffect.wav", "music.ogg"};
        //Load the gameAreaConfig regardless of method.
        loaderMockedStatic.when(() -> MapConfigLoader.loadMapDirectory(any())).thenReturn(gameAreaConfig);
        loaderMockedStatic.when(() -> MapConfigLoader.loadMapFile(any())).thenReturn(gameAreaConfig);

        TerrainFactory terrainFactory = mock(TerrainFactory.class);
        GdxGame game = mock(GdxGame.class);
        MapGameArea mapGameArea = new MapGameArea("configPath", terrainFactory, game);
        mapGameArea.loadAssets();

        verify(assetManager).load("soundeffect.wav", Sound.class);
        verify(assetManager).load("music.ogg", Sound.class);
    }

    @Test
    void loadBackgroundMusic() {
        gameAreaConfig.backgroundMusicPath = "backgroundMusic.wav";
        //Load the gameAreaConfig regardless of method.
        loaderMockedStatic.when(() -> MapConfigLoader.loadMapDirectory(any())).thenReturn(gameAreaConfig);
        loaderMockedStatic.when(() -> MapConfigLoader.loadMapFile(any())).thenReturn(gameAreaConfig);

        TerrainFactory terrainFactory = mock(TerrainFactory.class);
        GdxGame game = mock(GdxGame.class);
        MapGameArea mapGameArea = new MapGameArea("configPath", terrainFactory, game);
        mapGameArea.loadAssets();

        verify(assetManager).load("backgroundMusic.wav", Music.class);
    }
}