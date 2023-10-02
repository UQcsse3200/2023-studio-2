package com.csse3200.game.areas;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.mapConfig.*;
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
        loaderMockedStatic.when(() -> ConfigLoader.loadMapDirectory(any(), any(), eq(false))).thenReturn(gameAreaConfig);
        TerrainFactory terrainFactory = mock(TerrainFactory.class);
        GdxGame game = mock(GdxGame.class);

        assertDoesNotThrow(() -> new MapGameArea("level", "area", terrainFactory, game, 3));
    }

    @Test
    void constructorInvalidConfigPath() {
        //Fail the gameAreaConfig load regardless of method.
        loaderMockedStatic.when(() -> ConfigLoader.loadMapDirectory(any(), any(), eq(false))).thenThrow(InvalidConfigException.class);
        TerrainFactory terrainFactory = mock(TerrainFactory.class);
        GdxGame game = mock(GdxGame.class);

        MapGameArea mapGameArea = new MapGameArea("level", "area", terrainFactory, game, 3);
        mapGameArea.create();

        //Verify invalid load and that screen changes to main menu
        verify(game).setScreen(GdxGame.ScreenType.MAIN_MENU);
    }

    @Test
    void loadAndUnloadEntityTextures() {
        gameAreaConfig = spy(GameAreaConfig.class);
        when(gameAreaConfig.getEntityTextures()).thenReturn(new String[] {"Texture1.png", "Texture2.atlas"});
        //Load the gameAreaConfig regardless of method.
        loaderMockedStatic.when(() -> ConfigLoader.loadMapDirectory(any(), any(), eq(false))).thenReturn(gameAreaConfig);

        TerrainFactory terrainFactory = mock(TerrainFactory.class);
        GdxGame game = mock(GdxGame.class);
        MapGameArea mapGameArea = new MapGameArea("level", "area", terrainFactory, game, 3);
        mapGameArea.loadAssets();

        verify(assetManager).load("Texture1.png", Texture.class);
        verify(assetManager).load("Texture2.atlas", TextureAtlas.class);

        mapGameArea.unloadAssets();
        verify(assetManager).unload("Texture1.png");
        verify(assetManager).unload("Texture2.atlas");
    }

    @Test
    void loadAndUnloadTexturePaths() {
        gameAreaConfig.assets.texturePaths = new String[] {"Texture3.png", "Texture4.png"};
        //Load the gameAreaConfig regardless of method.
        loaderMockedStatic.when(() -> ConfigLoader.loadMapDirectory(any(), any(), eq(false))).thenReturn(gameAreaConfig);

        TerrainFactory terrainFactory = mock(TerrainFactory.class);
        GdxGame game = mock(GdxGame.class);
        MapGameArea mapGameArea = new MapGameArea("level", "area", terrainFactory, game, 3);
        mapGameArea.loadAssets();

        verify(assetManager).load("Texture3.png", Texture.class);
        verify(assetManager).load("Texture4.png", Texture.class);

        mapGameArea.unloadAssets();
        verify(assetManager).unload("Texture3.png");
        verify(assetManager).unload("Texture4.png");
    }

    @Test
    void loadAndUnloadTextureAtlasPaths() {
        gameAreaConfig.assets.textureAtlasPaths = new String[] {"Texture5.atlas", "Texture6.atlas"};
        //Load the gameAreaConfig regardless of method.
        loaderMockedStatic.when(() -> ConfigLoader.loadMapDirectory(any(), any(), eq(false))).thenReturn(gameAreaConfig);

        TerrainFactory terrainFactory = mock(TerrainFactory.class);
        GdxGame game = mock(GdxGame.class);
        MapGameArea mapGameArea = new MapGameArea("planet", "level", terrainFactory, game, 3);
        mapGameArea.loadAssets();

        ResourceService resourceService = ServiceLocator.getResourceService();
        verify(assetManager).load("Texture5.atlas", TextureAtlas.class);
        verify(assetManager).load("Texture6.atlas", TextureAtlas.class);

        mapGameArea.unloadAssets();
        verify(assetManager).unload("Texture5.atlas");
        verify(assetManager).unload("Texture6.atlas");
    }

    @Test
    void loadAndUnloadSoundEffects() {
        gameAreaConfig.assets.soundPaths = new String[] {"soundeffect.wav", "music.ogg"};
        //Load the gameAreaConfig regardless of method.
        loaderMockedStatic.when(() -> ConfigLoader.loadMapDirectory(any(), any(), eq(false))).thenReturn(gameAreaConfig);

        TerrainFactory terrainFactory = mock(TerrainFactory.class);
        GdxGame game = mock(GdxGame.class);
        MapGameArea mapGameArea = new MapGameArea("level", "area", terrainFactory, game, 3);
        mapGameArea.loadAssets();

        verify(assetManager).load("soundeffect.wav", Sound.class);
        verify(assetManager).load("music.ogg", Sound.class);

        mapGameArea.unloadAssets();
        verify(assetManager).unload("soundeffect.wav");
        verify(assetManager).unload("music.ogg");
    }

    @Test
    void loadAndUnloadParticleEffects() {
        gameAreaConfig.assets.particleEffectPaths = new String[] {"explosion.effect", "explosion_2.effect"};
        //Load the gameAreaConfig regardless of method.
        loaderMockedStatic.when(() -> ConfigLoader.loadMapDirectory(any(), any(), eq(false))).thenReturn(gameAreaConfig);

        TerrainFactory terrainFactory = mock(TerrainFactory.class);
        GdxGame game = mock(GdxGame.class);
        MapGameArea mapGameArea = new MapGameArea("planet", "level", terrainFactory, game, 3);
        mapGameArea.loadAssets();

        verify(assetManager).load("explosion.effect", ParticleEffect.class);
        verify(assetManager).load("explosion_2.effect", ParticleEffect.class);

        mapGameArea.unloadAssets();
        verify(assetManager).unload("explosion.effect");
        verify(assetManager).unload("explosion_2.effect");
    }

    @Test
    void loadAndUnloadBackgroundMusic() {
        gameAreaConfig.assets.backgroundMusicPath = "backgroundMusic.wav";
        //Load the gameAreaConfig regardless of method.
        loaderMockedStatic.when(() -> ConfigLoader.loadMapDirectory(any(), any(), eq(false))).thenReturn(gameAreaConfig);

        TerrainFactory terrainFactory = mock(TerrainFactory.class);
        GdxGame game = mock(GdxGame.class);
        MapGameArea mapGameArea = new MapGameArea("planet", "level", terrainFactory, game, 3);
        mapGameArea.loadAssets();

        verify(assetManager).load("backgroundMusic.wav", Music.class);

        mapGameArea.unloadAssets();
        verify(assetManager).unload("backgroundMusic.wav");
    }

    //TODO: Add tests for entities
    //TODO: Refactor asset tests
}