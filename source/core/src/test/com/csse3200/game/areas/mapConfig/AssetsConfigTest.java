package com.csse3200.game.areas.mapConfig;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.services.ResourceService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Spy;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class AssetsConfigTest {
    AssetsConfig assetsConfig;
    @Spy
    AssetManager assetManager;
    MockedStatic<FileLoader> mockFileLoader;
    MockedStatic<ConfigLoader> loaderMockedStatic;
    ResourceService resourceService;

    @BeforeEach
    void setup() {
        assetsConfig = new AssetsConfig();
        assetManager = spy(AssetManager.class);
        resourceService = new ResourceService(assetManager);

        mockFileLoader = mockStatic(FileLoader.class);
        loaderMockedStatic = mockStatic(ConfigLoader.class);
    }

    @AfterEach
    void teardown() {
        mockFileLoader.close();
        loaderMockedStatic.close();
    }

    @Test
    void loadAndUnloadTexturePaths() {
        assetsConfig.texturePaths = new String[] {"Texture3.png", "Texture4.png"};

        assetsConfig.load(resourceService);
        verify(assetManager).load("Texture3.png", Texture.class);
        verify(assetManager).load("Texture4.png", Texture.class);

        assetsConfig.unload(resourceService);
        verify(assetManager).unload("Texture3.png");
        verify(assetManager).unload("Texture4.png");
    }

    @Test
    void loadAndUnloadTextureAtlasPaths() {
        assetsConfig.textureAtlasPaths = new String[] {"Texture5.atlas", "Texture6.atlas"};

        assetsConfig.load(resourceService);
        verify(assetManager).load("Texture5.atlas", TextureAtlas.class);
        verify(assetManager).load("Texture6.atlas", TextureAtlas.class);

        assetsConfig.unload(resourceService);
        verify(assetManager).unload("Texture5.atlas");
        verify(assetManager).unload("Texture6.atlas");
    }

    @Test
    void loadAndUnloadSoundEffects() {
        assetsConfig.soundPaths = new String[] {"soundeffect.wav", "music.ogg"};

        assetsConfig.load(resourceService);
        verify(assetManager).load("soundeffect.wav", Sound.class);
        verify(assetManager).load("music.ogg", Sound.class);

        assetsConfig.unload(resourceService);
        verify(assetManager).unload("soundeffect.wav");
        verify(assetManager).unload("music.ogg");
    }

    @Test
    void loadAndUnloadParticleEffects() {
        assetsConfig.particleEffectPaths = new String[] {"explosion.effect", "explosion_2.effect"};

        assetsConfig.load(resourceService);
        verify(assetManager).load("explosion.effect", ParticleEffect.class);
        verify(assetManager).load("explosion_2.effect", ParticleEffect.class);

        assetsConfig.unload(resourceService);
        verify(assetManager).unload("explosion.effect");
        verify(assetManager).unload("explosion_2.effect");
    }

    @Test
    void loadAndUnloadBackgroundMusic() {
        assetsConfig.backgroundMusicPath = "backgroundMusic.wav";
        assetsConfig.load(resourceService);
        verify(assetManager).load("backgroundMusic.wav", Music.class);

        assetsConfig.unload(resourceService);
        verify(assetManager).unload("backgroundMusic.wav");
    }
}
