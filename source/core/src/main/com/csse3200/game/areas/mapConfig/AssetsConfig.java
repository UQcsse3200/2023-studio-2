package com.csse3200.game.areas.mapConfig;

import com.csse3200.game.services.ResourceService;

import java.util.Arrays;
import java.util.Objects;

public class AssetsConfig {
    public String[] texturePaths = null;
    public String[] textureAtlasPaths = null;
    public String[] soundPaths = null;
    public String[] particleEffectPaths = null;
    public String backgroundMusicPath = null;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AssetsConfig that = (AssetsConfig) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(texturePaths, that.texturePaths)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(textureAtlasPaths, that.textureAtlasPaths)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(soundPaths, that.soundPaths)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(particleEffectPaths, that.particleEffectPaths)) return false;
        return Objects.equals(backgroundMusicPath, that.backgroundMusicPath);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(texturePaths);
        result = 31 * result + Arrays.hashCode(textureAtlasPaths);
        result = 31 * result + Arrays.hashCode(soundPaths);
        result = 31 * result + Arrays.hashCode(particleEffectPaths);
        result = 31 * result + (backgroundMusicPath != null ? backgroundMusicPath.hashCode() : 0);
        return result;
    }

    /**
     * Loads all assets contained within this config class
     * @param resourceService Resource service to be loaded into
     */
    public void load(ResourceService resourceService) {
        if (texturePaths != null)
            resourceService.loadTextures(texturePaths);
        if (textureAtlasPaths != null)
            resourceService.loadTextureAtlases(textureAtlasPaths);
        if (soundPaths != null)
            resourceService.loadSounds(soundPaths);
        if (particleEffectPaths != null)
            resourceService.loadParticleEffects(particleEffectPaths);
        if (backgroundMusicPath != null)
            resourceService.loadMusic(new String[] {backgroundMusicPath});
    }

    /**
     * Unloads all assets contained within this config class
     * @param resourceService Resource service to be unloaded from
     */
    public void unload(ResourceService resourceService) {
        if (texturePaths != null)
            resourceService.unloadAssets(texturePaths);
        if (textureAtlasPaths != null)
            resourceService.unloadAssets(textureAtlasPaths);
        if (soundPaths != null)
            resourceService.unloadAssets(soundPaths);
        if (particleEffectPaths != null)
            resourceService.unloadAssets(particleEffectPaths);
        if (backgroundMusicPath != null)
            resourceService.unloadAssets(new String[] {backgroundMusicPath});
    }
}
